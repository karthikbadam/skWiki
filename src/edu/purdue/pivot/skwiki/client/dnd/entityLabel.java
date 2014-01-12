package edu.purdue.pivot.skwiki.client.dnd;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class entityLabel extends HTML{
	
	  public Image image;
	  public HTML info;
	
	public entityLabel(String imageUrl, String html){
		HorizontalPanel widget = new HorizontalPanel();
		
		
		
		 image = new Image(imageUrl);
		 this.info = new HTML(html);
		// this.info.setHTML(html);
		 widget.add(image);
		 widget.add(info);
		 //this.add
//		 initWidget(widget);
		// initWidget(info);
		
		
	}

}
