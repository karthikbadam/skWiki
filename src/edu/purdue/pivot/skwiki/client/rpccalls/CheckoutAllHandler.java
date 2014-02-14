package edu.purdue.pivot.skwiki.client.rpccalls;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import edu.purdue.pivot.skwiki.client.CheckoutAllServiceAsync;
import edu.purdue.pivot.skwiki.client.PreviewWidget;
import edu.purdue.pivot.skwiki.client.SkwikiEntryPoint;
import edu.purdue.pivot.skwiki.client.WidgetManager;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.RevisionHistory;

public class CheckoutAllHandler {

	ListBox revisionList;
	ArrayList<RevisionHistory> revisionHistory;
	WidgetManager widgets;
	PreviewWidget preview;
	CheckoutAllServiceAsync checkoutallService;
	TextBox Log;
	SkwikiEntryPoint skWiki;

	public CheckoutAllHandler(SkwikiEntryPoint skWiki,
			CheckoutAllServiceAsync checkoutallService, ListBox revisionList,
			ArrayList<RevisionHistory> revisionHistory, WidgetManager widgets,
			PreviewWidget preview, TextBox Log) {
		this.checkoutallService = checkoutallService;
		this.Log = Log;
		this.skWiki = skWiki;
		this.revisionHistory = revisionHistory;
		
	}
	
	
	public void sendToServer(ArrayList<Integer> selectedRevs,
			HashMap<Integer, PreviewWidget> previewWidgets2) {
		final ArrayList<Integer> selectedRevisions = selectedRevs;
		final HashMap<Integer, PreviewWidget> previewWidgets = previewWidgets2;
		
		DataPack packtoSend = new DataPack();
		packtoSend.downloadRevisions = selectedRevisions;
		if (packtoSend.updateRevision < 0) {
			//Window.alert("please a revision");
			return;
		}
		packtoSend.revisionList = revisionHistory;
		packtoSend.projectName = skWiki.current_project_name;
		
		try {
			// ServiceDefTarget endpoint = (ServiceDefTarget)
			// checkoutAllService;
			// PhonegapUtil.prepareService(endpoint,
			// "http://127.0.0.1:8080/Skwiki/skWiki/", "checkoutall");
			checkoutallService.checkoutAll(packtoSend,
					new AsyncCallback<DataPack>() {
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

							Log.setText("GWT Success: Successfully checked-out all the data at "
									+ dtf.format(date,
											TimeZone.createTimeZone(240)));
							// ******* update where this current revison is
							// from
							for (DataPack result_current: result.allData) {
								previewWidgets.get(result_current.fromRevision).createPreviewLayout(
									result_current.layoutHistoryList, result_current);
							}
						}
					});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

}
