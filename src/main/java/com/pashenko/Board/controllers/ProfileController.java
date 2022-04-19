package com.pashenko.Board.controllers;

import com.pashenko.Board.entities.User;
import com.pashenko.Board.entities.dto.PassChangeDto;
import com.pashenko.Board.entities.dto.UserDto;
import com.pashenko.Board.events.OnProfileChangeComplete;
import com.pashenko.Board.exceptions.profile.WrongPasswordException;
import com.pashenko.Board.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class ProfileController {
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ProfileController(UserService userService, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/profile")
    @Secured("ROLE_USER")
    String getUserProfile(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        model.addAttribute("userDto", user.getFullDto());
        return "profile";
    }

    @GetMapping("/profile/edit")
    @Secured("ROLE_USER")
    String getProfileEditForm(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        model.addAttribute("userDto", user.getShortDto());
        return "profile-edit";
    }

    @GetMapping("/profile/changePass")
    @Secured("ROLE_USER")
    String getChangePasswordForm(Authentication authentication, Model model){
        User caller = (User) authentication.getPrincipal();
        model.addAttribute("passChangeDto", new PassChangeDto(caller.getId()));
        return "change-pass";
    }

    @PostMapping("/profile/changePass")
    @Secured("ROLE_USER")
    String updateUserPassword(@Valid @ModelAttribute PassChangeDto dto, BindingResult bindingResult, Authentication authentication, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute(dto);
            return "change-pass";
        }
        try {
            userService.changeUserPassword((User) authentication.getPrincipal(), dto);
        } catch (WrongPasswordException e){
            bindingResult.addError(new FieldError(dto.getClass().getSimpleName(), "oldPass", e.getMessage()));
            dto.setOldPass("");
            model.addAttribute(dto);
            return "change-pass";
        }
        model.addAttribute("result", "passChanged");
        model.addAttribute("label", "Success");
        model.addAttribute("message", "Password changed!");
        return "operation-result";
    }

    @PostMapping("/profile")
    @Secured("ROLE_USER")
    String applyProfileChanges(
            @Valid @ModelAttribute(name = "userDto") UserDto newProfile,
            BindingResult bindingResult,
            Authentication authentication,
            Model model
    ){
        if(bindingResult.hasErrors()){
            model.addAttribute("user", newProfile);
            return "profile-edit";
        }
        User caller = (User) authentication.getPrincipal();
        User changed = userService.updateProfile(caller, newProfile);
        eventPublisher.publishEvent(new OnProfileChangeComplete(changed));
        model.addAttribute("user", changed.getFullDto());
        return "profile";
    }

    @ExceptionHandler(AccessDeniedException.class)
    String handleAccessDeniedException(AccessDeniedException e, Model model){
        model.addAttribute("result", "error");
        model.addAttribute("label", "Something went wrong!");
        model.addAttribute("errMessage", e.getMessage());
        return "operation-result";
    }

}
