package com.pashenko.Board.util;

import com.pashenko.Board.entities.EmailMessage;
import com.pashenko.Board.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
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
    @Autowired
    ResourceBundleMessageSource messageSource;

    public EmailMessage getPasswordResetMessage(User user, Map<String, Object> model, Locale locale){
        String body = getMessageBody("emails/password-reset", model, locale);
        String subject = messageSource.getMessage("email.template.password.reset.subject", null, locale);
        return assemblyMessage(user, subject, body);
    }

    public EmailMessage getAccountActivateMessage(User user, Map<String, Object> model, Locale locale){
        String body = getMessageBody("emails/activate-profile", model, locale);
        String subject = messageSource.getMessage("email.template.registration.confirm.subject", null, locale);
        return assemblyMessage(user, subject, body);
    }

    public EmailMessage getProfileChangedMessage(User user, Map<String, Object> model, Locale locale){
        String body = getMessageBody("emails/profile-changed", model, locale);
        String subject = messageSource.getMessage("email.template.profile.changed.subject", null, locale);
        return assemblyMessage(user, subject, body);
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
}
