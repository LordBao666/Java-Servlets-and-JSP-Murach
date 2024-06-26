package com.lordbao.filters;

import javax.servlet.*;
import java.io.IOException;

public class TestInitParamsFilter implements Filter {

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
        ServletContext sc = filterConfig.getServletContext();

        String filterName = filterConfig.getFilterName();

        String logFilename = filterConfig.getInitParameter("logFilename");

        sc.log(filterName + " | logFilename: " + logFilename);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }
}