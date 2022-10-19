package net.therap.knowledgeExchange.helper;

import net.therap.knowledgeExchange.common.Action;
import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.Post;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

import static net.therap.knowledgeExchange.common.Status.PENDING;
import static net.therap.knowledgeExchange.controller.PostController.POST;
import static net.therap.knowledgeExchange.utils.SessionUtil.getSessionUser;

/**
 * @author kawsar.bhuiyan
 * @since 10/18/22
 */
@Component
public class PostHelper {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PostService postService;

    public void setUpReferenceData(Forum forum, Status status, HttpServletRequest request, ModelMap model) {
        User user = getSessionUser(request);

        List<Post> posts = forum.isManagedByUser(user) ?
                postService.findAllByForumAndStatus(forum, status) :
                postService.findAllByForumAndUserAndStatus(forum, user, status);

        model.addAttribute("posts", posts);
    }

    public void setUpReferenceData(Post post, HttpServletRequest request, ModelMap model) {
        model.addAttribute(POST, post);
        model.addAttribute(post.getStatus().name(), true);
    }

    public void setUpReferenceData(Action action, Forum forum, HttpServletRequest request, ModelMap model) {
        User user = getSessionUser(request);

        model.addAttribute(POST, new Post(user, forum));
        model.addAttribute("action", action.name().toLowerCase());
        model.addAttribute(action.name(), true);
    }

    public void setUpReferenceData(Action action, Post post, HttpServletRequest request, ModelMap model) {
        post.setStatus(PENDING);

        model.addAttribute(POST, post);
        model.addAttribute("action", action.name().toLowerCase());
        model.addAttribute(action.name(), true);
    }

    public void setUpReferenceData(Action action, ModelMap model) {
        model.addAttribute(action.name(), true);
    }

    public void setUpFlashData(String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message",
                messageSource.getMessage(message, null, Locale.ENGLISH));
    }
}