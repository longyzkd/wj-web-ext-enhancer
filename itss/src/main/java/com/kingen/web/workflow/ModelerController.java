package com.kingen.web.workflow;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kingen.bean.Log;
import com.kingen.util.Constants;
import com.kingen.util.JsonResultBuilder;
import com.kingen.util.Page;
import com.kingen.web.CommonController;

@Controller
//@RequiresPermissions("admin:*")
@RequestMapping("/workflow/model")
public class ModelerController extends CommonController{
	private static final Logger logger = LoggerFactory.getLogger(ModelerController.class);
	
	@Autowired
	private RepositoryService repositoryService;
	
	/**
	 * 模型列表
	 * @return
	 */
	@RequestMapping(value = "/")
    public ModelAndView modelList() {
        ModelAndView mav = new ModelAndView("workflow/model-list");
      
        return mav;
    }
	/**
	 * 模型FORM编辑
	 * @return
	 */
	@RequestMapping(value = "/toEdit")
	public ModelAndView modelEdit(String id,String action) {
		ModelAndView mav = new ModelAndView("workflow/model-edit");
		mav.addObject("id", id);
		mav.addObject("action", action);
		return mav;
	}
	/**
	 * 模型FORM编辑
	 * @return
	 */
	@RequestMapping(value = "/toImport")
	public ModelAndView toImport() {
		ModelAndView mav = new ModelAndView("workflow/model-import");
		return mav;
	}
	
	/**
	 * 查找分页后的grid
	 */
	//Model.name 是 xml的文件名
	@RequestMapping(value="listData")
	public void listData(Page page,Log vo,HttpServletResponse response) {
		
		  ModelQuery modelQuery = repositoryService.createModelQuery();
	       // int[] pageParams = PaginationThreadUtils.setPage(modelQuery.list().size());
		  
	        List<Model> list = modelQuery.listPage(page.getFirstResult(), page.getLimit());
	        writeJson(response,list);
	}
	
	
	/**
	 * 创建模型
	 * @param name
	 * @param key
	 * @param description
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
    public void create( String name, String key, String description,String serviceType,String flowTemplate,
                       HttpServletRequest request, HttpServletResponse response) {
		logger.info("name: "+name+" key: "+key+" des: "+description);
		JSONObject jsonObject= new JSONObject();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilSetNode);
            
            Model modelData = repositoryService.newModel();

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            description = StringUtils.defaultString(description);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            
            modelObjectNode.put(Constants.STATUS, Constants.ActivitiStatusEnum.notWork.getIndex());//未启用
            
            
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(name);
            modelData.setKey(StringUtils.defaultString(key));

            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

//            response.sendRedirect(request.getContextPath() + "/modeler/service/editor?id=" + modelData.getId());
//            response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
            
         

            jsonObject = JsonResultBuilder.success(true).msg("成功").data(modelData.getId()).json();
            
        } catch (Exception e) {
            logger.error("创建模型失败：", e);
            jsonObject = JsonResultBuilder.success(false).msg("失败").json();
        }
        writeJson(response,jsonObject); 
    }
	
	/**
	 * 根据Model部署流程
	 * @param modelId
	 * @param redirectAttributes
	 * @return
	 */
    @RequestMapping(value = "deploy/{modelId}")
    public void  deploy(@PathVariable("modelId") String modelId, RedirectAttributes redirectAttributes,HttpServletResponse response) {
    	JSONObject jsonObject= new JSONObject();
        try {
            Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            byte[] bpmnBytes = null;
            
            //修改model的状态为 启用
            ObjectMapper mapper = new ObjectMapper();  
            String metaInfo = modelData.getMetaInfo();
            ObjectNode node = mapper.readValue(metaInfo, ObjectNode.class);
//            String status = node.get(STATUS).asText();
            node.put(Constants.STATUS, Constants.ActivitiStatusEnum.work.getIndex());
            modelData.setMetaInfo(node.toString());
            repositoryService.saveModel(modelData); 
            
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            bpmnBytes = new BpmnXMLConverter().convertToXML(model);

            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes)).deploy();
            
//            redirectAttributes.addFlashAttribute("message", "部署成功，部署ID=" + deployment.getId());
            jsonObject = JsonResultBuilder.success(true).msg("部署成功，部署ID=" + deployment.getId()).data(deployment.getId()).json();
        } catch (Exception e) {
//        	redirectAttributes.addFlashAttribute("message", "根据模型部署流程失败:modelId="+modelId);
        	 jsonObject = JsonResultBuilder.success(false).msg( "根据模型部署流程失败:modelId="+modelId).json();
            logger.error("根据模型部署流程失败：modelId={}" + modelId, e);
        }
//        return "redirect:/modelAction/listModel_page";
        writeJson(response,jsonObject); 
    }

    /**
     * 导出model的xml文件
     * @param modelId
     * @param response
     */
    @RequestMapping(value = "export/{modelId}")
    public void export(@PathVariable("modelId") String modelId, HttpServletResponse response) {
        try {
            Model modelData = repositoryService.getModel(modelId);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            IOUtils.copy(in, response.getOutputStream());
            String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.flushBuffer();
        } catch (Exception e) {
            logger.error("导出model的xml文件失败：modelId={}" + modelId, e);
        }
    }

    /**
     * 删除模型
     * @param modelId
     * @return
     */
    @RequestMapping(value = "delete/{modelId}")
    public void delete(@PathVariable("modelId") String modelId,HttpServletResponse response) {
        repositoryService.deleteModel(modelId);
        JSONObject  jsonObject = JsonResultBuilder.success(true).msg("删除成功").json();
//        return "redirect:/modelAction/listModel_page";
        writeJson(response,jsonObject); 
    }
    /**
     * 删除模型
     * @param modelId
     * @return
     */
    @RequestMapping(value = "deleteThem")
    public void deleteThem( String[] modelIds,HttpServletResponse response) {
    	Assert.notEmpty(modelIds);
    	
    	for(String modelId : modelIds){
    		
    		repositoryService.deleteModel(modelId);
    	}
    	
    	 JSONObject  jsonObject = JsonResultBuilder.success(true).msg("删除成功").json();
    	 writeJson(response,jsonObject); 
//    	return "redirect:/modelAction/listModel_page";
    }
}
