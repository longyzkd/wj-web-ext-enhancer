package com.kingen.bean;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * SysUserOrg entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_user_org")
public class SysUserOrg implements java.io.Serializable {

	// Fields

	private SysUserOrgId id;

	// Constructors

	/** default constructor */
	public SysUserOrg() {
	}

	/** full constructor */
	public SysUserOrg(SysUserOrgId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "userId", column = @Column(name = "user_id", nullable = false, length = 32)),
			@AttributeOverride(name = "orgId", column = @Column(name = "org_id", nullable = false, length = 32)) })
	public SysUserOrgId getId() {
		return this.id;
	}

	public void setId(SysUserOrgId id) {
		this.id = id;
	}

}