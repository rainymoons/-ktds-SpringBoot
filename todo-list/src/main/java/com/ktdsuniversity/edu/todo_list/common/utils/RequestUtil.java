package com.ktdsuniversity.edu.todo_list.common.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public final class RequestUtil {

	public static HttpServletRequest getRequest() {
		ServletRequestAttributes request = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();;
		return request.getRequest();
	}
	
	public static String getIp() {
		return getRequest().getRemoteAddr();
	}
	
}
