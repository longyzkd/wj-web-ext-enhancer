package com.kingen.bean;

import java.util.List;

/**
 * 适合EXT的权限model 
 * 带checked
 * @author wj
 *
 */
public class RightData {
	/**
	 * 就是menuId
	 */
	private String menuId; 
	private String menuName;
	private String pmenuId;
	private String Note;
	private Boolean checked;
	private Boolean leaf;
	private Boolean expanded;
	private List<RightData> children;
	
	
	
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getPmenuId() {
		return pmenuId;
	}
	public void setPmenuId(String pmenuId) {
		this.pmenuId = pmenuId;
	}
	public String getNote() {
		return Note;
	}
	public void setNote(String note) {
		Note = note;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public Boolean getLeaf() {
		return leaf;
	}
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}
	public Boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	public List<RightData> getChildren() {
		return children;
	}
	public void setChildren(List<RightData> children) {
		this.children = children;
	}
}
