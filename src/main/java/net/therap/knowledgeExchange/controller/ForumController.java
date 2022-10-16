package net.therap.knowledgeExchange.controller;

import net.therap.knowledgeExchange.common.Action;
import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.helper.ForumHelper;
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
import static net.therap.knowledgeExchange.common.Status.PENDING;
import static net.therap.knowledgeExchange.utils.Constant.FORUM;
import static net.therap.knowledgeExchange.utils.Constant.SUCCESS_MESSAGE;
import static net.therap.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static net.therap.knowledgeExchange.utils.Url.*;

/**
 * @author kawsar.bhuiyan
 * @since 10/15/22
 */
@Controller
@SessionAttributes(FORUM)
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private ForumHelper forumHelper;

    @Autowired
    private ForumService forumService;

    @InitBinder(FORUM)
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

        binder.setDisallowedFields("id");
    }

    @GetMapping("/list")
    public String viewList(@RequestParam Status status,
                           HttpServletRequest request,
                           ModelMap model) {

        forumHelper.setUpReferenceData(status, request, model);

        return FORUM_LIST_PAGE;
    }

    @GetMapping("/save")
    public String save(HttpServletRequest request,
                       ModelMap model) {

        forumHelper.setUpReferenceData(SAVE, request, model);

        return FORUM_FORM_PAGE;
    }

    @PostMapping
    public String process(@Valid @ModelAttribute Forum forum,
                          Errors errors,
                          @RequestParam Action action,
                          HttpServletRequest request,
                          ModelMap model,
                          SessionStatus sessionStatus,
                          RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            forumHelper.setUpReferenceData(action, model);

            return FORUM_FORM_PAGE;
        }

        forumService.saveOrUpdate(forum);

        forumHelper.setUpFlashData(action, SUCCESS_MESSAGE, redirectAttributes);

        sessionStatus.setComplete();

        return redirectTo(FORUM_PENDING_LIST + PENDING);
    }
}