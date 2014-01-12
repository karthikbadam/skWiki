package edu.purdue.pivot.skwiki.client.sketch;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class EntityPanel extends FlowPanel{
	
	
	TreeItem root = new TreeItem("root");
	
	//TODO ****** could add lists of textbox and canvas and layout
	public EntityPanel()
	{
		// Create a tree with a few items in it.
	    
	    root.addItem("item0");
	    root.addItem("item1");
	    root.addItem("item2");

	    // Add a CheckBox to the tree
	    TreeItem item = new TreeItem(new CheckBox("item3"));
	    root.addItem(item);

	    Tree t = new Tree();
	    t.addItem(root);
	    add(t);
	}
	
	
	public void add(String text)
	{
		root.addItem(text);
	}
	

}
