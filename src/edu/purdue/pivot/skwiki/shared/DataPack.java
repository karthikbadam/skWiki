package edu.purdue.pivot.skwiki.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import edu.purdue.pivot.skwiki.shared.history.AbstractHistory;

public class DataPack implements Serializable {

	public Boolean isSuccess = false;
	
	public int[] dataArray;
	private int index = 0;
	public String comment = "";

	// ********** history function
	public boolean newID = false;
	public int updateRevision = 0;
	public int fromRevision = 0;
	public String fromUID = "";
	public String searchedTag = "";
	public String entity_key = "";

//**** project view
	public String projectName = "Some Project";
	public ProjectPack projectInfo = new ProjectPack();
	public UserPack userInfo = new UserPack();
	
	public ArrayList<String> projectsNameList = new ArrayList<String>();
	public ArrayList<String> userList = new ArrayList<String>();
	
	
//	public ArrayList<String> diffStrList = new
//			ArrayList<String>();
//	public ArrayList<Integer> diffOpList = new
//			ArrayList<Integer>();
	public int layoutHisotrySettleIndex = 0;
	public int nonWholeSequence_id = 0;
	public int maxnonWholeSequence_id = 0;

	
	
	// ********index for ending a part of list of revision
	public int historyIndex = 0;
	public String id = "";

	public String updateHtml = "";

	// ********new history update set for
	public HashMap<String, TextPack> updateHtmlMap;
	public HashMap<String, CanvasPack> updateCanvasMap;
	public HashMap<String, ImagePack> updateImageMap;
	public HashMap<String, String> canvasTagMap;
	public HashMap<String, String> textTaglMap;
	
	//to download all the data
	public ArrayList<DataPack> allData;
	public ArrayList<Integer> downloadRevisions;
	
	public ArrayList<AbstractLayoutHistory> layoutHistoryList;
	public ArrayList<RevisionHistory> revisionList;
	public ArrayList<SearchTagResult> searchTagList;

	public ArrayList<SearchTagResult> allRevList;

	public final static int SIZE = 100;
	// public ArrayList<String> updatedHistory = new ArrayList<String>();
	public ArrayList<AbstractHistory> updatedHistory = new ArrayList<AbstractHistory>();

	// public ArrayList<DataPackInside> updatedHistory = new
	// ArrayList<DataPackInside>();
	// AbstractHistory updatedHistory[] = new AbstractHistory

	public DataPack() {
		dataArray = new int[SIZE];
		layoutHistoryList = new ArrayList<AbstractLayoutHistory>();
		revisionList = new ArrayList<RevisionHistory>();
		allData = new ArrayList<DataPack>();
		
		/* tags */
		canvasTagMap = new HashMap<String,String>();
		textTaglMap = new HashMap<String,String>();
		searchTagList = new ArrayList<SearchTagResult>();
		allRevList = new ArrayList<SearchTagResult>();
		downloadRevisions = new ArrayList<Integer>();
		
		/* content: canvas and text */
		updateHtmlMap = new HashMap<String, TextPack>();
		updateImageMap = new HashMap<String, ImagePack>();
		updateCanvasMap = new HashMap<String,CanvasPack>();
		
	}

	public void addData(int i) {
		if (i > SIZE) {
			System.err.println("out of data pack boundary");
			return;
		}

		dataArray[index] = i;
		index++;
	}

	public void dataPlus1() {
		for (int i = 0; i < SIZE; i++) {
			dataArray[i] += 2;
		}

	}

	@Override
	public String toString() {
		String returnStr = "";
		for (int i = 0; i < SIZE; i++) {
			returnStr = returnStr + " " + dataArray[i];
		}
		return returnStr;
	}

}
