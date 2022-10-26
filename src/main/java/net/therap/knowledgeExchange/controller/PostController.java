package net.therap.knowledgeExchange.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.Post;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.helper.PostHelper;
import net.therap.knowledgeExchange.service.ForumService;
import net.therap.knowledgeExchange.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static net.therap.knowledgeExchange.common.Action.*;
import static net.therap.knowledgeExchange.controller.CommentController.COMMENT;
import static net.therap.knowledgeExchange.controller.PostController.POST;
import static net.therap.knowledgeExchange.utils.Constant.*;
import static net.therap.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static net.therap.knowledgeExchange.utils.SessionUtil.getSessionUser;
import static net.therap.knowledgeExchange.utils.Url.FORUM_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author kawsar.bhuiyan
 * @since 10/18/22
 */
@Controller
@SessionAttributes({POST, COMMENT})
@RequestMapping("/post")
public class PostController {

    public static final String POST = "post";
    private static final String POST_VIEW_PAGE = "/post/view";
    private static final String POST_FORM_PAGE = "/post/form";
    private static final String POST_LIST_PAGE = "/post/list";

    private final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostHelper postHelper;

    @Autowired
    private PostService postService;

    @Autowired
    private ForumService forumService;

    @InitBinder(POST)
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        binder.setDisallowedFields("id");
    }

    @RequestMapping(value = "/list", method = GET)
    public String viewList(@RequestParam int forumId,
                           @RequestParam Status status,
                           HttpServletRequest request,
                           ModelMap model) {

        Forum forum = forumService.findById(forumId);

        postHelper.setUpReferenceData(forum, status, request, model);

        return POST_LIST_PAGE;
    }

    @RequestMapping(value = "/view", method = GET)
    public String view(@RequestParam int postId,
                       HttpServletRequest request,
                       ModelMap model) {

        Post post = postService.findById(postId);

        postHelper.checkAccess(VIEW, request, post);

        postHelper.setUpReferenceData(post, request, model);

        return POST_VIEW_PAGE;
    }

    @RequestMapping(value = "/save", method = GET)
    public String save(@RequestParam int forumId,
                       HttpServletRequest request,
                       ModelMap model) {

        Forum forum = forumService.findById(forumId);

        postHelper.checkAccess(SAVE, request, forum);

        postHelper.setUpReferenceData(SAVE, forum, request, model);

        return POST_FORM_PAGE;
    }

    @RequestMapping(value = "/update", method = GET)
    public String update(@RequestParam int postId,
                         HttpServletRequest request,
                         ModelMap model) {

        Post post = postService.findById(postId);

        postHelper.checkAccess(UPDATE, request, post);

        postHelper.setUpReferenceData(UPDATE, post, model);

        return POST_FORM_PAGE;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute Post post,
                       Errors errors,
                       HttpServletRequest request,
                       ModelMap model,
                       SessionStatus sessionStatus,
                       RedirectAttributes redirectAttributes) {

        postHelper.checkAccess(SAVE, request, post.getForum());

        if (errors.hasErrors()) {
            postHelper.setUpReferenceData(SAVE, model);

            return POST_FORM_PAGE;
        }

        logger.debug("[Post]: SAVE with id : {}", post.getId());

        postService.saveOrUpdate(post);

        sessionStatus.setComplete();

        if (post.getUser().equals(post.getForum().getManager())) {
            postHelper.setUpFlashData(POST_ADDED_MESSAGE, redirectAttributes);
        } else {
            postHelper.setUpFlashData(POST_PENDING_APPROVAL_MESSAGE, redirectAttributes);
        }

        return redirectTo(FORUM_VIEW + post.getForum().getId());
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute Post post,
                         Errors errors,
                         HttpServletRequest request,
                         ModelMap model,
                         SessionStatus sessionStatus,
                         RedirectAttributes redirectAttributes) {

        postHelper.checkAccess(UPDATE, request, post);

        if (errors.hasErrors()) {
            postHelper.setUpReferenceData(UPDATE, model);

            return POST_FORM_PAGE;
        }

        logger.debug("[Post]: UPDATE with id : {}", post.getId());

        postService.saveOrUpdate(post);

        sessionStatus.setComplete();

        if (post.getUser().equals(post.getForum().getManager())) {
            postHelper.setUpFlashData(POST_UPDATED_MESSAGE, redirectAttributes);
        } else {
            postHelper.setUpFlashData(POST_PENDING_APPROVAL_MESSAGE, redirectAttributes);
        }

        return redirectTo(FORUM_VIEW + post.getForum().getId());
    }

    @RequestMapping(value = "/approve", method = RequestMethod.POST)
    public String approve(@RequestParam int postId,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {

        Post post = postService.findById(postId);

        postHelper.checkAccess(APPROVE, request, post.getForum());

        logger.debug("[Post]: APPROVE with id : {}", post.getId());

        postService.approve(post);

        postHelper.setUpFlashData(POST_APPROVED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_VIEW + post.getForum().getId());
    }

    @RequestMapping(value = "/decline", method = RequestMethod.POST)
    public String decline(@RequestParam int postId,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {

        Post post = postService.findById(postId);

        postHelper.checkAccess(DECLINE, request, post.getForum());

        logger.debug("[Post]: DECLINE with id : {}", post.getId());

        postService.decline(post);

        postHelper.setUpFlashData(POST_DECLINED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_VIEW + post.getForum().getId());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam int postId,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        Post post = postService.findById(postId);

        postHelper.checkAccess(DELETE, request, post);

        logger.debug("[Post]: DELETE with id : {}", post.getId());

        postService.delete(post);

        postHelper.setUpFlashData(POST_DELETED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_VIEW + post.getForum().getId());
    }

    @RequestMapping(value = "/like", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String like(@RequestParam int postId, HttpServletRequest request) throws JsonProcessingException {

        Post post = postService.findById(postId);

        postHelper.checkAccess(LIKE, request, post);

        User user = getSessionUser(request);

        logger.debug("[Post]: LIKE with id : {}", post.getId());

        postService.addOrRemoveLike(post, user);

        Map<String, String> hashMap = new HashMap<>();

        hashMap.put("likeButtonText", postService.isLikedByUser(post, user) ? "Unlike" : "Like");
        hashMap.put("totalLikes", String.valueOf(post.getTotalLikes()));

        return new ObjectMapper().writeValueAsString(hashMap);
    }
}