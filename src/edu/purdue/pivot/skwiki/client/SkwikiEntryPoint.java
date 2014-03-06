package edu.purdue.pivot.skwiki.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Label;
//import com.googlecode.gwtphonegap.client.PhoneGap;
//import com.googlecode.gwtphonegap.client.PhoneGapAvailableEvent;
//import com.googlecode.gwtphonegap.client.PhoneGapAvailableHandler;
//import com.googlecode.gwtphonegap.client.PhoneGapTimeoutEvent;
//import com.googlecode.gwtphonegap.client.PhoneGapTimeoutHandler;
import edu.purdue.pivot.skwiki.client.image.MyImageViewer;
import edu.purdue.pivot.skwiki.client.pathviewer.PathViewer;
import edu.purdue.pivot.skwiki.client.pathviewer.PathViewerFullScreen;
import edu.purdue.pivot.skwiki.client.rpccalls.CheckoutHandler;
import edu.purdue.pivot.skwiki.client.sketch.MyCanvasEditor;
import edu.purdue.pivot.skwiki.client.text.MyTextEditor;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.ProjectPack;
import edu.purdue.pivot.skwiki.shared.RevisionHistory;
import edu.purdue.pivot.skwiki.shared.SearchTagResult;
import edu.purdue.pivot.skwiki.shared.UUID;
import edu.purdue.pivot.skwiki.shared.UserPack;

