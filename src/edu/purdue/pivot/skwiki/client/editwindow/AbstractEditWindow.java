package edu.purdue.pivot.skwiki.client.editwindow;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AbstractEditWindow extends VerticalPanel{

	final private HorizontalPanel buttonPanel;
	final protected Button btnEdit;
	final protected Button btnCommit;
	//final private btn

	public AbstractEditWindow() {
		// TODO Auto-generated constructor stub
	btnEdit = new Button("Edit");
	btnCommit = new Button("Commit");
	buttonPanel = new HorizontalPanel();
	buttonPanel.add(btnCommit);
	buttonPanel.add(btnEdit);
	add(buttonPanel);
	
	}
	
}
