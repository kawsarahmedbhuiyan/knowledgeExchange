package com.kawsar.knowledgeExchange.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static com.kawsar.knowledgeExchange.utils.RedirectUtil.redirectTo;
import static com.kawsar.knowledgeExchange.utils.Url.LOGIN;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author kawsar.bhuiyan
 * @since 10/27/22
 */
@Controller
@RequestMapping("/logout")
public class LogoutController {

    @RequestMapping(method = GET)
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();

        return redirectTo(LOGIN);
    }
}