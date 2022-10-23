package net.therap.knowledgeExchange.controller;

import net.therap.knowledgeExchange.helper.ForumHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import static net.therap.knowledgeExchange.utils.Url.HOME_PAGE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author kawsar.bhuiyan
 * @since 10/13/22
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ForumHelper forumHelper;

    @RequestMapping(method = GET)
    public String view(ModelMap model) {
        forumHelper.setUpReferenceData(model);

        return HOME_PAGE;
    }
}