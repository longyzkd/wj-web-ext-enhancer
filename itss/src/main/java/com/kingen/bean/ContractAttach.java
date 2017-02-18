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
@Table(name = "t_contract_attach")
public class ContractAttach implements java.io.Serializable {

	// Fields

	private String id;
	private String contractId;
//	private String attach;
	
//	@Lob
//	private Blob attachContent;  //hibernate4已经取消createBlob
	
	private byte[] attachContent;
	private String attachName;

	// Constructors

	/** default constructor */
	public ContractAttach() {
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

	

	
	
	

	@Column(name = "contract_id")
	public String getContractId() {
		return contractId;
	}

	@Column(name = "attach")
	public byte[] getAttachContent() {
		return attachContent;
	}


	public void setAttachContent(byte[] attachContent) {
		this.attachContent = attachContent;
	}


	public void setContractId(String contract_id) {
		this.contractId = contract_id;
	}

	@Column(name = "attach_name")
	public String getAttachName() {
		return attachName;
	}


	public void setAttachName(String attach_name) {
		this.attachName = attach_name;
	}


}