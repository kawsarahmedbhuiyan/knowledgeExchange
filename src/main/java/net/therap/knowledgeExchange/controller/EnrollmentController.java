package net.therap.knowledgeExchange.controller;

import net.therap.knowledgeExchange.domain.Enrollment;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.helper.EnrollmentHelper;
import net.therap.knowledgeExchange.service.EnrollmentService;
import net.therap.knowledgeExchange.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static net.therap.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static net.therap.knowledgeExchange.utils.SessionUtil.getSessionUser;
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
    private EnrollmentHelper enrollmentHelper;

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/enroll")
    public String enroll(@RequestParam int forumId,
                       HttpServletRequest request,
                       RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);

        User user = getSessionUser(request);

        enrollmentService.saveOrUpdate(new Enrollment(forum, user));

        return redirectTo(FORUM_VIEW + forumId);
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int forumId,
                         HttpServletRequest request,
                         RedirectAttributes redirectAttributes) {

        Forum forum = forumService.findById(forumId);

        User user = getSessionUser(request);

        Enrollment enrollment = enrollmentService.findByForumAndUser(forum, user);

        enrollmentService.delete(enrollment);

        return redirectTo(FORUM_VIEW + forumId);
    }
}