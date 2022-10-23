package net.therap.knowledgeExchange.helper;

import net.therap.knowledgeExchange.common.Action;
import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.service.EnrollmentService;
import net.therap.knowledgeExchange.service.ForumService;
import net.therap.knowledgeExchange.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

import static java.util.Locale.ENGLISH;
import static net.therap.knowledgeExchange.common.Action.VIEW;
import static net.therap.knowledgeExchange.common.Status.APPROVED;
import static net.therap.knowledgeExchange.controller.UserController.USER;

/**
 * @author kawsar.bhuiyan
 * @since 10/18/22
 */
@Component
public class UserHelper {

    @Autowired
    private MessageSource messageSource;

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
        redirectAttributes.addFlashAttribute("message",
                messageSource.getMessage(message, null, ENGLISH));
    }
}