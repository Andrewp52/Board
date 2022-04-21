package com.pashenko.Board.eventlisteners;

import com.pashenko.Board.entities.EmailMessage;
import com.pashenko.Board.entities.User;
import com.pashenko.Board.events.OnProfileChangeComplete;
import com.pashenko.Board.services.SendMailService;
import com.pashenko.Board.util.EmailMessageFactory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProfileChangeListener implements ApplicationListener<OnProfileChangeComplete> {
    private final SendMailService mailService;
    private final EmailMessageFactory messageFactory;
    private final String from;

    @Autowired
    public ProfileChangeListener(@Value("${spring.mail.username}") String from, EmailMessageFactory messageFactory, SendMailService mailService) {
        this.mailService = mailService;
        this.messageFactory = messageFactory;
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
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        EmailMessage message = messageFactory.getProfileChangedMessage(user, model, event.getLocale());
        mailService.sendMessage(message);
    }
}
