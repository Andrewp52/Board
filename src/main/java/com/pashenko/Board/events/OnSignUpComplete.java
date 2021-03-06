package com.pashenko.Board.events;

import com.pashenko.Board.entities.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;


public class OnSignUpComplete extends ApplicationEvent {
    private final Locale locale;

    public OnSignUpComplete(User source, Locale locale) {
        super(source);
        this.locale = locale;
    }

    public Locale getLocale() {
        return this.locale;
    }
}
