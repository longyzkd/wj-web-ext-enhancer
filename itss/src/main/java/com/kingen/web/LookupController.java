/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.kingen.web;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.kingen.aop.ControllerLogAnnotation;
import com.kingen.bean.Lookup;
import com.kingen.service.lookup.LookupService;
import com.kingen.util.JsonResultBuilder;
import com.kingen.util.Page;


/**
 * 数据字典管理
 * @author wj
 * @date 2016-12-16
 */
@Controller
@RequestMapping(value = "/lookup")
public class LookupController extends CommonController{

	private static Logger logger = LoggerFactory.getLogger(LookupController.class);
	

	@Autowired
	private LookupService service;

	
	@RequestMapping(value="/{type}")
	@ControllerLogAnnotation(moduleName="服务管理",option="事件分类管理")
	@RequiresPermissions("eventCat:view")
	public String execute(@PathVariable("type") String type ,Model m,HttpServletResponse response) throws Exception{
		m.addAttribute("type", type);
		if("eventCat".equals(type)){
			
			return "lookup/list"; 
		}else if("priority".equals(type)){ //需要AOP
			return priority(); 
		}else if("faultLv".equals(type)){
			return faultLv(); 
		}
		
		return ""; 
	}
	/**
	 * 查找分页后的grid
	 */
	@RequestMapping(value="/data/{type}")
	public void data(@PathVariable("type") String type ,Page<Lookup> page,HttpServletResponse response) {
		page = service.data(page,type);
		writeJson(response,page);
	}
	
	@RequestMapping(value="toEdit/{type}")
	public String toEdit(@PathVariable("type") String type ,String id,String action,HttpServletResponse response,HttpServletRequest request,Model model){
		model.addAttribute("type", type);
		model.addAttribute("action", action);
		model.addAttribute("id", id);//null的话 前台是空串
		return "lookup/edit"; 
	}
	
	@RequestMapping(value="/one")
	public void one(String id, HttpServletResponse response) {
		Lookup u  = service.unique(id);
		writeJson(response,u);
	}
	
	
//	@RequestMapping(value="priority")
	@ControllerLogAnnotation(moduleName="服务管理",option="优先级管理")
	@RequiresPermissions("priority:view")
	public String priority() throws Exception{
		return "lookup/list"; 
	}
	
	
	
//	@RequestMapping(value="faultLv")
	@ControllerLogAnnotation(moduleName="服务管理",option="故障级别管理")
	@RequiresPermissions("faultLv:view")
	public String faultLv() throws Exception{
		return "lookup/list"; 
	}
	
	
	/**
	 * 删除
	 */
	@ControllerLogAnnotation(moduleName="服务管理（事件分类或优先级或故障级别）",option="删除")
	@RequestMapping(value="deleteThem")
	public void deleteThem(String[] ids ,HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try{
		
			service.delThem(Arrays.asList(ids));
			json = JsonResultBuilder.success(true).msg("删除成功").json();
		}catch(Exception e){
			json = JsonResultBuilder.success(false).msg("系统出错").json();
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		writeJson(response,json);
	}
	


	/**
	 * 新建
	 * @throws Exception 
	 */
	@RequestMapping(value="save")
	@ControllerLogAnnotation(moduleName="服务管理（事件分类或优先级或故障级别）",option="新增")
	public void save(Lookup data,HttpServletResponse response
			)  {
		JSONObject json = new JSONObject();
		try {
			service.add(data);
			json = JsonResultBuilder.success(true).msg("保存成功").json();
		} catch (Exception e) {//service ：回滚、记录异常日志
			// TODO Auto-generated catch block
			e.printStackTrace();
			json = JsonResultBuilder.success(false).msg(e.getMessage()).json();
			logger.error(e.getMessage());
			
		}
		  writeJson(response,json);
		
	}
	
	/**
	 * 更新
	 */
	@RequestMapping(value="update")
	@ControllerLogAnnotation(moduleName="服务管理（事件分类或优先级或故障级别）",option="编辑")
	public void update(Lookup data,HttpServletResponse response
			) {
		JSONObject json = new JSONObject();
		try {
			service.update(data);
			
			json = JsonResultBuilder.success(true).msg("保存成功").json();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json = JsonResultBuilder.success(false).msg(e.getMessage()).json();
			logger.error(e.getMessage());
		}
		
		writeJson(response,json);
	}

	
	
}

