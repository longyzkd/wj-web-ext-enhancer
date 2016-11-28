/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.kingen.service.org;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kingen.aop.ServiceLogAnnotation;
import com.kingen.bean.SysOrg;
import com.kingen.bean.SysUserOrg;
import com.kingen.bean.SysUserOrgId;
import com.kingen.bean.User;
import com.kingen.repository.account.UserDao;
import com.kingen.repository.org.OrgDao;
import com.kingen.util.Page;

/**
 * 用户管理类.
 * 
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class OrgService {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;

	private static Logger logger = LoggerFactory.getLogger(OrgService.class);
	
	@Autowired
	private OrgDao dao;
	@Autowired
	private UserDao userDao;

	@ServiceLogAnnotation(action="保存组")
	public void create(SysOrg obj) {
		dao.save(obj);
		
	}
	@ServiceLogAnnotation(action="修改组")
	public void update(SysOrg SysOrg) {
		dao.update(SysOrg);
		
	}

	@ServiceLogAnnotation(action="删除组用户")
	public void delOrgUsers(String orgId, List<String> userIds) {
		
		Assert.hasLength(orgId,"组ID不应为空");
		Assert.notEmpty(userIds,"用户ID不应为空");
		dao.delOrgUsers(orgId,userIds);
		
	}
	@ServiceLogAnnotation(action="删除组")
	public void delOrg(String orgId) {
		
		Assert.hasLength(orgId,"组ID不应为空");
		
		dao.delOrg(orgId);
		
	}
	@ServiceLogAnnotation(action="保存组用户")
	public void saveOrgUser(String orgId,String userId) throws Exception{
		if (orgId != null) {
			SysUserOrgId userOrgId = new SysUserOrgId(userId,orgId);
			SysUserOrg u = new SysUserOrg(userOrgId);
			dao.saveme(u);
		}
	}
	@ServiceLogAnnotation(action="保存组用户")
	public void saveOrgUsers(String orgId,List<String> userIds) throws Exception{
		if (orgId != null) {
			Assert.notEmpty(userIds);
			for(String userId :userIds){
				SysUserOrgId userOrgId = new SysUserOrgId(userId,orgId);
				SysUserOrg u = new SysUserOrg(userOrgId);
				dao.saveme(u);
			}
		}
	}
	


	public SysOrg one(SysOrg o) {
		
		return dao.findByPK(o.getId());
	}



	public Page<SysOrg> getOrgs(Page<SysOrg> page, SysOrg object) {
		Page<SysOrg> p = dao.findOrgs(page,object);
		return p;
	}
	public Page<User> getOrgUsers(Page<User> page, String orgId) {
		Page<User> p = userDao.findOrgUsers(page,orgId);
		return p;
	}
	public Page<User> getOrgUsersRemain(Page<User> page, String orgId) {
		Page<User> p = userDao.findOrgUsersRemain(page,orgId);
		return p;
	}
	
}
