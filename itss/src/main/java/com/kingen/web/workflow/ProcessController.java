package com.kingen.web.workflow;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FilenameUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kingen.bean.User;
import com.kingen.bean.workflow.BaseVO;
import com.kingen.service.account.AccountService;
import com.kingen.service.workflow.ProcessService;
import com.kingen.service.workflow.WorkflowDeployService;
import com.kingen.service.workflow.WorkflowTraceService;
import com.kingen.util.ActivitiUtils;
import com.kingen.util.JsonResultBuilder;
import com.kingen.util.Page;
import com.kingen.util.PageUtil;
import com.kingen.util.workflow.WorkflowUtils;
import com.kingen.web.CommonController;

/**
 * 流程管理控制器
 * @author wj
 *
 */
@Controller
@RequestMapping("/workflow")
public class ProcessController extends CommonController{
	private static final Logger logger = LoggerFactory.getLogger(ProcessController.class);
    
	@Autowired
	protected AccountService userService;
    
    @Autowired
    protected WorkflowTraceService traceService;

	@Autowired
	private ProcessService processService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private WorkflowDeployService workflowProcessDefinitionService;
    
    /**
	 * 查询待办任务
	 * @param session
	 * @param redirectAttributes
	 * @param model
	 * @return
	 * @throws Exception
	 */
//	@RequiresPermissions("user:task:todoTask")
	@RequestMapping(value = "/todoTask")
	public String todoTask(HttpSession session, Model model) throws Exception{
		
		return "task/task-list";
	}
	@RequestMapping(value = "/todoTask/data")
	public void todoTaskData(Page<BaseVO> page,HttpSession session, Model model,HttpServletRequest request, HttpServletResponse response) throws Exception{
		String userId = getCurrentUser().getUserId();
		User user = this.userService.unique(userId);
		page = this.processService.findTodoTask(user, page);
//		model.addAttribute("tasklist", taskList);
//		model.addAttribute("taskType", BaseVO.CANDIDATE);
		
		 writeJson(response,page);
	}
	
	
	@RequestMapping(value = "/finishedTask")
	public String finishedTask(HttpSession session, Model model) throws Exception{
		
		return "task/task-end";
	}
	
	
    /**
     * 查看已完成任务列表
     *
     * @return
     * @throws Exception 
     */
//    @RequiresPermissions("user:process:finished")
    @RequestMapping(value = "/finishedTask/data")
    public void finishedTaskData(Page<BaseVO> page,HttpSession session, Model model, HttpServletResponse response) throws Exception {
    	User user = getCurrentUser();
    	 page = this.processService.findFinishedTaskInstances(user, page);
//    	model.addAttribute("tasklist", tasklist);
//    	model.addAttribute("taskType", BaseVO.FINISHED);
//    	return "task/end_task";
    	 writeJson(response,page);
    }

	
	/**
	 * 签收任务
	 * @return
	 */
//	@RequiresPermissions("user:task:claim")
	@RequestMapping("/claim/{taskId}")
	public  @ResponseBody JSONObject  claim(@PathVariable("taskId") String taskId, HttpSession session, RedirectAttributes redirectAttributes) throws Exception{
		User user = getCurrentUser();
		this.processService.doClaim(user, taskId);
		JSONObject jsonObject=JsonResultBuilder.success(true).msg("任务已签收").json();
		return jsonObject;
		
//        redirectAttributes.addFlashAttribute("message", "任务已签收");
//        return "redirect:/processAction/todoTaskList_page";
		
		
	}
    
    /**
     * 显示流程图,带流程跟踪
     * @param processInstanceId
     * @param response
     * @throws Exception 
     */
    @RequestMapping(value = "/process/showDiagram/{processInstanceId}", method = RequestMethod.GET)
	public void showDiagram(@PathVariable("processInstanceId") String processInstanceId, HttpServletResponse response) throws Exception {
	        InputStream imageStream = this.processService.getDiagram(processInstanceId);
	        // 输出资源内容到相应对象
	        byte[] b = new byte[1024];
	        int len;
	        while ((len = imageStream.read(b, 0, 1024)) != -1) {
	            response.getOutputStream().write(b, 0, len);
	        }
	}
    
