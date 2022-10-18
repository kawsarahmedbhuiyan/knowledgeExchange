package net.therap.knowledgeExchange.controller;

import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.Post;
import net.therap.knowledgeExchange.helper.PostHelper;
import net.therap.knowledgeExchange.service.ForumService;
import net.therap.knowledgeExchange.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static net.therap.knowledgeExchange.common.Action.SAVE;
import static net.therap.knowledgeExchange.controller.PostController.POST;
import static net.therap.knowledgeExchange.utils.Constant.POST_PENDING_APPROVAL_MESSAGE;
import static net.therap.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static net.therap.knowledgeExchange.utils.Url.FORUM_VIEW;

/**
 * @author kawsar.bhuiyan
 * @since 10/18/22
 */
@Controller
@SessionAttributes(POST)
@RequestMapping("/post")
public class PostController {

    public static final String POST = "post";
    private static final String POST_VIEW_PAGE="/post/view";
    private static final String POST_FORM_PAGE = "/post/form";

    @Autowired
    private PostHelper postHelper;

    @Autowired
    private PostService postService;

    @Autowired
    private ForumService forumService;

    @GetMapping("/save")
    public String save(@RequestParam int forumId,
                       HttpServletRequest request,
                       ModelMap model) {

        Forum forum = forumService.findById(forumId);

        postHelper.setUpReferenceData(SAVE, forum, request, model);

        return POST_FORM_PAGE;
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Post post,
                          Errors errors,
                          HttpServletRequest request,
                          ModelMap model,
                          SessionStatus sessionStatus,
                          RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            postHelper.setUpReferenceData(SAVE, model);

            return POST_FORM_PAGE;
        }

        postService.saveOrUpdate(post);

        postHelper.setUpFlashData(POST_PENDING_APPROVAL_MESSAGE, redirectAttributes);

        sessionStatus.setComplete();

        return redirectTo(FORUM_VIEW + post.getForum().getId());
    }
}