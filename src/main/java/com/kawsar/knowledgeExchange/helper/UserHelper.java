package com.kawsar.knowledgeExchange.helper;

import com.kawsar.knowledgeExchange.common.Action;
import com.kawsar.knowledgeExchange.common.Status;
import com.kawsar.knowledgeExchange.domain.Forum;
import com.kawsar.knowledgeExchange.domain.User;
import com.kawsar.knowledgeExchange.exception.UnauthorizedException;
import com.kawsar.knowledgeExchange.service.EnrollmentService;
import com.kawsar.knowledgeExchange.service.ForumService;
import com.kawsar.knowledgeExchange.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.kawsar.knowledgeExchange.common.Action.VIEW;
import static com.kawsar.knowledgeExchange.common.Status.APPROVED;
import static com.kawsar.knowledgeExchange.controller.UserController.USER;
import static com.kawsar.knowledgeExchange.utils.SessionUtil.getSessionUser;

/**
 * @author kawsar.bhuiyan
 * @since 10/18/22
 */
@Component
public class UserHelper {

    @Autowired
    private MessageSourceAccessor msa;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ForumService forumService;

    @Autowired
    private EnrollmentService enrollmentService;

    public void setUpReferenceData(Forum forum, Status status, ModelMap model) {
        List<User> users = new ArrayList<>();

        enrollmentService.findByForumAndStatus(forum, status).forEach(enrollment -> users.add(enrollment.getUser()));

        model.addAttribute("users", users);
        model.addAttribute("forum", forum);
        model.addAttribute(status.name(), true);
    }

    public void setUpReferenceData(Action action, User user, ModelMap model) {
        model.addAttribute(USER, user);

        if (VIEW.equals(action)) {
            List<Forum> joinedForums = new ArrayList<>();
            enrollmentService.findByUserAndStatus(user, APPROVED)
                    .forEach(enrollment -> joinedForums.add(enrollment.getForum()));

            List<Forum> managedForums = forumService.findAllByManagerAndStatus(user, APPROVED);

            model.addAttribute("joinedForums", joinedForums);
            model.addAttribute("managedForums", managedForums);
        } else {
            model.addAttribute("roleList", roleService.findAll());
        }
    }

    public void setUpReferenceData(ModelMap model) {
        model.addAttribute("roleList", roleService.findAll());
    }

    public void setUpFlashData(String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", msa.getMessage(message));
    }

    public void checkAccess(HttpServletRequest request, User user) {
        User sessionUser = getSessionUser(request);

        if (!(sessionUser.isAdmin() || sessionUser.equals(user))) {
            throw new UnauthorizedException("You are not authorized to update this user");
        }
    }
}