package com.lordbao.filters;

import com.lordbao.util.*;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LogRequestFilter implements Filter {

    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        ServletContext sc = filterConfig.getServletContext();

        String logString = filterConfig.getFilterName() + " | ";
        logString += "Servlet path: " + httpRequest.getServletPath() + " | ";

        Cookie[] cookies = httpRequest.getCookies();
        String emailAddress = CookieUtil.getCookieValue(
                cookies, "emailCookie");
        logString += "Email cookie: ";
        if (emailAddress.length() != 0) {
            logString += emailAddress;
        } else {
            logString += "Not found";
        }

        sc.log(logString);

        chain.doFilter(httpRequest, response);
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }
}