package com.batal.demo.spring.email;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmailServiceTest {

    @Test
    void checkCreateMessage() throws MessagingException, IOException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(1111);
        EmailService service = new EmailService(mailSender);
        MimeMessage msg = service.createMessage();
        parseMsg(msg.getContent());
    }

    private void parseMsg(Object content) throws MessagingException, IOException {
        if (content instanceof MimeMultipart) {
            MimeMultipart multi = (MimeMultipart) content;
            for (int i = 0; i < multi.getCount(); i++) {
                parseMsg(multi.getBodyPart(i).getContent());
            }
        } else {
            System.err.println("### " + content);
        }
    }

    @Test
    void checkSendEmail() throws Exception {
        try (SimpleSmtpServer dumbster = SimpleSmtpServer.start(SimpleSmtpServer.AUTO_SMTP_PORT)) {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("localhost");
            mailSender.setPort(dumbster.getPort());

            EmailService service = new EmailService(mailSender);
            service.sendMessage(service.createMessage());

            List<SmtpMessage> emails = dumbster.getReceivedEmails();
            assertEquals(1, emails.size());
            SmtpMessage email = emails.get(0);
            assertEquals("Testing from Spring Boot", email.getHeaderValue("Subject"));
            assertEquals("to_@email", email.getHeaderValue("To"));
            assertTrue(email.getBody().indexOf("<h1>Check attachment for image!</h1>") > 0);
        }
    }
}
