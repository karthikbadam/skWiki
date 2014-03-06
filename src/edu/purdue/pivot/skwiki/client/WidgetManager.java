package edu.purdue.pivot.skwiki.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.grid.ListGrid;
import edu.purdue.pivot.skwiki.client.dnd.WindowController;
import edu.purdue.pivot.skwiki.client.dnd.WindowPanel;
import edu.purdue.pivot.skwiki.client.image.MyImageViewer;
import edu.purdue.pivot.skwiki.client.sketch.AttachedPanel;
import edu.purdue.pivot.skwiki.client.sketch.CanvasToolbar;
import edu.purdue.pivot.skwiki.client.sketch.MyCanvasEditor;
import edu.purdue.pivot.skwiki.client.sketch.TouchPad;
import edu.purdue.pivot.skwiki.client.text.MyTextEditor;
import edu.purdue.pivot.skwiki.client.tree.TreeNode;
import edu.purdue.pivot.skwiki.client.vector.VectorEditor;
import edu.purdue.pivot.skwiki.client.vector.VectorPanel;
import edu.purdue.pivot.skwiki.client.vector.VectorToolbar;
import edu.purdue.pivot.skwiki.shared.AbstractLayoutHistory;
import edu.purdue.pivot.skwiki.shared.AddToParentHistory;
import edu.purdue.pivot.skwiki.shared.CanvasPack;
import edu.purdue.pivot.skwiki.shared.ChangePosHistory;
import edu.purdue.pivot.skwiki.shared.ChangeSizeHistory;
import edu.purdue.pivot.skwiki.shared.CreateEntityHistory;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.EditorType;
import edu.purdue.pivot.skwiki.shared.UUID;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.IFileInput.FileInputType;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

public class WidgetManager {

	private static final int BUTTON_SIZE = 70;
	// panels
	private int windowWidth;
	private int windowHeight;
	private ScrollPanel widgetPanel;
	public AbsolutePanel boundaryPanel;
	private WindowController windowController;
	//private PopupPanel imagePopup;
	// private PopupPanel settings_popup;
	public ListGrid entityList;
	
	// mask
	String mask = "";

	// new!
	int layoutSettleIndex = 0;

	// Settings panel like in photoshop but with less icons
	public VerticalPanel settings_panel;

	// user ID
	private String uid;

	// editor indices
	/*
	 * text editor index, canvas editor index, vector editor index, image editor
	 * index
	 */
	private int textEditorIndex = 0;
	private int canvasEditorIndex = 0;
	private int vectorEditorIndex = 0;
	private int imageEditorIndex = 0;
	
	

	// Lists of editors
	public ArrayList<MyTextEditor> textEditors = new ArrayList<MyTextEditor>();
	ArrayList<VectorEditor> vectorEditors = new ArrayList<VectorEditor>();
	public ArrayList<MyCanvasEditor> canvasEditors = new ArrayList<MyCanvasEditor>();
	ArrayList<MyImageViewer> imageEditors = new ArrayList<MyImageViewer>();
	ArrayList<WindowPanel> editors = new ArrayList<WindowPanel>();
	final HashMap<String, WindowPanel> windowPanelmap = new HashMap<String, WindowPanel>();

//	ArrayList<CanvasToolbar> toolbars = new ArrayList<CanvasToolbar>();

	// layout history
	final ArrayList<AbstractLayoutHistory> layoutHistoryList = new ArrayList<AbstractLayoutHistory>();

	// layout Tree -- create root
	final TreeNode root = new TreeNode("root", null, null);

	// history accumulator for undo/redo operations
	HashMap<String, ChangePosHistory> posHistoryAccumulator = new HashMap<String, ChangePosHistory>();
	HashMap<String, ChangeSizeHistory> sizeHistoryAccumulator = new HashMap<String, ChangeSizeHistory>();

	public void accumulateHistory(
			ArrayList<AbstractLayoutHistory> layoutHistoryList) {
		for (AbstractLayoutHistory layoutHistory : layoutHistoryList) {
			// if (layoutHistory.)
		}
	}

	// constructor
	public WidgetManager(ScrollPanel widgetPanel, int windowWidth,
			int windowHeight, String uid, ListGrid entityList) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.widgetPanel = widgetPanel;
		this.uid = uid;
		this.boundaryPanel = new AbsolutePanel();

