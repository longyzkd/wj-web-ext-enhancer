package com.kingen.service.storeRoom;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.kingen.bean.Lookup;
import com.kingen.bean.StoreRoom;
import com.kingen.bean.User;
import com.kingen.service.CommonService;
import com.kingen.util.BeanUtils;
import com.kingen.util.Page;

/**
 * 日志管理类.
 * 
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class StoreRoomService extends CommonService<StoreRoom,String>{

	private  Logger logger = LoggerFactory.getLogger(getClass());
	
	

//	public Page<StoreRoom> data(Page<StoreRoom> page) {
//		
//		
//		return find(page, params);
//	}
//	

	public void  updateForm(StoreRoom data){
		StoreRoom t = unique(data.getId());
		BeanUtils.copyNotNullProperties(data, t);
		update(t);
		
	}



	public void delCascade(String id) {
		// TODO Auto-generated method stub
		
	}


}
