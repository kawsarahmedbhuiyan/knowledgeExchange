package net.therap.knowledgeExchange.helper;

import net.therap.knowledgeExchange.common.Action;
import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

import static net.therap.knowledgeExchange.utils.Constant.FORUM;
import static net.therap.knowledgeExchange.utils.Constant.FORUMS;
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
    private MessageSource messageSource;

    public void setUpReferenceData(Action action, HttpServletRequest request, ModelMap model) {
        User manager = getSessionUser(request);

        model.addAttribute(FORUM, new Forum(manager));
        model.addAttribute(action.name(), true);
    }

    public void setUpReferenceData(Status status, HttpServletRequest request, ModelMap model) {
        User manager = getSessionUser(request);

        List<Forum> forums = forumService.findAllByManagerAndStatus(manager, status);

        model.addAttribute(FORUMS, forums);
        model.addAttribute(status.name(), true);
    }

    public void setUpReferenceData(Action action, ModelMap model) {
        model.addAttribute(action.name(), true);
    }

    public void setUpFlashData(Action action, String message, RedirectAttributes redirectAttributes) {
        String[] messageArgs = {FORUM, action.name()};
        redirectAttributes.addFlashAttribute(message,
                messageSource.getMessage(message, messageArgs, Locale.ENGLISH));
    }
}