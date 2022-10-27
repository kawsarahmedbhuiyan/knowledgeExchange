package net.therap.knowledgeExchange.helper;

import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static net.therap.knowledgeExchange.common.RoleType.MEMBER;
import static net.therap.knowledgeExchange.utils.Constant.USER;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Component
public class RegistrationHelper {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MessageSourceAccessor msa;

    public void setUpReferenceData(User user, ModelMap model) {
        user.getRoles().add(roleService.findByType(MEMBER));

        model.addAttribute(USER, user);
    }

    public void setUpFlashData(String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", msa.getMessage(message));
    }
}