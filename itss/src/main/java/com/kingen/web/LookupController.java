/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.kingen.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.kingen.aop.ControllerLogAnnotation;
import com.kingen.bean.Log;
import com.kingen.service.log.LogService;
import com.kingen.util.Json;
import com.kingen.util.JsonResultBuilder;
import com.kingen.util.Page;
import com.kingen.util.StringUtils;
import com.kingen.util.excel.ExportExcel;


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
	private LogService service;

	
	
	
	@RequestMapping(value="/{type}")
	@ControllerLogAnnotation(moduleName="服务管理",option="事件分类管理")
	@RequiresPermissions("eventCat:view")
	public String execute(@PathVariable String type ,Model m,HttpServletResponse response) throws Exception{
		m.addAttribute("type", type);
		if("eventCat".equals(type)){
			
			return "lookup/list"; 
		}else if("priority".equals(type)){
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
	public void data(@PathVariable String type ,Page<Log> page,HttpServletResponse response) {
		page = service.getLogs(page,type);
		writeJson(response,page);
	}
	
	
	
//	@RequestMapping(value="priority")
	@ControllerLogAnnotation(moduleName="服务管理",option="优先级管理")
	@RequiresPermissions("priority:view")
	public String priority() throws Exception{
		return "lookup/priority-list"; 
	}
//	/**
//	 * 查找分页后的grid
//	 */
//	@RequestMapping(value="priority/data")
//	public void priorityData(Page<Log> page,Log vo,HttpServletResponse response) {
//		page = service.getLogs(page,vo);
//		writeJson(response,page);
//	}
//	
	
	
	
//	@RequestMapping(value="faultLv")
	@ControllerLogAnnotation(moduleName="服务管理",option="故障级别管理")
	@RequiresPermissions("faultLv:view")
	public String faultLv() throws Exception{
		return "lookup/faultLv-list"; 
	}
//	/**
//	 * 查找分页后的grid
//	 */
//	@RequestMapping(value="faultLv/data")
//	public void faultLvData(Page<Log> page,Log vo,HttpServletResponse response) {
//		page = service.getLogs(page,vo);
//		writeJson(response,page);
//	}
//	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 删除
	 */
	@ControllerLogAnnotation(moduleName="日志管理",option="删除")
	@RequestMapping(value="deleteThem")
	public void deleteThem(String[] ids ,HttpServletResponse response) {
		Json json = new Json();
		
		try{
		
			service.del(ids);
			json.setSuccess(true);
			json.setMsg("删除成功");
		}catch(Exception e){
			json.setMsg("系统出错");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		writeJson(response,json);
	}
	

	/**
	 * 导出日志数据
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@ControllerLogAnnotation(moduleName="日志管理",option="导出")
    @RequestMapping(value = "exportFile", method=RequestMethod.GET)
    public void exportFile(Page<Log> page,Log vo,HttpServletResponse response) {
		JSONObject jsonObject= new JSONObject();
		try {
			String fileName ="";
			if(StringUtils.isEmpty(vo.getFromDate())){
				fileName  = "日志（"+vo.getToDate()+"之前）.xlsx";
			}else{
				 fileName = "日志（"+vo.getFromDate()+"至"+vo.getToDate()+"）.xlsx";
			}
//            page = service.getLogs(page,vo);
//    		new ExportExcel("日志", Log.class).setDataList(page.getDataList()).write(response, fileName).dispose();
			 List<Log> result = service.getLogs(vo);
    		new ExportExcel("日志", Log.class).setDataList(result).write(response, fileName).dispose();
    	//	jsonObject = JsonResultBuilder.success(true).msg( "导出成功").json();
		} catch (Exception e) {
			
			jsonObject = JsonResultBuilder.success(false).msg( "导出用户失败！失败信息："+e.getMessage()).json();

			writeJson(response,jsonObject); //防止写两次responese
		}
    }

	
	
}

