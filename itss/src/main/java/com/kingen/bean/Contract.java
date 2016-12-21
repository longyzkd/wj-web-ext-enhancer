package com.kingen.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TContract entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_contract")
public class Contract implements java.io.Serializable {

	// Fields

	private String id;
	/**
	 * 合同名称
	 */
	private String contractName;
	/**
	 * 客户单位
	 */
	private String clientId;
	/**
	 * 客户方联系人
	 */
	private String clientContactId;
	private String contractNo;
	private String seviceLv;
	private String contractType;
	private double contractAmt;
	private String serviceBeginTime;
	private String serviceEndTime;
	private String desc;
	private String attach;
	private String procDefName;

	// Constructors

	/** default constructor */
	public Contract() {
	}

	/** full constructor */
	public Contract(String contractName, String clientId,
			String clientContactId, String contractNo, String seviceLv,
			String contractType, double contractAmt, String serviceBeginTime,
			String serviceEndTime, String desc, String attach,
			String procDefName) {
		this.contractName = contractName;
		this.clientId = clientId;
		this.clientContactId = clientContactId;
		this.contractNo = contractNo;
		this.seviceLv = seviceLv;
		this.contractType = contractType;
		this.contractAmt = contractAmt;
		this.serviceBeginTime = serviceBeginTime;
		this.serviceEndTime = serviceEndTime;
		this.desc = desc;
		this.attach = attach;
		this.procDefName = procDefName;
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

	@Column(name = "contract_name", length = 100)
	public String getContractName() {
		return this.contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	@Column(name = "client_id", length = 32)
	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Column(name = "client_contact_id", length = 32)
	public String getClientContactId() {
		return this.clientContactId;
	}

	public void setClientContactId(String clientContactId) {
		this.clientContactId = clientContactId;
	}

	@Column(name = "contract_no", length = 100)
	public String getContractNo() {
		return this.contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	@Column(name = "sevice_lv", length = 10)
	public String getSeviceLv() {
		return this.seviceLv;
	}

	public void setSeviceLv(String seviceLv) {
		this.seviceLv = seviceLv;
	}

	@Column(name = "contract_type", length = 10)
	public String getContractType() {
		return this.contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	@Column(name = "contract_amt", precision = 12)
	public double getContractAmt() {
		return this.contractAmt;
	}

	public void setContractAmt(double contractAmt) {
		this.contractAmt = contractAmt;
	}

	@Column(name = "service_begin_time", length = 32)
	public String getServiceBeginTime() {
		return this.serviceBeginTime;
	}

	public void setServiceBeginTime(String serviceBeginTime) {
		this.serviceBeginTime = serviceBeginTime;
	}

	@Column(name = "service_end_time", length = 32)
	public String getServiceEndTime() {
		return this.serviceEndTime;
	}

	public void setServiceEndTime(String serviceEndTime) {
		this.serviceEndTime = serviceEndTime;
	}

	@Column(name = "[desc]", length = 100)
	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "attach")
	public String getAttach() {
		return this.attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	@Column(name = "proc_def_name", length = 32)
	public String getProcDefName() {
		return this.procDefName;
	}

	public void setProcDefName(String procDefName) {
		this.procDefName = procDefName;
	}

}