package edu.purdue.pivot.skwiki.client.pathviewer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

class PathViewerCell extends Label implements ClickHandler {

	private int revision;
	private String comment;
	public int selectedRevision;
	
	public PathViewerCell(int revision) {
		super("comment");
		this.revision = revision;
	}

	@Override
	public void onClick(ClickEvent event) {
		selectedRevision = revision;
	}
	
}
