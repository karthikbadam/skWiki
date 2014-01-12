package edu.purdue.pivot.skwiki.client.pathviewer;

import java.util.ArrayList;
import java.util.HashMap;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Text;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.ListBox;

import edu.purdue.pivot.skwiki.client.CheckoutAllService;
import edu.purdue.pivot.skwiki.client.CheckoutAllServiceAsync;
import edu.purdue.pivot.skwiki.client.Global;
import edu.purdue.pivot.skwiki.client.PreviewWidget;
import edu.purdue.pivot.skwiki.client.SkwikiEntryPoint;
import edu.purdue.pivot.skwiki.client.rpccalls.CheckoutAllHandler;
import edu.purdue.pivot.skwiki.client.rpccalls.CheckoutHandler;
import edu.purdue.pivot.skwiki.shared.RevisionHistory;

public class SkwikiPathViewer extends AbsolutePanel {

	SkwikiPathViewer browser;

	// *********** checkout all
	public final CheckoutAllServiceAsync checkoutallService = GWT
			.create(CheckoutAllService.class);

	HashMap<String, Integer> columnNumMap = new HashMap<String, Integer>();
	HashMap<String, Integer> userColorKeyMap = new HashMap<String, Integer>();
	HashMap<String, String> userColorMap = new HashMap<String, String>();
	HashMap<Integer, String> revisionUserMap = new HashMap<Integer, String>();
	HashMap<Integer, String> revisionNextUserMap = new HashMap<Integer, String>();

	ArrayList<RevisionHistory> revisionHistory;

	DrawingArea canvas = new DrawingArea(10800, 17500);
	ArrayList<FocusPanel> thumbnailList = new ArrayList<FocusPanel>();

	ArrayList<Integer> blacklist = new ArrayList<Integer>();
	int Hspace = 300;
	int Vspace = 170;

	HashMap<String, Integer> userRevisionCount = new HashMap<String, Integer>();

	public CheckoutHandler checkoutHandler;
	String uid;
	ListBox revisionList;

	int windowWidth;
	int windowHeight;

	SkwikiEntryPoint skWiki;
	ArrayList<String> colors = new ArrayList<String>();

	AbsolutePanel selectionMask;

	public SkwikiPathViewer(SkwikiEntryPoint skWiki,
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

		// filling the list of colors for the users
		for (int i = 0; i < Global.MAX_NUMBER_OF_USERS; i++) {
			colors.add(Global.COLORSCALE.get(i % Global.COLORSCALE.size()));
		}
	}

