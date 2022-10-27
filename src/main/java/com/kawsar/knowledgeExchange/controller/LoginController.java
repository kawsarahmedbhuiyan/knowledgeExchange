package com.kawsar.knowledgeExchange.controller;

import com.kawsar.knowledgeExchange.domain.Credential;
import com.kawsar.knowledgeExchange.helper.LoginHelper;
import com.kawsar.knowledgeExchange.service.UserService;
import com.kawsar.knowledgeExchange.validator.CredentialValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.kawsar.knowledgeExchange.utils.Constant.CREDENTIAL;
import static com.kawsar.knowledgeExchange.utils.Constant.WELCOME_MESSAGE;
import static com.kawsar.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static com.kawsar.knowledgeExchange.utils.SessionUtil.setUpSessionData;
import static com.kawsar.knowledgeExchange.utils.Url.HOME;
import static com.kawsar.knowledgeExchange.utils.Url.LOGIN_PAGE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private CredentialValidator credentialValidator;

    @InitBinder(CREDENTIAL)
    public void initBinderForCredential(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.setAllowedFields("username", "password");
        binder.addValidators(credentialValidator);
    }


    @RequestMapping(method = GET)
    public String login(ModelMap model) {
        loginHelper.setUpReferenceData(new Credential(), model);

        return LOGIN_PAGE;
    }

    @RequestMapping(method = POST)
    public String login(@Valid @ModelAttribute Credential credential,
                        Errors errors,
                        HttpServletRequest request,
                        SessionStatus sessionStatus,
                        RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return LOGIN_PAGE;
        }

        setUpSessionData(userService.findByUsername(credential.getUsername()), request);

        loginHelper.setUpFlashData(WELCOME_MESSAGE, redirectAttributes);

        return redirectTo(HOME);
    }
}