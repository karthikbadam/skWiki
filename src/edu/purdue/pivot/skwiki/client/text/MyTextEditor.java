package edu.purdue.pivot.skwiki.client.text;

import java.util.ArrayList;

import edu.purdue.pivot.skwiki.client.sketch.AttachedPanel;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.Patch;
import edu.purdue.pivot.skwiki.shared.TextPack;

public class MyTextEditor extends RichTextEditor implements AttachedPanel {

	private String textEditorID = "";
	String tag = "text";
	private String nameHead = "Text";
	private int textEditorNumber = 0;
	ArrayList<Patch> patches = new ArrayList<Patch>();
	Patch tempPatch = new Patch();
	String oldHtml = "";

	public MyTextEditor(String uuid, String uid) {
		super(uuid, uid);
		textEditorID = uuid;
	}

	public MyTextEditor(String uuid, String uid, float scaleWidth,
			float scaleHeight) {
		super(uuid, uid, scaleWidth, scaleHeight);
		textEditorID = uuid;
	}

	public void commitOnSuccess(TextPack returnPack) {
		this.oldHtml = returnPack.updateHtml;
	}

	public void updateEditor(DataPack data) {
		String key = textEditorID;
		TextPack tempPack = (TextPack) data.updateHtmlMap.get(key);

		patches.clear();
		for (Patch tempPatch : tempPack.patches) {
			patches.add(tempPatch);
		}

		String updateText = patch_apply();
		oldHtml = updateText;
		setHtml(updateText);
		tag = data.textTaglMap.get(key);
	}

	public void newTempPatch() {
		tempPatch = new Patch();
	}

	public void addDiffToTempPatch(int operation, String text) {
		tempPatch.addDiff(operation, text);
	}

	private void addTempPatchToPatches() {
		patches.add(tempPatch);
	}

	public void getChange(DataPack pack) {

		String key = textEditorID;
		diff_main(oldHtml, getHtml());
		TextPack returnPack = new TextPack();
		returnPack.addPatch(tempPatch);
		returnPack.updateHtml = getHtml();
		pack.updateHtmlMap.put(key, returnPack);
		pack.textTaglMap.put(key, tag);
	}

	public int getSize() {
		return patches.size();
	}

	public int getDiffs_size(int patchIndex) {
		return patches.get(patchIndex).diffs.size();
	}

	public String getDiffText(int patchIndex, int diffIndex) {

		return patches.get(patchIndex).diffs.get(diffIndex).text;
	}

	public int getDiffOperation(int patchIndex, int diffIndex) {

		return patches.get(patchIndex).diffs.get(diffIndex).operation;
	}

	private native String patch_apply() /*-{
		var dmp = new $wnd.diff_match_patch();
		var patches = new Array();
		for ( var i = 0; i < this.@edu.purdue.pivot.skwiki.client.text.MyTextEditor::getSize()(); i++) {
			var tempPatch = new $wnd.diff_match_patch.patch_obj();
			for ( var j = 0; j < this.@edu.purdue.pivot.skwiki.client.text.MyTextEditor::getDiffs_size(I)(i); j++) {
				var text = this.@edu.purdue.pivot.skwiki.client.text.MyTextEditor::getDiffText(II)(i,j);
				var operation = this.@edu.purdue.pivot.skwiki.client.text.MyTextEditor::getDiffOperation(II)(i,j);
				var diff = [ operation, text ]
				tempPatch.diffs[j] = diff;
			}
			patches[i] = tempPatch
		}
		var returnText = dmp.patch_apply(patches, '');
		return returnText[0];
	}-*/;

	private native void diff_main(String text1, String text2) /*-{
																var dmp = new $wnd.diff_match_patch();
																var instance = this;
																var result = dmp.diff_main(text1, text2, false)
																instance.@edu.purdue.pivot.skwiki.client.text.MyTextEditor::newTempPatch()();
																for(var i= 0; i < result.length; i++)
																{
																//$wnd.alert(result[i]+" "+text1+" "+text2);
																this.@edu.purdue.pivot.skwiki.client.text.MyTextEditor::addDiffToTempPatch(ILjava/lang/String;)((result[i])[0], (result[i])[1]);
																}
																this.@edu.purdue.pivot.skwiki.client.text.MyTextEditor::addTempPatchToPatches()();
																var size = instance.@edu.purdue.pivot.skwiki.client.text.MyTextEditor::getSize()();
																//$wnd.alert(size);
																}-*/;
	
	public String getEditorKey() {
		return textEditorID;
	}

	@Override
	public String getID() {
		return getEditorKey();
	}

	@Override
	public String toString() {
		return textEditorID;
	}
}
