package com.dogx.core.common.interceptor;

import com.dogx.core.common.constants.AuthorizeConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author panxf
 * @create 2022/1/14 10:58
 * @desc
 */
@Component
@Slf4j
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes != null){
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    if(name.equalsIgnoreCase(AuthorizeConstants.AUTHORIZE_USER_ID) || name.equalsIgnoreCase(AuthorizeConstants.AUTHORIZE_TOKEN) ){
                        template.header(name, request.getHeader(name));
                    }
                }
            }
        }
    }
}
