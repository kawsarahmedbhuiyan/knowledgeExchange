package com.kawsar.knowledgeExchange.helper;

import com.kawsar.knowledgeExchange.domain.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.kawsar.knowledgeExchange.utils.Constant.CREDENTIAL;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Component
public class LoginHelper {

    @Autowired
    private MessageSourceAccessor msa;

    public void setUpReferenceData(Credential credential, ModelMap model) {
        model.addAttribute(CREDENTIAL, credential);
    }

    public void setUpFlashData(String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", msa.getMessage(message));
    }
}