package com.kingen.service.contract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kingen.bean.Contract;
import com.kingen.repository.contract.ContractDao;
import com.kingen.service.CommonService;

/**
 * 服务
 * 
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class ContractService extends CommonService<Contract>{
	
	@Autowired
	private ContractDao dao;

	private  Logger logger = LoggerFactory.getLogger(getClass());
	
	

	



}
