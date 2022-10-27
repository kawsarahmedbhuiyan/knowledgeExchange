package com.kawsar.knowledgeExchange.helper;

import com.kawsar.knowledgeExchange.common.Action;
import com.kawsar.knowledgeExchange.common.Status;
import com.kawsar.knowledgeExchange.domain.Enrollment;
import com.kawsar.knowledgeExchange.domain.Forum;
import com.kawsar.knowledgeExchange.domain.Post;
import com.kawsar.knowledgeExchange.domain.User;
import com.kawsar.knowledgeExchange.exception.UnauthorizedException;
import com.kawsar.knowledgeExchange.service.EnrollmentService;
import com.kawsar.knowledgeExchange.service.ForumService;
import com.kawsar.knowledgeExchange.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static com.kawsar.knowledgeExchange.common.Status.APPROVED;
import static com.kawsar.knowledgeExchange.controller.ForumController.FORUM;
import static com.kawsar.knowledgeExchange.utils.Constant.CREATION_REQUEST_LIST;
import static com.kawsar.knowledgeExchange.utils.Constant.JOIN_REQUEST_LIST;
import static com.kawsar.knowledgeExchange.utils.SessionUtil.getSessionUser;

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
    private MessageSourceAccessor msa;

    public void setUpReferenceData(ModelMap model) {
        List<Forum> forums = forumService.findAllByStatus(APPROVED);

        model.addAttribute("forums", forums);
    }

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
        model.addAttribute("action", action.name().toLowerCase());
    }

    public void setUpReferenceData(Action action, ModelMap model) {
        model.addAttribute("action", action.name().toLowerCase());
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
        model.addAttribute("listType", listType);
    }

    public void setUpFlashData(String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", msa.getMessage(message));
    }

    public void checkAccess(Action action, HttpServletRequest request, Forum forum) {
        User sessionUser = getSessionUser(request);

        switch (action) {
            case APPROVE:
            case DECLINE:
                if (!sessionUser.isAdmin()) {
                    throw new UnauthorizedException("You do not have admin access");
                }

                break;

            case DELETE:
                if (!(sessionUser.isAdmin() || forum.isManagedByUser(sessionUser))) {
                    throw new UnauthorizedException("You are not authorized to delete this forum");
                }

                break;
        }
    }
}