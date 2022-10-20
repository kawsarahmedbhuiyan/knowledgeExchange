package net.therap.knowledgeExchange.controller;

import net.therap.knowledgeExchange.domain.Comment;
import net.therap.knowledgeExchange.domain.Post;
import net.therap.knowledgeExchange.helper.CommentHelper;
import net.therap.knowledgeExchange.service.CommentService;
import net.therap.knowledgeExchange.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static net.therap.knowledgeExchange.common.Action.SAVE;
import static net.therap.knowledgeExchange.common.Action.UPDATE;
import static net.therap.knowledgeExchange.controller.CommentController.COMMENT;
import static net.therap.knowledgeExchange.utils.Constant.COMMENT_ADDED_MESSAGE;
import static net.therap.knowledgeExchange.utils.Constant.COMMENT_UPDATED_MESSAGE;
import static net.therap.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static net.therap.knowledgeExchange.utils.Url.POST_VIEW;

/**
 * @author kawsar.bhuiyan
 * @since 10/19/22
 */
@Controller
@SessionAttributes(COMMENT)
@RequestMapping("/comment")
public class CommentController {

    public static final String COMMENT="comment";
    private static final String COMMENT_FORM_PAGE="/comment/form";

    @Autowired
    private CommentHelper commentHelper;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @InitBinder(COMMENT)
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        
        binder.setDisallowedFields("id");
    }

    @GetMapping("/save")
    public String save(@RequestParam int postId,
                         HttpServletRequest request,
                         ModelMap model) {

        Post post = postService.findById(postId);

        commentHelper.setUpReferenceData(SAVE, post, request, model);

        return COMMENT_FORM_PAGE;
    }

    @GetMapping("/update")
    public String update(@RequestParam int commentId,
                         HttpServletRequest request,
                         ModelMap model) {

        Comment comment = commentService.findById(commentId);

        commentHelper.setUpReferenceData(UPDATE, comment, model);

        return COMMENT_FORM_PAGE;
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Comment comment,
                       Errors errors,
                       HttpServletRequest request,
                       ModelMap model,
                       SessionStatus sessionStatus,
                       RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            commentHelper.setUpReferenceData(SAVE, model);

            return COMMENT_FORM_PAGE;
        }

        commentService.saveOrUpdate(comment);

        commentHelper.setUpFlashData(COMMENT_ADDED_MESSAGE, redirectAttributes);

        sessionStatus.setComplete();

        return redirectTo(POST_VIEW + comment.getPost().getId());
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute Comment comment,
                       Errors errors,
                       HttpServletRequest request,
                       ModelMap model,
                       SessionStatus sessionStatus,
                       RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            commentHelper.setUpReferenceData(UPDATE, model);

            return COMMENT_FORM_PAGE;
        }

        commentService.saveOrUpdate(comment);

        commentHelper.setUpFlashData(COMMENT_UPDATED_MESSAGE, redirectAttributes);

        sessionStatus.setComplete();

        return redirectTo(POST_VIEW + comment.getPost().getId());
    }
}