		boundaryPanel.setSize(windowWidth + "px", windowHeight + "px");
		boundaryPanel.addStyleName("demo-WindowExample");
		this.widgetPanel.add(boundaryPanel);

		windowController = new WindowController(boundaryPanel,
				layoutHistoryList);

		this.entityList = entityList;
		entityList.setAutoHeight();
		entityList.setShowAllRecords(true);
		entityList.setWidth(windowWidth/3);
		
		settings_panel = new VerticalPanel();
		settings_panel.add(CanvasToolbar.getInstance());
		
		// settings_window = new com.smartgwt.client.widgets.Window();
		// settings_window.setTitle("Settings");
		// settings_window.setShowMinimizeButton(true);
		// settings_window.addCloseClickHandler(new
		// com.smartgwt.client.widgets.events.CloseClickHandler() {
		//
		// @Override
		// public void onCloseClick(CloseClickEvent event) {
		// //settings_window.hide();
		// }
		// });
		//
		// settings_window.setCanDragReposition(true);
		// settings_window.setCanDragResize(true);
		// settings_window.setWidth(BUTTON_SIZE);
		// settings_window.setHeight(5*BUTTON_SIZE);
		// settings_window.setOverflow(com.smartgwt.client.types.Overflow.AUTO);
		// settings_window.setLeft(windowWidth-100);
		
		
	}

	protected void addVector() {

		final VectorEditor editor = new VectorEditor(uid, vectorEditorIndex);
		final VectorPanel vectorPanel = editor.getVectorCanvas();
		vectorPanel.addStyleName("gwt-TouchPanelWidget");
		HorizontalPanel header = new HorizontalPanel();
		header.add(new Label("Vector" + (vectorEditorIndex + 1)));

		final WindowPanel windowPanel = new WindowPanel(windowController,
				header, vectorPanel, false);
		boundaryPanel.add(windowPanel, 1, 1);

		// layout.addItem("Vector" + vectorEditorIndex);

		// Add layout items to the lists
		vectorEditors.add(editor);
		editors.add(windowPanel);
		vectorEditorIndex++;

		// Add focus listener
		editor.getVectorCanvas().addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				// settings_window.clear();
				// settings_window.hide();
				VectorToolbar toolbar = editor.getVectorCanvas().getToolbar();
				// settings_panel.clear();
				// settings_panel.addChild(toolbar);
				// settings_window.clear();
				// settings_window.addItem(toolbar);
				// add close settings button
				Button close_popup = new Button("X");
				close_popup.setWidth("15px");
				close_popup.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						// settings_window.clear();
						// settings_window.hide();

					}
				});

				// settings_panel.addChild(close_popup);
				// settings_window.addItem(settings_panel);
				// settings_window.show();
				// settings_window.setLeft(windowPanel.getAbsoluteLeft()
				// + windowPanel.getOffsetWidth());
				// settings_window.setTop(windowPanel.getAbsoluteTop());
				// windowPanel.getElement().getStyle().setZIndex(2);
			}
		});

	}

	/* Adds a Text Box */
	protected void addTextBox(String textEditorUUID, int created) {

		final MyTextEditor myEditor = new MyTextEditor(textEditorUUID, uid);
		myEditor.getArea().setStyleName("gwt-TouchPanelWidget");
		HorizontalPanel header = new HorizontalPanel();
		header.add(new Label("Text" + (textEditorIndex + 1)));

		final WindowPanel windowPanel = new WindowPanel(windowController,
				header, myEditor.getArea(), false);
		boundaryPanel.add(windowPanel, 40, 40);
		windowPanel.editPanel = (AttachedPanel) myEditor.getArea();

		// layout.addItem("Text" + (textEditorIndex + 1));

		// Add layout items to the lists!
		textEditors.add(myEditor);
		editors.add(windowPanel);
		windowPanelmap.put(myEditor.getID(), windowPanel);

		if (created == 1) {
			// update the layout History list
			layoutHistoryList.add(new CreateEntityHistory(textEditorUUID,
					EditorType.TEXT));
		}

		// TODO update the entity tree, add the entity to the root
		TreeNode tempText = new TreeNode(textEditorUUID, myEditor,
				EditorType.TEXT);
		root.addChild(tempText);
		textEditorIndex++;

//		myEditor.getArea().addFocusHandler(new FocusHandler() {
//
//			@Override
//			public void onFocus(FocusEvent event) {
//				// settings_window.hide();
//				for (CanvasToolbar toolbar : toolbars) {
//					toolbar.setVisible(false);
//				}
//				myEditor.getArea().setStyleName("gwt-TouchPanelWidget2");
//
//			}
//
//		});
	}

	/* clone a Text Box */
	protected void addTextBox(String textEditorUUID, int created,
			DataPack result) {

		final MyTextEditor myEditor = new MyTextEditor(textEditorUUID, uid);
		myEditor.getArea().setStyleName("gwt-TouchPanelWidget");
		HorizontalPanel header = new HorizontalPanel();
		header.add(new Label("Text" + (textEditorIndex + 1)));

		final WindowPanel windowPanel = new WindowPanel(windowController,
				header, myEditor.getArea(), false);
		boundaryPanel.add(windowPanel, 40, 40);
		windowPanel.editPanel = (AttachedPanel) myEditor.getArea();

		// layout.addItem("Text" + (textEditorIndex + 1));

		// Add layout items to the lists!
		textEditors.add(myEditor);
		editors.add(windowPanel);
		windowPanelmap.put(myEditor.getID(), windowPanel);

		if (created == 1) {
			// update the layout History list
			layoutHistoryList.add(new CreateEntityHistory(textEditorUUID,
					EditorType.TEXT));
		}

		// TODO update the entity tree, add the entity to the root
		TreeNode tempText = new TreeNode(textEditorUUID, myEditor,
				EditorType.TEXT);
		root.addChild(tempText);
		textEditorIndex++;

		myEditor.updateEditor(result);
	}

	private String imageEditorUUID;
	/* image popup window */
	com.smartgwt.client.widgets.Window imagePopup ;

	
	/* add Image */
	protected void addImage(String imageEditorUUID) {
		
		this.imageEditorUUID = imageEditorUUID;

		/* Create a new multiuploader and attach it to the document */
		MultiUploader defaultUploader = new MultiUploader();
		
		imagePopup = new com.smartgwt.client.widgets.Window();
		/* prepare the popup window */
		imagePopup.setTitle("Upload image");
		imagePopup.setShowMinimizeButton(true);
		HeaderControl close = new HeaderControl(HeaderControl.CLOSE,
				new com.smartgwt.client.widgets.events.ClickHandler() {
					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						imagePopup.destroy();
					}

				});

		close.setWidth(25);
		close.setHeight(25);
		imagePopup.setHeaderControls(HeaderControls.HEADER_LABEL, close);
		imagePopup.setCanDragReposition(true);
		imagePopup.setCanDragResize(true);
		imagePopup.setAutoSize(true);
		imagePopup.setCanDragReposition(true);
		imagePopup.setCanDragResize(true);
		
		imagePopup.addItem(defaultUploader);
		imagePopup.show();
		imagePopup.setLeft(400 - imagePopup.getWidth() - 10);
		imagePopup.setTop(300 - imagePopup.getHeight() - 20);
		imagePopup.getHeader().setHeight(28);
		
		//imagePopup.getElement().getStyle().setZIndex(4);
		//imagePopup.setAutoHideEnabled(true);
		// Enable/disable the component
		defaultUploader.setEnabled(true);
		// Add a finish handler which will load the image once the upload
		// finishes
		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);

	}

	// Load the image in the document and in the case of success attach it to
	// the viewer
	String localURL;
	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		@Override
		public void onFinish(IUploader uploader) {
			if (uploader.getStatus() == Status.SUCCESS) {
				localURL = uploader.fileUrl();
				new PreloadedImage(uploader.fileUrl(), showImage);

				// The server sends useful information to the client by default
				UploadedInfo info = uploader.getServerInfo();
				System.out.println("File name " + info.name);
				System.out.println("File content-type " + info.ctype);
				System.out.println("File size " + info.size);

				// Also, you can send any customized message and parse it
				System.out.println("Server message " + info.message);

				imagePopup.hide();
			}
		}
	};

	// Attach an image to the pictures viewer
	private OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {
		@Override
		public void onLoad(PreloadedImage image) {
			image.setUniqId(imageEditorUUID);
			image.setWidth("200px");

			/*
			 * final DialogBox imagePanel = new DialogBox(); VerticalPanel
			 * vpanel = new VerticalPanel(); // image cropper final GWTCropper
			 * crop = new GWTCropper(image); vpanel.add(crop); Button btn = new
			 * Button(); btn.setText("Crop"); btn.addClickHandler(new
			 * ClickHandler() {
			 * 
			 * @Override public void onClick(ClickEvent event) {
			 * Window.alert("Selected area from (" +
			 * crop.getSelectionXCoordinate() + "," +
			 * crop.getSelectionYCoordinate() + ") " + " with width=" +
			 * crop.getSelectionWidth() + " and height=" +
			 * crop.getSelectionHeight()); imagePanel.hide(); }
			 * 
			 * }); vpanel.add(btn); imagePanel.add(vpanel); imagePanel.center();
			 */

			HorizontalPanel header = new HorizontalPanel();
			header.add(new Label("image" + (imageEditorIndex + 1)));
			MyImageViewer myEditor = new MyImageViewer(image.getUrl(),
					image.getWidth(), image.getHeight(), image.getWidth(),
					image.getHeight(), 0, 0, image, imageEditorUUID);
			WindowPanel windowPanel = new WindowPanel(windowController, header,
					myEditor.getImage(), false);
			boundaryPanel.add(windowPanel, 1, 1);

			// layout.addItem("Image" + (imageEditorIndex + 1));
			imageEditors.add(myEditor);
			editors.add(windowPanel);

			// update the layout History list
			layoutHistoryList.add(new CreateEntityHistory(myEditor.getID(),
					EditorType.IMAGE));

			TreeNode tempImage = new TreeNode(myEditor.getID(), myEditor,
					EditorType.IMAGE);

			root.addChild(tempImage);
			imageEditorIndex++;
			windowPanelmap.put(myEditor.getID(), windowPanel);
			entityList.addMember(new Label("Image "+(imageEditors.indexOf(myEditor)+1)));
			
		}
	};

	/* Add a Canvas */
	protected void addCanvas(String canvasEditorUUID, int created) {

		final MyCanvasEditor myEditor = new MyCanvasEditor(canvasEditorUUID,
				uid, null, windowWidth, windowHeight);
		final TouchPad myCanvas = myEditor.getSurface();

		HorizontalPanel header = 	new HorizontalPanel();
		header.add(new Label("Canvas" + (canvasEditorIndex + 1)));
		header.setStyleName("gwt-PanelHeaderWidget");

		final WindowPanel windowPanel = new WindowPanel(windowController,
				header, myCanvas, false);
		myCanvas.setStyleName("gwt-TouchPanelWidget");
		windowPanel.editPanel = myCanvas;

		boundaryPanel.add(windowPanel, 100, 20);
		// layout.addItem("Canvas" + (canvasEditorIndex + 1));
		canvasEditors.add(myEditor);
		editors.add(windowPanel);

		if (created == 1) {
			// update the layout History list
			layoutHistoryList.add(new CreateEntityHistory(canvasEditorUUID,
					EditorType.CANVAS));
		}

		// update the entity tree, add the entity to the root
		TreeNode tempCanvas = new TreeNode(canvasEditorUUID, myEditor,
				EditorType.CANVAS);

		root.addChild(tempCanvas);
		canvasEditorIndex++;
		windowPanelmap.put(myEditor.getID(), windowPanel);
		entityList.addMember(new Label("Canvas "+(canvasEditors.indexOf(myEditor)+1)));
		
		
		// toolbar canvas
//		final CanvasToolbar toolbar = myEditor.getSurface().getToolbar();
//		// toolbar.setVisible(false);
//		for (CanvasToolbar temp_toolbar : toolbars) {
//			temp_toolbar.setVisible(false);
//		}
//		toolbars.add(toolbar);
		
	}

	/* Add a Canvas */
	protected void addCanvas(String canvasEditorUUID, int created,
			DataPack result) {

		// cheating a little bit here to do merge
		String newCanvasEditorUUID = "Canvas"+UUID.uuid(8, 16);

		CanvasPack result1 = result.updateCanvasMap.get(canvasEditorUUID);
		String tag = result.canvasTagMap.get(canvasEditorUUID);

		for (MyCanvasEditor tempCanvasEditor : canvasEditors) {
			if (tempCanvasEditor.getID() == canvasEditorUUID) {
				return;
			}
		}

		canvasEditorUUID = newCanvasEditorUUID;
		final MyCanvasEditor myEditor = new MyCanvasEditor(canvasEditorUUID,
				uid, null, windowWidth, windowHeight);
		final TouchPad myCanvas = myEditor.getSurface();

		HorizontalPanel header = new HorizontalPanel();
		header.add(new Label("Canvas" + (canvasEditorIndex + 1)));

		final WindowPanel windowPanel = new WindowPanel(windowController,
				header, myCanvas, false);
		myCanvas.setStyleName("gwt-TouchPanelWidget");
		windowPanel.editPanel = myCanvas;

		boundaryPanel.add(windowPanel, 100, 20);
		// layout.addItem("Canvas" + (canvasEditorIndex + 1));
		canvasEditors.add(myEditor);
		editors.add(windowPanel);

		if (created == 0) {
			// update the layout History list
			layoutHistoryList.add(new CreateEntityHistory(canvasEditorUUID,
					EditorType.CANVAS));
		}

		// update the entity tree, add the entity to the root
		TreeNode tempCanvas = new TreeNode(canvasEditorUUID, myEditor,
				EditorType.CANVAS);

		root.addChild(tempCanvas);
		canvasEditorIndex++;
		windowPanelmap.put(myEditor.getID(), windowPanel);

		// toolbar canvas
//		final CanvasToolbar toolbar = myEditor.getSurface().getToolbar();
//		// toolbar.setVisible(false);
//		for (CanvasToolbar temp_toolbar : toolbars) {
//			temp_toolbar.setVisible(false);
//		}
//		toolbars.add(toolbar);
//		settings_panel.add(toolbar);

		myEditor.updateEditor(result1, tag);
	}

	public int clear() {
		canvasEditors.clear();
		textEditors.clear();
		imageEditors.clear();
		windowPanelmap.clear();
		layoutHistoryList.clear();
		editors.clear();
		root.removeAll();
		textEditorIndex = 0;
		canvasEditorIndex = 0;
		imageEditorIndex = 0;
		boundaryPanel.clear();
		//toolbars.clear();
		settings_panel.clear();
		settings_panel.add(CanvasToolbar.getInstance());
		return 1;
	}

	public void updateLayoutTree2(
			ArrayList<AbstractLayoutHistory> layoutHistoryList1, DataPack result) {

		for (AbstractLayoutHistory tempLayoutHistory : layoutHistoryList1) {
			if (tempLayoutHistory instanceof AddToParentHistory) {

			} else if (tempLayoutHistory instanceof CreateEntityHistory) {

				String object = ((CreateEntityHistory) tempLayoutHistory)
						.getOperatingObject();

				if (((CreateEntityHistory) tempLayoutHistory).editorType == EditorType.TEXT) {
					addTextBox(object, 0, result);

				} else if (((CreateEntityHistory) tempLayoutHistory).editorType == EditorType.CANVAS) {
					addCanvas(object, 0, result);

				} else if (((CreateEntityHistory) tempLayoutHistory).editorType == EditorType.IMAGE) {

				}
			}
		}

	}

	public void updateLayoutTree(
			ArrayList<AbstractLayoutHistory> layoutHistoryList1, DataPack result) {
		// this.layoutHistoryList = layoutHistoryList;
		
		// change layout history to new layout
		clear();
		for (AbstractLayoutHistory tempLayoutHistory : layoutHistoryList1) {
			this.layoutHistoryList.add(tempLayoutHistory);
		}

		// ******* clear up the tree
		// Window.alert(layoutHistoryList.size() + " size of layout");

		for (AbstractLayoutHistory tempLayoutHistory : this.layoutHistoryList) {
			if (tempLayoutHistory instanceof AddToParentHistory) {

			} else if (tempLayoutHistory instanceof CreateEntityHistory) {

				String object = ((CreateEntityHistory) tempLayoutHistory)
						.getOperatingObject();
				if (((CreateEntityHistory) tempLayoutHistory).editorType == EditorType.TEXT) {
					addTextBox(object, 0);

				} else if (((CreateEntityHistory) tempLayoutHistory).editorType == EditorType.CANVAS) {
					addCanvas(object, 0);

				} else if (((CreateEntityHistory) tempLayoutHistory).editorType == EditorType.IMAGE) {

					final MyImageViewer myEditor = new MyImageViewer(object,
							uid);
					TreeNode tempImage = new TreeNode(object, myEditor,
							EditorType.IMAGE);
					imageEditors.add(myEditor);
					root.addChild(tempImage);
					myEditor.updateEditor(result);
					
					OnLoadPreloadedImageHandler showImage2 = new OnLoadPreloadedImageHandler() {
						@Override
						public void onLoad(PreloadedImage image) {
							HorizontalPanel header = new HorizontalPanel();
							myEditor.setImage(image);
							image.setWidth("200px");
							header.add(new Label("image"
									+ (imageEditorIndex + 1)));
							WindowPanel windowPanel1 = new WindowPanel(
									windowController, header, image, false);
							boundaryPanel.add(windowPanel1, 1, 1);
							// layout.addItem("Image" + (imageEditorIndex + 1));
							editors.add(windowPanel1);
							imageEditorIndex++;
							windowPanelmap.put(myEditor.getID(), windowPanel1);
							
							/* adjusting the positions of each image */
							
							for (AbstractLayoutHistory tempLayoutHistory : layoutHistoryList) {
								if (tempLayoutHistory instanceof ChangePosHistory) {
									String object = ((ChangePosHistory) tempLayoutHistory).getOperatingObject();
									
									if ( object.equals(myEditor.getID())) {
										int left = ((ChangePosHistory) tempLayoutHistory).getNewX();
										int top = ((ChangePosHistory) tempLayoutHistory).getNewY();
										// Window.alert("" + left + ", " + top);
										windowPanel1.moveTo(left, top);
									}
								}

								if (tempLayoutHistory instanceof ChangeSizeHistory) {
									String object = ((ChangeSizeHistory) tempLayoutHistory).getOperatingObject();
									
									if (object.equals(myEditor.getID())) {
										int width = ((ChangeSizeHistory) tempLayoutHistory)
												.getNewX();
										int height = ((ChangeSizeHistory) tempLayoutHistory)
												.getNewY();
										windowPanel1.setContentSize(width, height);
									}
								}
							}
						}
					};
					
					PreloadedImage image = new PreloadedImage(
							myEditor.getURL	(), showImage2);
					image.setUniqId(myEditor.getID());
					
				}
			}
		}
	}

	public void updateEditors(DataPack result) {

		for (MyTextEditor tempTextEditor : textEditors) {
			tempTextEditor.updateEditor(result);
		}

		// ******** update all canvas editors

		for (MyCanvasEditor tempCanvasEditor : canvasEditors) {
			tempCanvasEditor.updateEditor(result);
		}


		// // ******** update all image editors
		// for (MyImageViewer tempImageEditor : imageEditors) {
		// tempImageEditor.updateEditor(result);
		// }
	}

	public void updateLayoutList(
			ArrayList<AbstractLayoutHistory> layoutHistoryList) {
		// this.layoutHistoryList = layoutHistoryList;
		this.layoutHistoryList.clear();
		for (AbstractLayoutHistory tempLayoutHistory : layoutHistoryList) {
			this.layoutHistoryList.add(tempLayoutHistory);
		}
		layoutSettleIndex = layoutHistoryList.size();

	}

	public void shiftEntities() {
		for (AbstractLayoutHistory tempLayoutHistory : layoutHistoryList) {
			if (tempLayoutHistory instanceof ChangePosHistory) {
				if (windowPanelmap
						.containsKey(((ChangePosHistory) tempLayoutHistory)
								.getOperatingObject())) {
					WindowPanel tempPanel = windowPanelmap
							.get(((ChangePosHistory) tempLayoutHistory)
									.getOperatingObject());

					int left = ((ChangePosHistory) tempLayoutHistory).getNewX();
					int top = ((ChangePosHistory) tempLayoutHistory).getNewY();
					// Window.alert("" + left + ", " + top);
					tempPanel.moveTo(left, top);
				}
			}

			if (tempLayoutHistory instanceof ChangeSizeHistory) {
				if (windowPanelmap
						.containsKey(((ChangeSizeHistory) tempLayoutHistory)
								.getOperatingObject())) {
					WindowPanel tempPanel = windowPanelmap
							.get(((ChangeSizeHistory) tempLayoutHistory)
									.getOperatingObject());

					int width = ((ChangeSizeHistory) tempLayoutHistory)
							.getNewX();
					int height = ((ChangeSizeHistory) tempLayoutHistory)
							.getNewY();
					tempPanel.setContentSize(width, height);
				}
			}
		}
	}
}
