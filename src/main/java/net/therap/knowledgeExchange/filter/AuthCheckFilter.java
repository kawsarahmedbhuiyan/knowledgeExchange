package net.therap.knowledgeExchange.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

import static net.therap.knowledgeExchange.utils.Constant.LOGIN;
import static net.therap.knowledgeExchange.utils.Constant.SESSION_USER;

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

        if (Objects.isNull(session.getAttribute(SESSION_USER))) {
            res.sendRedirect(LOGIN);
        } else {
            chain.doFilter(request, response);
        }
    }
}