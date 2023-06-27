package com.dogx.core.common.utils;

import com.dogx.core.base.constant.ConfigConsts;
import com.dogx.core.base.utils.CommonUtils;
import com.dogx.core.base.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @Auther:hxl
 * @Date:2021/11/1-11-01 16:42
 * @Version:1.0
 */
@Slf4j
public class HttpParamUtils {
	/**
	 * 获取所有参数，包括 url和body
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static SortedMap<String, String> getAllParams(HttpServletRequest request) throws IOException {
		// 获取 url上的参数
		Map<String, String> urlParams = getUrlParams(request);
		// 获取 body上的参数
		Map<String, String> bodyParams = getBodyParams(request);
		// 总的参数的map
		SortedMap<String, String> allMap = new TreeMap<>();
		if(urlParams != null && urlParams.size() > 0) {
			allMap.putAll(urlParams);
		}
		if(bodyParams != null && bodyParams.size() > 0) {
			allMap.putAll(bodyParams);
		}
		return allMap;
	}

	/**
	 * 获取body中的参数
	 * 为空的判断 ，同学自己加
	 */
	private static Map<String, String> getBodyParams(HttpServletRequest request) throws IOException {
		InputStream stream = request.getInputStream();
		String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
		if(CommonUtils.isEmpty(body)){
			return null;
		}
		// 转map
		if (log.isDebugEnabled()) {
			log.debug("LogFilter, Content-Type: {}", request.getContentType());
			log.debug("LogFilter, body: {}", CommonUtils.truncateContent(body));
		}
		if (request.getContentType() != null && request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
			return JacksonUtils.toObj(body, Map.class);
		} else if (request.getContentType() != null && request.getContentType().contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
			return getParamMap(body);
		} else {
			return Collections.emptyMap();
		}
	}

	/***
	 * 读取url参数
	 * @param request
	 * @return
	 */
	private static Map<String, String> getUrlParams(HttpServletRequest request) throws UnsupportedEncodingException {
		String queryParam = "";
		if (StringUtils.hasLength(request.getQueryString())) {
			queryParam = URLDecoder.decode(request.getQueryString(), ConfigConsts.ENCODING);
		}
		return getParamMap(queryParam);
	}

	/**
	 * 读取参数
	 * @param queryParam a=1&b=2
	 * @return
	 */
	public static Map<String, String> getParamMap(String queryParam) {
		if (!StringUtils.hasLength(queryParam)) {
			return Collections.emptyMap();
		}
		Map<String, String> result = new HashMap<>();
		String[] split = queryParam.split("&");
		for (String s : split) {
			int i = s.indexOf("=");
			result.put(s.substring(0, i), s.substring(i + 1));
		}
		return result;
	}

}