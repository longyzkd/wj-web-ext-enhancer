package com.kingen.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kingen.bean.RightData;
import com.kingen.util.mapper.JsonMapper;
import com.kingen.vo.TreeNode;


/**
 * 递归生成生成静态树
 * @author wj
 *
 */
public class TreeConverter {
	
	private static JsonMapper mapper = new JsonMapper(Include.ALWAYS);
	
	
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
	public static List<TreeNode> getChildren(List<TreeNode> all, TreeNode p) {//2..N级
		List<TreeNode> list = new ArrayList<TreeNode>();
		for (TreeNode node : all) {
			if(!StringUtils.isEmpty(node.getParentId())){
				if(StringUtils.equals(p.getId(), node.getParentId())){//列表中有父类id
					
					
//					list.add(getChildren(all,node));
				}
			}
		}
		return list;
	}
	
	/**
	 * 转成ext格式的树
	 * @author wj
	 * @param src  所有的数据
	 * @return  复杂的树JSON
	 */
	public static String toComplexJsonString(List<TreeNode> src) {

		Map<String, TreeNode> lookup = Maps.newHashMap();

		for (TreeNode o : src) {
			lookup.put(o.getId(), o);
		}
		Set<String> keySet = lookup.keySet();
		for (String id : keySet) {
			TreeNode value = lookup.get(id);
			String parentId = value.getParentId();
			TreeNode parentNode = lookup.get(parentId);
			if (parentNode != null) {//有父节点
				parentNode.addChild(value);
				value.setParent(parentNode);
			}
		}
		for (String id : keySet) {
			TreeNode value = lookup.get(id);
			if (value.getParent() == null) {
				//....
//				return  mapper.toJson(Lists.newArrayList(value) );
				return  JsonMapper.toJsonString(Lists.newArrayList(value));
				
			}
		}
		return "";
	}
	/**
	 * 转成ext格式的树
	 * @author wj
	 * @param src  所有的数据
	 * @return  复杂的树对象
	 */
	public static TreeNode  toComplexTree(List<TreeNode> src) {
		
		Map<String, TreeNode> lookup = Maps.newHashMap();
		
		for (TreeNode o : src) {
			lookup.put(o.getId(), o);
		}
		Set<String> keySet = lookup.keySet();
		for (String id : keySet) {
			TreeNode value = lookup.get(id);
			String parentId = value.getParentId();
			TreeNode parentNode = lookup.get(parentId);
			if (parentNode != null) {//有父节点
				parentNode.addChild(value);
				value.setParent(parentNode);
			}
		}
		for (String id : keySet) {
			TreeNode value = lookup.get(id);
			if (value.getParent() == null) {
				return  value;
				
			}
		}
		return null;
	}
	
	
	public static void main(String[] a){
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		boolean s = CollectionUtils.isEmpty(list)?true:false;
		System.out.println(s); //true
	}
}
