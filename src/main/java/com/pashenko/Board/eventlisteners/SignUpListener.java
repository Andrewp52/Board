package com.pashenko.Board.eventlisteners;

import com.pashenko.Board.entities.EmailMessage;
import com.pashenko.Board.entities.User;
import com.pashenko.Board.events.OnSignUpComplete;
import com.pashenko.Board.services.SendMailService;
import com.pashenko.Board.services.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


import javax.mail.MessagingException;

@Component
public class SignUpListener implements ApplicationListener<OnSignUpComplete> {
    private final String from;
    private final UserService userService;
    private final SendMailService mailService;

    @Autowired
    public SignUpListener(@Value("${spring.mail.username}") String from, UserService userService, SendMailService mailService) {
        this.from = from;
        this.userService = userService;
        this.mailService = mailService;
    }

    @SneakyThrows
    @Async
    @Override
    public void onApplicationEvent(OnSignUpComplete event) {
        confirmRegistration(event);
    }

    public void confirmRegistration(OnSignUpComplete event) throws MessagingException, InterruptedException {
        User user = event.getUser();
        String token = userService.getConfirmToken(user);
        EmailMessage message = EmailMessage.builder()
                .mailFrom(this.from)
                .rcptTo(user.getEmail())
                .subject("Registration confirmation")
                .body("""
                         <p>Hello!</p>
                         <p>Follow the link below to confirm registration</p>
                         <a href=http://localhost:8080%s/confirm?token=%s>Confirm registration</a>
                        """.formatted(event.getUrl(), token)
                ).build();
        mailService.sendMessage(message);
//        String message1 = """
//                         <p>Hello!</p>
//                         <p>Follow the link below to confirm registration</p>
//                         <a href=http://localhost:8080%s/confirm?token=%s>Confirm registration</a>
//                        """.formatted(event.getUrl(), token);
//
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        mimeMessage.setFrom(from);
//        mimeMessage.setSubject("Registration confirmation");
//        mimeMessage.setRecipients(Message.RecipientType.TO, user.getEmail());
//
//        MimeBodyPart mimeBodyPart = new MimeBodyPart();
//        mimeBodyPart.setContent(message, "text/html; charset=utf-8");
//
//        Multipart multipart = new MimeMultipart();
//        multipart.addBodyPart(mimeBodyPart);
//
//        mimeMessage.setContent(multipart);
//        mailSender.send(mimeMessage);
    }
}