    /**
     * 显示图片通过部署id，不带流程跟踪(没有乱码问题)
     * @param processDefinitionId
     * @param resourceType	资源类型(xml|image)
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/process/process-definition")
    public void loadByDeployment(@RequestParam("processDefinitionId") String processDefinitionId, @RequestParam("resourceType") String resourceType,
                                 HttpServletResponse response) throws Exception {
    	InputStream resourceAsStream = this.processService.getDiagramByProDefinitionId_noTrace(resourceType, processDefinitionId);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
    
    
    /**
     * 显示图片通过流程id，不带流程跟踪(没有乱码问题)
     *
     * @param resourceType      资源类型(xml|image)
     * @param processInstanceId 流程实例ID
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/process/process-instance")
    public void loadByProcessInstance(@RequestParam("type") String resourceType, @RequestParam("pid") String processInstanceId, HttpServletResponse response)
            throws Exception {
        InputStream resourceAsStream = this.processService.getDiagramByProInstanceId_noTrace(resourceType, processInstanceId);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
    
    
    /**
     * 自定义流程跟踪信息-比较灵活(现在用的这个)
     *
     * @param processInstanceId
     * @return
     * @throws Exception
     */
    @RequiresPermissions("user:process:trace")
    @RequestMapping(value = "/process/trace/{pid}")
    @ResponseBody
    public List<Map<String, Object>> traceProcess(@PathVariable("pid") String processInstanceId) throws Exception {
        List<Map<String, Object>> activityInfos = traceService.traceProcess(processInstanceId);
        return activityInfos;
    }
    
    /**
     * 读取运行中的流程
     * @param businessType
     * @param session
     * @param model
     * @return
     * @throws Exception
     */
    @RequiresPermissions("user:process:running*") //process:vacation,salary,expense:running
    @RequestMapping(value="/process/runingProcessInstance/{businessType}/list_page")
    public String getRuningProcessInstance(@PathVariable("businessType") String businessType,HttpSession session , Model model) throws Exception{
    	User user = getCurrentUser();
    	List<BaseVO> baseVO = null;
    	if(BaseVO.VACATION.equals(businessType)){
    		//请假
    		baseVO = this.processService.listRuningVacation(user);
    		model.addAttribute("businessType", BaseVO.VACATION);
    	}else if(BaseVO.SALARY.equals(businessType)){
//    		//调薪
//    		baseVO = this.processService.listRuningSalaryAdjust(user);
//    		model.addAttribute("businessType", BaseVO.SALARY);
    	}else if(BaseVO.EXPENSE.equals(businessType)){
//    		//报销
//    		baseVO = this.processService.listRuningExpense(user);
//    		model.addAttribute("businessType", BaseVO.EXPENSE);
    	}
    	Pagination pagination = PaginationThreadUtils.get();
		model.addAttribute("page", pagination.getPageStr());
    	model.addAttribute("baseList", baseVO);
    	return "apply/list_running";
    }
    /**
     * 管理运行中的流程
     * @param model
     * @return
     * @throws Exception
     */
//    @RequiresPermissions("admin:process:*")
    @RequestMapping(value="/process/running")
    public String running(Model model,Page page) throws Exception{
    	
		return "workflow/running-process";
    }
    @RequestMapping(value="/process/running/data")
    public void listRuningProcess(Page page,Model model,HttpServletResponse response) throws Exception{
    	 page = this.processService.listRuningProcess(page);
    	
    	model.addAttribute("taskType", BaseVO.RUNNING);
    	
    	 writeJson(response,page);
    }
    
   
    /**
     * 管理已结束的流程
     *
     * @return
     * @throws Exception 
     */
    @RequiresPermissions("admin:process:*")
    @RequestMapping(value = "/process/finishedProcess_page")
    public String findFinishedProcessInstances(Model model) throws Exception {
    	List<BaseVO> processList = this.processService.findFinishedProcessInstances(model);
    	model.addAttribute("processList", processList);
    	model.addAttribute("taskType", BaseVO.FINISHED);
    	return "workflow/finished_process";
    }
    
