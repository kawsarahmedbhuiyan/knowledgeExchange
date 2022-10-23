package net.therap.knowledgeExchange.validator;

import net.therap.knowledgeExchange.domain.Credential;
import net.therap.knowledgeExchange.service.UserService;
import net.therap.knowledgeExchange.utils.HashGenerationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static java.util.Objects.isNull;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Component
public class CredentialValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Credential.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        Credential credential = (Credential) target;

        if (isNull(credential.getPassword())) {
            return;
        }

        credential.setPassword(HashGenerationUtil.getHashedValue(credential.getPassword()));

        if (!userService.isValidCredential(credential)) {
            errors.reject("invalid.credential");
        }
    }
}