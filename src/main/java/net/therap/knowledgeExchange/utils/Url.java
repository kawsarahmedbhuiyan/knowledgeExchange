package net.therap.knowledgeExchange.utils;

/**
 * @author kawsar.bhuiyan
 * @since 10/13/22
 */
public interface Url {

    String HOME = "/";
    String HOME_PAGE = "home";
    String LOGIN = "/auth/login";
    String LOGOUT = "/auth/logout";
    String LOGIN_PAGE = "login";
    String REGISTRATION_PAGE = "registration";
    String FORUM_VIEW = "/forum/view?forumId=";
    String FORUM_CREATION_REQUEST_LIST ="/forum/creationRequestList?status=";
    String ERROR_PAGE = "error";
}