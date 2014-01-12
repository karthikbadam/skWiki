package edu.purdue.pivot.skwiki.client.sketch;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.ImgButton;


public class CanvasToolbar extends Composite {


	private EventHandler handler = new EventHandler();
	private TouchPad canvas;   
		
	private ImgButton color;
	private ImgButton strokeSize;
	private ImgButton eraser;
	private ImgButton undo;
	private ImgButton redo;
	private ImgButton clear;
	private ImgButton resize;
		
	public CanvasToolbar(TouchPad canvas) {
		FlexTable basePanel = new FlexTable();
		initWidget(basePanel);
		this.canvas = canvas;
		
		basePanel.setWidget(0, 0, color = createImgButton("color.png"));
		basePanel.setWidget(1, 0, strokeSize = createImgButton("strokeSize.png"));
		basePanel.setWidget(2, 0, eraser = createImgButton("eraser.png"));
		basePanel.setWidget(3, 0, undo = createImgButton("undo.png"));
		basePanel.setWidget(4, 0, redo = createImgButton("redo.png"));
		
	}

	private ImgButton createImgButton(String string) {
		ImgButton button = new ImgButton();
		button.setWidth(48);
		button.setHeight(48);
		button.setSrc(string);
		button.setLabelHPad(5);
		button.setLabelVPad(10);
		button.addClickHandler(handler);
		return button;
	}

	private class EventHandler implements com.smartgwt.client.widgets.events.ClickHandler, KeyUpHandler {

		@Override
		public void onClick(final com.smartgwt.client.widgets.events.ClickEvent event) {
			Widget sender = (Widget) event.getSource();
			
			if (sender == color) {
		        canvas.changeColor();
		      } else if (sender == strokeSize) {
		        canvas.changeStrokeSize();
		      } else if (sender == eraser) {
		        canvas.erase();
		      } else if (sender == undo) {
		        canvas.undo();
		      } else if (sender == redo) {
		        canvas.redo();
		      } else if (sender == clear) {
		        canvas.clear();
		      } else if (sender == resize) {
			    canvas.resize();
			  } 
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {
		}
	}
	
}
