package edu.purdue.pivot.skwiki.client.rpccalls;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

import edu.purdue.pivot.skwiki.client.CheckoutServiceAsync;
import edu.purdue.pivot.skwiki.client.PreviewWidget;
import edu.purdue.pivot.skwiki.client.SkwikiEntryPoint;
import edu.purdue.pivot.skwiki.client.WidgetManager;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.RevisionHistory;

public class CheckoutHandler implements ClickHandler {

	ListBox revisionList;
	ArrayList<RevisionHistory> revisionHistory;
	WidgetManager widgets;
	PreviewWidget preview;
	CheckoutServiceAsync checkoutService;
	TextBox Log;
	SkwikiEntryPoint skWiki;

	public CheckoutHandler(SkwikiEntryPoint skWiki,
			CheckoutServiceAsync checkoutService, ListBox revisionList,
			ArrayList<RevisionHistory> revisionHistory, WidgetManager widgets,
			PreviewWidget preview, TextBox Log) {
		this.checkoutService = checkoutService;
		this.revisionList = revisionList;
		this.revisionHistory = revisionHistory;
		this.widgets = widgets;
		this.preview = preview;
		//this.entityList = entityList;
		this.Log = Log;
		this.skWiki = skWiki;
	}

	public void add_to_workspace() {
		sendToServer(false, revisionList.getSelectedIndex() + 1, preview, true);

	}

	PreviewWidget previewWidget;

	public void onClick(ClickEvent event, Boolean previewFlag) {
		sendToServer(previewFlag, revisionList.getSelectedIndex() + 1, preview);
	}

//	public void checkout(PreviewWidget previewWidget, int revision) {
//		ClickEvent event = null;
//		// revisionList.setSelectedIndex(revision - 1);
//		sendToServer(true, revision, previewWidget);
//		System.out.println(" Revision ID checkout for preview is " + revision);
//	}

	@Override
	public void onClick(ClickEvent event) {
		//Window.alert("Item count "+(revisionList.getSelectedIndex() + 1));
		sendToServer(false, revisionList.getSelectedIndex() + 1, preview);
	}

	public void sendToServer(Boolean previewFlg, int selectedRev,
			PreviewWidget previewWid) {
		final int selectedRevision = selectedRev;
		final PreviewWidget previewWidget = previewWid;
		final Boolean previewFlag = previewFlg;

		DataPack packtoSend = new DataPack();
		//Window.alert("Selected revision is: " + (revisionList.getSelectedIndex() + 1));
		int wantedRevision = revisionHistory.get(revisionList.getSelectedIndex()).getRevision();
		packtoSend.updateRevision = wantedRevision;// revisionList.getSelectedIndex()+1;
		packtoSend.projectName = skWiki.current_project_name;
		
		
		if(packtoSend.updateRevision<0)
		{
			//Window.alert("please a revision");
			return;
		}
		packtoSend.id = revisionHistory.get(revisionList.getSelectedIndex()	).getId();

		//TODO <chageAuto>
		//packtoSend.nonWholeSequence_id = skWiki.subrevisionList.getSelectedIndex();

		try {
			// ServiceDefTarget endpoint = (ServiceDefTarget)
			// checkoutService;
			// PhonegapUtil.prepareService(endpoint,
			// "http://127.0.0.1:8080/Skwiki/skWiki/", "checkout");
			checkoutService.checkout(packtoSend, new AsyncCallback<DataPack>() {
				@Override
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
					Log.setText("GWT ERROR: Failed to call server -- SERVER_ERROR"
							+ caught);
				}

				@Override
				public void onSuccess(DataPack result) {
					Date date = new Date();
					DateTimeFormat dtf = DateTimeFormat
							.getFormat("yyyy/MM/dd:HH:mm:ss");

					Log.setText("GWT Success: Successfully checked-out data at "
							+ dtf.format(date, TimeZone.createTimeZone(240)));
					// ******* update where this current revison is
					// from

					if (previewFlag == false) {
						// clean up all records
						//Window.alert(skWiki.fromRevision+","+selectedRev);
						Window.alert("Sketch Downloaded");
						skWiki.fromRevision = revisionList.getSelectedIndex() + 1;
						skWiki.fromUID = result.fromUID;
						// create new layout
						widgets.updateLayoutList(result.layoutHistoryList);
						preview.clearContent();
						widgets.clear();
						// create new layout
						widgets.updateLayoutTree(result.layoutHistoryList,
								result);
						// update the entities
						updateEntityList(result);
						widgets.updateEditors(result);
						widgets.shiftEntities();

					} else {
						System.out.println(" Path viewer response "
								+ selectedRevision);
						previewWidget.createPreviewLayout(
								result.layoutHistoryList, result);
					}
				}
			});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public void sendToServer(Boolean previewFlg, int selectedRev,
			PreviewWidget previewWid, Boolean copy2) {

		
		final int selectedRevision = selectedRev;
		final PreviewWidget previewWidget = previewWid;
		final Boolean previewFlag = previewFlg;

		DataPack packtoSend = new DataPack();
		//Window.alert("Selected revision is: " + (revisionList.getSelectedIndex() + 1));
		int wantedRevision = revisionHistory.get(revisionList.getSelectedIndex()).getRevision();
		packtoSend.updateRevision = wantedRevision;// revisionList.getSelectedIndex()+1;
		packtoSend.projectName = skWiki.current_project_name;
		
		
		if(packtoSend.updateRevision<0)
		{
			//Window.alert("please a revision");
			return;
		}
		packtoSend.id = revisionHistory.get(revisionList.getSelectedIndex()	).getId();

		final Boolean copy = copy2;		

		try {
			// ServiceDefTarget endpoint = (ServiceDefTarget)
			// checkoutService;
			// PhonegapUtil.prepareService(endpoint,
			// "http://127.0.0.1:8080/Skwiki/skWiki/", "checkout");
			checkoutService.checkout(packtoSend, new AsyncCallback<DataPack>() {
				@Override
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
					Log.setText("GWT ERROR: Failed to call server -- SERVER_ERROR"
							+ caught);
				}

				@Override
				public void onSuccess(DataPack result) {
					Date date = new Date();
					DateTimeFormat dtf = DateTimeFormat
							.getFormat("yyyy/MM/dd:HH:mm:ss");

					Log.setText("GWT Success: Successfully checked-out data at "
							+ dtf.format(date, TimeZone.createTimeZone(240)));
					// ******* update where this current revison is
					// from

					if (previewFlag == false) {
						// clean up all records
						preview.clearContent();
						if (!copy) {
							skWiki.fromRevision = revisionList.getSelectedIndex() + 1;
							skWiki.fromUID = result.fromUID;
							
							widgets.clear();
							// create new layout
							widgets.updateLayoutList(result.layoutHistoryList);
							
							
							widgets.updateLayoutTree(result.layoutHistoryList,
									result);
							// update the entities
							updateEntityList(result);
							widgets.updateEditors(result);
							widgets.shiftEntities();
						} else {
							widgets.updateLayoutTree2(result.layoutHistoryList,
									result);
						}

					} else {
						System.out.println(" Path viewer response "
								+ selectedRevision);
						previewWidget.createPreviewLayout(
								result.layoutHistoryList, result);
					}
				}
			});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	private void updateEntityList(DataPack result) {
//		for (MyTextEditor tempTextEditor : widgets.textEditors) {
//			entityList.addItem(tempTextEditor.toString());
//		}
//		// ******** commit all canvas editors
//
//		for (MyCanvasEditor tempCanvasEditor : widgets.canvasEditors) {
//			entityList.addItem(tempCanvasEditor.toString());
//		}
	}

}
