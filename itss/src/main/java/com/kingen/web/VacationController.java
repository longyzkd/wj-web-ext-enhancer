package com.kingen.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.kingen.bean.User;
import com.kingen.bean.workflow.BaseVO;
import com.kingen.bean.workflow.CommentVO;
import com.kingen.bean.workflow.Vacation;
import com.kingen.service.account.AccountService;
import com.kingen.service.oa.vocation.VacationService;
import com.kingen.service.workflow.ProcessService;
import com.kingen.util.ActivitiUtils;
import com.kingen.util.JsonResultBuilder;
import com.kingen.web.workflow.Pagination;
import com.kingen.web.workflow.PaginationThreadUtils;


/**
 * @ClassName: VacationAction
 * @Description:请假控制类,没有用动态任务分配
 * @author: zml
 * @date: 2014-12-1 上午12:35:50
 *
 */

@Controller
@RequestMapping("/vacation")
public class VacationController extends CommonController{
	private static final Logger logger = LoggerFactory.getLogger(VacationController.class);
	
	@Autowired
	private VacationService vacationService;
	
	@Autowired
	protected RuntimeService runtimeService;
	
    @Autowired
    protected IdentityService identityService;
    
    @Autowired
    protected TaskService taskService;
    
	@Autowired
	private AccountService userService;
	
	@Autowired
	private ProcessService processService;
	
	/**
	 * 查询某人的所有请假申请
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:vacation:list")
	@RequestMapping("/toList_page")
	public String toList(HttpSession session, Model model) throws Exception{
		User user = getCurrentUser();
		List<Vacation> list = this.vacationService.list(user.getUserId());
//		for(Vacation v : list){
//			if(BaseVO.APPROVAL_SUCCESS.equals(v.getStatus())){
//				Vacation vacation = (Vacation)this.historyService.createHistoricVariableInstanceQuery()
//					.processInstanceId(v.getProcessInstanceId()).variableName("entity");
//				
//			}
//		}
		Pagination pagination = PaginationThreadUtils.get();
		model.addAttribute("page", pagination.getPageStr());
		model.addAttribute("vacationList", list);
		return "vacation/list_vacation";
	}
	
	/**
	 * 跳转添加页面
	 * @param model
	 * @return
	 */
//	@RequiresPermissions("user:vacation:toAdd")
	@RequestMapping(value = "/toAdd", method = RequestMethod.GET)
	public ModelAndView toAdd(Model model){
		if(!model.containsAttribute("vacation")) {
            model.addAttribute("vacation", new Vacation());
        }
		return new ModelAndView("vacation/vacation-add").addObject(model);
	}
	
	/**
	 * 详细信息
	 * @param id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("user:vacation:details")
	@RequestMapping(value="/details/{id}", method = RequestMethod.GET)
	public String details(@PathVariable("id") Integer id, Model model) throws Exception{
		Vacation vacation = this.vacationService.unique(id);
		model.addAttribute("vacation", vacation);
		return "/vacation/details_vacation";
	}
	
    /**
     * 添加并启动请假流程
     *
     * @param leave
     */
//	@RequiresPermissions("user:vacation:doAdd")
	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	public void  doAdd(
			@ModelAttribute("vacation")  Vacation vacation,BindingResult results, 
			RedirectAttributes redirectAttributes, 
			HttpSession session,  HttpServletResponse response,
			Model model) throws Exception{
		User user = getCurrentUser();
        
        vacation.setUserId(user.getUserId());
        vacation.setUser_name(user.getUsername());
        vacation.setTitle(user.getUsername()+" 的请假申请");
        vacation.setBusinessType(BaseVO.VACATION); 			//业务类型：请假申请
        vacation.setStatus(BaseVO.PENDING);					//审批中
        vacation.setApplyDate(new Date());
        
        
        JSONObject jsonObject= new JSONObject();
        try {
        	String processInstanceId = this.processService.startVacation(vacation);
        	
//            redirectAttributes.addFlashAttribute("message", "流程已启动，流程ID：" + processInstanceId);
        	
            jsonObject = JsonResultBuilder.success(true).msg("流程已启动，流程ID：" + processInstanceId).json();
            logger.info("processInstanceId: "+processInstanceId);
        } catch (ActivitiException e) {
            if (e.getMessage().indexOf("no processes deployed with key") != -1) {
                logger.warn("没有部署流程!", e);
//                redirectAttributes.addFlashAttribute("error", "没有部署流程，请在[工作流]->[流程管理]页面点击<重新部署流程>-待完成");
                
                jsonObject = JsonResultBuilder.success(false).msg("没有部署流程，请在[工作流]->[流程管理]页面点击<重新部署流程>-待完成").json();
            } else {
                logger.error("启动请假流程失败：", e);
                jsonObject = JsonResultBuilder.success(false).msg("系统内部错误！").json();
//                redirectAttributes.addFlashAttribute("error", "系统内部错误！");
            }
        } catch (Exception e) {
            logger.error("启动请假流程失败：", e);
//            redirectAttributes.addFlashAttribute("error", "系统内部错误！");
            jsonObject = JsonResultBuilder.success(false).msg("系统内部错误！").json();
        }
//		return "redirect:/vacationAction/toAdd";
        writeJson(response,jsonObject); 
	}
	
