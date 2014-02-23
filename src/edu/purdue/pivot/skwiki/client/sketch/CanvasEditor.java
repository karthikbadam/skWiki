package edu.purdue.pivot.skwiki.client.sketch;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Label;
import com.kiouri.sliderbar.client.event.BarValueChangedEvent;
import com.kiouri.sliderbar.client.event.BarValueChangedHandler;

import edu.purdue.pivot.skwiki.shared.CanvasPack;
import edu.purdue.pivot.skwiki.shared.history.AbstractHistory;

public class CanvasEditor {

	private boolean preview = false;
	final Label valueBox = new Label("slidebar value");
	final int slideBarMax = 900;

	AnimationSlideBar myAnimationSliderBar = new AnimationSlideBar(slideBarMax,
			"120px", true);

	final ArrayList<AbstractHistory> returnHistoryList = new ArrayList<AbstractHistory>();

	LocalHistory myLocalHistory = new LocalHistory();

	int historyNewIndex = 0;
	int currentRevision = 0;
	int latestRevision = 0;
	int revisionListItemCount = 1;
	double currentSliderFactor = 1.00;

	/* index of this editor */
	String canvasEditorID;
	String idField;
	float scaleWidth = 1; 
	float scaleHeight = 1; 
	final TouchPad myPad;

	public CanvasEditor(String uuid, String uid, ArrayList<CanvasToolbar> toolbars, int windowWidth, int windowHeight) {
		this.idField = uid;
		this.canvasEditorID = uuid;
		myPad = new TouchPad(canvasEditorID, idField, toolbars, windowWidth, windowHeight);
	}

	// preview handler
	public CanvasEditor(String uuid, String uid, float scaleWidth, float scaleHeight, int windowWidth, int windowHeight) {
		this.idField = uid;
		this.canvasEditorID = uuid;
		myPad = new TouchPad(canvasEditorID, idField, scaleWidth, scaleHeight, windowWidth, windowHeight);
		preview = true;
		this.scaleWidth = scaleWidth;
		this.scaleHeight = scaleHeight;
	}

	public void updateCurrentRevision(int newRevision) {
		currentRevision = newRevision;
	}

	public TouchPad getSurface() {
		return myPad;
	}

	//Fill the canvas editor after check out 
	public void updateOperation(CanvasPack result) {

		if (preview) {
			myPad.scale(scaleWidth, scaleHeight);
		}
		myLocalHistory.returnHistoryList.clear();
		
		try {
		if (result.updatedHistory == null) {
			return;
		}
		} catch (Exception e) {
			System.out.println("exception --");
			return;
		}
		currentRevision = result.updateRevision;
		
		for (AbstractHistory tempHistory : result.updatedHistory) {
			myLocalHistory.returnHistoryList.add(tempHistory);
		}
		myPad.renewImage(myLocalHistory.returnHistoryList);
	}
	
	//Fill the canvas editor after check out 
	// TODO this is just a nasty tweak to do merge!
	public void updateOperation2(CanvasPack result) {
		myPad.renewImage(result.updatedHistory);
	}
	

	public CanvasPack commitData() {

		CanvasPack tempCanvasPack1 = new CanvasPack();

		tempCanvasPack1.id = idField.trim();
		tempCanvasPack1.updateRevision = currentRevision;

		if (currentRevision != 0) {
			tempCanvasPack1.historyIndex = myLocalHistory.returnHistoryList
					.get(myLocalHistory.returnHistoryList.size() - 1).historyNumber;

		} else {
			tempCanvasPack1.historyIndex = myLocalHistory.returnHistoryList
					.size();

		}

		historyNewIndex = myLocalHistory.returnHistoryList.size();

		myPad.updateDataPack(tempCanvasPack1, historyNewIndex);

		return tempCanvasPack1;

	}

	public void commitOnSuccess(CanvasPack result) {

		if (result.updatedHistory.size() == 0) {
			return;
		}

		myLocalHistory.commitFinishIndex = result.updatedHistory
				.get(result.updatedHistory.size() - 1).historyNumber;
		for (AbstractHistory tempHistory : result.updatedHistory) {
			returnHistoryList.add(tempHistory);
			myLocalHistory.returnHistoryList.add(tempHistory);

		}
		updateCurrentRevision(result.updateRevision);

	}

	public void checkoutOnSuccess(CanvasPack result) {

		myLocalHistory.returnHistoryList.clear();
		for (AbstractHistory tempHistory : result.updatedHistory) {
			// s returnHistoryList.add(tempHistory);
			myLocalHistory.returnHistoryList.add(tempHistory);

		}
		myPad.renewImage(myLocalHistory.returnHistoryList);
		currentRevision = result.updateRevision;
	}

}
