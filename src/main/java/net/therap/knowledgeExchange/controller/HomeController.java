package net.therap.knowledgeExchange.controller;

import net.therap.knowledgeExchange.helper.ForumHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static net.therap.knowledgeExchange.utils.Url.HOME_PAGE;

/**
 * @author kawsar.bhuiyan
 * @since 10/13/22
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private ForumHelper forumHelper;

    @GetMapping
    public String view(ModelMap model) {
        forumHelper.setUpReferenceData(model);

        return HOME_PAGE;
    }
}