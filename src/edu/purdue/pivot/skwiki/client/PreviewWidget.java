package edu.purdue.pivot.skwiki.client;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import edu.purdue.pivot.skwiki.client.sketch.MyCanvasEditor;
import edu.purdue.pivot.skwiki.client.sketch.TouchPad;
import edu.purdue.pivot.skwiki.client.text.MyTextEditor;
import edu.purdue.pivot.skwiki.shared.AbstractLayoutHistory;
import edu.purdue.pivot.skwiki.shared.AddToParentHistory;
import edu.purdue.pivot.skwiki.shared.ChangePosHistory;
import edu.purdue.pivot.skwiki.shared.ChangeSizeHistory;
import edu.purdue.pivot.skwiki.shared.CreateEntityHistory;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.EditorType;

public class PreviewWidget extends ScrollPanel {
	int width = 0;
	int height = 0;
	float scaleWidth = 1;
	float scaleHeight = 1;
	int windowWidth; 
	int windowHeight;
	
	// boundary panel
	public AbsolutePanel boundaryPanel;
	DataPack result;
	SkwikiEntryPoint skWiki;

	public PreviewWidget(int width, int height, float scaleWidth,
			float scaleHeight, int windowWidth, int windowHeight) {
		super();
		this.setWidth(width + "px");
		this.setHeight(height + "px");
		this.width = width;
		this.height = height;
		this.scaleWidth = scaleWidth;
		this.scaleHeight = scaleHeight;
		this.setStyleName("gwt-PreviewPanelWidget");
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		
		this.boundaryPanel = new AbsolutePanel();
//		int bWidth = (int) (1080.0 * scaleWidth);
//		int bHeight = (int) (1800.0 * scaleHeight);
//		System.out.println("Preview Height" + bWidth);
		boundaryPanel.setSize((width - 10) + "px", (height - 10) + "px");
		this.add(boundaryPanel);
		//boundaryPanel.setStyleName("gwt-PanelWidget");
	}

	public PreviewWidget(SkwikiEntryPoint skWiki, int width, int height, float scaleWidth,
			float scaleHeight) {
		super();
		this.setWidth(width + "px");
		this.setHeight(height + "px");
		this.skWiki = skWiki;
		this.width = width;
		this.height = height;
		this.scaleWidth = scaleWidth;
		this.scaleHeight = scaleHeight;
		this.setStyleName("gwt-PanelWidget");

		this.boundaryPanel = new AbsolutePanel();
		int bWidth = (int) (1080.0 * scaleWidth);
		int bHeight = (int) (1800.0 * scaleHeight);
		System.out.println("Preview Height" + bWidth);
		boundaryPanel.setSize(width + "px", height + "px");
		this.add(boundaryPanel);
		boundaryPanel.setStyleName("gwt-PanelWidget");
	}
	
	public void previewCanvas(String id,
			ArrayList<AbstractLayoutHistory> layoutHistoryList) {
		final String id2 = id;
		final MyCanvasEditor canvasEditor = new MyCanvasEditor(id, "user",
				scaleWidth, scaleHeight, windowWidth, windowHeight);
		TouchPad myCanvas = canvasEditor.getSurface();

		// copy option
		myCanvas.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
					
			}
			

		});
//		
//		myCanvas.addTouchStartHandler(new TouchStartHandler(){
//
//			@Override
//			public void onTouchStart(TouchStartEvent event) {
//				DialogBox popup = new DialogBox();
//				Button ok = new Button("OK");
//				Button cancel = new Button("Cancel");
//				HorizontalPanel okcancel = new HorizontalPanel();
//				okcancel.add(ok);
//				okcancel.add(cancel);
//				
//				VerticalPanel panel = new VerticalPanel();
//				panel.add(new Label("Are you sure you want to copy this element?"));
//				panel.add(okcancel);
//				popup.add(panel);
//				
//				ok.addClickHandler(new ClickHandler(){
//
//					@Override
//					public void onClick(ClickEvent event) {
//						Window.alert("copied");
//						skWiki.widgets.addCanvas(id2, 0);
//						
//					}
//					
//				});
//			}
//			
//		});

		myCanvas.setStyleName("gwt-TouchPanelWidget");
		boundaryPanel.add(myCanvas);
		canvasEditor.updateEditor(result);

		for (AbstractLayoutHistory tempLayoutHistory : layoutHistoryList) {
			if (tempLayoutHistory instanceof ChangePosHistory) {
				if (id == ((ChangePosHistory) tempLayoutHistory)
						.getOperatingObject()) {

					int left = ((ChangePosHistory) tempLayoutHistory).getNewX();
					int top = ((ChangePosHistory) tempLayoutHistory).getNewY();
					// Window.alert("" + left + ", " + top);
					boundaryPanel.setWidgetPosition(myCanvas,
							(int) (left * scaleWidth),
							(int) (top * scaleHeight));
				}
			}

			if (tempLayoutHistory instanceof ChangeSizeHistory) {
				if (id == ((ChangeSizeHistory) tempLayoutHistory)
						.getOperatingObject()) {

					int width = ((ChangeSizeHistory) tempLayoutHistory)
							.getNewX();
					int height = ((ChangeSizeHistory) tempLayoutHistory)
							.getNewY();
					
					//myCanvas.setHeight(height+"px");
					//myCanvas.setWidth(width+"px");
					//boundaryPanel.setContentSize(width, height);
				}
			}
		}

	}

	public void previewText(String id) {
		final MyTextEditor textEditor = new MyTextEditor(id, "user",
				scaleWidth, scaleHeight);
		boundaryPanel.add(textEditor);
		textEditor.updateEditor(result);
		textEditor.setStyleName("gwt-previewTextFontSize");
	}

	public void previewImage(String id) {

	}

	public void createPreviewLayout(
			ArrayList<AbstractLayoutHistory> layoutHistoryList, DataPack result) {
		clearContent();
		this.result = result;
		for (AbstractLayoutHistory tempLayoutHistory : layoutHistoryList) {
			if (tempLayoutHistory instanceof AddToParentHistory) {

			} else if (tempLayoutHistory instanceof CreateEntityHistory) {
				String id = ((CreateEntityHistory) tempLayoutHistory)
						.getOperatingObject();
				if (((CreateEntityHistory) tempLayoutHistory).editorType == EditorType.TEXT) {
					if (scaleWidth > 0.3) {
						previewText(id);
					}
					System.out.println("Found text");
				} else if (((CreateEntityHistory) tempLayoutHistory).editorType == EditorType.CANVAS) {
					previewCanvas(id, layoutHistoryList);
					System.out.println("Found canvas");
				}

			}
		}

	}

	public void clearContent() {
		boundaryPanel.clear();
	}
}
