package com.kingen.util;

import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kingen.bean.User;
import com.kingen.bean.workflow.ActivitiAware;
import com.kingen.util.workflow.ProcessDefinitionCache;

/**
 * 将activiti用到的一些bean转为 MAP，防止 序列化activiti bean时候
 * ，报错"Context.getCommandContext() returning null "
 * (这种情况多出现于EXT的STORE JSON环境下)
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
		m.put("version", p.getProcessDefinitionVersion());
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
		m.put("owner", p.getOwner());
		m.put("createTime", p.getCreateTime());
		m.put("formKey", p.getFormKey());
		m.put("taskDefinitionKey", p.getTaskDefinitionKey());

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
		m.put("endTime", p.getEndTime());
		m.put("businessKey", p.getBusinessKey());
		m.put("deploymentId", p.getDeploymentId());
		m.put("startActivityId", p.getStartActivityId());
		m.put("endActivityId", p.getEndActivityId());
		m.put("deleteReason", p.getDeleteReason());
		
		

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
		m.put("formKey", p.getFormKey());
		m.put("createTime", p.getCreateTime());
		m.put("startTime", p.getStartTime());
		m.put("endTime", p.getEndTime());
		m.put("claimTime", p.getClaimTime());
		m.put("deleteReason", p.getDeleteReason());
		
		return m;
	}

	public static Map mapSetterProcessInstanceWithEntity(ProcessInstance pi, ActivitiAware a) {

		if (pi == null)
			return null;

		Map<String, Object> m = Maps.newHashMap();
		m.put("id", pi.getId()); //如果当前是流程实例，那么ID跟processInstanceId 是一样的
		m.put("processInstanceId", pi.getProcessInstanceId());
		m.put("processDefinitionId", pi.getProcessDefinitionId());
		m.put("activityId", pi.getActivityId());
		m.put("name", pi.getName());
		m.put("description", pi.getDescription());
		m.put("suspended", pi.isSuspended());
		m.put("version", pi.getProcessDefinitionVersion());
		
		
		m.put("clientUint", a.getClientUint());
		m.put("contract", a.getContract());
		m.put("priority", a.getPriority());
		m.put("title", a.getTitle());
		m.put("businessType", a.getBusinessType());
		m.put("businessName", a.getBusinessType());
		m.put("applyDateStr", a.getApplyDateStr());
		
		//当前节点
		ProcessDefinitionCache.setRepositoryService(SpringContextHolder.getBean(org.activiti.engine.RepositoryService.class));
		String activityName = ProcessDefinitionCache.getActivityName(pi.getProcessDefinitionId(), ObjectUtils.toString(pi.getActivityId()));
		m.put("activityName", activityName);
		
		return m;
	
		
	}
	
	/**
	 * 区分于Activiti
	 * @author f
	 *
	 */
	public static class MapSetter{
		
		
		public static Map<String,Object> mapSetterSession(Session s) {
			
			if (s == null)
				return null;
			
			Map<String,Object> m = Maps.newHashMap();
			m.put("id", s.getId());
			m.put("host", s.getHost());
			m.put("lastAccessTime", s.getLastAccessTime());
			m.put("userName", principal(s).getUsername());
			m.put("forced", isForceLogout(s));
			
			return m;
		}
		
		  public static User principal(Session session) {
		        PrincipalCollection principalCollection =
		                (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);

		        return (User)principalCollection.getPrimaryPrincipal();
//		        return (String)principalCollection.getPrimaryPrincipal();
		    }
		  public static boolean isForceLogout(Session session) {
		        return session.getAttribute(Constants.SESSION_FORCE_LOGOUT_KEY) != null;
		    }
		
	}

}
