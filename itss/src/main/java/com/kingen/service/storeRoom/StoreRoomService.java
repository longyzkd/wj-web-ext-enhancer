package com.kingen.service.storeRoom;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.kingen.bean.StoreRoom;
import com.kingen.repository.storeRoom.StroreRoomDao;
import com.kingen.service.CommonService;
import com.kingen.util.BeanUtils;
import com.kingen.util.TreeConverter;
import com.kingen.util.mapper.BeanMapper;
import com.kingen.vo.TreeNode;

/**
 * 日志管理类.
 * 
 */
// Spring Service Bean的标识.
@Component
@Transactional
public class StoreRoomService extends CommonService<StoreRoom,String>{

	private  Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private StroreRoomDao dao ;
	

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
		List<StoreRoom> all = list();
		
		List<TreeNode> allConverted = BeanMapper.mapList(all, TreeNode.class);
		
		List<TreeNode> children = TreeConverter.getChildren(allConverted, id);
		
		dao.delete(id);
		if(CollectionUtils.isEmpty(children)){
			return ;
		}
		for(TreeNode node : children){
			dao.delete(node.getId());
		}
		
	}


}
