package com.kawsar.knowledgeExchange.helper;

import com.kawsar.knowledgeExchange.domain.User;
import com.kawsar.knowledgeExchange.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.kawsar.knowledgeExchange.common.RoleType.MEMBER;
import static com.kawsar.knowledgeExchange.utils.Constant.USER;

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