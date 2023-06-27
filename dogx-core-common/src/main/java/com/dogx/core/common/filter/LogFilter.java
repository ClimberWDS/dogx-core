package com.dogx.core.common.filter;

import com.dogx.core.base.constant.ConfigConsts;
import com.dogx.core.base.model.ExtraCodeEnum;
import com.dogx.core.base.model.RespResult;
import com.dogx.core.base.utils.JacksonUtils;
import com.dogx.core.common.constants.AuthorizeConstants;
import com.dogx.core.common.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RefreshScope
public class LogFilter implements Filter {
    @Qualifier("myAsync")
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        long start = System.currentTimeMillis();
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        //从cms h5 过来的才有
        String userId = httpServletRequest.getHeader(AuthorizeConstants.AUTHORIZE_USER_ID);
        String ip = IpUtils.getRemoteAddress(httpServletRequest);
        String url = httpServletRequest.getRequestURI();
        String requestContentType = servletRequest.getContentType();
        ResponseWrapper response = new ResponseWrapper((HttpServletResponse) servletResponse);
        Map<String, String> paramMap = Collections.EMPTY_MAP;
        try {
            if (StringUtils.isNotBlank(userId)) {
                CmsLoginUserHandlerUtil.setLoginUserId(userId);
            }
            if (requestContentType != null && requestContentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            if (requestContentType != null && requestContentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                //先不记录这种请求类型的入参日志
//                RequestWrapper request = new RequestWrapper((HttpServletRequest) servletRequest);
//                paramMap = HttpParamUtils.getAllParams(request);
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            } else {
                RequestWrapper request = new RequestWrapper((HttpServletRequest) servletRequest);
                paramMap = HttpParamUtils.getAllParams(request);
                filterChain.doFilter(request, response);
            }
            if (response.getStatus() == HttpStatus.OK.value() && response.getContentType() != null && response.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
                String respBody = new String(response.getContent(), ConfigConsts.ENCODING);
                if (StringUtils.isBlank(respBody)) {
                    long end = System.currentTimeMillis();
                    threadPoolTaskExecutor.execute(new LogWrite(url, ip, paramMap, RespResult.fail(ExtraCodeEnum.SERVER_ERROR.code, "未知错误"), start, end, userId));
                    return;
                }
                RespResult result = JacksonUtils.toObj(respBody, RespResult.class);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.getWriter().write(JacksonUtils.toJson(result));
                response.getWriter().flush();
                long end = System.currentTimeMillis();
                threadPoolTaskExecutor.execute(new LogWrite(url, ip, paramMap, result, start, end, userId));
            } else {
                //非 APPLICATION_JSON_VALUE 的情况
                long end = System.currentTimeMillis();
                if (hasIngoreLog(response.getContentType())) {
                    //非文件类型的
                    String respBody = response.getContent() == null ? "" : new String(response.getContent(), ConfigConsts.ENCODING);
                    log.debug("response.getResponse()===={}", respBody);
                }
                if (response.getStatus() == HttpStatus.OK.value()) {
                    threadPoolTaskExecutor.execute(new LogWrite(url, ip, paramMap, RespResult.success(ExtraCodeEnum.NORMAL), start, end, userId));
                } else {
                    log.info("响应头编码：{}", response.getStatus());
                    threadPoolTaskExecutor.execute(new LogWrite(url, ip, paramMap, RespResult.fail(ExtraCodeEnum.SERVER_ERROR), start, end, userId));
                }
                response.getResponse().getOutputStream().write(response.getContent());
                response.getResponse().getOutputStream().flush();
            }
        } catch (Exception e) {
            //由全局异常处理
            throw new RuntimeException("发生未知异常", e);
        } finally {
            if (StringUtils.isNotBlank(userId)) {
                CmsLoginUserHandlerUtil.removeLoginUserId();
            }
        }
    }

    private boolean hasIngoreLog(String contentType) {
        if (StringUtils.isBlank(contentType)) {
            return true;
        }
        List<String> logIngore = new ArrayList<>();
        logIngore.add("application/x-msdownload");
        logIngore.add("application/vnd.ms-excel");
        logIngore.add("application/octet-stream");
        logIngore.add("application/msword");
        logIngore.add("application/vnd.ms-powerpoint");
        logIngore.add("application/pdf");
        logIngore.add("application/zip");
        logIngore.add("application/javascript");
        logIngore.add("application/xml");
        logIngore.add("application/x-");
        logIngore.add("application/vnd");
        logIngore.add("image/");
        logIngore.add("text/");
        logIngore.add("audio/");
        logIngore.add("video/");

        for (String setingType : logIngore) {
            if (contentType.contains(setingType)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
