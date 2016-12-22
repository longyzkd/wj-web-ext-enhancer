package com.kingen.service.lookup;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.kingen.bean.Lookup;
import com.kingen.service.CommonService;
import com.kingen.util.Page;

/**
 * 日志管理类.
 * 
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class LookupService extends CommonService<Lookup,String>{

	private  Logger logger = LoggerFactory.getLogger(getClass());
	
	

	public Page<Lookup> data(Page<Lookup> page, String type) {
		
		Map<String,Object> params = Maps.newHashMap();
		params.put("type", type);
		
		return find(page, params);
	}
	



}
