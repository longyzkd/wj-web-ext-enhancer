/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.kingen.repository.log;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.kingen.bean.Log;
import com.kingen.bean.SysOrgMenu;
import com.kingen.bean.SysOrgMenuId;
import com.kingen.repository.CommonDao;
import com.kingen.util.Page;

@Component
public class LogDao extends CommonDao<Log,String>  {
	

	



	public Page<Log> findLogs(Page<Log> page, Log vo) {


		Map<String,Object> parameter = Maps.newHashMap();
		
		
		StringBuilder hql = new StringBuilder("from Log where 1=1 "); //超级管理员不在页面显示
		if(vo !=null && !StringUtils.isBlank(vo.getUserAgent())){
			hql.append(" and userAgent like :p1");
			parameter.put("p1", "%"+vo.getUserAgent()+"%");
		}
		if(vo !=null && !StringUtils.isBlank(vo.getFromDate())){
			hql.append(" and createDate >:p2");
			parameter.put("p2", vo.getFromDate());
		}
		if(vo !=null && !StringUtils.isBlank(vo.getToDate())){
			hql.append(" and createDate <= :p3");
			parameter.put("p3", vo.getToDate());
		}
		hql.append(" order by createDate desc");
		
		return     find(page, hql.toString(), parameter);
	
	
	}

	public List<Log> findLogs(Log vo) {

		Map<String,Object> parameter = Maps.newHashMap();
		
		StringBuilder hql = new StringBuilder("from Log where 1=1 "); //超级管理员不在页面显示
		if(vo !=null && !StringUtils.isBlank(vo.getUserAgent())){
			hql.append(" and userAgent like :p1");
			parameter.put("p1", "%"+vo.getUserAgent()+"%");
		}
		if(vo !=null && !StringUtils.isBlank(vo.getFromDate())){
			hql.append(" and createDate >:p2");
			parameter.put("p2", vo.getFromDate());
		}
		if(vo !=null && !StringUtils.isBlank(vo.getToDate())){
			hql.append(" and createDate <= :p3");
			parameter.put("p3", vo.getToDate());
		}
		hql.append(" order by createDate desc");
		
		return     find(hql.toString(), parameter);
	}






	
}
