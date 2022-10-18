package net.therap.knowledgeExchange.controller;

import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Enrollment;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.helper.ForumHelper;
import net.therap.knowledgeExchange.service.EnrollmentService;
import net.therap.knowledgeExchange.service.ForumService;
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
import static net.therap.knowledgeExchange.common.Status.*;
import static net.therap.knowledgeExchange.controller.ForumController.FORUM;
import static net.therap.knowledgeExchange.utils.Constant.*;
import static net.therap.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static net.therap.knowledgeExchange.utils.Url.FORUM_CREATION_REQUEST_LIST;

/**
 * @author kawsar.bhuiyan
 * @since 10/15/22
 */
@Controller
@SessionAttributes(FORUM)
@RequestMapping("/forum")
public class ForumController {

    public static final String FORUM="forum";
    private static final String FORUM_VIEW_PAGE="/forum/view";
    private static final String FORUM_FORM_PAGE = "/forum/form";
    private static final String FORUM_LIST_PAGE = "/forum/list";

    @Autowired
    private ForumHelper forumHelper;

    @Autowired
    private ForumService forumService;

    @Autowired
    private EnrollmentService enrollmentService;

    @InitBinder(FORUM)
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        binder.setDisallowedFields("id");
    }

    @GetMapping("/creationRequestList")
    public String viewCreationRequestList(@RequestParam Status status,
                           HttpServletRequest request,
                           ModelMap model) {

        forumHelper.setUpReferenceData(status, request, model, CREATION_REQUEST_LIST);

        return FORUM_LIST_PAGE;
    }

    @GetMapping("/joinRequestList")
    public String viewJoinRequestList(@RequestParam Status status,
                           HttpServletRequest request,
                           ModelMap model) {

        forumHelper.setUpReferenceData(status, request, model, JOIN_REQUEST_LIST);

        return FORUM_LIST_PAGE;
    }



    @GetMapping("/view")
    public String view(@RequestParam int forumId,
                       HttpServletRequest request,
                       ModelMap model) {

        forumHelper.setUpReferenceData(forumId, request, model);

        return FORUM_VIEW_PAGE;
    }

    @GetMapping("/save")
    public String save(HttpServletRequest request,
                       ModelMap model) {

        forumHelper.setUpReferenceData(SAVE, request, model);

        return FORUM_FORM_PAGE;
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Forum forum,
                          Errors errors,
                          HttpServletRequest request,
                          ModelMap model,
                          SessionStatus sessionStatus,
                          RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            forumHelper.setUpReferenceData(SAVE, model);

            return FORUM_FORM_PAGE;
        }

        forumService.saveOrUpdate(forum);

        sessionStatus.setComplete();

        forumHelper.setUpFlashData(FORUM_PENDING_APPROVAL_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_CREATION_REQUEST_LIST + PENDING);
    }

    @PostMapping("/approve")
    public String approve(@RequestParam int forumId,
                          RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);

        forumService.approve(forum);

        enrollmentService.saveOrUpdate(new Enrollment(forum, forum.getManager(), APPROVED));

        forumHelper.setUpFlashData(FORUM_APPROVED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_CREATION_REQUEST_LIST + APPROVED);
    }

    @PostMapping("/decline")
    public String decline(@RequestParam int forumId,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);

        forumService.decline(forum);

        forumHelper.setUpFlashData(FORUM_DECLINED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_CREATION_REQUEST_LIST + DECLINED);
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int forumId,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);

        forumService.delete(forum);

        forumHelper.setUpFlashData(FORUM_DELETED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_CREATION_REQUEST_LIST + APPROVED);
    }
}