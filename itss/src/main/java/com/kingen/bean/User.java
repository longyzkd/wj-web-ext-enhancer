package com.kingen.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.collect.Lists;


/**
 * 以后映射数据库
 */
@Entity
@Table(name = "tmanageruser")
public class User implements java.io.Serializable {

	// Fields

	private String userId;   //登录名
	private String username; //真实姓名
	private String password;
	private Integer userType;
	private Boolean isStoped;
	private String note;
	private String phone;

	private String  avatarPath;
	
	private String salt;
	private String mail;
	/**
	 * 0 屏蔽
	 * 1 激活
	 */
	private String status;
	
	private String deptName ;
	// Constructors

	/** default constructor */
	public User() {
	}
	@Column(name = "AvatarPath", length = 100)
	public String getAvatarPath() {
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}

	/** full constructor */
	public User(String userName, String password, Integer userType,
			Boolean isStoped, String note, String phone) {
		this.username = userName;
		this.password = password;
		this.userType = userType;
		this.isStoped = isStoped;
		this.note = note;
		this.phone = phone;
	}

	// Property accessors
	@Id
	@Column(name = "UserID", unique = true, nullable = false, length = 100)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "UserName", length = 100)
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "Password", length = 200)
	public String getPassword() {
		return this.password;
	}

	
	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "UserType")
	public Integer getUserType() {
		return this.userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	@Column(name = "IsStoped")
	public Boolean getIsStoped() {
		return this.isStoped;
	}

	public void setIsStoped(Boolean isStoped) {
		this.isStoped = isStoped;
	}

	@Column(name = "Note", length = 2000)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "Phone", length = 100)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column(name="salt")
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	@Column(name="mail")
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	@Column(name="status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="deptname")
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	
	
	
	
	

	

}