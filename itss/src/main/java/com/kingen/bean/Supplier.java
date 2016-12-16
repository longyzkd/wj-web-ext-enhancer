package com.kingen.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TSupplier entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_supplier")
public class Supplier implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String taxNo;
	private String addr;
	private String bank;
	private String postCode;
	private String bankAccount;
	private String businessLicenseNo;

	// Constructors

	/** default constructor */
	public Supplier() {
	}

	/** full constructor */
	public Supplier(String name, String taxNo, String addr, String bank,
			String postCode, String bankAccount, String businessLicenseNo) {
		this.name = name;
		this.taxNo = taxNo;
		this.addr = addr;
		this.bank = bank;
		this.postCode = postCode;
		this.bankAccount = bankAccount;
		this.businessLicenseNo = businessLicenseNo;
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

	@Column(name = "tax_no", length = 50)
	public String getTaxNo() {
		return this.taxNo;
	}

	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	@Column(name = "addr", length = 50)
	public String getAddr() {
		return this.addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	@Column(name = "bank", length = 50)
	public String getBank() {
		return this.bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	@Column(name = "post_code", length = 15)
	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Column(name = "bank_account", length = 25)
	public String getBankAccount() {
		return this.bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Column(name = "business_license_no", length = 50)
	public String getBusinessLicenseNo() {
		return this.businessLicenseNo;
	}

	public void setBusinessLicenseNo(String businessLicenseNo) {
		this.businessLicenseNo = businessLicenseNo;
	}

}