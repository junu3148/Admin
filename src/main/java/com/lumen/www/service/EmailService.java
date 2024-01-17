package com.lumen.www.service;

import com.lumen.www.entity.EmailMessage;
import jakarta.activation.FileDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String email;

    public String sendMail(EmailMessage emailMessage, String type) {
        String authNum = createCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(email); // 보내는사람
            mimeMessageHelper.setTo(emailMessage.getTo()); // 받는사람
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 제목

            // 이미지 파일 경로 추출
            String imagePath = extractImageUrl(emailMessage.getMessage());
            String cid = "image"; // CID 설정
            String modifiedHtmlMessage = emailMessage.getMessage().replace(imagePath, "cid:" + cid);
            mimeMessageHelper.setText(modifiedHtmlMessage, true);

            // 이미지 첨부 (CID 사용)
            FileSystemResource res = new FileSystemResource(new File("C:/lumen" + imagePath.substring(1)));
            mimeMessageHelper.addInline(cid, res);

            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully");
            return authNum;
        } catch (MessagingException e) {
            log.error("Email sending failed: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }




   /* public String sendMail(EmailMessage emailMessage, String type) {
        String authNum = createCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(email); // 보내는사람
            mimeMessageHelper.setTo(emailMessage.getTo()); // 받는사람
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // 제목
            mimeMessageHelper.setText(emailMessage.getMessage(), true);


            // 이미지 파일의 절대 경로
            String imagePath = extractImageUrl(emailMessage.getMessage());
            System.out.println(imagePath);

            mimeMessageHelper.addInline("image", new FileDataSource("C:/lumen" + imagePath.substring(1, imagePath.length())));


            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully");
            return authNum;
        } catch (MessagingException e) {
            log.error("Email sending failed: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
*/

    // 인증번호 및 임시 비밀번호 생성 메서드
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0:
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int) random.nextInt(26) + 65));
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

    public String extractImageUrl(String htmlContent) {
        String imageUrl = null;
        Pattern pattern = Pattern.compile("<img [^>]*src=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(htmlContent);

        if (matcher.find()) {
            imageUrl = matcher.group(1); // 첫 번째 일치하는 이미지의 src 값을 추출
        }

        return imageUrl;
    }

}