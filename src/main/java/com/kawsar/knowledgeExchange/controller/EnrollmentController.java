package com.kawsar.knowledgeExchange.controller;

import com.kawsar.knowledgeExchange.domain.Enrollment;
import com.kawsar.knowledgeExchange.domain.Forum;
import com.kawsar.knowledgeExchange.domain.User;
import com.kawsar.knowledgeExchange.helper.EnrollmentHelper;
import com.kawsar.knowledgeExchange.service.EnrollmentService;
import com.kawsar.knowledgeExchange.service.ForumService;
import com.kawsar.knowledgeExchange.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.kawsar.knowledgeExchange.common.Action.*;
import static com.kawsar.knowledgeExchange.common.Status.APPROVED;
import static com.kawsar.knowledgeExchange.common.Status.DECLINED;
import static com.kawsar.knowledgeExchange.utils.Constant.*;
import static com.kawsar.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static com.kawsar.knowledgeExchange.utils.SessionUtil.getSessionUser;
import static com.kawsar.knowledgeExchange.utils.Url.FORUM_MEMBER_LIST;
import static com.kawsar.knowledgeExchange.utils.Url.FORUM_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author kawsar.bhuiyan
 * @since 10/17/22
 */
@Controller
@RequestMapping("/enrollment")
public class EnrollmentController {

    private final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    private ForumService forumService;

    @Autowired
    private UserService userService;

    @Autowired
    private EnrollmentHelper enrollmentHelper;

    @Autowired
    private EnrollmentService enrollmentService;

    @RequestMapping(value = "/enroll", method = POST)
    public String enroll(@RequestParam int forumId,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);
        User user = getSessionUser(request);

        logger.debug("[Enrollment]: ENROLL with forum_id : {} and user_id : {}", forum.getId(), user.getId());

        enrollmentService.enroll(forum, user);

        enrollmentHelper.setUpFlashData(ENROLLMENT_PENDING_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_VIEW + forumId);
    }

    @RequestMapping(value = "/unenroll", method = POST)
    public String unenroll(@RequestParam int forumId,
                           HttpServletRequest request,
                           RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);
        User user = getSessionUser(request);
        Enrollment enrollment = enrollmentService.findByForumAndUser(forum, user);

        logger.debug("[Enrollment]: UNENROLL with forum_id : {} and user_id : {}", forum.getId(), user.getId());

        enrollmentService.delete(enrollment);

        enrollmentHelper.setUpFlashData(ENROLLMENT_CANCELLED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_VIEW + forumId);
    }

    @RequestMapping(value = "/approve", method = POST)
    public String approve(@RequestParam int forumId,
                          @RequestParam int userId,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);
        User user = userService.findById(userId);
        Enrollment enrollment = enrollmentService.findByForumAndUser(forum, user);

        enrollmentHelper.checkAccess(APPROVE, request, enrollment);

        logger.debug("[Enrollment]: APPROVE with forum_id : {} and user_id : {}", forum.getId(), user.getId());

        enrollmentService.approve(enrollment);

        enrollmentHelper.setUpFlashData(MEMBER_APPROVED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_MEMBER_LIST + forumId + STATUS + APPROVED);
    }

    @RequestMapping(value = "/decline", method = POST)
    public String decline(@RequestParam int forumId,
                          @RequestParam int userId,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);
        User user = userService.findById(userId);
        Enrollment enrollment = enrollmentService.findByForumAndUser(forum, user);

        logger.debug("[Enrollment]: DECLINE with forum_id : {} and user_id : {}", forum.getId(), user.getId());

        enrollmentHelper.checkAccess(DECLINE, request, enrollment);

        enrollmentService.decline(enrollment);

        enrollmentHelper.setUpFlashData(MEMBER_DECLINED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_MEMBER_LIST + forumId + STATUS + DECLINED);
    }

    @RequestMapping(value = "/delete", method = POST)
    public String delete(@RequestParam int forumId,
                         @RequestParam int userId,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);
        User user = userService.findById(userId);
        Enrollment enrollment = enrollmentService.findByForumAndUser(forum, user);

        logger.debug("[Enrollment]: DELETE with forum_id : {} and user_id : {}", forum.getId(), user.getId());

        enrollmentHelper.checkAccess(DELETE, request, enrollment);

        enrollmentService.delete(enrollment);

        enrollmentHelper.setUpFlashData(MEMBER_DELETED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_MEMBER_LIST + forumId + STATUS + APPROVED);
    }
}