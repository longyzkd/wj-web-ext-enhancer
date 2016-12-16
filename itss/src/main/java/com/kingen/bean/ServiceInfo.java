package com.kingen.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TServiceInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_service_info")
public class ServiceInfo implements java.io.Serializable {

	// Fields

	private String id;
	private String jnflCode;
	private String priorityCode;
	private String faultOccurrenceTime;
	private String faultLvCode;
	private String eventClassCode;
	private String title;
	private String desc;
	private String attach;

	// Constructors

	/** default constructor */
	public ServiceInfo() {
	}

	/** full constructor */
	public ServiceInfo(String jnflCode, String priorityCode,
			String faultOccurrenceTime, String faultLvCode,
			String eventClassCode, String title, String desc, String attach) {
		this.jnflCode = jnflCode;
		this.priorityCode = priorityCode;
		this.faultOccurrenceTime = faultOccurrenceTime;
		this.faultLvCode = faultLvCode;
		this.eventClassCode = eventClassCode;
		this.title = title;
		this.desc = desc;
		this.attach = attach;
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

	@Column(name = "jnfl_code", length = 10)
	public String getJnflCode() {
		return this.jnflCode;
	}

	public void setJnflCode(String jnflCode) {
		this.jnflCode = jnflCode;
	}

	@Column(name = "priority_code", length = 10)
	public String getPriorityCode() {
		return this.priorityCode;
	}

	public void setPriorityCode(String priorityCode) {
		this.priorityCode = priorityCode;
	}

	@Column(name = "fault_occurrence_time", length = 32)
	public String getFaultOccurrenceTime() {
		return this.faultOccurrenceTime;
	}

	public void setFaultOccurrenceTime(String faultOccurrenceTime) {
		this.faultOccurrenceTime = faultOccurrenceTime;
	}

	@Column(name = "fault_lv_code", length = 10)
	public String getFaultLvCode() {
		return this.faultLvCode;
	}

	public void setFaultLvCode(String faultLvCode) {
		this.faultLvCode = faultLvCode;
	}

	@Column(name = "event_class_code", length = 10)
	public String getEventClassCode() {
		return this.eventClassCode;
	}

	public void setEventClassCode(String eventClassCode) {
		this.eventClassCode = eventClassCode;
	}

	@Column(name = "title", length = 50)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "desc", length = 500)
	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "attach")
	public String getAttach() {
		return this.attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

}