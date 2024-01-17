package com.lumen.www.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailService {

    private JavaMailSender javaMailSender;

    // 단순 문자 메일 보내기
    public  void sendSimpleEmail(){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("이메일발송 테스트");
        message.setTo();

    }

    // HTML 메일 보내기
    public void sendHTMLEmail(){

    }

    // 6자리의 랜덤 비밀번호 생성
    public void createRandomPw(){

    }




}