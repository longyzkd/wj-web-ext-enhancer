package com.kingen.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;


/**
 * 树节点，默认是EXT格式的
 * 
 * @author f
 *
 */
public class TreeNode {

	private String id;
	private String parentId;
	private String text;
	private boolean leaf;
	private String iconCls;
	private List<TreeNode> children = Lists.newArrayList();
	private TreeNode  parent;
	
	@JsonIgnore
	public TreeNode getParent() {
		return parent;
	}
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	@JsonIgnore
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public void addChild(TreeNode node) {
	    if (children == null) {
	        children = Lists.newArrayList();
	    }
	    children.add(node);
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	
}
