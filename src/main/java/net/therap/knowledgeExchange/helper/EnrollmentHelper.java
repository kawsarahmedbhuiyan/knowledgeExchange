package net.therap.knowledgeExchange.helper;

import net.therap.knowledgeExchange.domain.Enrollment;
import net.therap.knowledgeExchange.domain.Forum;
import net.therap.knowledgeExchange.domain.User;
import net.therap.knowledgeExchange.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

/**
 * @author kawsar.bhuiyan
 * @since 10/17/22
 */
@Component
public class EnrollmentHelper {

    @Autowired
    private EnrollmentService enrollmentService;

    public Enrollment getOrCreateEnrollment(Forum forum, User user) {
        Enrollment enrollment = enrollmentService.findByForumAndUser(forum, user);

        if(isNull(enrollment)) {
            enrollment = new Enrollment(forum, user);
        }

        return enrollment;
    }
}
