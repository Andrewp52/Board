package com.pashenko.Board.events;

import com.pashenko.Board.entities.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnPasswordResetRequested extends ApplicationEvent {
    private final Locale locale;
    public OnPasswordResetRequested(User user, Locale locale) {
        super(user);
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }
}
