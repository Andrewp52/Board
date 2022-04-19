package com.pashenko.Board.events;

import com.pashenko.Board.entities.User;
import org.springframework.context.ApplicationEvent;


public class OnProfileChangeComplete extends ApplicationEvent {
    private final User user;

    public OnProfileChangeComplete(User user) {
        super(user);
        this.user = user;
    }
}
