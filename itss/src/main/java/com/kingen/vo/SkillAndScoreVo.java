package com.kingen.vo;

import com.kingen.bean.PersonSkillScore;
import com.kingen.bean.SkillCat;


/**
 * 
 * 技能和人员分数VO
 * @author wj
 *
 */
public class SkillAndScoreVo {
	
	private String userId;
	private String skillId;
	private Integer score;
	private String name;
	private String id;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getSkillId() {
		return skillId;
	}


	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}


	public Integer getScore() {
		return score;
	}


	public void setScore(Integer score) {
		this.score = score;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	
	
}