	public void setPathViewer() {
		columnNumMap.clear();
		thumbnailList.clear();
		this.clear();
		this.add(canvas);
		canvas.clear();

		if (revisionHistory.size() == 0) {
			Window.alert("please update revision list");
			return;
		}

		// setup the column
		boolean hasUID = false;
		for (RevisionHistory temp_revision_history : revisionHistory) {
			if (temp_revision_history.getId().equals(uid)) {
				hasUID = true;
				break;
			}

			// revision id - user map
			int revision = temp_revision_history.getRevision();
			String user = temp_revision_history.getId();
			revisionUserMap.put(revision, user);
		}

		// set the first column to the current user
		int column_number = 0;
		if (hasUID) {
			columnNumMap.put(uid, Integer.valueOf(0));
			column_number++;
		}

		// set the rest of the columns in the pathviewer
		ArrayList<Integer> from_revisions = new ArrayList<Integer>();

		for (RevisionHistory temp_revision_history : revisionHistory) {
			if (columnNumMap.get(temp_revision_history.getId()) == null) {
				// nameList.add(uid);
				columnNumMap.put(temp_revision_history.getId(),
						Integer.valueOf(column_number));
				column_number++;
			}

			// find revisions which are leaf of the tree
			if (temp_revision_history.getFromRevision() != 0) {
				from_revisions.add(temp_revision_history.getFromRevision());
				String user = temp_revision_history.getId();

				int from_revision = temp_revision_history.getFromRevision();

				// can have duplicate entries but we can neglect them for now
				revisionNextUserMap.put(from_revision, user);
			}
		}

		HashMap<Integer, PreviewWidget> preview_widgets = new HashMap<Integer, PreviewWidget>();

		ArrayList<String> ids = new ArrayList<String>();

		// create preview widget list
		ArrayList<Integer> downloadRevisions = new ArrayList<Integer>();

		int widgetWidth = windowWidth / 4;
		int widgetHeight = windowHeight / 4;

		// create the selection mask
		selectionMask.setWidth((widgetWidth - 2) + "px");
		selectionMask.setHeight((widgetHeight - 2) + "px");
		selectionMask.setStyleName("gwt-SelectionMask ");

		int index = 1;
		userRevisionCount.clear();

		for (RevisionHistory temp_revision_history : revisionHistory) {

			// user name
			if (!ids.contains(temp_revision_history.getId())) {
				Text titleText = new Text(widgetWidth / 2
						+ columnNumMap.get(temp_revision_history.getId())
						* (widgetWidth + 30), 15, temp_revision_history.getId());
				titleText.setFontFamily("Calibri");
				canvas.add(titleText);

				int key = ids.size();
				ids.add(temp_revision_history.getId());

				// titleText.setFillColor("#FF0000");
				if (key < 4) {
					titleText.setStrokeColor(colors.get(key));
				} else {
					titleText.setStrokeColor("#000000");
				}

				titleText.setFontSize(18);
				if (key < 4) {
					userColorMap.put(temp_revision_history.getId(),
							colors.get(key));
					userColorKeyMap.put(temp_revision_history.getId(), key);
				} else {
					userColorMap.put(temp_revision_history.getId(), "#000000");
					userColorKeyMap.put(temp_revision_history.getId(), key);
				}
			}

			// make list of revisions to be downloaded
			String currentUser = temp_revision_history.getId();
			// Show branches from other users!
			String nextUser = revisionNextUserMap.get(temp_revision_history
					.getRevision());

			if ((!from_revisions.contains(temp_revision_history.getRevision()) || currentUser != nextUser)
					&& !blacklist.contains(temp_revision_history.getRevision())) {

				downloadRevisions.add(temp_revision_history.getRevision());

				if (userRevisionCount.containsKey(temp_revision_history.getId())) {
					index = userRevisionCount.get(temp_revision_history.getId()) + 1;
					userRevisionCount.remove(temp_revision_history.getId());
					userRevisionCount.put(temp_revision_history.getId(), index);
				} else {
					index = 1;
					userRevisionCount.put(temp_revision_history.getId(), 1);
				}

				int posX = 30 + widgetWidth / 2
						+ columnNumMap.get(temp_revision_history.getId())
						* (widgetWidth + 30);
				int posY = 30 + widgetHeight / 2 + (index - 1)
						* (widgetHeight + 10);

				// adding thumbnail in the pathviewer
				PreviewWidget previewWidget = new PreviewWidget(widgetWidth,
						widgetHeight, (float) widgetWidth / windowWidth,
						(float) widgetHeight / windowHeight, windowWidth,
						windowHeight);
				preview_widgets.put(temp_revision_history.getRevision(),
						previewWidget);
				if (userColorKeyMap.get(temp_revision_history.getId()) < 4) {
					previewWidget
							.setStyleName("gwt-PreviewPanelWidget"
									+ (userColorKeyMap.get(temp_revision_history
											.getId()) + 1));
				} else {
					previewWidget.setStyleName("gwt-PreviewPanelWidget");
				}
				FocusPanel tempViewPanel = new FocusPanel();
				tempViewPanel.add(previewWidget);
				tempViewPanel.setSize((widgetWidth) + "px", (widgetHeight)
						+ "px");
				this.add(tempViewPanel, posX - widgetWidth / 2, posY
						- widgetHeight / 2);

				tempViewPanel
						.setTitle("" + (temp_revision_history.getRevision()));

				tempViewPanel.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						// node selected
						FocusPanel viewPanel = (FocusPanel) event.getSource();
						int revisionId = Integer.parseInt(viewPanel.getTitle());
						revisionList.setSelectedIndex(revisionId - 1);
						checkoutHandler.onClick(event);
						browser.add(selectionMask, viewPanel.getElement()
								.getOffsetLeft() - 1, viewPanel.getElement()
								.getOffsetTop() - 1);

					}
				});

				thumbnailList.add(tempViewPanel);
			}
		}

		// checkout all
		CheckoutAllHandler checkoutall = new CheckoutAllHandler(skWiki,
				checkoutallService, revisionList, revisionHistory,
				skWiki.widgets, skWiki.preview, skWiki.Log);
		checkoutall.sendToServer(downloadRevisions, preview_widgets);
	}

}
