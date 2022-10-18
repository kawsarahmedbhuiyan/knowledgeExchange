package net.therap.knowledgeExchange.helper;

import net.therap.knowledgeExchange.common.Action;
import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Enrollment;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.Post;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.service.EnrollmentService;
import net.therap.knowledgeExchange.service.ForumService;
import net.therap.knowledgeExchange.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Objects.nonNull;
import static net.therap.knowledgeExchange.common.Status.APPROVED;
import static net.therap.knowledgeExchange.controller.ForumController.FORUM;
import static net.therap.knowledgeExchange.utils.Constant.CREATION_REQUEST_LIST;
import static net.therap.knowledgeExchange.utils.Constant.JOIN_REQUEST_LIST;
import static net.therap.knowledgeExchange.utils.SessionUtil.getSessionUser;

/**
 * @author kawsar.bhuiyan
 * @since 10/15/22
 */
@Component
public class ForumHelper {

    @Autowired
    private ForumService forumService;

    @Autowired
    private PostService postService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private MessageSource messageSource;

    public void setUpReferenceData(int forumId, HttpServletRequest request, ModelMap model) {
        Forum forum = forumService.findById(forumId);

        model.addAttribute(FORUM, forum);

        User user = getSessionUser(request);

        Enrollment enrollment = enrollmentService.findByForumAndUser(forum, user);

        if (nonNull(enrollment)) {
            model.addAttribute(enrollment.getStatus().name(), true);
        }

        List<Post> approvedPosts = postService.findAllByForumAndStatus(forum, APPROVED);

        model.addAttribute("approvedPosts", approvedPosts);
    }

    public void setUpReferenceData(Action action, HttpServletRequest request, ModelMap model) {
        User manager = getSessionUser(request);

        model.addAttribute(FORUM, new Forum(manager));
        model.addAttribute(action.name(), true);
    }

    public void setUpReferenceData(Action action, ModelMap model) {
        model.addAttribute(action.name(), true);
    }

    public void setUpReferenceData(Status status, HttpServletRequest request, ModelMap model, String listType) {
        User user = getSessionUser(request);

        List<Forum> forums = new ArrayList<>();

        switch (listType) {
            case CREATION_REQUEST_LIST:
                forums = user.isAdmin() ? forumService.findAllByStatus(status) :
                        forumService.findAllByManagerAndStatus(user, status);
                break;

            case JOIN_REQUEST_LIST:
                for (Enrollment enrollment : enrollmentService.findByUserAndStatus(user, status)) {
                    forums.add(enrollment.getForum());
                }
                break;
        }

        model.addAttribute("forums", forums);
        model.addAttribute(status.name(), true);
        model.addAttribute("list", listType);
    }

    public void setUpFlashData(String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message",
                messageSource.getMessage(message, null, Locale.ENGLISH));
    }
}