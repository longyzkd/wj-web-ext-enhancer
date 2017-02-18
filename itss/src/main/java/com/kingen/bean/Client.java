package com.kingen.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.kingen.vo.Comboable;

/**
 * TClient entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_client")
public class Client implements java.io.Serializable,Comboable {

	// Fields

	private String id;
	private String unitName;
	private String addr;
	private String postCode;

	// Constructors

	/** default constructor */
	public Client() {
	}

	/** full constructor */
	public Client(String unitName, String addr, String postCode) {
		this.unitName = unitName;
		this.addr = addr;
		this.postCode = postCode;
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

	@Column(name = "unit_name", length = 50)
	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Column(name = "addr", length = 100)
	public String getAddr() {
		return this.addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	@Column(name = "post_code", length = 15)
	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Override
	@Transient
	public String getCode() {
	return id;
	}

	@Override
	@Transient
	public String getName() {
		return unitName;
	}

}