package com.kawsar.knowledgeExchange.helper;

import com.kawsar.knowledgeExchange.common.Action;
import com.kawsar.knowledgeExchange.common.Status;
import com.kawsar.knowledgeExchange.domain.Comment;
import com.kawsar.knowledgeExchange.domain.Forum;
import com.kawsar.knowledgeExchange.domain.Post;
import com.kawsar.knowledgeExchange.domain.User;
import com.kawsar.knowledgeExchange.exception.UnauthorizedException;
import com.kawsar.knowledgeExchange.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.kawsar.knowledgeExchange.common.Status.*;
import static com.kawsar.knowledgeExchange.controller.PostController.POST;
import static com.kawsar.knowledgeExchange.utils.SessionUtil.getSessionUser;

/**
 * @author kawsar.bhuiyan
 * @since 10/18/22
 */
@Component
public class PostHelper {

    @Autowired
    private MessageSourceAccessor msa;

    @Autowired
    private PostService postService;

    public void setUpReferenceData(Forum forum, Status status, HttpServletRequest request, ModelMap model) {
        User user = getSessionUser(request);

        List<Post> posts = forum.isManagedByUser(user) ?
                postService.findAllByForumAndStatus(forum, status) :
                postService.findAllByForumAndUserAndStatus(forum, user, status);

        model.addAttribute("posts", posts);
        model.addAttribute("listType", "requestList");
    }

    public void setUpReferenceData(Post post, HttpServletRequest request, ModelMap model) {
        User user = getSessionUser(request);

        model.addAttribute(POST, post);
        model.addAttribute(post.getStatus().name(), true);
        model.addAttribute("LIKED", postService.isLikedByUser(post, user));
        model.addAttribute("comment", new Comment(post, user));
    }

    public void setUpReferenceData(Action action, Forum forum, HttpServletRequest request, ModelMap model) {
        User user = getSessionUser(request);

        if (user.equals(forum.getManager())) {
            model.addAttribute(POST, new Post(user, forum, APPROVED));
        } else {
            model.addAttribute(POST, new Post(user, forum, PENDING));
        }

        model.addAttribute("action", action.name().toLowerCase());
    }

    public void setUpReferenceData(Action action, Post post, ModelMap model) {
        if (!post.getUser().equals(post.getForum().getManager())) {
            post.setStatus(PENDING);
        }

        model.addAttribute(POST, post);
        model.addAttribute("action", action.name().toLowerCase());
    }

    public void setUpReferenceData(Action action, ModelMap model) {
        model.addAttribute("action", action.name().toLowerCase());
    }

    public void setUpFlashData(String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", msa.getMessage(message));
    }

    public void checkAccess(Action action, HttpServletRequest request, Post post) {
        User sessionUser = getSessionUser(request);

        switch (action) {
            case VIEW:
            case LIKE:
                if (post.getForum().getEnrollments().stream()
                        .noneMatch(enrollment -> sessionUser.equals(enrollment.getUser())) ||
                        DELETED.equals(post.getStatus())) {

                    throw new UnauthorizedException("You are not authorized");
                }

                break;

            case UPDATE:
                if (!sessionUser.equals(post.getUser()) || DELETED.equals(post.getStatus())) {
                    throw new UnauthorizedException("You are not authorized to update this post");
                }

                break;

            case DELETE:
                if (!(sessionUser.equals(post.getForum().getManager()) || sessionUser.equals(post.getUser()))) {
                    throw new UnauthorizedException("You are not authorized to delete this post");
                }

                break;
        }
    }

    public void checkAccess(Action action, HttpServletRequest request, Forum forum) {
        User sessionUser = getSessionUser(request);

        switch (action) {
            case SAVE:
                if (forum.getEnrollments().stream().noneMatch(enrollment -> sessionUser.equals(enrollment.getUser()))) {
                    throw new UnauthorizedException("You must join the forum to add a new post");
                }

                break;

            case APPROVE:
            case DECLINE:
                if (!sessionUser.equals(forum.getManager())) {
                    throw new UnauthorizedException("You do not have manager access for this forum");
                }

                break;

            default:
                throw new RuntimeException("Unauthorized");
        }
    }
}