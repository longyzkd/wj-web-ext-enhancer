package com.kingen.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TAssets entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_assets")
public class Assets implements java.io.Serializable {

	// Fields

	private String id;
	private String assetsNo;
	private String assetsSpecification;
	private String bxBeginTime;
	private String bxEndTime;
	private String contractId;
	private String clientId;
	private String assetsType;
	private String manufacturerCode;
	private String supplierCode;
	private Integer count;
	private String installationLocation;

	// Constructors

	/** default constructor */
	public Assets() {
	}

	/** full constructor */
	public Assets(String assetsNo, String assetsSpecification,
			String bxBeginTime, String bxEndTime, String contractId,
			String clientId, String assetsType, String manufacturerCode,
			String supplierCode, Integer count, String installationLocation) {
		this.assetsNo = assetsNo;
		this.assetsSpecification = assetsSpecification;
		this.bxBeginTime = bxBeginTime;
		this.bxEndTime = bxEndTime;
		this.contractId = contractId;
		this.clientId = clientId;
		this.assetsType = assetsType;
		this.manufacturerCode = manufacturerCode;
		this.supplierCode = supplierCode;
		this.count = count;
		this.installationLocation = installationLocation;
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

	@Column(name = "assets_no", length = 32)
	public String getAssetsNo() {
		return this.assetsNo;
	}

	public void setAssetsNo(String assetsNo) {
		this.assetsNo = assetsNo;
	}

	@Column(name = "assets_specification", length = 32)
	public String getAssetsSpecification() {
		return this.assetsSpecification;
	}

	public void setAssetsSpecification(String assetsSpecification) {
		this.assetsSpecification = assetsSpecification;
	}

	@Column(name = "bx_begin_time", length = 32)
	public String getBxBeginTime() {
		return this.bxBeginTime;
	}

	public void setBxBeginTime(String bxBeginTime) {
		this.bxBeginTime = bxBeginTime;
	}

	@Column(name = "bx_end_time", length = 32)
	public String getBxEndTime() {
		return this.bxEndTime;
	}

	public void setBxEndTime(String bxEndTime) {
		this.bxEndTime = bxEndTime;
	}

	@Column(name = "contract_id", length = 32)
	public String getContractId() {
		return this.contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	@Column(name = "client_id", length = 32)
	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Column(name = "assets_type", length = 15)
	public String getAssetsType() {
		return this.assetsType;
	}

	public void setAssetsType(String assetsType) {
		this.assetsType = assetsType;
	}

	@Column(name = "manufacturer_code", length = 15)
	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}

	@Column(name = "supplier_code", length = 15)
	public String getSupplierCode() {
		return this.supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	@Column(name = "count")
	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name = "installation_location", length = 100)
	public String getInstallationLocation() {
		return this.installationLocation;
	}

	public void setInstallationLocation(String installationLocation) {
		this.installationLocation = installationLocation;
	}

}