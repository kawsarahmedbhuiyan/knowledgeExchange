package com.kawsar.knowledgeExchange.helper;

import com.kawsar.knowledgeExchange.common.Action;
import com.kawsar.knowledgeExchange.domain.Enrollment;
import com.kawsar.knowledgeExchange.domain.User;
import com.kawsar.knowledgeExchange.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.kawsar.knowledgeExchange.utils.SessionUtil.getSessionUser;

/**
 * @author kawsar.bhuiyan
 * @since 10/17/22
 */
@Component
public class EnrollmentHelper {

    @Autowired
    private MessageSourceAccessor msa;

    public void setUpFlashData(String message, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", msa.getMessage(message));
    }

    public void checkAccess(Action action, HttpServletRequest request, Enrollment enrollment) {
        User sessionUser = getSessionUser(request);

        switch (action) {
            case APPROVE:
            case DECLINE:
                if (!sessionUser.equals(enrollment.getForum().getManager())) {
                    throw new UnauthorizedException("You do not have manager access for this forum");
                }

                break;

            case DELETE:
                if (!(sessionUser.equals(enrollment.getForum().getManager()) || sessionUser.equals(enrollment.getUser()))) {
                    throw new UnauthorizedException("You are not authorized to delete this enrollment");
                }

                break;
        }
    }
}
