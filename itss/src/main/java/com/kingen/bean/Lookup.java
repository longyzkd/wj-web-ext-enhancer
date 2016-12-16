package com.kingen.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TLookup entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_lookup")
public class Lookup implements java.io.Serializable {

	// Fields

	private String id;
	private String type;
	private String name;
	private String desc;

	// Constructors

	/** default constructor */
	public Lookup() {
	}

	/** full constructor */
	public Lookup(String type, String name, String desc) {
		this.type = type;
		this.name = name;
		this.desc = desc;
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

	@Column(name = "type", length = 5)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "desc", length = 100)
	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}