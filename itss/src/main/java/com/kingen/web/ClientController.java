package com.kingen.web;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.kingen.aop.ControllerLogAnnotation;
import com.kingen.bean.Client;
import com.kingen.bean.ClientContact;
import com.kingen.service.client.ClientService;
import com.kingen.util.JsonResultBuilder;
import com.kingen.util.Page;


/**
 * 客户管理
 * @author wj
 * @date 2016-12-20
 */
@Controller
@RequestMapping(value = "/client")
public class ClientController extends CommonController{

	private static Logger logger = LoggerFactory.getLogger(ClientController.class);
	

	@Autowired
	private ClientService service;

	
	@RequestMapping(value="/")
	@ControllerLogAnnotation(moduleName="客户管理",option="客户管理")
	@RequiresPermissions("client:view")
	public String excute( Model m,HttpServletResponse response) throws Exception{
			
			return "client/list"; 
	}
	/**
	 * 客户
	 */
	@RequestMapping(value="data")
	public void data(Page<Client> page,HttpServletResponse response) {
		page = service.find(page);
		writeJson(response,page);
	}
	
	/**
	 * 客户联系人
	 */
	@RequestMapping(value="contact/data")
	public void contactdata(String clientId,Page<ClientContact> page,HttpServletResponse response) {
		page = service.findClientContacts(page, clientId);
		writeJson(response,page);
	}
	
	@RequestMapping(value="toEdit")
	public String toEdit(String id,String action,HttpServletResponse response,HttpServletRequest request,Model model){
		model.addAttribute("action", action);
		model.addAttribute("id", id);//null的话 前台是空串
		return "client/edit"; 
	}
	
	@RequestMapping(value="one")
	public void one(String id, HttpServletResponse response) {
		Client u  = service.unique(id);
		writeJson(response,u);
	}
	@RequestMapping(value="contact/toEdit")
	public String contactToEdit(String clientId,String id,String action,HttpServletResponse response,HttpServletRequest request,Model model){
		model.addAttribute("action", action);
		model.addAttribute("id", id);//null的话 前台是空串
		model.addAttribute("clientId", clientId);
		return "client/contact-edit"; 
	}
	
	@RequestMapping(value="contact/one")
	public void contactOne(String id, HttpServletResponse response) {
		ClientContact u  = service.uniqueEntity("ClientContact", "id", id);
		writeJson(response,u);
	}
	
	
	/**
	 * 删除
	 */
	@ControllerLogAnnotation(moduleName="客户管理",option="删除")
	@RequestMapping(value="deleteThem")
	public void deleteThem(String[] ids ,HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try{
		
			service.delThemCascade("client",Arrays.asList(ids));
			json = JsonResultBuilder.success(true).msg("删除成功").json();
		}catch(Exception e){
			json = JsonResultBuilder.success(false).msg("系统出错").json();
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		writeJson(response,json);
	}
	

	
//为了记录日志 所以把新增、修改分开
	/**
	 * 新建
	 * @throws Exception 
	 */
	@RequestMapping(value="save")
	@ControllerLogAnnotation(moduleName="客户管理",option="新增")
	public void save(Client data,HttpServletResponse response
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
	@ControllerLogAnnotation(moduleName="客户管理",option="编辑")
	public void update(Client data,HttpServletResponse response
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
	
	
	
//--------------------联系人
	
	/**
	 * 删除
	 */
	@ControllerLogAnnotation(moduleName="客户管理-客户联系人",option="删除")
	@RequestMapping(value="contact/deleteThem")
	public void contactDeleteThem(String[] ids ,HttpServletResponse response) {
		JSONObject json = new JSONObject();
		try{
			
			service.delThemCascade("contact",Arrays.asList(ids));
			json = JsonResultBuilder.success(true).msg("删除成功").json();
		}catch(Exception e){
			json = JsonResultBuilder.success(false).msg("系统出错").json();
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		writeJson(response,json);
	}
	
	

	
	//为了记录日志 所以把新增、修改分开
	/**
	 * 新建 联系人
	 * @throws Exception 
	 */
	@RequestMapping(value="contact/save")
	@ControllerLogAnnotation(moduleName="客户管理-客户联系人",option="新增")
	public void contactSave(String clientId ,ClientContact data,HttpServletResponse response
			)  {
		JSONObject json = new JSONObject();
		try {
			service.save(clientId, data);
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
	@RequestMapping(value="contact/update")
	@ControllerLogAnnotation(moduleName="客户管理-客户联系人",option="编辑")
	public void contactUpdate(ClientContact data,HttpServletResponse response
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

