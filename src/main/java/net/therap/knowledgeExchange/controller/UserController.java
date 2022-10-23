package net.therap.knowledgeExchange.controller;

import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.customEditor.RoleEditor;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.Role;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.helper.UserHelper;
import net.therap.knowledgeExchange.service.AccessCheckerService;
import net.therap.knowledgeExchange.service.ForumService;
import net.therap.knowledgeExchange.service.UserService;
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

import static net.therap.knowledgeExchange.common.Action.UPDATE;
import static net.therap.knowledgeExchange.common.Action.VIEW;
import static net.therap.knowledgeExchange.controller.UserController.USER;
import static net.therap.knowledgeExchange.utils.Constant.USER_UPDATED_MESSAGE;
import static net.therap.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static net.therap.knowledgeExchange.utils.Url.USER_VIEW;

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

    @Autowired
    private RoleEditor roleEditor;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private UserService userService;

    @Autowired
    private ForumService forumService;

    @Autowired
    private AccessCheckerService accessCheckerService;

    @InitBinder(USER)
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Role.class, roleEditor);

        binder.setDisallowedFields("id");
    }

    @GetMapping("/list")
    public String viewList(@RequestParam int forumId,
                           @RequestParam Status status,
                           HttpServletRequest request,
                           ModelMap model) {

        Forum forum = forumService.findById(forumId);

        userHelper.setUpReferenceData(forum, status, model);

        return USER_LIST_PAGE;
    }

    @GetMapping("/view")
    public String view(@RequestParam int userId,
                       ModelMap model) {

        User user = userService.findById(userId);

        userHelper.setUpReferenceData(VIEW, user, model);

        return USER_VIEW_PAGE;
    }

    @GetMapping("/update")
    public String update(@RequestParam int userId,
                         HttpServletRequest request,
                         ModelMap model) {

        User user = userService.findById(userId);

        accessCheckerService.checkUserUpdateAccess(request, user);

        userHelper.setUpReferenceData(UPDATE, user, model);

        return USER_FORM_PAGE;
    }

    @PostMapping("/update")
    public String process(@Valid @ModelAttribute User user,
                          Errors errors,
                          HttpServletRequest request,
                          ModelMap model,
                          SessionStatus sessionStatus,
                          RedirectAttributes redirectAttributes) {

        accessCheckerService.checkUserUpdateAccess(request, user);

        if (errors.hasErrors()) {
            userHelper.setUpReferenceData(model);

            return USER_FORM_PAGE;
        }

        userService.saveOrUpdate(user);

        sessionStatus.setComplete();

        userHelper.setUpFlashData(USER_UPDATED_MESSAGE, redirectAttributes);

        return redirectTo(USER_VIEW + user.getId());
    }
}