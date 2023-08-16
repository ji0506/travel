package com.spr.travel.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSender emailSender;

    public void sendSimpleMessage(String email,String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("test@gmail.com");  //보낸이
        message.setTo(email);               //받은이
        message.setSubject(subject);        // 제목
        message.setText(content);           // 내용
        emailSender.send(message);          //메일 전송
    }
}
