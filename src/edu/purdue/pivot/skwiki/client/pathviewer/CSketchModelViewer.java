package edu.purdue.pivot.skwiki.client.pathviewer;

import java.util.ArrayList;
import java.util.HashMap;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.ListBox;

import edu.purdue.pivot.skwiki.client.CheckoutAllService;
import edu.purdue.pivot.skwiki.client.CheckoutAllServiceAsync;
import edu.purdue.pivot.skwiki.client.PreviewWidget;
import edu.purdue.pivot.skwiki.client.SkwikiEntryPoint;
import edu.purdue.pivot.skwiki.client.rpccalls.CheckoutAllHandler;
import edu.purdue.pivot.skwiki.client.rpccalls.CheckoutHandler;
import edu.purdue.pivot.skwiki.shared.RevisionHistory;

public class CSketchModelViewer extends AbsolutePanel {
	
	
	CSketchModelViewer browser;
	// *********** checkout all
	public final CheckoutAllServiceAsync checkoutallService = GWT
			.create(CheckoutAllService.class);

	HashMap<String, Integer> columnNumMap = new HashMap<String, Integer>();
	HashMap<String, Integer> userColorKeyMap = new HashMap<String, Integer>();
	HashMap<String, String> userColorMap = new HashMap<String, String>();

	ArrayList<RevisionHistory> revisionHistory;

	DrawingArea canvas = new DrawingArea(10800, 17500);
	ArrayList<FocusPanel> thumbnailList = new ArrayList<FocusPanel>();

//	int Hspace = 300;
//	int Vspace = 170;

	HashMap<String, Integer> userRevisionCount = new HashMap<String, Integer>();

	ArrayList<String> colors = new ArrayList<String>();

	public CheckoutHandler checkoutHandler;
	String uid;
	ListBox revisionList;

	int windowWidth;
	int windowHeight;

	SkwikiEntryPoint skWiki;
	
	AbsolutePanel selectionMask;

	public CSketchModelViewer(SkwikiEntryPoint skWiki,
			ArrayList<RevisionHistory> revisionHistory,
			CheckoutHandler checkoutHandler, String uid, ListBox revisionList,
			int windowWidth, int windowHeight) {
		super();
		this.browser = this;
		this.checkoutHandler = checkoutHandler;
		this.setSize("10800px", "17500px");
		this.revisionHistory = revisionHistory;
		this.add(canvas);
		this.uid = uid;
		this.revisionList = revisionList;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.skWiki = skWiki;
		selectionMask = new AbsolutePanel();

		// standard color scale
		colors.add("#355FDE");
		colors.add("#FFA700");
		colors.add("#00C9A5");
		colors.add("#242424");

	}

