package com.kawsar.knowledgeExchange.controller;

import com.kawsar.knowledgeExchange.domain.Comment;
import com.kawsar.knowledgeExchange.helper.CommentHelper;
import com.kawsar.knowledgeExchange.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static com.kawsar.knowledgeExchange.common.Action.*;
import static com.kawsar.knowledgeExchange.controller.CommentController.COMMENT;
import static com.kawsar.knowledgeExchange.utils.Constant.*;
import static com.kawsar.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static com.kawsar.knowledgeExchange.utils.Url.POST_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author kawsar.bhuiyan
 * @since 10/19/22
 */
@Controller
@SessionAttributes(COMMENT)
@RequestMapping("/comment")
public class CommentController {

    public static final String COMMENT = "comment";
    private static final String COMMENT_FORM_PAGE = "/comment/form";

    private final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentHelper commentHelper;

    @Autowired
    private CommentService commentService;

    @InitBinder(COMMENT)
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        binder.setAllowedFields("body");
    }

    @RequestMapping(value = "/update", method = GET)
    public String update(@RequestParam int commentId,
                         HttpServletRequest request,
                         ModelMap model) {

        Comment comment = commentService.findById(commentId);

        commentHelper.checkAccess(UPDATE, request, comment);

        commentHelper.setUpReferenceData(UPDATE, comment, model);

        return COMMENT_FORM_PAGE;
    }

    @RequestMapping(value="/index", params = {"_action_save"}, method = POST)
    public String save(@Valid @ModelAttribute Comment comment,
                       Errors errors,
                       HttpServletRequest request,
                       ModelMap model,
                       SessionStatus sessionStatus,
                       RedirectAttributes redirectAttributes) {

        commentHelper.checkAccess(SAVE, request, comment);

        if (errors.hasErrors()) {
            commentHelper.setUpReferenceData(SAVE, model);

            return COMMENT_FORM_PAGE;
        }

        logger.debug("[Comment]: SAVE with id : {}", comment.getId());

        commentService.saveOrUpdate(comment);

        commentHelper.setUpFlashData(COMMENT_ADDED_MESSAGE, redirectAttributes);

        sessionStatus.setComplete();

        return redirectTo(POST_VIEW + comment.getPost().getId());
    }

    @RequestMapping(value="/index", params = {"_action_update"}, method = POST)
    public String update(@Valid @ModelAttribute Comment comment,
                         Errors errors,
                         HttpServletRequest request,
                         ModelMap model,
                         SessionStatus sessionStatus,
                         RedirectAttributes redirectAttributes) {

        commentHelper.checkAccess(UPDATE, request, comment);

        if (errors.hasErrors()) {
            commentHelper.setUpReferenceData(UPDATE, model);

            return COMMENT_FORM_PAGE;
        }

        logger.debug("[Comment]: UPDATE with id : {}", comment.getId());

        commentService.saveOrUpdate(comment);

        commentHelper.setUpFlashData(COMMENT_UPDATED_MESSAGE, redirectAttributes);

        sessionStatus.setComplete();

        return redirectTo(POST_VIEW + comment.getPost().getId());
    }

    @RequestMapping(value="/index", params = {"_action_delete"}, method = POST)
    public String delete(@RequestParam int commentId,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        Comment comment = commentService.findById(commentId);

        commentHelper.checkAccess(DELETE, request, comment);

        logger.debug("[Comment]: DELETE with id : {}", comment.getId());

        commentService.delete(comment);

        commentHelper.setUpFlashData(COMMENT_DELETED_MESSAGE, redirectAttributes);

        return redirectTo(POST_VIEW + comment.getPost().getId());
    }
}