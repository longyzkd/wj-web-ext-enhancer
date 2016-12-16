package com.kingen.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TPersonSkillScore entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_person_skill_score")
public class PersonSkillScore implements java.io.Serializable {

	// Fields

	private String id;
	private String userId;
	private String skillId;
	private Integer score;

	// Constructors

	/** default constructor */
	public PersonSkillScore() {
	}

	/** full constructor */
	public PersonSkillScore(String userId, String skillId, Integer score) {
		this.userId = userId;
		this.skillId = skillId;
		this.score = score;
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

	@Column(name = "user_id", length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "skill_id", length = 32)
	public String getSkillId() {
		return this.skillId;
	}

	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}

	@Column(name = "score")
	public Integer getScore() {
		return this.score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

}