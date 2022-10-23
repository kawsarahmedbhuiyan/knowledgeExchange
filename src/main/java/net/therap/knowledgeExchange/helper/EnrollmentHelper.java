package net.therap.knowledgeExchange.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static java.util.Locale.ENGLISH;

/**
 * @author kawsar.bhuiyan
 * @since 10/17/22
 */
@Component
public class EnrollmentHelper {

    @Autowired
    private MessageSource messageSource;

    public void setUpFlashData(String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message",
                messageSource.getMessage(message, null, ENGLISH));
    }
}