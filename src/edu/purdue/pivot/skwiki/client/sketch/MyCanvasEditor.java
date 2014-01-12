package edu.purdue.pivot.skwiki.client.sketch;

import java.util.ArrayList;

import edu.purdue.pivot.skwiki.shared.CanvasPack;
import edu.purdue.pivot.skwiki.shared.DataPack;

public class MyCanvasEditor extends CanvasEditor implements AttachedPanel{
	
	private String canvasEditorID = "";
	private String tag = "canvas";
	
	
	
	public MyCanvasEditor(String uuid, String uid, ArrayList<CanvasToolbar> toolbars, int windowWidth, int windowHeight)
	{
		super(uuid, uid, toolbars, windowWidth, windowHeight);
		this.canvasEditorID = uuid;
		
	}
	
	public MyCanvasEditor(String uuid, String uid, float scaleWidth, float scaleHeight, int windowWidth, int windowHeight) {
		super(uuid, uid, scaleWidth, scaleHeight, windowWidth, windowHeight);
		this.canvasEditorID = uuid;
	}
	
	
	public void updateEditor(DataPack data)
	{
		String key = canvasEditorID;
		updateOperation(data.updateCanvasMap.get(key));
		tag = data.canvasTagMap.get(key);

	}
	
	
	// TODO this is just a nasty tweak to do merge!
	public void updateEditor(CanvasPack data, String tag)
	{
		// setCanvas
		updateOperation2(data);
		this.tag = tag;
	}
	
	public void getChange(DataPack data)
	{
		String key = canvasEditorID;
		CanvasPack tempCanvasPack = commitData();
		tempCanvasPack.id = getID();
		data.updateCanvasMap.put(key, tempCanvasPack);
		data.canvasTagMap.put(key, tag);

	}
	
	@Override
	public String getID()
	{
		return canvasEditorID;
	}

	@Override
	public String toString()
	{
		return canvasEditorID;
	}
}
