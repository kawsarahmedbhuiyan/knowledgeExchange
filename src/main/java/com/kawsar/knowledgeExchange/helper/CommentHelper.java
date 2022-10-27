package com.kawsar.knowledgeExchange.helper;

import com.kawsar.knowledgeExchange.common.Action;
import com.kawsar.knowledgeExchange.domain.Comment;
import com.kawsar.knowledgeExchange.domain.Post;
import com.kawsar.knowledgeExchange.domain.User;
import com.kawsar.knowledgeExchange.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.kawsar.knowledgeExchange.common.Status.DELETED;
import static com.kawsar.knowledgeExchange.controller.CommentController.COMMENT;
import static com.kawsar.knowledgeExchange.utils.SessionUtil.getSessionUser;

/**
 * @author kawsar.bhuiyan
 * @since 10/19/22
 */
@Component
public class CommentHelper {

    @Autowired
    private MessageSourceAccessor msa;

    public void setUpReferenceData(Action action, Post post, HttpServletRequest request, ModelMap model) {
        User user = getSessionUser(request);

        model.addAttribute(COMMENT, new Comment(post, user));
        model.addAttribute("action", action.name().toLowerCase());
    }

    public void setUpReferenceData(Action action, Comment comment, ModelMap model) {
        model.addAttribute(COMMENT, comment);
        model.addAttribute("action", action.name().toLowerCase());
    }

    public void setUpReferenceData(Action action, ModelMap model) {
        model.addAttribute("action", action.name().toLowerCase());
    }

    public void setUpFlashData(String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", msa.getMessage(message));
    }

    public void checkAccess(Action action, HttpServletRequest request, Comment comment) {
        User sessionUser = getSessionUser(request);

        switch (action) {
            case SAVE:
                if (comment.getPost().getForum().getEnrollments()
                        .stream().noneMatch(enrollment -> sessionUser.equals(enrollment.getUser()))) {
                    throw new UnauthorizedException("You must join the forum to comment on this post");
                }

                break;

            case UPDATE:
                if (!sessionUser.equals(comment.getUser()) || DELETED.equals(comment.getStatus())) {
                    throw new UnauthorizedException("You are not authorized to update this comment");
                }

                break;

            case DELETE:
                if (!(sessionUser.equals(comment.getPost().getForum().getManager())
                        || sessionUser.equals(comment.getUser()))) {
                    throw new UnauthorizedException("You are not authorized to delete this comment");
                }

                break;

            default:
                throw new RuntimeException("Unauthorized");
        }
    }
}