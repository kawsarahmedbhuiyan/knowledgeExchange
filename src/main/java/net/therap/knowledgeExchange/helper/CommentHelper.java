package net.therap.knowledgeExchange.helper;

import net.therap.knowledgeExchange.common.Action;
import net.therap.knowledgeExchange.domain.Comment;
import net.therap.knowledgeExchange.domain.Post;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.exception.UnauthorizedException;
import net.therap.knowledgeExchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static java.util.Locale.ENGLISH;
import static net.therap.knowledgeExchange.controller.CommentController.COMMENT;
import static net.therap.knowledgeExchange.utils.SessionUtil.getSessionUser;

/**
 * @author kawsar.bhuiyan
 * @since 10/19/22
 */
@Component
public class CommentHelper {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

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
        redirectAttributes.addFlashAttribute("message",
                messageSource.getMessage(message, null, ENGLISH));
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
                if (!sessionUser.equals(comment.getUser())) {
                    throw new UnauthorizedException("You are not authorized to update this comment");
                }

                break;

            case DELETE:
                if (!(sessionUser.equals(comment.getPost().getForum().getManager())
                        || sessionUser.equals(comment.getUser()))) {
                    throw new UnauthorizedException("You are not authorized to delete this comment");
                }

                break;
        }
    }
}