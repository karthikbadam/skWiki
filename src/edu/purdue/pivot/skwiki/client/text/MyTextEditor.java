package edu.purdue.pivot.skwiki.client.text;


import edu.purdue.pivot.skwiki.client.sketch.AttachedPanel;
import edu.purdue.pivot.skwiki.shared.DataPack;
public class MyTextEditor extends RichTextEditor implements AttachedPanel{

	
	private String textEditorID = "";
	String tag = "text";
	
	public MyTextEditor(String uuid, String uid)
	{
		super(uuid, uid);
		textEditorID = uuid;
	}
	
	public MyTextEditor(String uuid, String uid, float scaleWidth, float scaleHeight)
	{
		super(uuid, uid, scaleWidth, scaleHeight);
		textEditorID = uuid;
	}
	
	public void updateEditor(DataPack data)
	{
		//update the texteditor
		//setHtml(data.updateHtml);
		String key = textEditorID;
		setHtml(data.updateHtmlMap.get(key));
		System.out.println("text -"+data.updateHtmlMap.get(key));
		tag = data.textTaglMap.get(key);
		
	}
	
	public void getChange(DataPack pack)
	{
		//pack.updateHtml = getHtml();
		String key = textEditorID;
		pack.updateHtmlMap.put(key, getHtml());
		//pack.updateHtmlMap.put("1", "2");
		pack.textTaglMap.put(key, tag);
		
	}
	
	public String getEditorKey()
	{
		return textEditorID;
	}
	
	@Override
	public String getID()
	{
		return getEditorKey();
	}
	@Override
	public String toString()
	{
		return textEditorID;
	}
}
