package com.dogx.core.common.filter;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

@Component
@Slf4j
public class TraceResponseFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletResponse response = servletResponse;
        try {
            String traceId = MDC.get("traceId");
            if (StringUtils.isNotBlank(traceId)) {
                HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper((HttpServletResponse) servletResponse);
                responseWrapper.addHeader("X-Trace-Id", MDC.get("traceId"));
                response = responseWrapper;
            }
        } catch (Exception ignored) {
        }
        filterChain.doFilter(servletRequest, response);
    }

    @Override
    public void destroy() {

    }
}
