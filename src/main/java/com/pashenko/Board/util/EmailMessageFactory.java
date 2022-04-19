package com.pashenko.Board.util;

import com.pashenko.Board.entities.EmailMessage;
import com.pashenko.Board.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;
import java.util.Map;

@Component
public class EmailMessageFactory {
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private TemplateEngine engine;

    public EmailMessage getPasswordResetMessage(User user, Map<String, Object> model, Locale locale){
        String body = getMessageBody("emails/password-reset", model, locale);
        return assemblyMessage(user, "Password reset", body);
    }

    public EmailMessage getAccountActivateMessage(User user, Map<String, Object> model, Locale locale){
        String body = getMessageBody("emails/activate-profile", model, locale);
        return assemblyMessage(user, "Profile activation", body);
    }

    public EmailMessage getProfileChangedMessage(User user, Locale locale){
        String body = getMessageBody("emails/profile-changed", locale);
        return assemblyMessage(user, "Profile changed", body);
    }

    private EmailMessage assemblyMessage(User user, String subject, String body){
        return EmailMessage.builder()
                .mailFrom(this.from)
                .rcptTo(user.getEmail())
                .subject(subject)
                .body(body)
                .build();
    }

    private String getMessageBody(String template, Map<String, Object> model, Locale locale){
        return engine.process(template, new Context(locale, model));
    }
    
    private String getMessageBody(String template, Locale locale){
        return engine.process(template, new Context(locale));
    }


}
