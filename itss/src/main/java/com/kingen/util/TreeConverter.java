package com.kingen.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.kingen.bean.RightData;


/**
 * 递归生成生成静态树
 * @author wj
 *
 */
public class TreeConverter {
	
	public static List<Map<String, Object>> tree(List<RightData> ts) {//一级
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if (ts.size() < 1)
			return list;
		
		for(RightData rightData : ts){
			if(StringUtils.isEmpty(rightData.getPmenuId())){
				Map<String, Object> child = new HashMap<String, Object>();
				child.put("menuId", rightData.getMenuId());
				child.put("pmenuId", rightData.getPmenuId());
				child.put("menuName", rightData.getMenuName());
				child.put("checked", rightData.getChecked());
				child.put("expanded", rightData.getExpanded());
				child.put("note", rightData.getNote());
				child.put("children", getChildren(ts, rightData));
				child.put("leaf", CollectionUtils.isEmpty(getChildren(ts, rightData))?true:false);
				list.add(child);
			}
		}
		return list;
	}
	
	public static List<Map<String, Object>> getChildren(List<RightData> ts, RightData p) {//2..N级
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (RightData rightData : ts) {
			if(!StringUtils.isEmpty(rightData.getPmenuId())){
				if(StringUtils.equals(p.getMenuId(), rightData.getPmenuId())){//列表中有父类id
					Map<String, Object> child = new HashMap<String, Object>();
					child.put("checked", rightData.getChecked());
					child.put("expanded", rightData.getExpanded());
					child.put("menuId", rightData.getMenuId());
					child.put("menuName", rightData.getMenuName());
					child.put("pmenuId", rightData.getPmenuId());
					child.put("note", rightData.getNote());
					child.put("children", getChildren(ts, rightData));
					child.put("leaf",CollectionUtils.isEmpty(getChildren(ts, rightData))?true:false);
					list.add(child);
				}
			}
		}
		return list;
	}
	
	
	public static void main(String[] a){
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		boolean s = CollectionUtils.isEmpty(list)?true:false;
		System.out.println(s); //true
	}
}
