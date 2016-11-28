/**
 * Copyright &copy; 2012-2016 <a href="https://github.com//jeesite">JeeSite</a> All rights reserved.
 */
package com.kingen.bean;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import com.kingen.util.StringUtils;
import com.kingen.util.excel.annotation.ExcelField;


/**
 * 日志Entity
 * @author wj
 * @version 2016-11-17
 */

@Entity
@Table(name = "sys_log")
public class Log  {

	
	private static final long serialVersionUID = 1L;
	private String id;
	private String type; 		// 日志类型（1：接入日志；2：错误日志）
	private String title;		// 操作
	private String status;      // 操作状态
	private String remoteAddr; 	// 操作用户的IP地址
	private String requestUri; 	// 操作的URI
	private String params; 		// 操作提交的数据
	private String userAgent;	// 操作用户代理信息
	private String dwmc;		// 操作用户单位名称
	private String exception; 	// 异常信息
	
	private String createDate;		// 创建日期
	
	// 日志类型（1：接入日志；2：错误日志）
	public static final String TYPE_ACCESS = "1";
	public static final String TYPE_EXCEPTION = "2";
	
	
	
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "remoteAddr")
	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	@Column(name = "requestUri")
	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	@Column(name = "params")
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	
	@ExcelField(title="用户名", align=2, sort=2)
	@Column(name = "userAgent")
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	@Column(name = "exception")
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	
	
	@ExcelField(title="时间", align=3, sort=4)
	@Column(name = "createDate")
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	@ExcelField(title="内容", align=2, sort=3)
	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="单位名称", align=2, sort=1)
	@Column(name = "dwmc")
	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}

	/**
	 * 设置请求参数
	 * @param paramMap
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setParams(Map paramMap){
		if (paramMap == null){
			return;
		}
		StringBuilder params = new StringBuilder();
		for (Map.Entry<String, String[]> param : ((Map<String, String[]>)paramMap).entrySet()){
			params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
			String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
			params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
		}
		this.params = params.toString();
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
	private String fromDate;
	private String toDate;


	@Transient
	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	@Transient
	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
}