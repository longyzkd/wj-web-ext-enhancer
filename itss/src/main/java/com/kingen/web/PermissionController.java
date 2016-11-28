package com.kingen.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.kingen.aop.ControllerLogAnnotation;
import com.kingen.bean.Menu;
import com.kingen.bean.RightData;
import com.kingen.bean.SysOrgMenu;
import com.kingen.service.account.AccountService;
import com.kingen.service.permission.PermissionService;
import com.kingen.util.Json;
import com.kingen.util.TreeConverter;

/**
 * 权限controller
 * @author wj
 */

@Controller
@RequestMapping(value = "/permission")
public class PermissionController extends CommonController{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private PermissionService service;
	@Autowired
	private AccountService accountservice;
	
	@ControllerLogAnnotation(moduleName="权限管理",option="查看")
	@RequiresPermissions("permission:view")
	@RequestMapping(value="/")
	public String execute(HttpServletResponse response){
		return "permission/permissions"; 
	}
	
	
	/**
	 * 静态菜单树
	 * @param orgId  有可能为空 
	 * @param response
	 */
	@RequestMapping(value="checkedTree")
	public void checkedTree(String orgId,HttpServletResponse response){//String node ，异步树，会传node过来，再根据此Node加载下级树
		
		List<RightData> allPermissions = iniRightDatas();
		List<SysOrgMenu> permissioned = null;
		if(StringUtils.isNotEmpty(orgId)){
			
			 permissioned = accountservice.findMenusBy(orgId);
		}
		if(!CollectionUtils.isEmpty(permissioned)){
			checked(allPermissions, permissioned);
		}
		Json json = new Json();
		List<Map<String, Object>> children = Lists.newArrayList(TreeConverter.tree(allPermissions)); 
		json.setChildren(children);//静态树必须这么玩
		json.setSuccess(true);
		writeJson(response,json);
	}
	
	/**
	 * 静态菜单树
	 * @param action
	 * @param userId
	 * @param response
	 */
	@ControllerLogAnnotation(moduleName="权限管理",option="分配权限")
	@RequestMapping(value="checkTree")
	public void checkTree(String orgId, String[] permissionIds  ,HttpServletResponse response){//String node ，异步树，会传node过来，再根据此Node加载下级树
		
		Json json = new Json();
		try {
			service.editPermission(orgId, permissionIds);
			json.setSuccess(true);
			json.setMsg("保存成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg(e.getMessage());
			logger.error(e.getMessage());
		}
		
		writeJson(response,json);
	}
	
	
	
	private  void checked(List<RightData> allPermissions, List<SysOrgMenu> permissioned){
		for(RightData rightData : allPermissions){
			for(SysOrgMenu tmanageruserfun : permissioned){
				if(StringUtils.equals(rightData.getMenuId(), tmanageruserfun.getId().getMenuId())){
					setRightChecked(allPermissions, rightData);
				}
			}
		}
	}
	
	/**
	 * 递归往上check
	 * @param allPermissions  所有权限树节点
	 * @param rightData   其中之一
	 */
	private void setRightChecked(List<RightData> allPermissions, RightData rightData){
		rightData.setChecked(true);
		for(RightData rightData2 : allPermissions){
			if(StringUtils.equals(rightData.getPmenuId(), rightData2.getMenuId())){
				rightData2.setChecked(true); //父节点
				setRightChecked(allPermissions, rightData2);
			}
		}
	}
//	private void setRightChecked(List<RightData> allPermissions, RightData rightData){
//		rightData.setChecked(true);
//		boolean flag = false;
//		for(RightData rightData2 : allPermissions){
//			if(StringUtils.equals(rightData.getFFunID(), rightData2.getFunID())){
//				rightData2.setChecked(true); //子节点
//				rightData = rightData2;
//				flag = true;
//			}
//		}
//		if(flag){//有子节点
//			setRightChecked(allPermissions, rightData);
//		}
//	}
	/**
	 * 查询所有菜单
	 * @return  权限树
	 */
	private List<RightData> iniRightDatas(){
		List<RightData> rightDatas = new ArrayList<RightData>();
		List<Menu> tmanagerfuns = accountservice.findAllMenus();
		if(tmanagerfuns!=null&&tmanagerfuns.size()>0){
			for(Menu tmanagerfun : tmanagerfuns){
				RightData rightData = new RightData();
				rightData.setChecked(false);
				rightData.setExpanded(false);
				rightData.setMenuId(tmanagerfun.getMenuId());
				rightData.setMenuName(tmanagerfun.getMenuName());
				rightData.setPmenuId(tmanagerfun.getFmenuId());
				rightDatas.add(rightData);
			}
		}	
		return rightDatas;
	}
	
}
