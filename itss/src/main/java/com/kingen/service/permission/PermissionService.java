/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.kingen.service.permission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kingen.aop.ServiceLogAnnotation;
import com.kingen.bean.User;
import com.kingen.repository.permission.PermissionDao;
import com.kingen.util.BeanUtils;

/**
 * 用户管理类.
 * 
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class PermissionService {

	private static Logger logger = LoggerFactory.getLogger(PermissionService.class);

	@Autowired
	private PermissionDao dao;

	@ServiceLogAnnotation(action="分配权限")
	public void editPermission(String orgId, String[] funidvalue) throws Exception {
		Assert.hasText(orgId, "组织ID不应为空");
		if (funidvalue != null) {
			dao.delOrgMenus(orgId);
			dao.saveOrgMenus(orgId, funidvalue);

		}

	}

}