    /**
     * 根据不同的userTask 的 ID ,跳转不同的 审批请假流程
     * @param taskId
     * @param model
     * @return
     * @throws NumberFormatException
     * @throws Exception
     */
//	@RequiresPermissions("user:vacation:toApproval") 	//*代表 经理、总监、人力
    @RequestMapping("/toApproval/{taskId}")
    public String  toApproval(@PathVariable("taskId") String taskId, Model model) throws NumberFormatException, Exception{
    	Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
		// 根据任务查询流程实例
    	String processInstanceId = task.getProcessInstanceId();
		ProcessInstance pi = this.runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		Vacation vacation = (Vacation) this.runtimeService.getVariable(pi.getId(), "entity");
//		vacation.setTask(task);
		
		vacation.setTask(ActivitiUtils.mapSetterTask(task));
		vacation.setProcessInstanceId(processInstanceId);
		List<CommentVO> commentList = this.processService.getComments(processInstanceId); // 所有的评论列表
		String taskDefinitionKey = task.getTaskDefinitionKey();  //userTask 的 ID
		logger.info("taskDefinitionKey: "+taskDefinitionKey);
		String result = null;
		if("modifyApply".equals(taskDefinitionKey)){
			result = "vacation/vacation-modify";
		}else{
			result = "vacation/vacation-audit";
		}
		model.addAttribute("vacation", vacation);
		model.addAttribute("commentList", commentList);
		
    	return result;
    }
    
    /**
     * 完成任务
     * @param content
     * @param completeFlag
     * @param taskId
     * @param redirectAttributes
     * @param session
     * @return
     * @throws Exception
     */
//	@RequiresPermissions("user:vacation:complate")  //数据库中权限字符串为user:*:complate， 通配符*匹配到vacation所以有权限操作 
    @RequestMapping("/complete/{taskId}")
    public @ResponseBody JSONObject complete(
    		@RequestParam("vacationId") Integer vacationId,
    		@RequestParam("content") String content,
    		@RequestParam("completeFlag") Boolean completeFlag,
    		@PathVariable("taskId") String taskId, 
    		RedirectAttributes redirectAttributes,
    		HttpSession session) throws Exception{
		User user = getCurrentUser();
    	String groupType = "";
        Vacation vacation = this.vacationService.findById(vacationId);
        Vacation baseVacation = (Vacation) this.runtimeService.getVariable(vacation.getProcessInstanceId(), "entity");
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("isPass", completeFlag);
		if(completeFlag){
			//由userTask自动分配审批权限
//			variables.put("auditGroup", "hr");
			if("hr".equals(groupType)){
				vacation.setStatus(BaseVO.APPROVAL_SUCCESS);
			}
		}else{
			baseVacation.setTitle(baseVacation.getUser_name()+" 的请假申请失败,需修改后重新提交！");
			vacation.setStatus(BaseVO.APPROVAL_FAILED);
			variables.put("entity", baseVacation);
		}
		this.vacationService.doUpdate(vacation);
		// 完成任务
		this.processService.complete(taskId, content, user.getUserId().toString(), variables);
		
		JSONObject jsonObject = JsonResultBuilder.success(true).msg("任务办理完成！").json();
//		redirectAttributes.addFlashAttribute("message", "任务办理完成！");
//    	return "redirect:/processAction/todoTaskList_page";
		return jsonObject;
    }
    
    /**
     * 调整请假申请
     * @param vacation
     * @param results
     * @param taskId
     * @param session
     * @param reApply
     * @param model
     * @return
     * @throws Exception 
     */
//	@RequiresPermissions("user:vacation:modify")
	@RequestMapping(value = "/modifyVacation/{taskId}", method = RequestMethod.POST)
	public @ResponseBody JSONObject modifyVacation(
			@ModelAttribute("vacation") Vacation vacation,
			BindingResult results,
			@PathVariable("taskId") String taskId,
			@RequestParam("processInstanceId") String processInstanceId,
			@RequestParam("reApply") Boolean reApply,
			RedirectAttributes redirectAttributes,
			HttpSession session,
			Model model
			) throws Exception {
//		
//		if(results.hasErrors()){
//        	model.addAttribute("vacation", vacation);
//        	return "vacation/modify_vacation";
//        }
		
		User user = getCurrentUser();

        Map<String, Object> variables = new HashMap<String, Object>();
        vacation.setUserId(user.getUserId());
        vacation.setUser_name(user.getUsername());
        vacation.setBusinessType(BaseVO.VACATION);
        vacation.setApplyDate(new Date());
        vacation.setBusinessKey(vacation.getId().toString());
        vacation.setProcessInstanceId(processInstanceId);
        
        JSONObject jsonObject= new JSONObject();
        if(reApply){
        	//修改请假申请
	        vacation.setTitle(user.getUsername()+" 的请假申请！");
	        vacation.setStatus(BaseVO.PENDING);
	        //由userTask自动分配审批权限
//	        redirectAttributes.addFlashAttribute("message", "任务办理完成，请假申请已重新提交！");
	        jsonObject = JsonResultBuilder.success(true).msg("任务办理完成，请假申请已重新提交！").json();
        }else{
        	vacation.setTitle(user.getUsername()+" 的请假申请已取消！");
        	vacation.setStatus(BaseVO.APPROVAL_FAILED);
//        	redirectAttributes.addFlashAttribute("message", "任务办理完成，已经取消您的请假申请！");
        	jsonObject = JsonResultBuilder.success(true).msg("任务办理完成，已经取消您的请假申请！").json();
        }
        this.vacationService.doUpdate(vacation);
        variables.put("entity", vacation);
        variables.put("reApply", reApply);
		this.processService.complete(taskId, null, user.getUserId().toString(), variables);
		
//    	return "redirect:/processAction/todoTaskList_page";
		return jsonObject;
    }
}
