package net.therap.knowledgeExchange.utils;

import net.therap.knowledgeExchange.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static net.therap.knowledgeExchange.utils.Constant.SESSION_USER;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
public class SessionUtil {

    public static void setUpSessionData(User user, HttpServletRequest request) {
        HttpSession session = request.getSession();

        session.setAttribute(SESSION_USER, user);
    }

    public static User getSessionUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(SESSION_USER);
    }

    public static boolean isSessionUser(User user, HttpServletRequest request) {
        return user.equals(getSessionUser(request));
    }
}