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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static net.therap.knowledgeExchange.common.Status.APPROVED;
import static net.therap.knowledgeExchange.common.Status.DECLINED;
import static net.therap.knowledgeExchange.utils.Constant.*;
import static net.therap.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static net.therap.knowledgeExchange.utils.SessionUtil.getSessionUser;
import static net.therap.knowledgeExchange.utils.Url.FORUM_MEMBER_LIST;
import static net.therap.knowledgeExchange.utils.Url.FORUM_VIEW;

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

    @PostMapping("/enroll")
    public String enroll(@RequestParam int forumId,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);

        User user = getSessionUser(request);

        enrollmentService.enroll(forum, user);
        
        return redirectTo(FORUM_VIEW + forumId);
    }

    @PostMapping("/unenroll")
    public String unenroll(@RequestParam int forumId,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);

        User user = getSessionUser(request);

        Enrollment enrollment = enrollmentService.findByForumAndUser(forum, user);

        enrollmentService.delete(enrollment);

        return redirectTo(FORUM_VIEW + forumId);
    }

    @PostMapping("/approve")
    public String approve(@RequestParam int forumId,
                          @RequestParam int userId,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);

        User user = userService.findById(userId);

        Enrollment enrollment = enrollmentService.findByForumAndUser(forum, user);

        enrollmentService.approve(enrollment);

        enrollmentHelper.setUpFlashData(MEMBER_APPROVED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_MEMBER_LIST + forumId + STATUS + APPROVED);
    }

    @PostMapping("/decline")
    public String decline(@RequestParam int forumId,
                          @RequestParam int userId,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);

        User user = userService.findById(userId);

        Enrollment enrollment = enrollmentService.findByForumAndUser(forum, user);

        enrollmentService.decline(enrollment);

        enrollmentHelper.setUpFlashData(MEMBER_DECLINED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_MEMBER_LIST + forumId + STATUS + DECLINED);
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int forumId,
                         @RequestParam int userId,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);

        User user = userService.findById(userId);

        Enrollment enrollment = enrollmentService.findByForumAndUser(forum, user);

        enrollmentService.delete(enrollment);

        enrollmentHelper.setUpFlashData(MEMBER_DELETED_MESSAGE, redirectAttributes);

        return redirectTo(FORUM_MEMBER_LIST + forumId + STATUS + APPROVED);
    }
}