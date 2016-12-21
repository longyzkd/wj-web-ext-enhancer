package com.kingen.service.serv;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.kingen.bean.Lookup;
import com.kingen.bean.ServiceLv;
import com.kingen.repository.serv.ServiceLvDao;
import com.kingen.service.CommonService;
import com.kingen.util.Page;

/**
 * 服务
 * 
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class ServService extends CommonService<ServiceLv>{
	
	@Autowired
	private ServiceLvDao servLvDao;

	private  Logger logger = LoggerFactory.getLogger(getClass());
	
	

	



}
