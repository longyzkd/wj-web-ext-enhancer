package com.kingen.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TSkillCat entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_skill_cat")
public class SkillCat implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String pid;

	// Constructors

	/** default constructor */
	public SkillCat() {
	}

	/** full constructor */
	public SkillCat(String name, String pid) {
		this.name = name;
		this.pid = pid;
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

	@Column(name = "pid", length = 32)
	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}