import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class SkwikiEntryPoint implements EntryPoint {

	int latestRevision = 0;
	int revisionListItemCount = 1;

	final ListBox revisionList = new ListBox();
	public final ListBox subrevisionList = new ListBox();

	// cookies
	public static final int COOKIE_EXPIRE_DAYS = 3;
	public static final long MILLISECS_PER_DAY = 1000L * 60L * 60L * 24L;

	private String 	uid = "";
	public String fromUID = "";
	public String current_project_name = "postchi_testing";

	// from revision
	public int fromRevision = 0;

	// Panels
	RootPanel rootPanel;
	ScrollPanel wikiPanel;
	public PreviewWidget preview;

	String name;
	public TextBox Log = new TextBox();

	// Last save
	Date lastTime = new Date();
	public final static long SECOND_MILLIS = 1000;
	public final static long MINUTE_MILLIS = SECOND_MILLIS * 60;

	// Widget manager
	public WidgetManager widgets;

	// Tag system
	private ToggleButton tagViewButton = new ToggleButton("TagViewer",
			"Editor Panel");
	private ToggleButton maskOnButton = new ToggleButton("Filter off",
			"Filter on");

	final ArrayList<RevisionHistory> revisionHistory = new ArrayList<RevisionHistory>();

	private Button getAllRevButton = new Button("Entity Revisions");
	final ArrayList<SearchTagResult> allRevHistory = new ArrayList<SearchTagResult>();

	final TagViewer tagViewer = new TagViewer();
	// public final ListBox entityList = new ListBox();
	public ListGrid entityList = new ListGrid();

	final ListBox allRevList = new ListBox();

	// pathviewer
	PathViewer pathViewer;
	PathViewerFullScreen pathViewer2;

	// comment box
	final TextBox commentText = new TextBox();
	final TextBox tagText = new TextBox();

	// commit
	private final CommitServiceAsync commitService = GWT
			.create(CommitService.class);

	// checkout
	public final CheckoutServiceAsync checkoutService = GWT
			.create(CheckoutService.class);

	// checkID
	private final CheckIDServiceAsync checkIDService = GWT
			.create(CheckIDService.class);

	// search tag
	private final SearchTagServiceAsync searchTagService = GWT
			.create(SearchTagService.class);

	// search all rev
	private final GetAllRevServiceAsync getAllRevService = GWT
			.create(GetAllRevService.class);

	// create Project
	private final CreateProjectServiceAsync createProjectRevService = GWT
			.create(CreateProjectService.class);

	private final CommitHandler myCommitHandler = new CommitHandler();
	private CheckoutHandler myCheckoutHandler;

	// get cookie!
	private String getCookieName() {
		if (Cookies.getCookie("name") == null)
			return getUsername();
		else
			return Cookies.getCookie("name");
	}

	private void setCookieName(String name) {
		Date now = new Date();
		long nowLong = now.getTime();
		nowLong = nowLong + COOKIE_EXPIRE_DAYS * MILLISECS_PER_DAY;// three days
		now.setTime(nowLong);
		Cookies.setCookie("name", name, now);
	}

	private native String getUsername()/*-{
		return $wnd.jQuery("#username-field-value").val();
	}-*/;

	private native String getPassword()/*-{
		return $wnd.jQuery("#password-field-value").val();
	}-*/;

	private native String getRetypePassword()/*-{
		return $wnd.jQuery("#newuser-password-field-value").val();
	}-*/;

	private native void vanishLoginScreen() /*-{
		$wnd.jQuery("#container").fadeOut("slow");
		$wnd.jQuery("#header-options").fadeIn(300);
	}-*/;

	private native void appearLoginScreen() /*-{
		$wnd.jQuery("#container").fadeIn(300);
		$wnd.jQuery("#header-options").fadeOut("slow");
		$wnd.jQuery("#project-viewer").fadeOut("slow");
	}-*/;

	private native void displayProjectViewer(String uid) /*-{
		$wnd.jQuery("#project-viewer").fadeIn(300);
		$wnd.jQuery("#skwiki-viewer").fadeOut("slow");
		$wnd.jQuery("#project-viewer-username-text").text("Hi " + uid);
	}-*/;

	private native void displaySkwikiViewer() /*-{
		$wnd.jQuery("#skwiki-viewer").fadeIn(300);
		$wnd.jQuery("#project-viewer").fadeOut("slow");
	}-*/;

	private native boolean checkLoginExistingUser() /*-{
		if ($wnd.jQuery(".login").text() === "SKWIKI LOGIN") {
			return true;
		} else if ($wnd.jQuery(".login").text() === "SKWIKI NEW USER LOGIN") {
			return false;
		}
	}-*/;

	private native void attachProjectBrowseHandler(SkwikiEntryPoint _self) /*-{
		$wnd
				.jQuery("#options-button")
				.click(
						function() {
							if ($wnd.jQuery("#options-button-text").text() === "All Projects") {
								_self.@edu.purdue.pivot.skwiki.client.SkwikiEntryPoint::showProjectDetails()();
								$wnd.jQuery("#options-button-text").text(
										"skWiki View");
							} else if ($wnd.jQuery("#options-button-text")
									.text() === "skWiki View") {
								$wnd.jQuery("#skwiki-viewer").fadeIn(300);
								$wnd.jQuery("#project-viewer").fadeOut("slow");
								$wnd.jQuery("#options-button-text").text(
										"All Projects");
							}
						});
	}-*/;

	private native void attachNewProjectCreateHandler(SkwikiEntryPoint _self) /*-{
		$wnd
				.jQuery("#new-project-form-submit")
				.click(
						function() {
							_self.@edu.purdue.pivot.skwiki.client.SkwikiEntryPoint::newProjectHandler()();
							$wnd.jQuery("#new-project-form").fadeOut(500);
						});
	}-*/;

	public native String newProjectName() /*-{
		return $wnd.jQuery("#new-project-name").val().toLowerCase();
	}-*/;

	public native String newProjectDetails() /*-{
		return $wnd.jQuery("#new-project-details").val();
	}-*/;

	public void showProjectDetails() {
		displayProjectViewer(uid);
		RootPanel userpanel = RootPanel.get("project-viewer-user-list");
		userpanel.clear();
		RootPanel projectPanel = RootPanel.get("project-viewer-project-list");
		projectPanel.clear();
		showUserList();
		showProjectList();
	}

	public void newProjectHandler() {
		final String projectName = newProjectName();
		String projectDescription = newProjectDetails();

		DataPack packtoSend = new DataPack();

		ProjectPack tempProjecPack1 = new ProjectPack(projectName,
				projectDescription);
		packtoSend.projectInfo = tempProjecPack1;
		packtoSend.id = uid;

		try {
			createProjectRevService.createProject(packtoSend,
					new AsyncCallback<DataPack>() {
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
						}

						public void onSuccess(DataPack result) {
							if (result.isSuccess == true) {
								System.out.println("created new project");
							}
							current_project_name = projectName;
							// Console.log("user"+result.projectInfo.projectNameList.size());
						}
					});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public void loginClickHandler() {
		if (getUsername().equals("") || getPassword().equals("")) {
			Window.alert("Wrong User name or Password");
			return;
		}
		String username = getUsername();
		String password = getPassword();

		if (checkLoginExistingUser() == true) {
			loginExistingUser(username, password);

		} else {
			String retypedPassword = getRetypePassword();
			if (password == retypedPassword) {
				loginNewUser(username, password);
			} else {
				Window.alert("Password Mismatch");
				return;
			}
		}

	}

	public Boolean loginNewUser(String username, String password) {

		DataPack packtoSend = new DataPack();
		packtoSend.projectName = "ME444";

		UserPack tempProjectPack = new UserPack(username, password);
		packtoSend.userInfo = tempProjectPack;

		final String username2 = username;
		try {
			createProjectRevService.createUser(packtoSend,
					new AsyncCallback<DataPack>() {
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							Window.alert("Login failed, please try again later.");
						}

						public void onSuccess(DataPack result) {
							if (result.isSuccess == true) {
								setCookieName(username2);
								uid = username2;
								fromUID = uid;
								vanishLoginScreen();
								onModuleLoad2();
							} else {
								Window.alert("Login failed, please try again later.");
							}
						}
					});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return true;
	}

	public Boolean loginExistingUser(String username, String password) {

		DataPack packtoSend = new DataPack();
		packtoSend.projectName = "postchi_testing";

		UserPack tempProjectPack = new UserPack(username, password);
		packtoSend.userInfo = tempProjectPack;

		final String username2 = username;
		try {
			createProjectRevService.authenticate(packtoSend,
					new AsyncCallback<DataPack>() {
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							Window.alert("Login failed, please try again later.");
						}

						public void onSuccess(DataPack result) {
							if (result.userInfo.authSuccess == true) {
								setCookieName(username2);
								uid = username2;
								fromUID = uid;
								vanishLoginScreen();
								onModuleLoad2();
							} else {
								Window.alert("Login failed, Wrong username or password.");
							}
						}
					});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return true;
	}

	public Boolean showProjectList() {
		DataPack packtoSend = new DataPack();
		try {
			createProjectRevService.getAllProjectList(packtoSend,
					new AsyncCallback<DataPack>() {
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							Window.alert("Failed to load user list");
						}

						public void onSuccess(DataPack result) {
							ArrayList<String> projectList = result.projectInfo.projectNameList;
							ArrayList<String> projectDescList = result.projectInfo.projectDescripList;

							RootPanel projectPanel = RootPanel
									.get("project-viewer-project-list");

							final ListGrid listGrid = new ListGrid();
							listGrid.setWidth(projectPanel.getOffsetWidth());
							listGrid.setHeight(projectPanel.getOffsetHeight());

							ListGridField projectField = new ListGridField(
									"projectNames", "Projects", projectPanel
											.getOffsetWidth() / 2);
							projectField.setAlign(Alignment.CENTER);

							ListGridField descField = new ListGridField(
									"projectDesc", "Description", projectPanel
											.getOffsetWidth() / 2);
							descField.setAlign(Alignment.CENTER);

							listGrid.setFields(projectField, descField);
							listGrid.draw();

							ListGridRecord[] records = new ListGridRecord[projectList
									.size()];

							for (int index = 0; index < projectList.size(); index++) {
								ListGridRecord projectRecord = new ListGridRecord();
								projectRecord.setAttribute("projectNames",
										projectList.get(index));
								projectRecord.setAttribute("projectDesc",
										projectDescList.get(index));

								records[index] = projectRecord;
							}

							listGrid.setData(records);
							projectPanel.add(listGrid);
						}
					});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return true;
	}

	public Boolean showUserList() {
		DataPack packtoSend = new DataPack();
		packtoSend.projectName = current_project_name;
		packtoSend.updateRevision = fromRevision;

		try {
			createProjectRevService.getAllUserList(packtoSend,
					new AsyncCallback<DataPack>() {
						public void onFailure(Throwable caught) {
							// Show the RPC error message to the user
							Window.alert("Failed to load user list");
						}

						public void onSuccess(DataPack result) {
							ArrayList<String> userList = result.userInfo.userList;
							RootPanel userPanel = RootPanel
									.get("project-viewer-user-list");

							final ListGrid listGrid = new ListGrid();
							listGrid.setWidth(userPanel.getOffsetWidth());
							listGrid.setHeight(userPanel.getOffsetHeight());

							ListGridField userField = new ListGridField(
									"userNames", "Users", userPanel
											.getOffsetWidth());
							userField.setAlign(Alignment.CENTER);

							listGrid.setFields(userField);
							listGrid.draw();

							ListGridRecord[] records = new ListGridRecord[userList
									.size()];
							// create List of users

							int index = 0;
							for (String user : userList) {
								ListGridRecord userId = new ListGridRecord();
								userId.setAttribute("userNames", user);
								records[index] = userId;
								index++;
							}

							listGrid.setData(records);
							userPanel.add(listGrid);
						}
					});
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return true;
	}

	public void logoutClickHandler() {
		Cookies.removeCookie("name");

		uid = "";
		fromRevision = 0;
		revisionHistory.clear();
		revisionList.clear();
		widgets.clear();
		isLoggedIn = false;
		appearLoginScreen();
		rootPanel.clear();
	}

	private static native void attachLoginHandler(SkwikiEntryPoint _self) /*-{
		$wnd
				.jQuery("#submit-button")
				.click(
						function() {
							_self.@edu.purdue.pivot.skwiki.client.SkwikiEntryPoint::loginClickHandler()();
						});
	}-*/;

	private static native void attachLogoutHandler(SkwikiEntryPoint _self) /*-{
		$wnd
				.jQuery("#logout-button")
				.click(
						function() {
							_self.@edu.purdue.pivot.skwiki.client.SkwikiEntryPoint::logoutClickHandler()();
						});
	}-*/;

	@Override
	public void onModuleLoad() {
		attachLoginHandler(this);
		attachLogoutHandler(this);
		attachProjectBrowseHandler(this);
		attachNewProjectCreateHandler(this);

		// get the cookie
		uid = getCookieName();
		fromUID = uid;
		if (!uid.equals("")) {
			isLoggedIn = true;
		}

		// show login page
		if (isLoggedIn == false) {

		} else {
			vanishLoginScreen();
			onModuleLoad2();
		}
	}

	Boolean isLoggedIn = false;

	// settings panels on UI
	FlexTable menuPanel;
	VLayout navigationPanel;

	public void onModuleLoad2() {
		// tagviewer
		tagViewButton.addClickHandler(new TagViewerToggle());

		// root Panel
		rootPanel = RootPanel.get("skwiki-viewer");
		// rootPanel.setStylePrimaryName("gwt-RootPanelWidget");
		final int windowWidth = Window.getClientWidth() - 20;
		final int windowHeight = Window.getClientHeight() - 80;
		// rootPanel.setSize(windowWidth + "px", windowHeight + "px");

		// main panel
		wikiPanel = new ScrollPanel();
		wikiPanel.setStyleName("gwt-PanelWidget");
		// wikiPanel.setSize(windowWidth + "px", windowHeight + "px");
		rootPanel.add(wikiPanel);

		// Widget Manager
		widgets = new WidgetManager(wikiPanel, windowWidth, windowHeight, uid,
				entityList);

		// create options panel on the left
		menuPanel = new FlexTable();
		menuPanel.setStyleName("gwt-MenuPanel");
		// save option to save current
		ImgButton save_button = new ImgButton();
		save_button.setWidth(48);
		save_button.setHeight(48);
		save_button.setLabelHPad(5);
		save_button.setLabelVPad(10);

		save_button.setStyleName("gwt-ButtonWidget");
		save_button.setSrc("save.png");
		save_button.setShadowDepth(1);
		menuPanel.setWidget(0, 0, save_button);

		save_button
				.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						// Window.alert("Save Option Clicked");
						SC.confirm("Sketch will be saved on the server",
								new BooleanCallback() {
									@Override
									public void execute(Boolean value) {
										if (value != null && value) {
											myCommitHandler.onClick(null);
										} else {

										}
									}
								});

					}

				});

		// // Send the page as a link
		// Image link_image = new Image("images/link.png");
		// Button send_link = new Button();
		// send_link.getElement().appendChild(link_image.getElement());
		// optionsPanel.add(send_link);
		// send_link.setStyleName("gwt-ButtonWidget");
		// send_link.setSize(65 + "px", 65 + "px");
		// menuPanel.setWidget(1, 0, send_link);

		// Drawing canvas
		ImgButton canvas_button = new ImgButton();
		canvas_button.setWidth(48);
		canvas_button.setHeight(48);
		canvas_button.setStyleName("gwt-ButtonWidget");
		canvas_button.setSrc("canvas.png");
		canvas_button.setLabelHPad(5);
		canvas_button.setLabelVPad(10);

		canvas_button.setShadowDepth(1);
		menuPanel.setWidget(1, 0, canvas_button);

		canvas_button
				.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						// Window.alert("Canvas selected");
						String canvasEditorUUID = UUID.uuid(8, 16);
						widgets.addCanvas("Canvas" + canvasEditorUUID, 1);
						// rootPanel.setWidgetPosition(menuPanel, 10,
						// windowHeight
						// / 2 - menuPanel.getOffsetHeight() / 2);
					}

				});

		menuPanel.setWidget(4, 0, widgets.settings_panel);

		// text
		ImgButton text_button = new ImgButton();
		text_button.setWidth(48);
		text_button.setHeight(48);
		// text_button.setStyleName("gwt-ButtonWidget");
		text_button.setSrc("text.png");
		text_button.setShadowSoftness(6);
		text_button.setShadowOffset(16);
		text_button.updateShadow();
		text_button.setLabelHPad(5);
		text_button.setLabelVPad(10);

		menuPanel.setWidget(2, 0, text_button);

		text_button
				.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						// Window.alert("Text selected");
						String textEditorUUID = UUID.uuid(8, 16);
						widgets.addTextBox("Text" + textEditorUUID, 1);
					}

				});

		// vector
		// Image vector_image = new Image("images/vector.png");
		// Button vector = new Button();
		// vector.setStyleName("gwt-ButtonWidget");
		// optionsPanel.add(vector);
		// vector.getElement().appendChild(vector_image.getElement());
		// vector.setSize(65 + "px", 65 + "px");
		// menuPanel.setWidget(2, 1, vector);
		//
		// vector.addClickHandler(new ClickHandler() {
		// @Override
		// public void onClick(ClickEvent event) {
		// String vectorEditorUUID = UUID.uuid(8, 16);
		// widgets.addVector();
		// }
		//
		// });
		//

		// image
		ImgButton image_button = new ImgButton();
		image_button.setWidth(48);
		image_button.setHeight(48);
		image_button.setSrc("image.png");
		image_button.setShadowSoftness(6);
		image_button.setShadowOffset(16);
		image_button.updateShadow();
		image_button.setLabelHPad(5);
		image_button.setLabelVPad(10);
		menuPanel.setWidget(3, 0, image_button);

		// Image image_image = new Image("images/image.png");
		// Button image = new Button();
		// image.getElement().appendChild(image_image.getElement());
		// image.setStyleName("gwt-ButtonWidget");
		// image.setSize(65 + "px", 65 + "px");
		// menuPanel.setWidget(2, 2, image);

		image_button
				.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						String imageEditorUUID = UUID.uuid(8, 16);
						widgets.addImage("Image" + imageEditorUUID);
					}
				});

		// path viewer
		preview = new PreviewWidget(windowWidth / 4, windowHeight / 4,
				(float) 1 / 4, (float) 1 / 4, windowWidth, windowHeight);

		// communication and RPC calls
		myCheckoutHandler = new CheckoutHandler(this, checkoutService,
				revisionList, revisionHistory, widgets, preview, Log);

		// clone a different revision
		ImgButton clone_button = new ImgButton();
		clone_button.setWidth(30);
		clone_button.setHeight(30);
		clone_button.setSrc("clone.png");

		// ask if the user wants to clone or merge
		VLayout confirmclone = new VLayout();
		com.smartgwt.client.widgets.Button confirm_yes = new com.smartgwt.client.widgets.Button(
				"Copy and merge into current layout");
		com.smartgwt.client.widgets.Button confirm_no = new com.smartgwt.client.widgets.Button(
				"Copy into a new layout (Warning! Current work will be lost!)");
		com.smartgwt.client.widgets.Button confirm_cancel = new com.smartgwt.client.widgets.Button(
				"Cancel");
		confirm_no.setWidth(400);
		confirm_no.setHeight(30);
		confirm_yes.setWidth(confirm_no.getWidth());
		confirm_yes.setHeight(30);
		confirm_cancel.setWidth(confirm_no.getWidth());
		confirm_cancel.setHeight(30);
		confirmclone.addMember(confirm_yes);
		confirmclone.addMember(confirm_no);
		confirmclone.addMember(confirm_cancel);

		final com.smartgwt.client.widgets.Window confirm_clone_win = new com.smartgwt.client.widgets.Window();
		confirm_clone_win.setTitle("Confirm Clone");
		confirm_clone_win.setAutoSize(true);
		confirm_clone_win.addItem(confirmclone);

		// final DialogBox confirm_clone_dialog = new DialogBox();
		// confirm_clone_dialog.setAutoHideEnabled(true);
		// confirm_clone_dialog.add(confirmclone);

		confirm_yes
				.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						myCheckoutHandler.add_to_workspace();
						confirm_clone_win.hide();
					}

				});

		confirm_no
				.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						myCheckoutHandler.onClick(null);
						confirm_clone_win.hide();
					}

				});

		confirm_cancel
				.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						confirm_clone_win.hide();
					}

				});

		clone_button
				.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						confirm_clone_win.show();
						confirm_clone_win.centerInPage();
					}

				});

		pathViewer = new PathViewer(this, revisionHistory, myCheckoutHandler,
				uid, revisionList, windowWidth, windowHeight);
		pathViewer2 = new PathViewerFullScreen(this, revisionHistory,
				myCheckoutHandler, uid, revisionList, windowWidth - 100,
				windowHeight - 100);

		// a wrap up for menuPanel
		// expand panel
		final ImgButton expandpanel_button = new ImgButton();
		expandpanel_button.setWidth(25);
		expandpanel_button.setHeight(50);
		expandpanel_button.setSrc("arrow-right.png");
		expandpanel_button.setStyleName("gwt-ButtonWidget3");

		menuPanel.setStyleName("gwt-MenuWidget");
		menuPanel.setVisible(false);
		rootPanel.add(expandpanel_button, 0, windowHeight / 2 - 22);

		expandpanel_button
				.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						if (menuPanel.getOffsetHeight() == 0
								&& menuPanel.getOffsetWidth() == 0) {
							menuPanel.setVisible(true);
							rootPanel.setWidgetPosition(expandpanel_button,
									menuPanel.getOffsetWidth(),
									windowHeight / 2 - 22);
							expandpanel_button.setSrc("arrow-left.png");
							rootPanel.setWidgetPosition(
									menuPanel,
									0,
									windowHeight / 2
											- menuPanel.getOffsetHeight() / 2);
						} else {
							menuPanel.setVisible(false);
							expandpanel_button.getElement().getStyle()
									.setZIndex(2);
							rootPanel.setWidgetPosition(expandpanel_button, 0,
									windowHeight / 2 - 22);
							expandpanel_button.setSrc("arrow-right.png");

						}
					}

				});

		menuPanel.getElement().getStyle().setZIndex(3);
		rootPanel.add(menuPanel, 0, windowHeight / 2 - 50);

		// create a new pathviewer panel
		final com.smartgwt.client.widgets.Window pathViewerWin = new com.smartgwt.client.widgets.Window();
		pathViewerWin.setWidth(windowWidth);
		pathViewerWin.setHeight(windowHeight);
		pathViewerWin.setTitle("Path Viewer");
		pathViewerWin.setShowMinimizeButton(true);

		// pathPopUp = new DialogBox();
		ScrollPanel path = new ScrollPanel();
		path.add(pathViewer2);
		pathViewer2.setVisible(true);
		path.setHeight((windowHeight - 40) + "px");
		path.setWidth((windowWidth - 30) + "px");
		pathViewerWin.addItem(path);

		// refresh for this
		// close_button_for_path.addClickHandler(new ClickHandler() {
		// @Override
		// public void onClick(ClickEvent event) {
		// pathPopUp.hide();
		// }
		// });

		/* refresh button for thumbnail path viewer */
		HeaderControl refresh = new HeaderControl(HeaderControl.REFRESH,
				new com.smartgwt.client.widgets.events.ClickHandler() {
					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						new CheckIDHandler().onClick(null);
						pathViewer2.setPathViewer();
						pathViewer2.setVisible(true);
					}

				});
		refresh.setWidth(25);
		refresh.setHeight(25);

		// Show options in path viewer
		VLayout buttonPanel = new VLayout();
		// buttonPanel.setShowEdges(true);
		buttonPanel.setMembersMargin(10);
		buttonPanel.setLayoutMargin(2);

		ImgButton refresh_button = new ImgButton();
		refresh_button.setWidth(30);
		refresh_button.setHeight(30);
		refresh_button.setLabelHPad(5);
		refresh_button.setLabelVPad(10);
		refresh_button.setStyleName("gwt-ButtonNavigation");
		refresh_button.setSrc("refresh.png");
		refresh_button.setShadowDepth(1);

		refresh_button
				.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						new CheckIDHandler().onClick(null);
						// pathViewer.setPathViewer();
						pathViewer.setVisible(true);
					}
				});

		ImgButton fullscreen_button = new ImgButton();
		fullscreen_button.setWidth(30);
		fullscreen_button.setHeight(30);
		fullscreen_button.setLabelHPad(5);
		fullscreen_button.setLabelVPad(10);
		fullscreen_button.setSrc("fullscreen.png");
		fullscreen_button.setShadowDepth(1);

		fullscreen_button
				.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						new CheckIDHandler().onClick(null);
						pathViewer2.setPathViewer();
						pathViewer2.setVisible(true);
						pathViewerWin.show();
						pathViewerWin.maximize();
						pathViewerWin.getHeader().setHeight(28);
					}

				});

		//buttonPanel.addMember(refresh_button);
		//buttonPanel.addMember(fullscreen_button);
		buttonPanel.addMember(clone_button);

		// new path viewer
		final ImgButton expandpanel_button2 = new ImgButton();
		expandpanel_button2.setWidth(25);
		expandpanel_button2.setHeight(50);
		expandpanel_button2.setSrc("arrow-left.png");

		rootPanel.add(expandpanel_button2, windowWidth - 14,
				windowHeight / 2 - 22);
		expandpanel_button2.setStyleName("gwt-ButtonWidget3");
		expandpanel_button2.getElement().getStyle().setZIndex(3);

		// create a navigation panel for path viewer
		navigationPanel = new VLayout();
		navigationPanel.setLayoutMargin(2);

		// create Horizontal panel of tabs
		TabSet tabSet = new TabSet();
		tabSet.setTabBarPosition(Side.TOP);

		// first tab
		Tab tTab1 = new Tab("Tree");
		Canvas tabPane1 = new Canvas();
		preview.setStyleName("gwt-previewPanel");
		tabPane1.addChild(pathViewer);
		tTab1.setPane(tabPane1);
		tabSet.addTab(tTab1);
		tabSet.setWidth(windowWidth / 3);
		tabSet.setHeight(windowHeight / 2);
		navigationPanel.addMember(tabSet);

		// add panel containing bottons for navigation panel
		HLayout hLayout = new HLayout();
		hLayout.addMember(buttonPanel);
		VLayout previewPanel = new VLayout();
		previewPanel.setShowEdges(true);
		previewPanel.setEdgeSize(1);
		com.smartgwt.client.widgets.Label previewLabel = new com.smartgwt.client.widgets.Label(
				"Preview:");
		previewLabel.setWrap(true);
		previewLabel.setAutoWidth();
		previewLabel.setAutoHeight();
		previewLabel.setPadding(5);
		previewPanel.addMember(previewLabel);
		previewPanel.addMember(preview);
		hLayout.addMember(previewPanel);

		navigationPanel.addMember(hLayout);
		navigationPanel.addMember(entityList);

		/* create a new pathviewer panel */
		final com.smartgwt.client.widgets.Window navigationWin = new com.smartgwt.client.widgets.Window();
		navigationWin.setTitle("Path Viewer");
		navigationWin
				.addCloseClickHandler(new com.smartgwt.client.widgets.events.CloseClickHandler() {
					@Override
					public void onCloseClick(CloseClickEvent event) {
						navigationWin.hide();
					}
				});

		/* close button for thumbnail pathviewer window */
		HeaderControl close = new HeaderControl(HeaderControl.CLOSE,
				new com.smartgwt.client.widgets.events.ClickHandler() {
					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						pathViewerWin.hide();
						navigationWin.hide();
					}

				});

		close.setWidth(25);
		close.setHeight(25);

		/* close button for navigation window */
		HeaderControl close2 = new HeaderControl(HeaderControl.CLOSE,
				new com.smartgwt.client.widgets.events.ClickHandler() {
					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						navigationWin.hide();
					}

				});

		close2.setWidth(25);
		close2.setHeight(25);

		/* refresh button for navigation window */
		HeaderControl refresh2 = new HeaderControl(HeaderControl.REFRESH,
				new com.smartgwt.client.widgets.events.ClickHandler() {
					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						new CheckIDHandler().onClick(null);
						pathViewer.setVisible(true);
					}

				});
		refresh2.setWidth(25);
		refresh2.setHeight(25);

		HeaderControl maximize2 = new HeaderControl(HeaderControl.MAXIMIZE,
				new com.smartgwt.client.widgets.events.ClickHandler() {
					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						
					}

				});
		maximize2.setWidth(25);
		maximize2.setHeight(25);

		
		navigationWin.setHeaderControls(HeaderControls.HEADER_LABEL, refresh2,
				maximize2, close2);

		pathViewerWin.setHeaderControls(HeaderControls.HEADER_LABEL, refresh,
				close);

		navigationWin.setCanDragReposition(true);
		navigationWin.addItem(navigationPanel);
		navigationWin.setAutoSize(true);
		final ScrollPanel SkwikiPathViewerPanel = new ScrollPanel();
		SkwikiPathViewerPanel.setStyleName("gwt-pathViewer");

		new CheckIDHandler().onClick(null);

		expandpanel_button2
				.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						if (!navigationWin.isVisible()
								|| !navigationWin.isDrawn()) {
							// new CheckIDHandler().onClick(null);
							navigationWin.show();
							navigationWin.getHeader().setHeight(28);

							// pathViewer.setPathViewer();
							pathViewer.setVisible(true);
							navigationWin.setLeft(windowWidth
									- navigationWin.getWidth() - 30);
							navigationWin.setTop(60);
						}
					}

				});

		tagViewButton.addClickHandler(new TagViewerToggle());
		getAllRevButton.addClickHandler(new GetAllRevHandler());
		maskOnButton.addClickHandler(new FilterToggle());
		allRevList.addChangeHandler(new EntityMaskHandler());

		// add tagviewer related items to the UI
		entityList.setWidth(70 + "px");

		// rootPanel.add(loginDetails, 4 * windowWidth / 5, 0);

		// switch between tagviewer and normal editor
		tagViewer.setVisible(false);
		rootPanel.add(tagViewer);

		// Adding the LOG panel
		Log.setText("GWT LOG: ");
		Log.setSize(400 + "px", 14 + "px");
		rootPanel.add(Log, 10, windowHeight - 20);

		// commit every 20 sec
		final Timer timer = new Timer() {
			@Override
			public void run() {

				// SC.confirm("Sketch will be saved on the server",
				// new BooleanCallback() {
				// public void execute(Boolean value) {
				// if (value != null && value) {
				// myCommitHandler.onClick(null);
				// } else {
				//
				// }
				// }
				// });
				Date date = new Date();
				int diff = minutesDiff(lastTime, date);

				Log.setText("Last Saved " + diff + " min ago");
			}
		};
		timer.scheduleRepeating(1000 * 60);
	}

	public static int minutesDiff(Date earlierDate, Date laterDate) {
		if (earlierDate == null || laterDate == null)
			return 0;

		return (int) ((laterDate.getTime() / MINUTE_MILLIS) - (earlierDate
				.getTime() / MINUTE_MILLIS));
	}

	class CommitHandler implements ClickHandler {
		/**
		 * Fired when the user clicks on the commitButton.
		 */

		@Override
		public void onClick(ClickEvent event) {

			// commit all layout history
			DataPack packtoSend = new DataPack();

			packtoSend.id = uid;
			packtoSend.fromUID = fromUID;
			packtoSend.comment = commentText.getText();
			packtoSend.projectName = current_project_name;

			if (uid.equals("")) {
				Window.alert("login please");
				return;
			}
			packtoSend.fromRevision = fromRevision;
			// Window.alert("Sketch is saved on the server");

			for (int i = widgets.layoutSettleIndex; i <= widgets.layoutHistoryList
					.size() - 1; i++) {
				packtoSend.layoutHistoryList.add(widgets.layoutHistoryList
						.get(i));
			}

			// for (AbstractLayoutHistory tempLayoutHistory :
			// widgets.layoutHistoryList) {
			// packtoSend.layoutHistoryList.add(tempLayoutHistory);
			// }

			// commit all text editors

			for (MyTextEditor tempTextEditor : widgets.textEditors) {
				tempTextEditor.getChange(packtoSend);
			}

			// commit all canvas editors

			for (MyCanvasEditor tempCanvasEditor : widgets.canvasEditors) {
				tempCanvasEditor.getChange(packtoSend);
			}

			// commit all image editors
			for (MyImageViewer tempImageEditor : widgets.imageEditors) {
				tempImageEditor.getChange(packtoSend);
			}

			// send the commit request

			try {
				// ServiceDefTarget endpoint = (ServiceDefTarget) commitService;
				// PhonegapUtil.prepareService(endpoint,
				// "http://127.0.0.1:8080/Skwiki/skWiki/", "greet");
				commitService.commit(packtoSend, new AsyncCallback<DataPack>() {
					@Override
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						Log.setText("GWT ERROR: Failed to call server--SERVER_ERROR");
					}

					@Override
					public void onSuccess(DataPack result) {
						Date date = new Date();
						DateTimeFormat dtf = DateTimeFormat
								.getFormat("yyyy/MM/dd:HH:mm:ss");

						widgets.layoutSettleIndex += result.layoutHisotrySettleIndex;

						Log.setText("GWT Success: Successfully commited at "
								+ dtf.format(date, TimeZone.createTimeZone(240)));

						lastTime = date;

						// show the commit result, update the
						// information label;
						for (MyCanvasEditor tempCanvasEditor : widgets.canvasEditors) {
							tempCanvasEditor
									.commitOnSuccess(result.updateCanvasMap
											.get(tempCanvasEditor.getID()));
						}
						fromRevision = result.updateRevision;
						// Window.alert(""+fromRevision);

					}
				});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	class CheckIDHandler implements ClickHandler {
		/**
		 * Fired when the user clicks on the commitButton.
		 */
		@Override
		public void onClick(ClickEvent event) {
			DataPack packtoSend = new DataPack();
			packtoSend.projectName = current_project_name;
			packtoSend.id = uid;
			
			try {
				// ServiceDefTarget endpoint = (ServiceDefTarget)
				// checkIDService;
				// PhonegapUtil.prepareService(endpoint,
				// "http://127.0.0.1:8080/Skwiki/skWiki/", "checkID");
				checkIDService.checkID(packtoSend,
						new AsyncCallback<DataPack>() {
							@Override
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user

							}

							@Override
							public void onSuccess(DataPack result) {
								if (result.updateRevision == 0) {
									// Window.alert("new ID");
									return;
								}

								updateRevisionHistory_List(result);
								pathViewer.setPathViewer();
							}
						});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

		}
	}

	public void updateRevisionHistory_List(DataPack result) {
		revisionHistory.clear();
		for (RevisionHistory tempHistory : result.revisionList) {
			revisionHistory.add(tempHistory);
		}
		revisionList.clear();
		for (RevisionHistory tempHistory : result.revisionList) {
			revisionList.addItem(tempHistory.toString());
		}

	}

	public void updateRevisionHistory(DataPack result) {
		for (RevisionHistory tempHistory : result.revisionList) {
			revisionHistory.add(tempHistory);
		}
	}

	public void updateRevisionList(DataPack result) {
		// Async revision list

		latestRevision = result.updateRevision;

		for (int i = 1; i < revisionListItemCount; i++) {
			revisionList.removeItem(0);
		}

		revisionListItemCount = 1;
		for (int i = 0; i < latestRevision; i++) {
			revisionList.addItem((i + 1) + "");
			revisionListItemCount++;
		}

	}

	// tagtoggle button handler
	class TagViewerToggle implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			if (tagViewButton.isDown()) {
				// pathViewer.setPathViewer();
				tagViewer.setVisible(true);
				widgets.boundaryPanel.setVisible(false);

			} else {
				widgets.boundaryPanel.setVisible(true);
				tagViewer.setVisible(false);

			}
		}
	}

	class FilterToggle implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			if (maskOnButton.isDown()) {
				// pathViewer.setPathViewer();
				// maskOnButton.setVisible(true);
				// widgets.mask = entityList.getItemText(entityList
				// .getSelectedIndex());
				// boundaryPanel.setVisible(false);

			} else {
				widgets.mask = "";

			}
		}
	}

	class GetAllRevHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			DataPack packtoSend = new DataPack();
			// TODO Change this in future
			// packtoSend.entity_key = entityList.getItemText(entityList
			// .getSelectedIndex());

			try {
				getAllRevService.getAllRev(packtoSend,
						new AsyncCallback<DataPack>() {
							@Override
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user

							}

							@Override
							public void onSuccess(DataPack result) {
								allRevList.clear();
								allRevHistory.clear();

								for (SearchTagResult tempInt : result.allRevList) {
									allRevList.addItem(tempInt.getRevision()
											+ "");
									allRevHistory.add(tempInt);
								}

							}
						});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	class EntityMaskHandler implements ChangeHandler {

		@Override
		public void onChange(ChangeEvent event) {
			DataPack packtoSend = new DataPack();
			// packToSend.updateRevision
			packtoSend.updateRevision = allRevHistory.get(
					allRevList.getSelectedIndex()).getRevision();

			packtoSend.id = allRevHistory.get(allRevList.getSelectedIndex())
					.getId();

			// myCheckoutHandler.sendToServer(packtoSend, );

		}
	}

	class TagViewer extends AbsolutePanel {

		private TextBox tagSearchBox = new TextBox();
		private Button searchButton = new Button("search");
		private Label searchResultLabel = new Label("Search Result");
		final private Label selectedLabel = new Label("   ");
		ArrayList<SearchTagResult> searchTagList;
		CellTable<SearchTagResult> table = new CellTable<SearchTagResult>();

		public TagViewer() {
			super();
			this.setSize("850px", "750px");
			searchTagList = new ArrayList<SearchTagResult>();

			tagSearchBox.setText("Input searched tag here");

			searchButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					DataPack packtoSend = new DataPack();
					packtoSend.searchedTag = tagSearchBox.getText().trim();

					try {
						searchTagService.searchTag(packtoSend,
								new AsyncCallback<DataPack>() {
									@Override
									public void onFailure(Throwable caught) {
										// Show the RPC error message to the
										// user
									}

									@Override
									public void onSuccess(DataPack result) {

										updateSearchTagList(result);
										setTagViewer();
										searchResultLabel.setText(result.searchTagList
												.size()
												+ " records are found with tag"
												+ " \""
												+ tagSearchBox.getText().trim()
												+ "\"");

									}
								});
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}

				}
			});

			this.add(tagSearchBox, 20, 30);
			this.add(searchButton, 200, 30);
			this.add(searchResultLabel, 280, 30);
			this.add(selectedLabel, 600, 30);

			final SingleSelectionModel<SearchTagResult> selectionModel = new SingleSelectionModel<SearchTagResult>();
			table.setSelectionModel(selectionModel);
			selectionModel
					.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						@Override
						public void onSelectionChange(SelectionChangeEvent event) {
							SearchTagResult selected = selectionModel
									.getSelectedObject();
							if (selected != null) {
								selectedLabel.setText("Revision "
										+ selected.getRevision()
										+ " is selected");
								// Window.alert("You selected: " +
								// selected.name);
							}
						}
					});
			this.add(table, 20, 80);
		}

		private void updateSearchTagList(DataPack result) {
			searchTagList.clear();
			for (SearchTagResult tempResult : result.searchTagList) {
				this.searchTagList.add(tempResult);
			}

			for (int i = table.getColumnCount(); i > 0; i--) {
				table.removeColumn(0);
			}
		}

		public void setTagViewer() {

			TextColumn<SearchTagResult> revisionColumn = new TextColumn<SearchTagResult>() {
				@Override
				public String getValue(SearchTagResult object) {
					return object.getRevision() + "";
				}
			};
			table.addColumn(revisionColumn, "Revision");

			TextColumn<SearchTagResult> idColumn = new TextColumn<SearchTagResult>() {
				@Override
				public String getValue(SearchTagResult object) {
					return object.getId();
				}
			};
			table.addColumn(idColumn, "User ID");

			TextColumn<SearchTagResult> entity_IDColumn = new TextColumn<SearchTagResult>() {
				@Override
				public String getValue(SearchTagResult object) {
					return object.getEntity_id();
				}
			};
			table.addColumn(entity_IDColumn, "Entity_ID");

			TextColumn<SearchTagResult> tagColumn = new TextColumn<SearchTagResult>() {
				@Override
				public String getValue(SearchTagResult object) {
					return object.getTag();
				}
			};
			table.addColumn(tagColumn, "Tags");
			table.setRowCount(searchTagList.size(), true);
			table.setRowData(searchTagList);
		}
	}
}
