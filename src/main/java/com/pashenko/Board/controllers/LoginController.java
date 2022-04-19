package com.pashenko.Board.controllers;

import com.pashenko.Board.entities.User;
import com.pashenko.Board.entities.dto.UserRegDto;
import com.pashenko.Board.events.OnSignUpComplete;
import com.pashenko.Board.exceptions.registration.*;
import com.pashenko.Board.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
        model.addAttribute("userRegDto", new UserRegDto());
        return "signup";
    }

    @GetMapping("/signup/confirm")
    String confirmUserRegistration(@RequestParam(name = "token") String token, Model model){
        userService.confirmRegistration(token);
        model.addAttribute("result", "confirmed");
        model.addAttribute("label", "Success!");
        model.addAttribute("message", "Account activated.");
        return "operation-result";
    }

    @GetMapping("/login/restorePass")
    String getRestorePassForm(){
        return "restore-pass";
    }

    @PostMapping("/signup")
    String registerNewUser(@Valid @ModelAttribute UserRegDto dto, BindingResult bindingResult, Model model, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            model.addAttribute("userRegDto", dto);
            return "signup";
        }
        try{
            User registered = userService.registerNewUser(dto);
            model.addAttribute("result", "registered");
            model.addAttribute("label", "Almost done!");
            model.addAttribute("message", "Confirmation e-mail was sent. Check your email to proceed registration.");
            eventPublisher.publishEvent(new OnSignUpComplete(registered, request.getLocale()));
            return "operation-result";
        } catch (EmailOccupiedExceprtion e){
            bindingResult.addError(new FieldError(dto.getClass().getSimpleName(), "email", "E-mail is already registered"));
            model.addAttribute("userRegDto", dto);
            return "signup";
        }

    }

    @ExceptionHandler({
            UserIdIsNotNullException.class,
            ConfirmTokenNotFoundException.class,
            ConfirmTokenExpiredException.class
    })
    String handleRegistrationExceptions(RegistrationException e, Model model){
        model.addAttribute("result", "error");
        model.addAttribute("label", "Something went wrong!");
        model.addAttribute("errMessage", e.getMessage());
        return "operation-result";
    }

}
