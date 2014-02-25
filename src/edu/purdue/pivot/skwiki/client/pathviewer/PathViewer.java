package edu.purdue.pivot.skwiki.client.pathviewer;

import java.util.ArrayList;
import java.util.HashMap;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ListBox;

import edu.purdue.pivot.skwiki.shared.RevisionHistory;
import edu.purdue.pivot.skwiki.client.SkwikiEntryPoint;
import edu.purdue.pivot.skwiki.client.rpccalls.CheckoutHandler;

public class PathViewer extends AbsolutePanel {

	HashMap<String, Integer> columnNumMap = new HashMap<String, Integer>();
	HashMap<String, Integer> userColorKeyMap = new HashMap<String, Integer>();
	HashMap<String, String> userColorMap = new HashMap<String, String>();
	ArrayList<RevisionHistory> revisionHistory;

	DrawingArea canvas = new DrawingArea(10800, 17500);
	ArrayList<ViewLabel> labelList = new ArrayList<ViewLabel>();
	int Hspace = 100;
	int Vspace = 50;
	
	ArrayList<String> colors = new ArrayList<String>();
	ArrayList<MyCircle> circles = new ArrayList<MyCircle>();

	public CheckoutHandler checkoutHandler;
	String uid;
	ListBox revisionList;

	int windowWidth;
	int windowHeight;
	SkwikiEntryPoint skWiki;

	public PathViewer(SkwikiEntryPoint skWiki,
			ArrayList<RevisionHistory> revisionHistory,
			CheckoutHandler checkoutHandler, String uid, ListBox revisionList,
			int windowWidth, int windowHeight) {
		super();
		this.checkoutHandler = checkoutHandler;
		this.setSize("1080px", "7500px");
		this.revisionHistory = revisionHistory;
		this.add(canvas);
		this.uid = uid;
		this.revisionList = revisionList;
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.skWiki = skWiki;
		
		
		// standard color scale
		colors.add("#355FDE");
		colors.add("#FFA700");
		colors.add("#00C9A5");
		colors.add("#242424");
		
	}

	public void setPathViewer() {

		columnNumMap.clear();
		labelList.clear();
		canvas.clear();

		if (revisionHistory.size() == 0) {
			//Window.alert("please update revision list");
			return;
		}
		
		this.setSize("1080px", 70*revisionHistory.size()+"px");
		
		// setup the column
		boolean hasUID = false;
		for (RevisionHistory tempRevisionHistory : revisionHistory) {
			if (tempRevisionHistory.getId().equals(uid)) {
				// nameList.add(uid);
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
		for (RevisionHistory tempRevisionHistory : revisionHistory) {
			if (columnNumMap.get(tempRevisionHistory.getId()) == null) {
				// nameList.add(uid);
				columnNumMap.put(tempRevisionHistory.getId(),
						Integer.valueOf(columnNumber));
				columnNumber++;
			}
		}

		int leftBackOffset = 20;
		int rowCount = 0;

		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<Integer> fromRevisions = new ArrayList<Integer>();

		for (RevisionHistory tempRevisionHistory : revisionHistory) {
			if (ids.indexOf(tempRevisionHistory.getId()) == -1) {
				Text titleText = new Text((columnNumMap.get(tempRevisionHistory
						.getId()) + 1) * Hspace - 2 * leftBackOffset, 20,
						tempRevisionHistory.getId());
				titleText.setFontFamily("Calibri");
				canvas.add(titleText);
				int key = ids.size();
				ids.add(tempRevisionHistory.getId());
				
				if (key < 4) {
					titleText.setStrokeColor(colors.get(key));
				} else {
					titleText.setStrokeColor("#000000");
				}
				titleText.setFontSize(18);
				if (key < 4) {
					userColorMap.put(tempRevisionHistory.getId(), colors.get(key));
					userColorKeyMap.put(tempRevisionHistory.getId(), key);
				} else {
					userColorMap.put(tempRevisionHistory.getId(), "#000000");
					userColorKeyMap.put(tempRevisionHistory.getId(), key);
				}
			}

			if (tempRevisionHistory.getFromRevision() != 0) {
				fromRevisions.add(tempRevisionHistory.getFromRevision());
			}

			ViewLabel tempViewLabel = new ViewLabel(
					(columnNumMap.get(tempRevisionHistory.getId()) + 1) * Hspace
							- leftBackOffset, (rowCount + 1) * Vspace,
					"Revision " + tempRevisionHistory.getRevision(),
					tempRevisionHistory.getComment(),
					tempRevisionHistory.getRevision());

			circles.add(tempViewLabel.getCircle());
			tempViewLabel.getCircle().setColor(userColorMap.get(tempRevisionHistory.getId()));
			tempViewLabel.setColor(userColorMap.get(tempRevisionHistory.getId()));

			tempViewLabel.getCircle().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					// unhighlight all circles
					for (MyCircle circle : circles) {
						circle.unHighlight();
					}
					
					// Node selected
					MyCircle circle = (MyCircle) event.getSource();
					revisionList.setSelectedIndex(circle.getIndex() - 1);

					// highlight selected node
					circle.highlight();

					// handler checkout
					checkoutHandler.onClick(event, true);
				}
			});
			labelList.add(tempViewLabel);
			rowCount++;
		}

		
		for (RevisionHistory tempRevisionHistory : revisionHistory) {
			if (tempRevisionHistory.getFromRevision() != 0) {
				ViewLabel fromLabel = labelList.get(tempRevisionHistory
						.getFromRevision() - 1);
				ViewLabel toLabel = labelList.get(tempRevisionHistory
						.getRevision() - 1);
				Connection tempConnection = new Connection(fromLabel, toLabel);
				
				tempConnection.append(canvas);
				
			}
		}
		
		for (ViewLabel tempLabel : labelList) {
			tempLabel.appendTo(canvas);
		}

	}
}
