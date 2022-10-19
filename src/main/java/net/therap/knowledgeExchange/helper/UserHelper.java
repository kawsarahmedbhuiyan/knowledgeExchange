package net.therap.knowledgeExchange.helper;

import net.therap.knowledgeExchange.common.Status;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kawsar.bhuiyan
 * @since 10/18/22
 */
@Component
public class UserHelper {

    @Autowired
    private EnrollmentService enrollmentService;

    public void setUpReferenceData(Forum forum, Status status, HttpServletRequest request, ModelMap model) {
        List<User> users = new ArrayList<>();

        enrollmentService.findByForumAndStatus(forum, status).forEach(enrollment -> users.add(enrollment.getUser()));

        model.addAttribute("users", users);
        model.addAttribute("forum", forum);
        model.addAttribute(status.name(), true);
    }
}
