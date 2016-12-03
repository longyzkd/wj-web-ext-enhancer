package com.kingen.bean.workflow;

/**
 * 流程发起的业务数据
 * 
 * 流程业务数据的共性,供以后扩展用
 * @author wj
 *
 */
public interface ActivitiAware {
	/**
	 * 标题
	 * @return
	 */
	String getTitle();
	/**
	 * 服务项code
	 * @return
	 */
	String getBusinessType();
	/**
	 * 服务项名称
	 * @return
	 */
	String getBusinessName();
	
	
	
	
	/**
	 * 启动日期，为方便用字符串
	 */
	String getApplyDateStr();
	/**
	 * 优先级
	 * @return
	 */
	String getPriority();
	/**
	 * 客户单位
	 * @return
	 */
	String getClientUint();
	/**
	 * 合同
	 * @return
	 */
	String getContract();

}
