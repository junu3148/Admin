package com.lumen.www.service;

import com.lumen.www.dao.AdminRepository;
import com.lumen.www.dto.email.EmailMessage;
import com.lumen.www.dto.promotion.PromotionsDTO;
import com.lumen.www.dto.user.UserDTO;
import com.lumen.www.exception.CustomException;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final AdminRepository adminRepository;

    @Value("${spring.mail.username}")
    private String email;

    // 프로모션 메일 배포
    @Override
    @Transactional
    public ResponseEntity<String> sendMailPromo(PromotionsDTO promotionsDTO) {
        // 프로모션 정보를 바탕으로 이메일 메시지 객체 생성
        EmailMessage emailMessage = EmailMessage.builder()
                .subject(promotionsDTO.getPromotionsTitle())
                .message(promotionsDTO.getPromotionsContent())
                .build();

        // 프로모션 수신을 동의한 유저의 이메일 목록을 불러옴
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
                            throw new CustomException("Failed to send email to " + recipient, e);
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

    // 비밀번호 초기화 메일 배포
    @Override
    public String sendMailPWReset(UserDTO userDTO) {

        EmailMessage emailMessage = EmailMessage.builder().subject("비밀번호 초기화 메일알림").message("비밀번호 초기화 해주세요").build();

        try {
            // 모든 수신자에 대해 이메일을 비동기적으로 발송
            sendMailAsync(emailMessage, userDTO.getUserId());
            return "ok";
        } catch (MessagingException e) {
            // MessagingException 포함 모든 예외를 여기서 처리
            throw new CustomException("Failed to send email", e);
        }
    }

    // 메시지 객체 생성
    private void sendMailAsync(EmailMessage emailMessage, String recipient) throws MessagingException {
        // JavaMailSender를 사용하여 MIME 메시지를 생성합니다.
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom(email); // 발신자 설정
        mimeMessageHelper.setTo(recipient); // 개별 수신자 설정
        mimeMessageHelper.setSubject(emailMessage.getSubject()); // 제목 설정

        processImage(emailMessage, mimeMessageHelper); // 이미지 첨부 처리 (해당 메서드는 별도로 정의되어야 함)

        javaMailSender.send(mimeMessage); // 메일 발송

    }

    // 이미지 첨부
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
                throw new CustomException("Failed to attach image", e);
            }
        });
    }

    // 이미지 경로 검색
    private List<String> extractImageUrls(String htmlContent) {
        List<String> imageUrls = new ArrayList<>();
        Pattern pattern = Pattern.compile("<img [^>]*src=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(htmlContent);

        while (matcher.find()) {
            imageUrls.add(matcher.group(1)); // 모든 일치하는 이미지의 src 값을 리스트에 추가
        }

        return imageUrls;
    }


}
