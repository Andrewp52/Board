package com.pashenko.Board.controllers;

import com.pashenko.Board.entities.User;
import com.pashenko.Board.events.OnSignUpComplete;
import com.pashenko.Board.exceptions.registration.*;
import com.pashenko.Board.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    ApplicationEventPublisher eventPublisher;
    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    String getLoginForm(){
        return "login";
    }

    @GetMapping("/signup")
    String getSignupForm(Model model){
        model.addAttribute("user", new User());
        return "signup";
    }

    @GetMapping("/signup/confirm")
    String confirmUserRegistration(@RequestParam(name = "token") String token, Model model){
        userService.confirmRegistration(token);
        model.addAttribute("result", "confirmed");
        return "operation-result";
    }

    @PostMapping("/signup")
    String registerNewUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            model.addAttribute("user", user);
            return "signup";
        }

        User registered = userService.registerNewUser(user);
        eventPublisher.publishEvent(new OnSignUpComplete(registered, request.getRequestURI()));
        model.addAttribute("result", "registered");
        return "operation-result";
    }

    @ExceptionHandler({
            UserIdIsNotNullException.class,
            ConfirmTokenNotFoundException.class,
            ConfirmTokenExpiredException.class
    })
    String handleRegistrationExceptions(RegistrationException e, Model model){
        model.addAttribute("result", "error");
        model.addAttribute("errMessage", e.getMessage());
        return "operation-result";
    }

}
