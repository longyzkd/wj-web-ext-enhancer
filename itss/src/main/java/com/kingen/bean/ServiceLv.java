package com.kingen.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TServiceLv entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_service_lv")
public class ServiceLv implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String type;
	private Integer servBeiginHour;
	private Integer servEndHour;
	private Integer highPriRespHour;
	private Integer highPriSolveHour;
	private Integer midPriRespHour;
	private Integer midPriSolveHour;
	private Integer lowPriRespHour;
	private Integer lowPriSolveHour;

	// Constructors

	/** default constructor */
	public ServiceLv() {
	}

	/** full constructor */
	public ServiceLv(String name, String type, Integer servBeiginHour,
			Integer servEndHour, Integer highPriRespHour,
			Integer highPriSolveHour, Integer midPriRespHour,
			Integer midPriSolveHour, Integer lowPriRespHour,
			Integer lowPriSolveHour) {
		this.name = name;
		this.type = type;
		this.servBeiginHour = servBeiginHour;
		this.servEndHour = servEndHour;
		this.highPriRespHour = highPriRespHour;
		this.highPriSolveHour = highPriSolveHour;
		this.midPriRespHour = midPriRespHour;
		this.midPriSolveHour = midPriSolveHour;
		this.lowPriRespHour = lowPriRespHour;
		this.lowPriSolveHour = lowPriSolveHour;
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

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "type", length = 10)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "serv_beigin_hour")
	public Integer getServBeiginHour() {
		return this.servBeiginHour;
	}

	public void setServBeiginHour(Integer servBeiginHour) {
		this.servBeiginHour = servBeiginHour;
	}

	@Column(name = "serv_end_hour")
	public Integer getServEndHour() {
		return this.servEndHour;
	}

	public void setServEndHour(Integer servEndHour) {
		this.servEndHour = servEndHour;
	}

	@Column(name = "high_pri_resp_hour")
	public Integer getHighPriRespHour() {
		return this.highPriRespHour;
	}

	public void setHighPriRespHour(Integer highPriRespHour) {
		this.highPriRespHour = highPriRespHour;
	}

	@Column(name = "high_pri_solve_hour")
	public Integer getHighPriSolveHour() {
		return this.highPriSolveHour;
	}

	public void setHighPriSolveHour(Integer highPriSolveHour) {
		this.highPriSolveHour = highPriSolveHour;
	}

	@Column(name = "mid_pri_resp_hour")
	public Integer getMidPriRespHour() {
		return this.midPriRespHour;
	}

	public void setMidPriRespHour(Integer midPriRespHour) {
		this.midPriRespHour = midPriRespHour;
	}

	@Column(name = "mid_pri_solve_hour")
	public Integer getMidPriSolveHour() {
		return this.midPriSolveHour;
	}

	public void setMidPriSolveHour(Integer midPriSolveHour) {
		this.midPriSolveHour = midPriSolveHour;
	}

	@Column(name = "low_pri_resp_hour")
	public Integer getLowPriRespHour() {
		return this.lowPriRespHour;
	}

	public void setLowPriRespHour(Integer lowPriRespHour) {
		this.lowPriRespHour = lowPriRespHour;
	}

	@Column(name = "low_pri_solve_hour")
	public Integer getLowPriSolveHour() {
		return this.lowPriSolveHour;
	}

	public void setLowPriSolveHour(Integer lowPriSolveHour) {
		this.lowPriSolveHour = lowPriSolveHour;
	}

}