package com.kawsar.knowledgeExchange.validator;

import com.kawsar.knowledgeExchange.domain.User;
import com.kawsar.knowledgeExchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Component
public class UserValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (userService.isDuplicateByUsername(user)) {
            errors.rejectValue("username", "duplicate.username",
                    "A user with this username already exists!");
        }
    }
}