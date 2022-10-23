package net.therap.knowledgeExchange.helper;

import net.therap.knowledgeExchange.domain.Credential;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

import static net.therap.knowledgeExchange.common.RoleType.MEMBER;
import static net.therap.knowledgeExchange.utils.Constant.CREDENTIAL;
import static net.therap.knowledgeExchange.utils.Constant.USER;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Component
public class AuthenticationHelper {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MessageSource messageSource;

    public void setUpReferenceData(Credential credential, ModelMap model) {
        model.addAttribute(CREDENTIAL, credential);
    }

    public void setUpReferenceData(User user, ModelMap model) {
        user.getRoles().add(roleService.findByType(MEMBER));

        model.addAttribute(USER, user);
    }

    public void setUpFlashData(String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message",
                messageSource.getMessage(message, null, Locale.ENGLISH));
    }
}