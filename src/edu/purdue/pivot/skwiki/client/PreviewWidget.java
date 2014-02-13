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
		
		myCanvas.setStyleName("gwt-TouchPanelWidget");
		boundaryPanel.add(myCanvas);

		for (AbstractLayoutHistory tempLayoutHistory : layoutHistoryList) {
			if (tempLayoutHistory instanceof ChangePosHistory) {
				if (id == ((ChangePosHistory) tempLayoutHistory)
						.getOperatingObject()) {

					int left = ((ChangePosHistory) tempLayoutHistory).getNewX() + 30;
					int top = ((ChangePosHistory) tempLayoutHistory).getNewY() + 30;
					// Window.alert("" + left + ", " + top);
					boundaryPanel.setWidgetPosition(myCanvas,
							(int) (left * scaleWidth),
							(int) (top * scaleHeight));
				}
			}

			if (tempLayoutHistory instanceof ChangeSizeHistory) {
				if (id == ((ChangeSizeHistory) tempLayoutHistory)
						.getOperatingObject()) {

					int width = (int)((int)(((ChangeSizeHistory) tempLayoutHistory)
							.getNewX())*scaleWidth);
					int height = (int)((int)(((ChangeSizeHistory) tempLayoutHistory)
							.getNewY())*scaleHeight);
					System.out.println("Resize - "+width+", "+height);
					//myCanvas.setHeight(height+"px");
					//myCanvas.setWidth(width+"px");
					myCanvas.clear();
					myCanvas.setSize(width, height);
					//boundaryPanel.setContentSize(width, height);
				}
			}
		}
		canvasEditor.updateEditor(result);

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
