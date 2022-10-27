package com.kawsar.knowledgeExchange.utils;

import com.kawsar.knowledgeExchange.domain.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.kawsar.knowledgeExchange.utils.Constant.ADMIN;
import static com.kawsar.knowledgeExchange.utils.Constant.SESSION_USER;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
public class SessionUtil {

    public static void setUpSessionData(User user, HttpServletRequest request) {
        HttpSession session = request.getSession();

        session.setAttribute(SESSION_USER, user);
        session.setAttribute(ADMIN, user.isAdmin());
    }

    public static User getSessionUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(SESSION_USER);
    }
}