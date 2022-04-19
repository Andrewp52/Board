package com.pashenko.Board.controllers;

import com.pashenko.Board.entities.User;
import com.pashenko.Board.events.OnPasswordResetRequested;
import com.pashenko.Board.services.UserService;
import com.pashenko.Board.util.EmailMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private EmailMessageFactory messageFactory;
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping
    ResponseEntity getMessage(HttpServletRequest request){
        User u = (User) userService.loadUserByUsername("Andrew.p52@yandex.ru");
        eventPublisher.publishEvent(
                new OnPasswordResetRequested(u, request.getLocale())
        );
        return ResponseEntity.ok().build();
    }
}
