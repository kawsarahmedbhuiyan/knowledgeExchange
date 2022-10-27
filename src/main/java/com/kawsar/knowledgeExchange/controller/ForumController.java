package com.kawsar.knowledgeExchange.controller;

import com.kawsar.knowledgeExchange.common.Status;
import com.kawsar.knowledgeExchange.domain.Enrollment;
import com.kawsar.knowledgeExchange.domain.Forum;
import com.kawsar.knowledgeExchange.helper.ForumHelper;
import com.kawsar.knowledgeExchange.service.EnrollmentService;
import com.kawsar.knowledgeExchange.service.ForumService;
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
import static com.kawsar.knowledgeExchange.common.Status.*;
import static com.kawsar.knowledgeExchange.controller.ForumController.FORUM;
import static com.kawsar.knowledgeExchange.utils.Constant.*;
import static com.kawsar.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static com.kawsar.knowledgeExchange.utils.Url.FORUM_CREATION_REQUEST_LIST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author kawsar.bhuiyan
 * @since 10/15/22
 */
@Controller
@SessionAttributes(FORUM)
@RequestMapping("/forum")
public class ForumController {

    public static final String FORUM = "forum";
    private static final String FORUM_VIEW_PAGE = "/forum/view";
    private static final String FORUM_FORM_PAGE = "/forum/form";
    private static final String FORUM_LIST_PAGE = "/forum/list";

    private final Logger logger = LoggerFactory.getLogger(ForumController.class);

    @Autowired
    private ForumHelper forumHelper;

    @Autowired
    private ForumService forumService;

    @Autowired
    private EnrollmentService enrollmentService;

    @InitBinder(FORUM)
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        binder.setAllowedFields("name", "description");
    }

    @RequestMapping(value = "/creationRequestList", method = GET)
    public String viewCreationRequestList(@RequestParam Status status,
                                          HttpServletRequest request,
                                          ModelMap model) {

        forumHelper.setUpReferenceData(status, request, model, CREATION_REQUEST_LIST);

        return FORUM_LIST_PAGE;
    }

    @RequestMapping(value = "/joinRequestList", method = GET)
    public String viewJoinRequestList(@RequestParam Status status,
                                      HttpServletRequest request,
                                      ModelMap model) {

        forumHelper.setUpReferenceData(status, request, model, JOIN_REQUEST_LIST);

        return FORUM_LIST_PAGE;
    }


    @RequestMapping(value = "/view", method = GET)
    public String view(@RequestParam int forumId,
                       HttpServletRequest request,
                       ModelMap model) {

        forumHelper.setUpReferenceData(forumId, request, model);

        return FORUM_VIEW_PAGE;
    }

    @RequestMapping(value = "/save", method = GET)
    public String save(HttpServletRequest request,
                       ModelMap model) {

        forumHelper.setUpReferenceData(SAVE, request, model);

        return FORUM_FORM_PAGE;
    }

    @RequestMapping(value="/index", params={"_action_save"}, method = POST)
    public String save(@Valid @ModelAttribute Forum forum,
                       Errors errors,
                       ModelMap model,
                       SessionStatus sessionStatus,
                       RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            forumHelper.setUpReferenceData(SAVE, model);

            return FORUM_FORM_PAGE;
        }

        logger.debug("[Forum]: SAVE with id : {}", forum.getId());

        forumService.saveOrUpdate(forum);

        sessionStatus.setComplete();

        forumHelper.setUpFlashData(FORUM_PENDING_APPROVAL_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_CREATION_REQUEST_LIST + PENDING);
    }

    @RequestMapping(value="/index", params={"_action_approve"}, method = POST)
    public String approve(@RequestParam int forumId,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);

        forumHelper.checkAccess(APPROVE, request, forum);

        logger.debug("[Forum]: Approve with id : {}", forum.getId());

        forumService.approve(forum);

        enrollmentService.saveOrUpdate(new Enrollment(forum, forum.getManager(), APPROVED));

        forumHelper.setUpFlashData(FORUM_APPROVED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_CREATION_REQUEST_LIST + APPROVED);
    }

    @RequestMapping(value="/index", params={"_action_decline"}, method = POST)
    public String decline(@RequestParam int forumId,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);

        forumHelper.checkAccess(DECLINE, request, forum);

        logger.debug("[Forum]: DECLINE with id : {}", forum.getId());

        forumService.decline(forum);

        forumHelper.setUpFlashData(FORUM_DECLINED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_CREATION_REQUEST_LIST + DECLINED);
    }

    @RequestMapping(value="/index", params={"_action_delete"}, method = POST)
    public String delete(@RequestParam int forumId,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);

        forumHelper.checkAccess(DELETE, request, forum);

        logger.debug("[Forum]: DELETE with id : {}", forum.getId());

        forumService.delete(forum);

        forumHelper.setUpFlashData(FORUM_DELETED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_CREATION_REQUEST_LIST + APPROVED);
    }
}