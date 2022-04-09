package com.pashenko.Board.events;

import com.pashenko.Board.entities.User;
import org.springframework.context.ApplicationEvent;


public class OnSignUpComplete extends ApplicationEvent {
    private final User user;
    private final String url;

    public OnSignUpComplete(User source, String url) {
        super(source);
        this.user = source;
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public String getUrl() {
        return url;
    }
}
