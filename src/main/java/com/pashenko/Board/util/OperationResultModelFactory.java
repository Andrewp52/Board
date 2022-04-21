package com.pashenko.Board.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OperationResultModelFactory {
    private final ResourceBundleMessageSource messageSource;

    public Map<String, String> getSignupCompleteModel(Locale locale){
        return getModelFromPrefix("operation.result.signup.complete", locale, false);
    }

    public Map<String, String> getSignupConfirmedModel(Locale locale){
        return getModelFromPrefix("operation.result.signup.confirmed", locale, false);
    }

    public Map<String, String> getSignupTokenExpiredModel(Locale locale){
        return getModelFromPrefix("operation.result.signup.token.expired", locale, true);
    }

    public Map<String, String> getPasswordChangedModel(Locale locale) {
        return getModelFromPrefix("operation.result.password.changed", locale, false);
    }

    private Map<String, String> getModelFromPrefix(String prefix, Locale locale, boolean error){
        String label = messageSource.getMessage(prefix + ".label", null, locale);
        String message = messageSource.getMessage(prefix + ".message", null, locale);
        Map<String, String> model = new HashMap<>();
        model.put("result", error ? "error" : "");
        model.put("label", label);
        model.put("message", message);
        return model;
    }
}
