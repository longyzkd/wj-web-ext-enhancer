/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.kingen.service.log;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kingen.aop.ServiceLogAnnotation;
import com.kingen.bean.Log;
import com.kingen.repository.log.LogDao;
import com.kingen.util.Page;

/**
 * 日志管理类.
 * 
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class LogService {


	private  Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LogDao dao;


	public void saveLog(Log l){
		dao.saveme(l);
		
	}


	public Page<Log> getLogs(Page<Log> page, Log vo) {
		Page<Log> p = dao.findLogs(page,vo);
		return p;
	}
	public List<Log>  getLogs( Log vo) {
		List<Log>  p = dao.findLogs(vo);
		return p;
	}


	public void del(Log vo) {
		dao.delete(vo);
		
	}

	@ServiceLogAnnotation(action="删除日志")
	public void del(String[] ids) {
		Assert.notEmpty(ids,"日志ID不应为空");
		for(String id :ids ){
			dao.delete(id);
		}
		
	}
}
