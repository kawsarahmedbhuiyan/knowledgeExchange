package net.therap.knowledgeExchange.validator;

import net.therap.knowledgeExchange.domain.Credential;
import net.therap.knowledgeExchange.service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Component
public class CredentialValidator implements Validator {

    @Autowired
    private CredentialService credentialService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Credential.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        Credential credential = (Credential) target;

        if (!credentialService.isValidCredential(credential)) {
            errors.reject("invalid.credential");
        }
    }
}