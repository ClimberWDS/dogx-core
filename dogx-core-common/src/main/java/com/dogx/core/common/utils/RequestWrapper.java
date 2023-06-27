package com.dogx.core.common.utils;

import com.dogx.core.base.constant.ConfigConsts;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * 保存过滤器里面的流
 */
public class RequestWrapper extends HttpServletRequestWrapper {
 
    private final byte[] body;
 
    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        String sessionStream = StreamUtils.copyToString(request.getInputStream(), Charset.forName(ConfigConsts.ENCODING));
        body = sessionStream.getBytes(Charset.forName(ConfigConsts.ENCODING));
    }

    public RequestWrapper(HttpServletRequest request, String bodyJsonStr) throws IOException {
        super(request);
        body = bodyJsonStr.getBytes(Charset.forName(ConfigConsts.ENCODING));
    }
 
    @Override
    public BufferedReader getReader() {
 
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
 
    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() {

                return bais.read();
            }
        };
    }
}