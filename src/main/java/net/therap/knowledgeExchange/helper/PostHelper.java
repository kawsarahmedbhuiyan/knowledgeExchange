package net.therap.knowledgeExchange.helper;

import net.therap.knowledgeExchange.common.Action;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.Post;
import net.therap.knowledgeExchange.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import java.util.Locale;

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

    public void setUpReferenceData(Action action, Forum forum, HttpServletRequest request, ModelMap model) {
        User user = getSessionUser(request);

        model.addAttribute(POST, new Post(user, forum));
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
