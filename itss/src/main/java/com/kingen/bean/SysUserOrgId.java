package com.kingen.bean;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SysUserOrgId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class SysUserOrgId implements java.io.Serializable {

	// Fields

	private String userId;
	private String orgId;

	// Constructors

	/** default constructor */
	public SysUserOrgId() {
	}

	/** full constructor */
	public SysUserOrgId(String userId, String orgId) {
		this.userId = userId;
		this.orgId = orgId;
	}

	// Property accessors

	@Column(name = "user_id", nullable = false, length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "org_id", nullable = false, length = 32)
	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SysUserOrgId))
			return false;
		SysUserOrgId castOther = (SysUserOrgId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this
				.getUserId() != null
				&& castOther.getUserId() != null && this.getUserId().equals(
				castOther.getUserId())))
				&& ((this.getOrgId() == castOther.getOrgId()) || (this
						.getOrgId() != null
						&& castOther.getOrgId() != null && this.getOrgId()
						.equals(castOther.getOrgId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getOrgId() == null ? 0 : this.getOrgId().hashCode());
		return result;
	}

}