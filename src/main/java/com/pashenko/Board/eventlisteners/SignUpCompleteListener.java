package com.pashenko.Board.eventlisteners;

import com.pashenko.Board.entities.ConfirmationToken;
import com.pashenko.Board.entities.EmailMessage;
import com.pashenko.Board.entities.User;
import com.pashenko.Board.events.OnSignUpComplete;
import com.pashenko.Board.services.SendMailService;
import com.pashenko.Board.services.UserService;
import com.pashenko.Board.util.EmailMessageFactory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


import javax.mail.MessagingException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class SignUpCompleteListener implements ApplicationListener<OnSignUpComplete> {
    private final SendMailService mailService;
    private final UserService userService;
    private final EmailMessageFactory messageFactory;

    @Autowired
    public SignUpCompleteListener(SendMailService mailService, UserService userService, EmailMessageFactory messageFactory) {
        this.mailService = mailService;
        this.userService = userService;
        this.messageFactory = messageFactory;
    }

    @SneakyThrows
    @Async
    @Override
    public void onApplicationEvent(OnSignUpComplete event) {
        confirmRegistration(event);
    }

    public void confirmRegistration(OnSignUpComplete event) throws MessagingException {
        User user = (User) event.getSource();
        ConfirmationToken token = userService.getConfirmToken((User) event.getSource());
        Map<String, Object> model = new HashMap<>();
        model.put("token", token);
        model.put("expires", token.getExpirationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        EmailMessage message = messageFactory.getAccountActivateMessage(user, model, event.getLocale());
        mailService.sendMessage(message);
    }
}
