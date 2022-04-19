package com.pashenko.Board.eventlisteners;

import com.pashenko.Board.entities.EmailMessage;
import com.pashenko.Board.entities.User;
import com.pashenko.Board.events.OnPasswordResetRequested;
import com.pashenko.Board.services.SendMailService;
import com.pashenko.Board.services.UserService;
import com.pashenko.Board.util.EmailMessageFactory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PasswordChangeListener implements ApplicationListener<OnPasswordResetRequested> {
    private final UserService userService;
    private final SendMailService mailService;
    private final EmailMessageFactory messageFactory;

    @Autowired
    public PasswordChangeListener(UserService userService, SendMailService mailService, EmailMessageFactory messageFactory) {
        this.userService = userService;
        this.mailService = mailService;
        this.messageFactory = messageFactory;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(OnPasswordResetRequested event) {
        User user = (User) event.getSource();
        Map<String, Object> model = new HashMap<>();

        model.put("token", userService.getConfirmToken((User) event.getSource()));
        EmailMessage messageBody = messageFactory.getPasswordResetMessage(user, model, event.getLocale());

        mailService.sendMessage(messageBody);
    }
}
