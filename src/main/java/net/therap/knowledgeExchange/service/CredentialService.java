package net.therap.knowledgeExchange.service;

import net.therap.knowledgeExchange.domain.Credential;
import net.therap.knowledgeExchange.utils.HashGenerationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Service
public class CredentialService {

    @Autowired
    private UserService userService;

    public boolean isValidCredential(Credential credential) {
        if (isNull(credential.getPassword())) {
            return false;
        }

        credential.setPassword(HashGenerationUtil.getHashedValue(credential.getPassword()));

        return userService.isValidCredential(credential);
    }
}