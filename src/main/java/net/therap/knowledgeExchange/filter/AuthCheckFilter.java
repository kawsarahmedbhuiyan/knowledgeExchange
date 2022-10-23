package net.therap.knowledgeExchange.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static java.util.Objects.isNull;
import static net.therap.knowledgeExchange.utils.Constant.SESSION_USER;
import static net.therap.knowledgeExchange.utils.Url.LOGIN;

/**
 * @author kawsar.bhuiyan
 * @since 10/14/22
 */
public class AuthCheckFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession();

        if (isNull(session.getAttribute(SESSION_USER))) {
            res.sendRedirect(LOGIN);
        } else {
            chain.doFilter(request, response);
        }
    }
}