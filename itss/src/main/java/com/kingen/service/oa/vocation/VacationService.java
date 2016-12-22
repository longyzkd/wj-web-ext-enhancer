package com.kingen.service.oa.vocation;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.kingen.bean.workflow.Vacation;
import com.kingen.service.CommonService;

@Service
@Transactional
public class VacationService extends CommonService<Vacation,Integer>  {

	
	public Serializable doAdd(Vacation vacation) throws Exception {
		return add(vacation);
	}

	
	public void doUpdate(Vacation vacation) throws Exception {
		update(vacation);
	}

	
	public void doDelete(Vacation vacation) throws Exception {
		delete(vacation);
	}

	
	public List<Vacation> toList(Integer userId) throws Exception {
		List<Vacation> list = findByPage("Vacation", new String[]{"userId"}, new String[]{userId.toString()});
		return list;
	}

	
	public Vacation findById(Integer id) throws Exception {
		return getUnique("Vacation", new String[]{"id"}, new String[]{id.toString()});
	}

	
	public List<Vacation> findByStatus(Serializable userId, String status) throws Exception {
		List<Vacation> list = findByPage("Vacation", new String[]{"userId","status"}, new String[]{userId.toString(), status});
		return list;
	}
}
