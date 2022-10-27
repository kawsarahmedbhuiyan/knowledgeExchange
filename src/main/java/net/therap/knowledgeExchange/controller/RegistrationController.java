package net.therap.knowledgeExchange.controller;

import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.helper.RegistrationHelper;
import net.therap.knowledgeExchange.service.UserService;
import net.therap.knowledgeExchange.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static net.therap.knowledgeExchange.utils.Constant.USER;
import static net.therap.knowledgeExchange.utils.Constant.WELCOME_MESSAGE;
import static net.therap.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static net.therap.knowledgeExchange.utils.SessionUtil.setUpSessionData;
import static net.therap.knowledgeExchange.utils.Url.HOME;
import static net.therap.knowledgeExchange.utils.Url.REGISTRATION_PAGE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private RegistrationHelper registrationHelper;

    @InitBinder(USER)
    public void initBinderForUser(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.setDisallowedFields("id");
        binder.addValidators(userValidator);
    }

    @RequestMapping(method = GET)
    public String register(ModelMap model) {
        registrationHelper.setUpReferenceData(new User(), model);

        return REGISTRATION_PAGE;
    }

    @RequestMapping(method = POST)
    public String register(@Valid @ModelAttribute User user,
                           Errors errors,
                           HttpServletRequest request,
                           SessionStatus sessionStatus,
                           RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return REGISTRATION_PAGE;
        }

        logger.debug("[Authentication]: REGISTER");

        userService.saveOrUpdate(user);

        setUpSessionData(user, request);

        registrationHelper.setUpFlashData(WELCOME_MESSAGE, redirectAttributes);

        return redirectTo(HOME);
    }
}