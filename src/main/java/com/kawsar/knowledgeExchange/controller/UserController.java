package com.kawsar.knowledgeExchange.controller;

import com.kawsar.knowledgeExchange.common.Status;
import com.kawsar.knowledgeExchange.customEditor.RoleEditor;
import com.kawsar.knowledgeExchange.domain.Forum;
import com.kawsar.knowledgeExchange.domain.Role;
import com.kawsar.knowledgeExchange.domain.User;
import com.kawsar.knowledgeExchange.helper.UserHelper;
import com.kawsar.knowledgeExchange.service.ForumService;
import com.kawsar.knowledgeExchange.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static com.kawsar.knowledgeExchange.common.Action.UPDATE;
import static com.kawsar.knowledgeExchange.common.Action.VIEW;
import static com.kawsar.knowledgeExchange.controller.UserController.USER;
import static com.kawsar.knowledgeExchange.utils.Constant.USER_UPDATED_MESSAGE;
import static com.kawsar.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static com.kawsar.knowledgeExchange.utils.Url.USER_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author kawsar.bhuiyan
 * @since 10/18/22
 */
@Controller
@SessionAttributes(USER)
@RequestMapping("/user")
public class UserController {

    public static final String USER = "user";
    private static final String USER_LIST_PAGE = "/user/list";
    private static final String USER_FORM_PAGE = "/user/form";
    private static final String USER_VIEW_PAGE = "/user/view";

    private final Logger logger= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RoleEditor roleEditor;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private UserService userService;

    @Autowired
    private ForumService forumService;

    @InitBinder(USER)
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Role.class, roleEditor);

        binder.setAllowedFields("username", "password", "name", "roles");
    }

    @RequestMapping(value = "/list", method = GET)
    public String viewList(@RequestParam int forumId,
                           @RequestParam Status status,
                           ModelMap model) {

        Forum forum = forumService.findById(forumId);

        userHelper.setUpReferenceData(forum, status, model);

        return USER_LIST_PAGE;
    }

    @RequestMapping(value = "/view", method = GET)
    public String view(@RequestParam int userId,
                       ModelMap model) {

        User user = userService.findById(userId);

        userHelper.setUpReferenceData(VIEW, user, model);

        return USER_VIEW_PAGE;
    }

    @RequestMapping(value = "/update", method = GET)
    public String update(@RequestParam int userId,
                         HttpServletRequest request,
                         ModelMap model) {

        User user = userService.findById(userId);

        userHelper.checkAccess(request, user);

        userHelper.setUpReferenceData(UPDATE, user, model);

        return USER_FORM_PAGE;
    }

    @RequestMapping(value = "/update", method = POST)
    public String process(@Valid @ModelAttribute User user,
                          Errors errors,
                          HttpServletRequest request,
                          ModelMap model,
                          SessionStatus sessionStatus,
                          RedirectAttributes redirectAttributes) {

        userHelper.checkAccess(request, user);

        if (errors.hasErrors()) {
            userHelper.setUpReferenceData(model);

            return USER_FORM_PAGE;
        }

        logger.debug("[User]: UPDATE with id : {}", user.getId());

        userService.saveOrUpdate(user);

        sessionStatus.setComplete();

        userHelper.setUpFlashData(USER_UPDATED_MESSAGE, redirectAttributes);

        return redirectTo(USER_VIEW + user.getId());
    }
}