    /**
     * 激活、挂起流程实例-根据processInstanceId
     * @param status
     * @param processInstanceId
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequiresPermissions("admin:process:suspend,active")
    @RequestMapping(value = "/process/updateProcessStatusByProInstanceId/{status}/{processInstanceId}")
    public String updateProcessStatusByProInstanceId(
    		@PathVariable("status") String status, 
    		@PathVariable("processInstanceId") String processInstanceId,
            RedirectAttributes redirectAttributes) throws Exception{
    	
    	if (status.equals("active")) {
    		this.processService.activateProcessInstance(processInstanceId);
            redirectAttributes.addFlashAttribute("message", "已激活ID为[ " + processInstanceId + " ]的流程实例。");
        } else if (status.equals("suspend")) {
        	this.processService.suspendProcessInstance(processInstanceId);
            redirectAttributes.addFlashAttribute("message", "已挂起ID为[ " + processInstanceId + " ]的流程实例。");
        }
    	return "redirect:/processAction/process/runningProcess_page";
    }
    
    /**
     * 激活、挂起流程实例-根据processDefinitionId
     * @param status
     * @param processInstanceId
     * @param redirectAttributes
     * @return
     * @throws Exception
     */
    @RequiresPermissions("admin:process:suspend,active")
    @RequestMapping(value = "/process/updateProcessStatusByProDefinitionId")
    public String updateProcessStatusByProDefinitionId(
    		@RequestParam("status") String status,
    		@RequestParam("processDefinitionId") String processDefinitionId,
            RedirectAttributes redirectAttributes) throws Exception{
    	//如果用/{status}/{processDefinitionId} rest风格，@PathVariable获取的processDefinitionId 为com.zml.oa,实际是com.zml.oa.vacation:1:32529.难道是BUG?
    	if (status.equals("active")) {
            redirectAttributes.addFlashAttribute("message", "已激活ID为[" + processDefinitionId + "]的流程定义。");
            repositoryService.activateProcessDefinitionById(processDefinitionId, true, null);
        } else if (status.equals("suspend")) {
            repositoryService.suspendProcessDefinitionById(processDefinitionId, true, null);
            redirectAttributes.addFlashAttribute("message", "已挂起ID为[" + processDefinitionId + "]的流程定义。");
        }
    	return "redirect:/processAction/process/listProcess_page";
    }
    
    
    
    
    /**
     * 流程定义
     * @param request
     * @return
     * @throws Exception
     */
//    @RequiresPermissions("admin:process:*")
    @RequestMapping(value = "/process/list")
    public ModelAndView listProcess(HttpServletRequest request,HttpServletResponse response) throws Exception{
    	ModelAndView mav = new ModelAndView("workflow/process-list");
    	return mav;
    }
    /**
     * 流程定义
     * @param request
     * @return
     * @throws Exception
     */
//    @RequiresPermissions("admin:process:*")
    @RequestMapping(value = "/process/list/data")
    public  void listProcessData(Page page,HttpServletRequest request,HttpServletResponse response) throws Exception{
    	
    	//objects保存两个对象，Object[0]:是ProcessDefinition（流程定义），Object[1]:是Deployment（流程部署）
    	List<Object[]> objects = new ArrayList<Object[]>();
    	ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
//    	int[] pageParams = PaginationThreadUtils.setPage(processDefinitionQuery.list().size());
//    	List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(pageParams[0], pageParams[1]);
    	page.setTotal(processDefinitionQuery.list().size());
    	List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(page.getFirstResult(), page.getLimit());
    	/*
    	for (ProcessDefinition processDefinition : processDefinitionList) {
    		String deploymentId = processDefinition.getDeploymentId();
    		Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
    		objects.add(new Object[]{processDefinition, deployment});
    	}
    	*/
//    	Pagination pagination = PaginationThreadUtils.get();
//    	mav.addObject("obj", objects);
//    	mav.addObject("page", pagination.getPageStr());
//    	page.setDataList(objects);
//    	page.setDataList(processDefinitionList);
    	

     	page.setDataList(ActivitiUtils.mapSetter(processDefinitionList));
    	writeJson(response, page);
//    	writeJsonInclude(response, page,new String[]{"id"});
//    	writeJackson(response, page,null,new String[]{"id"});
    	
//    	return page;
    }
    
    /**
     * 删除部署的流程，级联删除流程实例 true。
     * 不管是否指定级联删除，部署的相关数据均会被删除，这些数据包括流程定义的身份数据（IdentityLink）、流程定义数据（ProcessDefinition）、流程资源（Resource）
     * 部署数据（Deployment）。
     * 如果设置级联(true)，则会删除流程实例数据（ProcessInstance）,其中流程实例也包括流程任务（Task）与流程实例的历史数据；如果设置flase 将不会级联删除。
     * 如果数据库中已经存在流程实例数据，那么将会删除失败，因为在删除流程定义时，流程定义数据的ID已经被流程实例的相关数据所引用。
     *
     * @param deploymentId 流程部署ID
     */
    @RequestMapping(value = "/process/delete")
    public String delete(@RequestParam("deploymentId") String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
        return "redirect:/processAction/process/listProcess_page";
    }
    
    /**
     * 导入部署
     * 
     * --@Value用于将一个SpEL表达式结果映射到到功能处理方法的参数上。
     * @RequestParam(value = "file", required = false) required = false时可以不用传递这个参数，默认为true
     * @param exportDir
     * @param file
     * @return
     */
//    @RequiresPermissions("admin:process:*")
    @RequestMapping(value = "/process/deploy")
    public void deploy(@Value("#{APP_PROPERTIES['activiti.export.diagram.path']}") String exportDir, 
    					  @RequestParam(value = "file", required = false) MultipartFile file,
    					  RedirectAttributes redirectAttributes, HttpServletResponse response) {
    	//@Value("${export.diagram.path}")
    	//@Value("#{APP_PROPERTIES['export.diagram.path']}") String exportDir,
        String fileName = file.getOriginalFilename();
        
        JSONObject jsonObject= new JSONObject();
        try {
            InputStream fileInputStream = file.getInputStream();
            Deployment deployment = null;

            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
            } else {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            }

            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();

            for (ProcessDefinition processDefinition : list) {
                WorkflowUtils.exportDiagramToFile(repositoryService, processDefinition, exportDir);
            }
//            redirectAttributes.addFlashAttribute("message", "流程部署成功！");
            jsonObject = JsonResultBuilder.success(true).msg("流程部署成功！").json();
            
            
        } catch (Exception e) {
//        	redirectAttributes.addFlashAttribute("message", "流程部署失败！");
        	jsonObject = JsonResultBuilder.success(false).msg("流程部署失败！").json();
            logger.error("error on deploy process, because of file input stream", e);
        }

//        return "redirect:/processAction/process/listProcess_page";
        
