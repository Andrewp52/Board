package com.pashenko.Board.services.scheduled;

import com.pashenko.Board.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class WipeExpiredConfirmTokensAndUsers {
    private final UserService service;

    @Autowired
    public WipeExpiredConfirmTokensAndUsers(UserService service) {
        this.service = service;
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 30L)
    public void wipeExpiredTokens(){
        System.out.println("Wiping expired signup tokens");
        service.deleteExpiredTokensAndUsers();
    }
}
