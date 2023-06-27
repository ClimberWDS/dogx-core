package com.dogx.core.base.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author hxl
 * @Date:2021/8/17-08-17 12:25
 * @Version:1.0
 */
public class HttpLogInterceptor implements Interceptor {
    private Logger logger = LoggerFactory.getLogger(HttpLogInterceptor.class);

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.nanoTime();
        logger.info(String.format("Sending request %s on %s",
                request.url(), chain.connection()));
        Response response = chain.proceed(request);
        long endTime = System.nanoTime();
        logger.info(String.format("Received response for %s in %.1fms",
                response.request().url(), (endTime - startTime) / 1e6d));
        return response;
    }
}
