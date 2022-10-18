package net.therap.knowledgeExchange.controller;

import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.helper.UserHelper;
import net.therap.knowledgeExchange.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;

import static net.therap.knowledgeExchange.controller.UserController.USER;

/**
 * @author kawsar.bhuiyan
 * @since 10/18/22
 */
@Controller
@SessionAttributes(USER)
@RequestMapping("/user")
public class UserController {

    public static final String USER="user";
    private static final String USER_LIST_PAGE="/user/list";

    @Autowired
    private ForumService forumService;

    @Autowired
    private UserHelper userHelper;

    @GetMapping("/list")
    public String viewList(@RequestParam int forumId,
                           @RequestParam Status status,
                           HttpServletRequest request,
                           ModelMap model) {

        Forum forum = forumService.findById(forumId);

        userHelper.setUpReferenceData(forum, status, request, model);

        return USER_LIST_PAGE;
    }
}