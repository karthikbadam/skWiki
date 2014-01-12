package edu.purdue.pivot.skwiki.client.tree;

import java.util.ArrayList;

import edu.purdue.pivot.skwiki.client.sketch.AttachedPanel;
import edu.purdue.pivot.skwiki.shared.EditorType;

public class TreeNode {

	String ID;
	ArrayList<TreeNode> children;
	 EditorType type;
	 AttachedPanel editPanel;
	
	//TreeNode parent;
	 
	 
	public TreeNode(String ID, AttachedPanel panel, EditorType type)
	{
		this.ID = ID;
		children = new ArrayList<TreeNode>();
		editPanel = panel;
		this.type = type;
		
		
	}
	
	public void addChild(TreeNode child)
	{
		children.add(child);
	}
	
	public void removeAll()
	{
		children.clear();
	}
}
