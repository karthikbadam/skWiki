package edu.purdue.pivot.skwiki.client.image;

import edu.purdue.pivot.skwiki.client.dnd.WindowPanel;
import edu.purdue.pivot.skwiki.client.sketch.AttachedPanel;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.ImagePack;
import gwtupload.client.PreloadedImage;

public class MyImageViewer implements AttachedPanel {
	PreloadedImage image;
	String URL;
	int myWidth;
	int myHeight;
	double leftX;
	double topY;
	int layoutWidth;
	int layoutHeight;
	private String imageEditorID = "";
	String uid;
	
	public MyImageViewer(String URL, int width, int height, int layoutWidth, int layoutHeight, double leftX,
			double topY, PreloadedImage image, String uuid) {
		super();
		this.URL = URL;
		this.myWidth = width;
		this.myHeight = height;
		this.leftX = leftX;
		this.topY = topY;
		this.image = image;
		this.layoutWidth = layoutWidth;
		this.layoutHeight = layoutHeight;
		imageEditorID = uuid;
	}
	
	public MyImageViewer(String uuid, String uid) {
		imageEditorID = uuid;
		this.uid = uid;
	}
	
	public void getChange(DataPack data) {
		String key = imageEditorID;
		ImagePack tempImagePack = new ImagePack(URL, leftX, topY, myWidth,
				myHeight, layoutWidth, layoutHeight, imageEditorID);
		data.updateImageMap.put(key, tempImagePack);
	}
	
	public void updateEditor(DataPack data)
	{
		String key = imageEditorID;
		//setHtml((String)data.updatHtmlMap.get(key));
		// setCanvas
		updateOperation(data.updateImageMap.get(key));
	}
	
	public void updateWidthAndHeight(WindowPanel panel) {
		myWidth = panel.contentPanel.getOffsetWidth();
		myHeight = panel.contentPanel.getOffsetHeight(); 
	}
	
	private void updateOperation(ImagePack imagePack) {
		this.URL = imagePack.URL;
		this.myWidth = imagePack.width;
		this.myHeight = imagePack.height;
		this.leftX = imagePack.leftX;
		this.topY = imagePack.topY;
		this.layoutWidth = imagePack.layoutWidth;
		this.layoutHeight = imagePack.layoutHeight;
	}

	public String getURL() {
		return URL;
	}

	public int getWidth() {
		return myWidth;
	}
	
	public void setWidth(int width) {
		myWidth = width;
	}
	
	public int getHeight() {
		return myHeight;
	}
	
	public void setHeight(int height) {
		myHeight = height;
	}
	
	public double getLeft() {
		return leftX;
	}

	public double getTop() {
		return topY;
	}

	public PreloadedImage getImage() {
		return image;
	}

	@Override
	public String getID() {
		return imageEditorID;
	}

	public void setImage(PreloadedImage image2) {
		this.image = image2;
	}
}
