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
 * TClientContact entity.
 * 
 * @author MyEclipse Persistence Tools
 */
/**
 * 客户联系人
 * @author wj
 *
 */
@Entity
@Table(name = "t_client_contact")
public class ClientContact implements java.io.Serializable,Comboable {

	// Fields

	private String id;
	private String email;
	private String userName;
	private String pwd;
	private String phone;

	
	
	private String salt;
	
	// Constructors

	/** default constructor */
	public ClientContact() {
	}

	/** full constructor */
	public ClientContact(String email, String userName, String pwd,
			String phone) {
		this.email = email;
		this.userName = userName;
		this.pwd = pwd;
		this.phone = phone;
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

	@Column(name = "email", length = 50)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "user_name", length = 50)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "pwd", length = 100)
	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Column(name = "phone", length = 20)
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
	
	@Override
	@Transient
	public String getCode() {
	return id;
	}

	@Override
	@Transient
	public String getName() {
		return userName;
	}
}