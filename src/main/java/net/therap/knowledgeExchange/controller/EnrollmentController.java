package net.therap.knowledgeExchange.controller;

import net.therap.knowledgeExchange.domain.Enrollment;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.helper.EnrollmentHelper;
import net.therap.knowledgeExchange.service.EnrollmentService;
import net.therap.knowledgeExchange.service.ForumService;
import net.therap.knowledgeExchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static net.therap.knowledgeExchange.common.Action.*;
import static net.therap.knowledgeExchange.common.Status.APPROVED;
import static net.therap.knowledgeExchange.common.Status.DECLINED;
import static net.therap.knowledgeExchange.utils.Constant.*;
import static net.therap.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static net.therap.knowledgeExchange.utils.SessionUtil.getSessionUser;
import static net.therap.knowledgeExchange.utils.Url.FORUM_MEMBER_LIST;
import static net.therap.knowledgeExchange.utils.Url.FORUM_VIEW;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author kawsar.bhuiyan
 * @since 10/17/22
 */
@Controller
@RequestMapping("/enrollment")
public class EnrollmentController {

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

        enrollmentHelper.setUpFlashData(ENROLLMENT_CANCELLED_MESSAGE, redirectAttributes);

        enrollmentService.delete(enrollment);

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

        enrollmentHelper.checkAccess(DELETE, request, enrollment);

        enrollmentService.delete(enrollment);

        enrollmentHelper.setUpFlashData(MEMBER_DELETED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_MEMBER_LIST + forumId + STATUS + APPROVED);
    }
}