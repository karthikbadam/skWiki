package edu.purdue.pivot.skwiki.shared;

public class CreateEntityHistory extends AbstractLayoutHistory{
	
	private String parentName = "root";
	public EditorType editorType;
	public CreateEntityHistory()
	{
		
		
	}
	public CreateEntityHistory(String object, EditorType type)
	{
		super(object);
		editorType = type;
	}

}
