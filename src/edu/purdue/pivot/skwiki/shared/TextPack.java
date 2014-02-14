package edu.purdue.pivot.skwiki.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class TextPack implements Serializable {

	public ArrayList<Patch> patches= new ArrayList<Patch>();
	public String id = "";
	public String updateHtml = "";
	
	public TextPack() {
	}
	
	public void addPatch(Patch patch) {
		patches.add(patch);
	}
}
