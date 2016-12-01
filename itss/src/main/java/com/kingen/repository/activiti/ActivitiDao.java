/*******************************************************************************
 *******************************************************************************/
package com.kingen.repository.activiti;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kingen.bean.User;
import com.kingen.repository.CommonDao;

@Component
public class ActivitiDao extends CommonDao<Object> { //必须要加泛型，不然会认为是commondao，导致commonservice里的commondao多出来几个
	private static Logger logger = LoggerFactory.getLogger(ActivitiDao.class);
	public void deleteAllUser() {
		String sql = "delete from ACT_ID_USER";
		executeSql(sql);
		logger.debug("deleted from activiti user.");
	}

	public void deleteAllGroup() {
		String sql = "delete from ACT_ID_GROUP";
		executeSql(sql);
		logger.debug("deleted from activiti group.");
	}

	public void deleteAllMemerShip() {
		String sql = "delete from ACT_ID_MEMBERSHIP";
		executeSql(sql);
		logger.debug("deleted from activiti membership.");
	}

	
}
