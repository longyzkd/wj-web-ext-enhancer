package com.kingen.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TKnowledgeBase entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_knowledge_base")
public class KnowledgeBase implements java.io.Serializable {

	// Fields

	private String id;
	private String skillCatId;
	private String problemDesc;
	private String event;
	private String assetsType;
	private String problemReason;
	private String solution;
	private String attach;

	// Constructors

	/** default constructor */
	public KnowledgeBase() {
	}

	/** full constructor */
	public KnowledgeBase(String skillCatId, String problemDesc, String event,
			String assetsType, String problemReason, String solution,
			String attach) {
		this.skillCatId = skillCatId;
		this.problemDesc = problemDesc;
		this.event = event;
		this.assetsType = assetsType;
		this.problemReason = problemReason;
		this.solution = solution;
		this.attach = attach;
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

	@Column(name = "skill_cat_id", length = 32)
	public String getSkillCatId() {
		return this.skillCatId;
	}

	public void setSkillCatId(String skillCatId) {
		this.skillCatId = skillCatId;
	}

	@Column(name = "problem_desc", length = 100)
	public String getProblemDesc() {
		return this.problemDesc;
	}

	public void setProblemDesc(String problemDesc) {
		this.problemDesc = problemDesc;
	}

	@Column(name = "event", length = 32)
	public String getEvent() {
		return this.event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@Column(name = "assets_type", length = 32)
	public String getAssetsType() {
		return this.assetsType;
	}

	public void setAssetsType(String assetsType) {
		this.assetsType = assetsType;
	}

	@Column(name = "problem_reason", length = 100)
	public String getProblemReason() {
		return this.problemReason;
	}

	public void setProblemReason(String problemReason) {
		this.problemReason = problemReason;
	}

	@Column(name = "solution", length = 100)
	public String getSolution() {
		return this.solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	@Column(name = "attach")
	public String getAttach() {
		return this.attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

}