package com.lumen.www.util;

import com.lumen.www.entity.EmailMessage;
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

    // 메일 발송
    public String sendMail(EmailMessage emailMessage, String type) {
        String[] recipients = new String[]{"junu3148@gmail.com", "junu3148@naver.com"};

        try {
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

    // 메일 발송
    @Async
    public void sendMailAsync(EmailMessage emailMessage, String recipient) throws MessagingException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(email); // 발신자 설정
            mimeMessageHelper.setTo(recipient); // 개별 수신자 설정
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 제목 설정

            processImage(emailMessage, mimeMessageHelper); // 이미지 첨부

            javaMailSender.send(mimeMessage); // 메일발송
        } catch (MessagingException e) {
            log.error("Email sending failed: " + e.getMessage());
            throw e; // MessagingException을 다시 던집니다.
        }
    }

    // 이미지 첨부
    private void processImage(EmailMessage emailMessage, MimeMessageHelper mimeMessageHelper) throws MessagingException {
        List<String> imagePaths = extractImageUrls(emailMessage.getMessage()); // 여러 이미지 경로 추출
        String modifiedHtmlMessage = emailMessage.getMessage();

        // 이미지 CID 매핑을 저장하기 위한 Map 생성
        Map<String, String> cidMap = IntStream.range(0, imagePaths.size()).boxed().collect(Collectors.toMap(imagePaths::get, i -> "image" + i));

        // 모든 이미지 경로를 CID로 대체
        for (Map.Entry<String, String> entry : cidMap.entrySet()) {
            modifiedHtmlMessage = modifiedHtmlMessage.replace(entry.getKey(), "cid:" + entry.getValue());
        }

        // 본문 텍스트 설정
        mimeMessageHelper.setText(modifiedHtmlMessage, true);

        // 각 이미지 첨부 (CID 사용)
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