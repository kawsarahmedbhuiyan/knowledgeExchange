package com.kawsar.knowledgeExchange.utils;

/**
 * @author kawsar.bhuiyan
 * @since 10/13/22
 */
public interface Url {

    String HOME = "/";
    String HOME_PAGE = "home";
    String LOGIN = "/login";
    String LOGIN_PAGE = "login";
    String REGISTRATION_PAGE = "registration";
    String FORUM_VIEW = "/forum/view?forumId=";
    String FORUM_CREATION_REQUEST_LIST = "/forum/creationRequestList?status=";
    String FORUM_MEMBER_LIST = "/user/list?forumId=";
    String POST_VIEW = "/post/view?postId=";
    String USER_VIEW = "/user/view?userId=";
    String ERROR_PAGE = "error";
}