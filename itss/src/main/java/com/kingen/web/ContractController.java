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
import com.kingen.bean.Contract;
import com.kingen.service.contract.ContractService;
import com.kingen.util.JsonResultBuilder;
import com.kingen.util.Page;


/**
 * 合同
 * @author wj
 * @date 2016-12-20
 */
@Controller
@RequestMapping(value = "/contract")
//TODO 还未完成
public class ContractController extends CommonController{

	private static Logger logger = LoggerFactory.getLogger(ContractController.class);
	

	@Autowired
	private ContractService service;

	
	@RequestMapping(value="/")
	@ControllerLogAnnotation(moduleName="服务管理",option="合同管理")
	@RequiresPermissions("contract:view")
	public String serviceLv( Model m,HttpServletResponse response) throws Exception{
			
			return "contract/list"; 
	}
	/**
	 * 查找分页后的grid
	 */
	@RequestMapping(value="data")
	public void data(Page<Contract> page,HttpServletResponse response) {
		page = service.find(page);
		writeJson(response,page);
	}
	
	@RequestMapping(value="toEdit")
	public String toEdit(String id,String action,HttpServletResponse response,HttpServletRequest request,Model model){
		model.addAttribute("action", action);
		model.addAttribute("id", id);//null的话 前台是空串
		return "serv/edit"; 
	}
	
	@RequestMapping(value="one")
	public void one(String id, HttpServletResponse response) {
		Contract u  = service.unique(id);
		writeJson(response,u);
	}
	
	
	
	/**
	 * 删除
	 */
	@ControllerLogAnnotation(moduleName="服务管理-合同管理",option="删除")
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
	@ControllerLogAnnotation(moduleName="服务管理-合同管理",option="新增")
	public void save(Contract data,HttpServletResponse response
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
	@ControllerLogAnnotation(moduleName="服务管理-合同管理",option="编辑")
	public void update(Contract data,HttpServletResponse response
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