	public void setPathViewer() {
		columnNumMap.clear();
		thumbnailList.clear();
		this.clear();
		this.add(canvas);
		canvas.clear();
		

		if (revisionHistory.size() == 0) {
			//Window.alert("please update revision list");
			return;
		}
		// setup the column
		boolean hasUID = false;
		for (RevisionHistory tempRevisionHistory : revisionHistory) {
			if (tempRevisionHistory.getId().equals(uid)) {
				hasUID = true;
				break;
			}
		}

		// set the first column
		int columnNumber = 0;
		if (hasUID) {
			columnNumMap.put(uid, Integer.valueOf(0));
			columnNumber++;
		}

		// set the rest
		ArrayList<Integer> fromRevisions = new ArrayList<Integer>();

		for (RevisionHistory tempRevisionHistory : revisionHistory) {
			if (columnNumMap.get(tempRevisionHistory.getId()) == null) {
				columnNumMap.put(tempRevisionHistory.getId(),
						Integer.valueOf(columnNumber));
				columnNumber++;
			}

			// find revisions which are leaf of the tree
			if (tempRevisionHistory.getFromRevision() != 0) {
				fromRevisions.add(tempRevisionHistory.getFromRevision());
			}
		}

		HashMap<Integer, PreviewWidget> previewWidgets = new HashMap<Integer, PreviewWidget>();

		ArrayList<String> ids = new ArrayList<String>();

		// create preview widget list
		ArrayList<Integer> downloadRevisions = new ArrayList<Integer>();

		int widgetWidth = windowWidth / 4;
		int widgetHeight = windowHeight / 4;
		
		//create the selection mask
		selectionMask.setWidth((widgetWidth -2)+"px");
		selectionMask.setHeight((widgetHeight -2)+"px");
		selectionMask.setStyleName("gwt-SelectionMask ");
		int index = 1;
		userRevisionCount.clear();

		for (RevisionHistory tempRevisionHistory : revisionHistory) {

			// user name
			if (!ids.contains(tempRevisionHistory.getId())) {
				Text titleText = new Text(widgetWidth / 2
						+ columnNumMap.get(tempRevisionHistory.getId())
						* (widgetWidth + 30), 20, tempRevisionHistory.getId());
				titleText.setFontFamily("Calibri");
				canvas.add(titleText);
				int key = ids.size();
				ids.add(tempRevisionHistory.getId());
				titleText.setStrokeColor(colors.get(key));
				titleText.setFontSize(18);
				userColorMap.put(tempRevisionHistory.getId(), colors.get(key));
				userColorKeyMap.put(tempRevisionHistory.getId(), key);
			}

			// make list of revisions to be downloaded
			if (!fromRevisions.contains(tempRevisionHistory.getRevision())) {
				downloadRevisions.add(tempRevisionHistory.getRevision());

				if (userRevisionCount.containsKey(tempRevisionHistory.getId())) {
					index = userRevisionCount.get(tempRevisionHistory.getId()) + 1;
					userRevisionCount.remove(tempRevisionHistory.getId());
					userRevisionCount.put(tempRevisionHistory.getId(), index);
				} else {
					index = 1;
					userRevisionCount.put(tempRevisionHistory.getId(), 1);
				}

				int posX = 30 + widgetWidth / 2
						+ columnNumMap.get(tempRevisionHistory.getId())
						* (widgetWidth + 30);
				int posY = 30 + widgetHeight / 2 + (index - 1)
						* (widgetHeight + 10);

				// adding thumbnail in the pathviewer
				PreviewWidget previewWidget = new PreviewWidget(widgetWidth,
						widgetHeight, (float) widgetWidth / windowWidth,
						(float) widgetHeight / windowHeight, windowWidth, windowHeight);
				previewWidgets.put(tempRevisionHistory.getRevision(),
						previewWidget);

				previewWidget
						.setStyleName("gwt-PreviewPanelWidget"
								+ (userColorKeyMap.get(tempRevisionHistory
										.getId()) + 1));
				
				
				//Create a D3 node !!
				
				//add nodes
				
				//set click handler 
				
				
				FocusPanel tempViewPanel = new FocusPanel();
				tempViewPanel.add(previewWidget);
				tempViewPanel.setSize((widgetWidth) + "px", (widgetHeight)
						+ "px");

				this.add(tempViewPanel, posX - widgetWidth / 2, posY
						- widgetHeight / 2);

				tempViewPanel
						.setTitle("" + (tempRevisionHistory.getRevision()));

				tempViewPanel.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						// node selected
						FocusPanel viewPanel = (FocusPanel) event.getSource();
						int revisionId = Integer.parseInt(viewPanel.getTitle());
						revisionList.setSelectedIndex(revisionId - 1);
						checkoutHandler.onClick(event);
						browser.add(selectionMask, viewPanel.getElement().getOffsetLeft()-1, viewPanel.getElement().getOffsetTop()-1);
						
					}
				});

				thumbnailList.add(tempViewPanel);
			}
		}

		// checkout all
		CheckoutAllHandler checkoutall = new CheckoutAllHandler(skWiki,
				checkoutallService, revisionList, revisionHistory,
				skWiki.widgets, skWiki.preview,  skWiki.Log);
		checkoutall.sendToServer(downloadRevisions, previewWidgets);

		// for (RevisionHistory tempRevisionHistory : revisionHistory) {
		// if (tempRevisionHistory.getFromRevision() != 0) {
		// ViewLabel fromLabel = labelList.get(tempRevisionHistory
		// .getFromRevision() - 1);
		// ViewLabel toLabel = labelList.get(tempRevisionHistory
		// .getRevision() - 1);
		// Connection tempConnection = new Connection(fromLabel, toLabel);// ,
		// // "Revision "+tempRevisionHistory.getRevision(),"comment");
		// tempConnection.append(canvas);
		//
		// }
		// }

	}
}
