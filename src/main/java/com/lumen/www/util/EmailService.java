package com.lumen.www.util;

import com.lumen.www.dto.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String email;
    long startTime = System.nanoTime();
    long endTime = System.nanoTime();
    long duration = endTime - startTime;

    /**
     * 주어진 이메일 메시지를 특정 수신자들에게 비동기적으로 발송합니다.
     * <p>
     * 이 메서드는 정의된 수신자 목록에 대해 각각 비동기 이메일 발송 메서드(sendMailAsync)를 호출합니다.
     * 이메일 전송이 성공하면 콘솔에 서비스 시간을 출력하고, 실패하면 로그에 오류를 기록하고 RuntimeException을 발생시킵니다.
     * 모든 이메일 전송 시도 후에는 "ok" 문자열을 반환하여 전송이 시작되었음을 나타냅니다.
     *
     * @param emailMessage 발송할 이메일 메시지 객체.
     * @param type         이메일 유형을 나타내는 문자열 (현재 사용되지 않음).
     * @return 이메일 전송이 시작되었음을 나타내는 "ok" 문자열.
     * @throws RuntimeException 이메일 전송 중 예외가 발생한 경우.
     */
    public String sendMail(EmailMessage emailMessage, String type) {
        // 정의된 수신자 목록
        String[] recipients = new String[]{"junu3148@gmail.com", "junu3148@naver.com"};

        try {
            // 모든 수신자에 대해 이메일을 비동기적으로 발송
            Arrays.stream(recipients).forEach(recipient -> {
                try {
                    sendMailAsync(emailMessage, recipient);
                    System.out.println("서비스 시간1: " + duration);
                } catch (MessagingException e) {
                    log.error("Email sending failed: " + e.getMessage());
                    throw new RuntimeException("Failed to send email", e);
                }
            });

            log.info("Email sending initiated successfully");

            return "ok";
        } catch (Exception e) {
            log.error("Email sending failed: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }


    public String sendMail2(EmailMessage emailMessage, String email) {

        System.out.println(email);
        // 정의된 수신자 목록
        try {
            // 모든 수신자에 대해 이메일을 비동기적으로 발송
            try {
                sendMailAsync(emailMessage, email);
                System.out.println("서비스 시간1: " + duration);
            } catch (MessagingException e) {
                log.error("Email sending failed: " + e.getMessage());
                throw new RuntimeException("Failed to send email", e);
            }

            log.info("Email sending initiated successfully");

            return "ok";
        } catch (Exception e) {
            log.error("Email sending failed: " + e.getMessage());
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
    @Async
    public void sendMailAsync(EmailMessage emailMessage, String recipient) throws MessagingException {
        try {
            // JavaMailSender를 사용하여 MIME 메시지를 생성합니다.
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(email); // 발신자 설정
            mimeMessageHelper.setTo(recipient); // 개별 수신자 설정
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 제목 설정

            processImage(emailMessage, mimeMessageHelper); // 이미지 첨부 처리 (해당 메서드는 별도로 정의되어야 함)

            javaMailSender.send(mimeMessage); // 메일 발송
        } catch (MessagingException e) {
            log.error("Email sending failed: " + e.getMessage());
            throw e; // MessagingException을 다시 던집니다.
        }
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
        Map<String, String> cidMap = IntStream.range(0, imagePaths.size()).boxed()
                .collect(Collectors.toMap(imagePaths::get, i -> "image" + i));

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
                log.error("Failed to attach image: " + e.getMessage());
                throw new RuntimeException("Failed to attach image", e);
            }
        });
    }


    // 인증번호 및 임시 비밀번호 생성 메서드
    public String createCode() {
        Random random = new Random();
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


}