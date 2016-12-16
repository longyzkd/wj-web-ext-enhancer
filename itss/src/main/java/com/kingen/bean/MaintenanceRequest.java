package com.kingen.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TMaintenanceRequest entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_maintenance_request")
public class MaintenanceRequest implements java.io.Serializable {

	// Fields

	private String id;
	private String clientId;
	private String clientContactId;
	private String assetsId;
	private String serviceInfoId;
	private String procDefName;
	private String procInstId;
	private String status;
	private String applyDate;
	private String userId;

	// Constructors

	/** default constructor */
	public MaintenanceRequest() {
	}

	/** full constructor */
	public MaintenanceRequest(String clientId, String clientContactId,
			String assetsId, String serviceInfoId, String procDefName,
			String procInstId, String status, String applyDate, String userId) {
		this.clientId = clientId;
		this.clientContactId = clientContactId;
		this.assetsId = assetsId;
		this.serviceInfoId = serviceInfoId;
		this.procDefName = procDefName;
		this.procInstId = procInstId;
		this.status = status;
		this.applyDate = applyDate;
		this.userId = userId;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "client_id", length = 32)
	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Column(name = "client_contact_id", length = 32)
	public String getClientContactId() {
		return this.clientContactId;
	}

	public void setClientContactId(String clientContactId) {
		this.clientContactId = clientContactId;
	}

	@Column(name = "assets_id", length = 32)
	public String getAssetsId() {
		return this.assetsId;
	}

	public void setAssetsId(String assetsId) {
		this.assetsId = assetsId;
	}

	@Column(name = "service_info_id", length = 32)
	public String getServiceInfoId() {
		return this.serviceInfoId;
	}

	public void setServiceInfoId(String serviceInfoId) {
		this.serviceInfoId = serviceInfoId;
	}

	@Column(name = "proc_def_name", length = 32)
	public String getProcDefName() {
		return this.procDefName;
	}

	public void setProcDefName(String procDefName) {
		this.procDefName = procDefName;
	}

	@Column(name = "PROC_INST_ID")
	public String getProcInstId() {
		return this.procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	@Column(name = "status", length = 15)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "apply_date", length = 32)
	public String getApplyDate() {
		return this.applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	@Column(name = "user_id", length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}