/**
 * Copyright &copy; 2012-2013 <a href="https://github.com//jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.kingen.web;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.kingen.bean.User;
import com.kingen.util.DateUtils;
import com.kingen.util.FastjsonFilter;

import groovy.json.StringEscapeUtils;


/**
 * 如果是get，需要修改 Tomcat根式目录的 conf/server.xml文件中，找<Connector port="8080" />，在里面加<Connector port="8080" uRIEncoding="utf-8" />
 * 
 * 控制器支持类
 * @author 
 * @version 2013-3-23
 */
public abstract class CommonController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * current session's user
	 * @return
	 */
	protected User getCurrentUser() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		return user;
	}
	
	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : text.trim());
//				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
//				setValue(text == null ? null : StringEscapeUtils.escapeJavaScript(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
					
					setValue(DateUtils.parseDate(StringUtils.trim(text)));
					logger.debug("日期---"+getValue());
			}
		});
	}
	
	public void writeJson(HttpServletResponse response,Object object) {
		writeJsonByFilter(response,object, null, null);
	}
	public void writeJson(HttpServletResponse response,Object object,String[] includesProperties, String[] excludesProperties) {
		writeJsonByFilter(response,object, includesProperties, excludesProperties);
	}
	public void writeJsonInclude(HttpServletResponse response,Object object, String[] includesProperties) {
		writeJsonByFilter(response,object, includesProperties, null);
	}
	public void writeJson(HttpServletResponse response,Object object, String[] excludesProperties) {
		writeJsonByFilter(response,object, null, excludesProperties);
	}
	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * 
	 * @param object
	 * @param includesProperties
	 *            需要转换的属性
	 * @param excludesProperties
	 *            不需要转换的属性
	 */
	public void writeJsonByFilter(HttpServletResponse response,Object object, String[] includesProperties, String[] excludesProperties) {
		try {
			FastjsonFilter filter = new FastjsonFilter();// excludes优先于includes
			if (excludesProperties != null && excludesProperties.length > 0) {
				filter.getExcludes().addAll(Arrays.<String> asList(excludesProperties));
			}
			if (includesProperties != null && includesProperties.length > 0) {
				filter.getIncludes().addAll(Arrays.<String> asList(includesProperties));
			}
			//logger.info("对象转JSON：要排除的属性[" + excludesProperties + "]要包含的属性[" + includesProperties + "]");
			String json;
			String User_Agent = getRequest().getHeader("User-Agent");
			if (StringUtils.indexOfIgnoreCase(User_Agent, "MSIE 6") > -1) {
				// 使用SerializerFeature.BrowserCompatible特性会把所有的中文都会序列化为\\uXXXX这种格式，字节数会多一些，但是能兼容IE6
				json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.BrowserCompatible);
			} else {
				// 使用SerializerFeature.WriteDateUseDateFormat特性来序列化日期格式的类型为yyyy-MM-dd hh24:mi:ss
				// 使用SerializerFeature.DisableCircularReferenceDetect特性关闭引用检测和生成
				json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
			}
			//logger.info("转换后的JSON字符串：" + json);
			response.setContentType("text/html;charset=utf-8");
			
			
//			response.getWriter().write(json);
//			response.getWriter().flush();
//			response.getWriter().close();
			
//			response.getOutputStream().write(json.getBytes());
			response.getOutputStream().write(json.getBytes("UTF-8"));
			response.getOutputStream().flush();
			response.getOutputStream().close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * 
	 * @param object
	 * @param includesProperties
	 *            需要转换的属性
	 * @param excludesProperties
	 *            不需要转换的属性
	 */
	public void writeJackson(HttpServletResponse response,Object object, String[] includesProperties, String[] excludesProperties) {
		try {
			
			ObjectMapper mapper = new ObjectMapper(); //转换器  
	          
	        String json=serializeOnlyGivenFields(object,Arrays.asList(excludesProperties));//将对象转换成json  
			response.setContentType("text/html;charset=utf-8");
			
			response.getOutputStream().write(json.getBytes("UTF-8"));
			response.getOutputStream().flush();
			response.getOutputStream().close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String FILTER_NAME = "fieldFilter";
	public static String serializeOnlyGivenFields(Object o, Collection<String> fields) throws JsonProcessingException {
		if ((fields == null) || fields.isEmpty())
			fields = new HashSet<String>();

		Set<String> properties = new HashSet<String>(fields);

		SimpleBeanPropertyFilter filter = new SimpleBeanPropertyFilter.FilterExceptFilter(properties);
		SimpleFilterProvider fProvider = new SimpleFilterProvider();
		fProvider.addFilter(FILTER_NAME, filter);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setAnnotationIntrospector(new AnnotationIntrospector());

		String json = mapper.writer(fProvider).writeValueAsString(o);
		return json;
	}

	private static class AnnotationIntrospector extends JacksonAnnotationIntrospector {
		@Override
		public Object findFilterId(Annotated a) {
			return FILTER_NAME;
		}
	}
	
	
	
	
	
	/**
	 * 获得request
	 * 
	 * @return
	 */
	public HttpServletRequest getRequest() {
//		return ServletActionContext.getRequest();
		return ((ServletRequestAttributes)RequestContextHolder
				.getRequestAttributes()).getRequest();
	}

	/**
	 * 获得response
	 * 
	 * @return
	 */

	/**
	 * 获得session
	 * 
	 * @return
	 */
	public HttpSession getSession() {
//		return ServletActionContext.getRequest().getSession();
		return ((ServletRequestAttributes)RequestContextHolder
				.getRequestAttributes()).getRequest().getSession();
	}
	

	
	
	
}
