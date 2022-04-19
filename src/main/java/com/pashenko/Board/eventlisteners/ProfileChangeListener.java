package com.pashenko.Board.eventlisteners;

import com.pashenko.Board.entities.EmailMessage;
import com.pashenko.Board.entities.User;
import com.pashenko.Board.events.OnProfileChangeComplete;
import com.pashenko.Board.services.SendMailService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public class ProfileChangeListener implements ApplicationListener<OnProfileChangeComplete> {
    private final SendMailService mailService;
    private final String from;

    @Autowired
    public ProfileChangeListener(@Value("${spring.mail.username}") String from, SendMailService mailService) {
        this.mailService = mailService;
        this.from = from;
    }

    @SneakyThrows
    @Async
    @Override
    public void onApplicationEvent(OnProfileChangeComplete event) {
       sendMessage(event);
    }

    private void sendMessage(OnProfileChangeComplete event) throws MessagingException {
        User user = (User) event.getSource();
        EmailMessage message = EmailMessage.builder()
                .mailFrom(this.from)
                .rcptTo(user.getEmail())
                .subject("Profile data changed")
                .body("")
                .build();
        mailService.sendMessage(message);
    }
}
