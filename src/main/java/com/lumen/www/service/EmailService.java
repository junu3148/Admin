package com.lumen.www.service;

import com.lumen.www.dao.AdminRepository;
import com.lumen.www.dto.email.EmailMessage;
import com.lumen.www.dto.promotion.PromotionsDTO;
import com.lumen.www.dto.user.UserDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final AdminRepository adminRepository;

    @Value("${spring.mail.username}")
    private String email;


    /**
     * 주어진 이메일 메시지를 특정 수신자들에게 비동기적으로 발송합니다.
     * <p>
     * 이 메서드는 정의된 수신자 목록에 대해 각각 비동기 이메일 발송 메서드(sendMailAsync)를 호출합니다.
     * 이메일 전송이 성공하면 콘솔에 서비스 시간을 출력하고, 실패하면 로그에 오류를 기록하고 RuntimeException을 발생시킵니다.
     * 모든 이메일 전송 시도 후에는 "ok" 문자열을 반환하여 전송이 시작되었음을 나타냅니다.
     *
     * @param promotionsDTO 이메일 발송에 사용할 프로모션 데이터 객체
     * @return 이메일 전송이 시작되었음을 나타내는 "ok" 문자열.
     * @throws RuntimeException 이메일 전송 중 예외가 발생한 경우.
     */

    @Transactional
    public ResponseEntity<String> sendMailPromo(PromotionsDTO promotionsDTO) {
        // 프로모션 정보를 바탕으로 이메일 메시지 객체 생성
        EmailMessage emailMessage = EmailMessage.builder()
                .subject(promotionsDTO.getPromotionsTitle())
                .message(promotionsDTO.getPromotionsContent())
                .build();

        // 프로모션 수신을 동의한 관리자의 이메일 목록을 불러옴
        List<String> recipients = adminRepository.getPromotionsAccept();

        try {
            // 수신자 목록에 대해 비동기 이메일 발송 작업을 생성하고 실행
            CompletableFuture<?>[] futuresArray = recipients.stream()
                    .map(recipient -> CompletableFuture.runAsync(() -> {
                        try {
                            // 각 수신자에게 비동기적으로 이메일 발송
                            sendMailAsync(emailMessage, recipient);
                        } catch (MessagingException e) {
                            // 이메일 발송 중 예외 발생 시 RuntimeException으로 포장하여 던짐
                            throw new RuntimeException("Failed to send email to " + recipient, e);
                        }
                    }))
                    .toArray(CompletableFuture[]::new);

            // 생성된 모든 비동기 작업이 완료될 때까지 대기
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(futuresArray);
            allFutures.join();
            // 모든 이메일이 성공적으로 발송되면, 클라이언트에게 성공 메시지 전송
            return ResponseEntity.ok("Emails successfully sent to all recipients.");

        } catch (RuntimeException e) {
            // 비동기 작업 중 예외 발생 시, 500 Internal Server Error와 함께 오류 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send emails to all recipients. Error: " + e.getCause().getMessage());
        }
    }


    /**
     * 비밀번호 재설정 이메일을 특정 사용자에게 비동기적으로 발송합니다.
     * <p>
     * 이 메서드는 주어진 이메일 주소에 대해 비밀번호 재설정 이메일을 비동기적으로 발송합니다.
     * 이메일 전송이 성공하면 콘솔에 서비스 시간을 출력하고, 실패하면 로그에 오류를 기록하고 RuntimeException을 발생시킵니다.
     * 메서드 호출 후에는 "ok" 문자열을 반환하여 전송이 시작되었음을 나타냅니다.
     *
     * @param userDTO 발송할 이메일 메시지 객체.
     * @return 이메일 전송이 시작되었음을 나타내는 "ok" 문자열.
     * @throws RuntimeException 이메일 전송 중 예외가 발생한 경우.
     */
    public String sendMailPWReset(UserDTO userDTO) {

        EmailMessage emailMessage = EmailMessage.builder().subject("비밀번호 초기화 메일알림").message("비밀번호 초기화 해주세요").build();

        try {
            // 모든 수신자에 대해 이메일을 비동기적으로 발송
            sendMailAsync(emailMessage, userDTO.getUserId());
            return "ok";
        } catch (MessagingException e) {
            // MessagingException 포함 모든 예외를 여기서 처리
            throw new RuntimeException("Failed to send email", e);
        }
    }


    /**
     * 주어진 이메일 메시지를 비동기적으로 특정 수신자에게 발송합니다.
     * <p>
     * 이 메서드는 JavaMailSender를 사용하여 MIME 메시지를 생성하고 설정합니다.
     * 발신자, 수신자, 제목을 설정하고, 필요한 경우 이미지를 처리하여 첨부합니다.
     * 모든 설정이 완료되면, JavaMailSender를 통해 이메일을 발송합니다.
     *
     * @param emailMessage 발송할 이메일 메시지 객체.
     * @param recipient    이메일의 수신자 주소.
     * @throws MessagingException 메일 발송 중 발생하는 예외.
     */
    public void sendMailAsync(EmailMessage emailMessage, String recipient) throws MessagingException {
        // JavaMailSender를 사용하여 MIME 메시지를 생성합니다.
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(email); // 발신자 설정
        mimeMessageHelper.setTo(recipient); // 개별 수신자 설정
        mimeMessageHelper.setSubject(emailMessage.getSubject()); // 제목 설정

        processImage(emailMessage, mimeMessageHelper); // 이미지 첨부 처리 (해당 메서드는 별도로 정의되어야 함)

        javaMailSender.send(mimeMessage); // 메일 발송

    }


    /**
     * 이메일 본문에 포함된 이미지를 처리하여 MIME 메시지에 첨부합니다.
     * <p>
     * 이 메서드는 이메일 메시지에서 이미지 URL을 추출하고, 각 이미지 URL을 Content-ID(CID)로 매핑합니다.
     * 그런 다음 HTML 본문에서 이미지 URL을 해당 CID로 대체하고, MIME 메시지에 이미지를 첨부합니다.
     *
     * @param emailMessage      발송할 이메일 메시지 객체. 이미지 URL을 포함한 HTML 본문을 가지고 있어야 합니다.
     * @param mimeMessageHelper MIME 메시지를 구성하는 데 사용되는 MimeMessageHelper 객체.
     * @throws MessagingException 이미지 처리 중 발생하는 예외.
     */
    private void processImage(EmailMessage emailMessage, MimeMessageHelper mimeMessageHelper) throws MessagingException {
        // 이메일 본문에서 이미지 경로 추출
        List<String> imagePaths = extractImageUrls(emailMessage.getMessage());
        String modifiedHtmlMessage = emailMessage.getMessage();

        // 이미지 CID 매핑을 저장하기 위한 Map 생성
        Map<String, String> cidMap = IntStream.range(0, imagePaths.size()).boxed().collect(Collectors.toMap(imagePaths::get, i -> "image" + i));

        // 모든 이미지 경로를 CID로 대체
        for (Map.Entry<String, String> entry : cidMap.entrySet()) {
            modifiedHtmlMessage = modifiedHtmlMessage.replace(entry.getKey(), "cid:" + entry.getValue());
        }

        // 본문 텍스트 설정 (HTML로 처리)
        mimeMessageHelper.setText(modifiedHtmlMessage, true);

        // 각 이미지를 CID를 사용하여 첨부
        imagePaths.forEach(imagePath -> {
            try {
                FileSystemResource res = new FileSystemResource(new File("C:/lumen" + imagePath.substring(1)));
                mimeMessageHelper.addInline(cidMap.get(imagePath), res);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to attach image", e);
            }
        });
    }

    /**
     * HTML 콘텐츠에서 이미지 URL을 추출합니다.
     * <p>
     * 이 메서드는 주어진 HTML 문자열에서 <img> 태그의 src 속성을 분석하여 모든 이미지 URL을 추출합니다.
     * 정규 표현식을 사용하여 HTML 콘텐츠 내의 <img> 태그를 찾고, 해당 태그의 src 속성 값을 추출합니다.
     * 추출된 이미지 URL은 리스트 형태로 반환됩니다.
     *
     * @param htmlContent 이미지 URL을 추출할 HTML 콘텐츠.
     * @return 추출된 이미지 URL 목록.
     */
    // 이미지 경로 검색
    public List<String> extractImageUrls(String htmlContent) {
        List<String> imageUrls = new ArrayList<>();
        Pattern pattern = Pattern.compile("<img [^>]*src=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(htmlContent);

        while (matcher.find()) {
            imageUrls.add(matcher.group(1)); // 모든 일치하는 이미지의 src 값을 리스트에 추가
        }

        return imageUrls;
    }


    // 인증번호 및 임시 비밀번호 생성 메서드
    public String createCode() {
        Random random = new Random(8);
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0:
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                default:
                    key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

    // thymeleaf를 통한 html 적용
    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }


}
/*

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final AdminRepository adminRepository;

    @Value("${spring.mail.username}")
    private String email;

    @Transactional
    public ResponseEntity<String> sendMailPromo(PromotionsDTO promotionsDTO) {
        EmailMessage emailMessage = createEmailMessageFromPromotionsDTO(promotionsDTO);
        List<String> recipients = adminRepository.getPromotionsAccept();
        CompletableFuture<Void> allFutures = sendEmailsToRecipientsAsync(emailMessage, recipients);

        try {
            allFutures.join();
            return ResponseEntity.ok("Emails successfully sent to all recipients.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send emails to all recipients. Error: " + getRootCause(e).getMessage());
        }
    }

    public String sendMailPWReset(UserDTO userDTO) {
        EmailMessage emailMessage = EmailMessage.builder().subject("비밀번호 초기화 메일알림")
                .message("비밀번호 초기화 해주세요").build();

        try {
            sendMailAsync(emailMessage, userDTO.getUserId());
            return "ok";
        } catch (MessagingException e) {
            throw new EmailSendingException("Failed to send email", e);
        }
    }

    private void sendMailAsync(EmailMessage emailMessage, String recipient) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(email);
        mimeMessageHelper.setTo(recipient);
        mimeMessageHelper.setSubject(emailMessage.getSubject());

        processImage(emailMessage, mimeMessageHelper);

        javaMailSender.send(mimeMessage);
    }

    private void processImage(EmailMessage emailMessage, MimeMessageHelper mimeMessageHelper) throws MessagingException {
        List<String> imagePaths = extractImageUrls(emailMessage.getMessage());
        Map<String, String> cidMap = generateCidMapForImages(imagePaths);
        String modifiedHtmlMessage = replaceImageUrlsWithCids(emailMessage.getMessage(), cidMap);

        mimeMessageHelper.setText(modifiedHtmlMessage, true);
        attachImagesToEmail(imagePaths, cidMap, mimeMessageHelper);
    }

    private CompletableFuture<Void> sendEmailsToRecipientsAsync(EmailMessage emailMessage, List<String> recipients) {
        CompletableFuture<?>[] futuresArray = recipients.stream()
                .map(recipient -> CompletableFuture.runAsync(() -> {
                    try {
                        sendMailAsync(emailMessage, recipient);
                    } catch (MessagingException e) {
                        throw new EmailSendingException("Failed to send email to " + recipient, e);
                    }
                })).toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futuresArray);
    }

    // Additional methods like extractImageUrls, generateCidMapForImages, replaceImageUrlsWithCids, attachImagesToEmail
    // are refactored similarly to improve readability, reduce duplication, and improve error handling.
}*/
