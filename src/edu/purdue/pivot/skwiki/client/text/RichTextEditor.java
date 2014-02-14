package edu.purdue.pivot.skwiki.client.text;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RichTextArea;

import edu.purdue.pivot.skwiki.client.sketch.AttachedPanel;

public class RichTextEditor extends RichTextArea implements AttachedPanel {

	private RichTextToolbar richTextToolbar;
	RichTextArea richTextArea;
	
	//uuid
	private String uuid;
	String uid;
	public RichTextEditor(String uuid, String uid) {
		super();
		this.uuid = uuid;
		this.uid = uid;
		richTextArea = this;
		richTextToolbar = new RichTextToolbar(richTextArea);
		this.setSize("500px", "100px");
	}

	public RichTextEditor(String uuid, String uid, float scaleWidth, float scaleHeight) {
		super();
		this.uuid = uuid;
		this.uid = uid;
		richTextArea = this;
		richTextToolbar = new RichTextToolbar(richTextArea);
		this.setSize(500*scaleWidth+"px", 100*scaleHeight+"px");
		DOM.setStyleAttribute(richTextArea.getElement(), "font-size", (int)(scaleWidth * 100) + "%");
		
	}

	public String getHtml() {
		return richTextArea.getHTML();
	}

	public void setHtml(String html) {
		richTextArea.setHTML(html);
	}

	public RichTextArea getArea() {
		return richTextArea;
	}

	public RichTextToolbar getToolbar() {
		return richTextToolbar;
	}

	

	public void clear() {
		richTextArea.setText("");
	}


	@Override
	public String getID() {
		return uuid;
	}
}
