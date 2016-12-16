package com.kingen.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TFwx entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_fwx")
public class Fwx implements java.io.Serializable {

	// Fields

	private String id;
	private String serviceCatId;
	private String procDefName;
	private String procDefCode;
	private String procDefType;
	private String procDefTemplate;
	private String desc;
	private byte isClientInvestigation;

	// Constructors

	/** default constructor */
	public Fwx() {
	}

	/** full constructor */
	public Fwx(String serviceCatId, String procDefName, String procDefCode,
			String procDefType, String procDefTemplate, String desc,
			byte isClientInvestigation) {
		this.serviceCatId = serviceCatId;
		this.procDefName = procDefName;
		this.procDefCode = procDefCode;
		this.procDefType = procDefType;
		this.procDefTemplate = procDefTemplate;
		this.desc = desc;
		this.isClientInvestigation = isClientInvestigation;
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

	@Column(name = "service_cat_id", length = 32)
	public String getServiceCatId() {
		return this.serviceCatId;
	}

	public void setServiceCatId(String serviceCatId) {
		this.serviceCatId = serviceCatId;
	}

	@Column(name = "proc_def_name", length = 50)
	public String getProcDefName() {
		return this.procDefName;
	}

	public void setProcDefName(String procDefName) {
		this.procDefName = procDefName;
	}

	@Column(name = "proc_def_code", length = 20)
	public String getProcDefCode() {
		return this.procDefCode;
	}

	public void setProcDefCode(String procDefCode) {
		this.procDefCode = procDefCode;
	}

	@Column(name = "proc_def_type", length = 15)
	public String getProcDefType() {
		return this.procDefType;
	}

	public void setProcDefType(String procDefType) {
		this.procDefType = procDefType;
	}

	@Column(name = "proc_def_template", length = 15)
	public String getProcDefTemplate() {
		return this.procDefTemplate;
	}

	public void setProcDefTemplate(String procDefTemplate) {
		this.procDefTemplate = procDefTemplate;
	}

	@Column(name = "desc", length = 100)
	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "isClientInvestigation")
	public byte getIsClientInvestigation() {
		return this.isClientInvestigation;
	}

	public void setIsClientInvestigation(byte isClientInvestigation) {
		this.isClientInvestigation = isClientInvestigation;
	}

}