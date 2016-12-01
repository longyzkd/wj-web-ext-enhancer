package com.kingen.util;

import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 将activiti用到的一些bean转为 MAP，防止 序列化activiti bean时候
 * ，报错"Context.getCommandContext() returning null "
 * 
 * @author wj
 *
 */
public class ActivitiUtils {

	/**
	 * 流程定义列表组装
	 * 
	 * @param ProcessDefinition
	 * @return
	 */
	public static List<Map> mapSetter(List<ProcessDefinition> processDefinitionList) {

		List<Map> result = Lists.newArrayList();
		for (ProcessDefinition p : processDefinitionList) {

			Map<String, Object> m = Maps.newHashMap();
			m.put("id", p.getId());
			m.put("description", p.getDescription());
			m.put("deploymentId", p.getDeploymentId());
			m.put("name", p.getName());
			m.put("version", p.getVersion());
			m.put("resourceName", p.getResourceName());
			m.put("diagramResourceName", p.getDiagramResourceName());
			result.add(m);
		}
		return result;
	}

	/**
	 * 运行中流程 列表组装
	 * 
	 * @param ProcessInstance
	 * @return
	 */
	public static List<Map> mapSetterProcessInstance(List<ProcessInstance> list) {

		List<Map> result = Lists.newArrayList();
		for (ProcessInstance p : list) {

			Map<String, Object> m = Maps.newHashMap();
			m.put("id", p.getId());
			m.put("processInstanceId", p.getProcessInstanceId());
			m.put("processDefinitionId", p.getProcessDefinitionId());
			m.put("activityId", p.getActivityId());
			m.put("name", p.getName());
			m.put("description", p.getDescription());
			result.add(m);
		}
		return result;
	}

	/**
	 * 任务 列表组装
	 * 
	 * @param Task
	 * @return
	 */
	public static List<Map> mapSetterTask(List<Task> list) {

		List<Map> result = Lists.newArrayList();
		for (Task p : list) {

			Map<String, Object> m = Maps.newHashMap();
			m.put("id", p.getId());
			m.put("processInstanceId", p.getProcessInstanceId());
			m.put("processDefinitionId", p.getProcessDefinitionId());
			m.put("name", p.getName());
			m.put("description", p.getDescription());

			m.put("assignee", p.getAssignee());
			m.put("createTime", p.getCreateTime());
			m.put("formKey", p.getFormKey());

			result.add(m);
		}
		return result;
	}

	/**
	 * 历史 列表组装
	 * 
	 * @param Task
	 * @return
	 */
	public static List<Map> mapSetterHistoricProcessInstance(List<HistoricProcessInstance> list) {

		List<Map> result = Lists.newArrayList();
		for (HistoricProcessInstance p : list) {

			Map<String, Object> m = Maps.newHashMap();
			m.put("id", p.getId());
			m.put("processDefinitionId", p.getProcessDefinitionId());
			m.put("name", p.getName());
			m.put("description", p.getDescription());

			m.put("startTime", p.getStartTime());
			m.put("sndTime", p.getEndTime());
			m.put("businessKey", p.getBusinessKey());
			m.put("deploymentId", p.getDeploymentId());
			m.put("startActivityId", p.getStartActivityId());
			m.put("endActivityId", p.getEndActivityId());

			result.add(m);
		}
		return result;
	}

	
	
	
	/**
	 * 流程定义列表组装
	 * 
	 * @param ProcessDefinition
	 * @return
	 */
	public static Map mapSetter(ProcessDefinition p) {
		if (p == null)
			return null;

		Map<String, Object> m = Maps.newHashMap();
		m.put("id", p.getId());
		m.put("description", p.getDescription());
		m.put("deploymentId", p.getDeploymentId());
		m.put("name", p.getName());
		m.put("version", p.getVersion());
		m.put("resourceName", p.getResourceName());
		m.put("diagramResourceName", p.getDiagramResourceName());
		return m;
	}

	/**
	 * 运行中流程 列表组装
	 * 
	 * @param ProcessInstance
	 * @return
	 */
	public static Map mapSetterProcessInstance(ProcessInstance p) {
		if (p == null)
			return null;

		Map<String, Object> m = Maps.newHashMap();
		m.put("id", p.getId());
		m.put("processInstanceId", p.getProcessInstanceId());
		m.put("processDefinitionId", p.getProcessDefinitionId());
		m.put("activityId", p.getActivityId());
		m.put("name", p.getName());
		m.put("description", p.getDescription());
		m.put("suspended", p.isSuspended());
		return m;
	}

	/**
	 * 任务 列表组装
	 * 
	 * @param Task
	 * @return
	 */
	public static Map mapSetterTask(Task p) {

		if (p == null)
			return null;

		Map<String, Object> m = Maps.newHashMap();
		m.put("id", p.getId());
		m.put("processInstanceId", p.getProcessInstanceId());
		m.put("processDefinitionId", p.getProcessDefinitionId());
		m.put("name", p.getName());
		m.put("description", p.getDescription());

		m.put("assignee", p.getAssignee());
		m.put("createTime", p.getCreateTime());
		m.put("formKey", p.getFormKey());

		return m;
	}

	/**
	 * 历史 列表组装
	 * 
	 * @param Task
	 * @return
	 */
	public static Map mapSetterHistoricProcessInstance(HistoricProcessInstance p) {

		if (p == null)
			return null;
		Map<String, Object> m = Maps.newHashMap();
		m.put("id", p.getId());
		m.put("processDefinitionId", p.getProcessDefinitionId());
		m.put("name", p.getName());
		m.put("description", p.getDescription());

		m.put("startTime", p.getStartTime());
		m.put("sndTime", p.getEndTime());
		m.put("businessKey", p.getBusinessKey());
		m.put("deploymentId", p.getDeploymentId());
		m.put("startActivityId", p.getStartActivityId());
		m.put("endActivityId", p.getEndActivityId());

		return m;
	}
	/**
	 * 历史 列表组装
	 * 
	 * @param Task
	 * @return
	 */
	public static Map mapSetterHistoricTaskInstance(HistoricTaskInstance p) {
		
		if (p == null)
			return null;
		Map<String, Object> m = Maps.newHashMap();
		m.put("id", p.getId());
		m.put("processInstanceId", p.getProcessInstanceId());
		m.put("processDefinitionId", p.getProcessDefinitionId());
		m.put("name", p.getName());
		m.put("description", p.getDescription());

		m.put("assignee", p.getAssignee());
		m.put("createTime", p.getCreateTime());
		m.put("formKey", p.getFormKey());
		
		return m;
	}

}
