package net.therap.knowledgeExchange.controller;

import net.therap.knowledgeExchange.domain.Credential;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.helper.AuthenticationHelper;
import net.therap.knowledgeExchange.service.UserService;
import net.therap.knowledgeExchange.validator.CredentialValidator;
import net.therap.knowledgeExchange.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static net.therap.knowledgeExchange.utils.Constant.*;
import static net.therap.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static net.therap.knowledgeExchange.utils.SessionUtil.setUpSessionData;
import static net.therap.knowledgeExchange.utils.Url.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Controller
@SessionAttributes({CREDENTIAL, USER})
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private CredentialValidator credentialValidator;

    @Autowired
    private AuthenticationHelper authenticationHelper;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        binder.registerCustomEditor(String.class, stringTrimmerEditor);

        binder.setDisallowedFields("id");

        if (CREDENTIAL.equals(binder.getObjectName())) {
            binder.addValidators(credentialValidator);
        } else {
            binder.addValidators(userValidator);
        }
    }

    @RequestMapping(value = "/login", method = GET)
    public String login(ModelMap model) {
        authenticationHelper.setUpReferenceData(new Credential(), model);

        return LOGIN_PAGE;
    }

    @RequestMapping(value = "/login", method = POST)
    public String login(@Valid @ModelAttribute Credential credential,
                        Errors errors,
                        HttpServletRequest request,
                        SessionStatus sessionStatus,
                        RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return LOGIN_PAGE;
        }

        setUpSessionData(userService.findByUsername(credential.getUsername()), request);

        authenticationHelper.setUpFlashData(WELCOME_MESSAGE, redirectAttributes);

        sessionStatus.setComplete();

        return redirectTo(HOME);
    }

    @RequestMapping(value = "/logout", method = GET)
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();

        return redirectTo(LOGIN);
    }

    @RequestMapping(value = "/register", method = GET)
    public String register(ModelMap model) {
        authenticationHelper.setUpReferenceData(new User(), model);

        return REGISTRATION_PAGE;
    }

    @RequestMapping(value = "/register", method = POST)
    public String register(@Valid @ModelAttribute User user,
                           Errors errors,
                           HttpServletRequest request,
                           SessionStatus sessionStatus,
                           RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return REGISTRATION_PAGE;
        }

        userService.saveOrUpdate(user);

        setUpSessionData(user, request);

        authenticationHelper.setUpFlashData(WELCOME_MESSAGE, redirectAttributes);

        sessionStatus.setComplete();

        return redirectTo(HOME);
    }
}