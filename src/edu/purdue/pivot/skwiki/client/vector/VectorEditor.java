package edu.purdue.pivot.skwiki.client.vector;


public class VectorEditor {
	final String idField;
	int index;
	private VectorPanel panel = new VectorPanel(400, 400);
	
	public VectorEditor(String id, int index){
		this.index = index;
		this.idField = id;
	}
	
	public VectorPanel getVectorCanvas(){
		return panel;
	}
}
