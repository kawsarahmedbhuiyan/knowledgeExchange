package net.therap.knowledgeExchange.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.Post;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.helper.PostHelper;
import net.therap.knowledgeExchange.service.AccessCheckerService;
import net.therap.knowledgeExchange.service.ForumService;
import net.therap.knowledgeExchange.service.PostService;
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

import static net.therap.knowledgeExchange.common.Action.SAVE;
import static net.therap.knowledgeExchange.common.Action.UPDATE;
import static net.therap.knowledgeExchange.controller.CommentController.COMMENT;
import static net.therap.knowledgeExchange.controller.PostController.POST;
import static net.therap.knowledgeExchange.utils.Constant.*;
import static net.therap.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static net.therap.knowledgeExchange.utils.SessionUtil.getSessionUser;
import static net.therap.knowledgeExchange.utils.Url.FORUM_VIEW;

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

    @Autowired
    private PostHelper postHelper;

    @Autowired
    private PostService postService;

    @Autowired
    private ForumService forumService;

    @Autowired
    private AccessCheckerService accessCheckerService;

    @InitBinder(POST)
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        binder.setDisallowedFields("id");
    }

    @GetMapping("/list")
    public String viewList(@RequestParam int forumId,
                           @RequestParam Status status,
                           HttpServletRequest request,
                           ModelMap model) {

        Forum forum = forumService.findById(forumId);

        postHelper.setUpReferenceData(forum, status, request, model);

        return POST_LIST_PAGE;
    }

    @GetMapping("/view")
    public String view(@RequestParam int postId,
                       HttpServletRequest request,
                       ModelMap model) {

        Post post = postService.findById(postId);

        accessCheckerService.checkPostViewAccess(request, post);

        postHelper.setUpReferenceData(post, request, model);

        return POST_VIEW_PAGE;
    }

    @GetMapping("/save")
    public String save(@RequestParam int forumId,
                       HttpServletRequest request,
                       ModelMap model) {

        Forum forum = forumService.findById(forumId);

        accessCheckerService.checkPostSaveAccess(request, forum);

        postHelper.setUpReferenceData(SAVE, forum, request, model);

        return POST_FORM_PAGE;
    }

    @GetMapping("/update")
    public String update(@RequestParam int postId,
                         HttpServletRequest request,
                         ModelMap model) {

        Post post = postService.findById(postId);

        accessCheckerService.checkPostUpdateAccess(request, post);

        postHelper.setUpReferenceData(UPDATE, post, model);

        return POST_FORM_PAGE;
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Post post,
                       Errors errors,
                       HttpServletRequest request,
                       ModelMap model,
                       SessionStatus sessionStatus,
                       RedirectAttributes redirectAttributes) {

        accessCheckerService.checkPostSaveAccess(request, post.getForum());

        if (errors.hasErrors()) {
            postHelper.setUpReferenceData(SAVE, model);

            return POST_FORM_PAGE;
        }

        postService.saveOrUpdate(post);

        if(post.getUser().equals(post.getForum().getManager())) {
            postHelper.setUpFlashData(POST_ADDED_MESSAGE, redirectAttributes);
        } else {
            postHelper.setUpFlashData(POST_PENDING_APPROVAL_MESSAGE, redirectAttributes);
        }

        sessionStatus.setComplete();

        return redirectTo(FORUM_VIEW + post.getForum().getId());
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute Post post,
                         Errors errors,
                         HttpServletRequest request,
                         ModelMap model,
                         SessionStatus sessionStatus,
                         RedirectAttributes redirectAttributes) {

        accessCheckerService.checkPostUpdateAccess(request, post);

        if (errors.hasErrors()) {
            postHelper.setUpReferenceData(UPDATE, model);

            return POST_FORM_PAGE;
        }

        postService.saveOrUpdate(post);

        if(post.getUser().equals(post.getForum().getManager())) {
            postHelper.setUpFlashData(POST_UPDATED_MESSAGE, redirectAttributes);
        } else {
            postHelper.setUpFlashData(POST_PENDING_APPROVAL_MESSAGE, redirectAttributes);
        }

        sessionStatus.setComplete();

        return redirectTo(FORUM_VIEW + post.getForum().getId());
    }

    @PostMapping("/approve")
    public String approve(@RequestParam int postId,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {

        Post post = postService.findById(postId);

        accessCheckerService.checkManagerAccess(request, post.getForum());

        postService.approve(post);

        postHelper.setUpFlashData(POST_APPROVED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_VIEW + post.getForum().getId());
    }

    @PostMapping("/decline")
    public String decline(@RequestParam int postId,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {

        Post post = postService.findById(postId);

        accessCheckerService.checkManagerAccess(request, post.getForum());

        postService.decline(post);

        postHelper.setUpFlashData(POST_DECLINED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_VIEW + post.getForum().getId());
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int postId,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        Post post = postService.findById(postId);

        accessCheckerService.checkPostDeleteAccess(request, post);

        postService.delete(post);

        postHelper.setUpFlashData(POST_DELETED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_VIEW + post.getForum().getId());
    }

    @PostMapping(value = "/like", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String like(@RequestParam int postId, HttpServletRequest request) throws JsonProcessingException {

        Post post = postService.findById(postId);

        accessCheckerService.checkPostLikeAccess(request, post);

        User user = getSessionUser(request);

        postService.addOrRemoveLike(post, user);

        Map<String, String> hashMap = new HashMap<>();

        hashMap.put("likeButtonText", postService.isLikedByUser(post, user) ? "Unlike" : "Like");
        hashMap.put("totalLikes", String.valueOf(post.getTotalLikes()));

        return new ObjectMapper().writeValueAsString(hashMap);
    }
}