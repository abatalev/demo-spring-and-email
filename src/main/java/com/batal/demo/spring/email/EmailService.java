package com.batal.demo.spring.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private JavaMailSender sender;

    @Autowired
    public EmailService(JavaMailSender sender) {
        this.sender = sender;
    }

    public MimeMessage createMessage() throws MessagingException {
        MimeMessage msg = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
        helper.setTo("to_@email");
        helper.setSubject("Testing from Spring Boot");
        helper.setText(
                "[Text]Check attachment for image!",
                "<h1>Check attachment for image!</h1>");

        // hard coded a file path
        // FileSystemResource file = new FileSystemResource(new File("path/android.png"));
        // helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));
        return msg;
    }

    public void sendMessage(MimeMessage msg) {
        sender.send(msg);
    }
}