        writeJson(response,jsonObject); 
    }
    
    /**
     * 部署全部流程
     *
     * @return
     * @throws Exception
     */
    @RequiresPermissions("admin:process:*")
    @RequestMapping(value = "/process/redeploy/all")
    public void redeployAll(@Value("#{APP_PROPERTIES['export.diagram.path']}") String exportDir, 
				    		HttpServletResponse response,
				    		RedirectAttributes redirectAttributes) throws Exception {
    	PrintWriter out = response.getWriter();
    	try {
    		List<Deployment> deploymentList = this.repositoryService.createDeploymentQuery().list();
    		//删除现有所有流程实例
    		for(Deployment deployment : deploymentList){
    			String deploymentId = deployment.getId();
    			this.repositoryService.deleteDeployment(deploymentId, true);
    		}
    		//重新部署全部流程实例
    		//方法一：通过classpath/deploy目录下的.zip或.bar文件部署
    		workflowProcessDefinitionService.deployAllFromClasspath(exportDir);
    		
    		//方法二：通过classpath/bpmn下的流程描述文件部署-流程图错乱，一直提倡用打包部署没有任何问题。
//        	workflowProcessDefinitionService.redeployBpmn(exportDir);

//        	redirectAttributes.addFlashAttribute("message", "已重新部署全部流程！");
        	out.print("success");
		} catch (Exception e) {
//			redirectAttributes.addFlashAttribute("message", "重新部署流程失败！");
			out.print("fail");
			throw e;
		}
//        return "redirect:/processAction/process/listProcess_page";
    }
    
    /**
     * 部署单个流程
     *
     * @return
     * @throws Exception
     */
    @RequiresPermissions("admin:process:*")
    @RequestMapping(value = "/process/redeploy/single")
    public String redeploySingle(@Value("#{APP_PROPERTIES['export.diagram.path']}") String exportDir,
    							@RequestParam("resourceName") String resourceName,
    							@RequestParam(value = "diagramResourceName", required = false) String diagramResourceName,
    							@RequestParam("deploymentId") String deploymentId,
    							RedirectAttributes redirectAttributes) throws Exception {
        try {
        	this.repositoryService.deleteDeployment(deploymentId, true);
        	//方法一：通过classpath/deploy目录下的.zip或.bar文件部署
        	String processKey = resourceName.substring(0, resourceName.indexOf('.'))+".zip";
        	workflowProcessDefinitionService.redeploySingleFrom(exportDir, processKey);
        	//方法二：通过classpath/bpmn下的流程描述文件部署--流程图错乱，一直提倡用打包部署没有任何问题。
//        	workflowProcessDefinitionService.redeployBpmn(exportDir, resourceName,diagramResourceName);
        	redirectAttributes.addFlashAttribute("message", "已重新部署流程！");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "重新部署流程失败！");
			throw e;
		}
        return "redirect:/processAction/process/listProcess_page";
    }
    
    /**
     * 转换为model
     * @param processDefinitionId
     * @return
     * @throws UnsupportedEncodingException
     * @throws XMLStreamException
     */
    @RequiresPermissions("admin:process:*")
    @RequestMapping(value = "/process/convert_to_model")
    public String convertToModel(@RequestParam("processDefinitionId") String processDefinitionId)
            throws UnsupportedEncodingException, XMLStreamException {
    	ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId).singleResult();
        InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
        XMLStreamReader xtr = xif.createXMLStreamReader(in);
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

        BpmnJsonConverter converter = new BpmnJsonConverter();
        com.fasterxml.jackson.databind.node.ObjectNode modelNode = converter.convertToJson(bpmnModel);
        org.activiti.engine.repository.Model modelData = repositoryService.newModel();
        modelData.setKey(processDefinition.getKey());
        modelData.setName(processDefinition.getResourceName());
        modelData.setCategory(processDefinition.getDeploymentId());

        ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
        modelData.setMetaInfo(modelObjectNode.toString());

        repositoryService.saveModel(modelData);

        repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));

        return "redirect:/processAction/process/listProcess_page";
    }
    
}
