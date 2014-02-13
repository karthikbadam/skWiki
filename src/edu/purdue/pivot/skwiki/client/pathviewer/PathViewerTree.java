//package edu.purdue.pivot.skwiki.client.pathviewer;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import org.vaadin.gwtgraphics.client.DrawingArea;
//import org.vaadin.gwtgraphics.client.shape.Text;
//
//import com.github.gwtd3.api.Coords;
//import com.github.gwtd3.api.D3;
//import com.github.gwtd3.api.arrays.Array;
//import com.github.gwtd3.api.arrays.ForEachCallback;
//import com.github.gwtd3.api.core.Selection;
//import com.github.gwtd3.api.core.Transition;
//import com.github.gwtd3.api.core.UpdateSelection;
//import com.github.gwtd3.api.core.Value;
//import com.github.gwtd3.api.functions.DatumFunction;
//import com.github.gwtd3.api.functions.KeyFunction;
//import com.github.gwtd3.api.layout.Link;
//import com.github.gwtd3.api.layout.Node;
//import com.github.gwtd3.api.layout.TreeLayout;
//import com.github.gwtd3.api.svg.Diagonal;
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.core.client.JavaScriptObject;
//import com.google.gwt.dom.client.Element;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.resources.client.ClientBundle;
//import com.google.gwt.resources.client.CssResource;
//import com.google.gwt.user.client.ui.FlowPanel;
//import com.google.gwt.user.client.ui.IsWidget;
//import com.google.gwt.user.client.ui.ListBox;
//
//import edu.purdue.pivot.skwiki.client.SkwikiEntryPoint;
//import edu.purdue.pivot.skwiki.client.rpccalls.CheckoutHandler;
//import edu.purdue.pivot.skwiki.shared.RevisionHistory;
//
//public class PathViewerTree extends FlowPanel implements IsWidget {
//
//	final int width = 960;
//	final int height = 600;
//	final MyResources css = Bundle.INSTANCE.css();
//
//	static int i = 0;
//	static TreeDemoNode root = null;
//	static Selection svg = null;
//	static TreeLayout tree = null;
//	static Diagonal diagonal = null;
//
//	HashMap<String, Integer> columnNumMap = new HashMap<String, Integer>();
//	HashMap<String, Integer> userColorKeyMap = new HashMap<String, Integer>();
//	HashMap<String, String> userColorMap = new HashMap<String, String>();
//	
//	//variables with D3
//	HashMap<Integer, TreeDemoNode> allNodes = new HashMap<Integer, TreeDemoNode>();
//	HashMap<TreeDemoNode, ArrayList<TreeDemoNode>> children = new HashMap<TreeDemoNode, ArrayList<TreeDemoNode>>();
//	
//	ArrayList<RevisionHistory> revisionHistory;
//
//	ArrayList<ViewLabel> labelList = new ArrayList<ViewLabel>();
//	int Hspace = 100;
//	int Vspace = 50;
//
//	ArrayList<String> colors = new ArrayList<String>();
//	ArrayList<MyCircle> circles = new ArrayList<MyCircle>();
//
//	public CheckoutHandler checkoutHandler;
//	String uid;
//	ListBox revisionList;
//
//	SkwikiEntryPoint skWiki;
//
//	public interface Bundle extends ClientBundle {
//
//		public static final Bundle INSTANCE = GWT.create(Bundle.class);
//
//		@Source("PathViewerTreeStyles.css")
//		public MyResources css();
//	}
//
//	interface MyResources extends CssResource {
//
//		String link();
//
//		String node();
//
//		String border();
//	}
//
//	public PathViewerTree(SkwikiEntryPoint skWiki,
//			ArrayList<RevisionHistory> revisionHistory,
//			CheckoutHandler checkoutHandler, String uid, ListBox revisionList) {
//		super();
//		Bundle.INSTANCE.css().ensureInjected();
//
//		this.checkoutHandler = checkoutHandler;
//		this.revisionHistory = revisionHistory;
//		this.uid = uid;
//		this.revisionList = revisionList;
//		this.skWiki = skWiki;
//	}
//
//	public void start() {
//		// get tree layout
//		tree = D3.layout().tree().size(width, height);
//
//		// set the global way to draw paths
//		diagonal = D3.svg().diagonal()
//				.projection(new DatumFunction<Array<Double>>() {
//					@Override
//					public Array<Double> apply(Element context, Value d,
//							int index) {
//						// TreeDemoNode data = d.<TreeDemoNode> as();
//						// return Array.fromDoubles(data.x(), data.y());
//						return null;
//					}
//				});
//
//		// add the SVG
//		svg = D3.select(this).append("svg").attr("width", width + 20)
//				.attr("height", height + 280).append("g")
//				.attr("transform", "translate(10, 140)");
//
//		// create tree nodes based on the attributes of skWiki
//
//		columnNumMap.clear();
//		labelList.clear();
//		// canvas.clear();
//
//		if (revisionHistory.size() == 0) {
//			return;
//		}
//
//		// setup the column
//		boolean hasUID = false;
//		for (RevisionHistory tempRevisionHistory : revisionHistory) {
//			if (tempRevisionHistory.getId().equals(uid)) {
//				hasUID = true;
//				break;
//			}
//		}
//
//		// set the first column
//		int columnNumber = 0;
//		if (hasUID) {
//			columnNumMap.put(uid, Integer.valueOf(0));
//			columnNumber++;
//		}
//		// set the rest
//		for (RevisionHistory tempRevisionHistory : revisionHistory) {
//			if (columnNumMap.get(tempRevisionHistory.getId()) == null) {
//				columnNumMap.put(tempRevisionHistory.getId(),
//						Integer.valueOf(columnNumber));
//				columnNumber++;
//			}
//		}
//
//		int leftBackOffset = 20;
//		int rowCount = 0;
//
//		ArrayList<String> ids = new ArrayList<String>();
//		ArrayList<Integer> fromRevisions = new ArrayList<Integer>();
//
//		for (RevisionHistory tempRevisionHistory : revisionHistory) {
//			if (ids.indexOf(tempRevisionHistory.getId()) == -1) {
//
//				int key = ids.size();
//				ids.add(tempRevisionHistory.getId());
//
//				if (key < 4) {
//					userColorMap.put(tempRevisionHistory.getId(),
//							colors.get(key));
//					userColorKeyMap.put(tempRevisionHistory.getId(), key);
//				} else {
//					userColorMap.put(tempRevisionHistory.getId(), "#000000");
//					userColorKeyMap.put(tempRevisionHistory.getId(), key);
//				}
//			}
//
//			if (tempRevisionHistory.getFromRevision() != 0) {
//				fromRevisions.add(tempRevisionHistory.getFromRevision());
//			}
//
//			double x = (columnNumMap.get(tempRevisionHistory.getId()) + 1)
//					* Hspace - leftBackOffset;
//			double y = (rowCount + 1) * Vspace;
//
//			TreeDemoNode node1 = new TreeDemoNode();
//			node1.id(tempRevisionHistory.getRevision());
//			node1.setAttr("x0", x);
//			node1.setAttr("y0", y);
//			
//			allNodes.put(tempRevisionHistory.getRevision(), node1);
//			
//		}
//	}
//
//	// Text titleText = new Text((columnNumMap.get(tempRevisionHistory
//	// .getId()) + 1) * Hspace - 2 * leftBackOffset, 20,
//	// tempRevisionHistory.getId());
//	// titleText.setFontFamily("Calibri");
//	// canvas.add(titleText);
//	// int key = ids.size();
//	// ids.add(tempRevisionHistory.getId());
//	//
//	// if (key < 4) {
//	// titleText.setStrokeColor(colors.get(key));
//	// } else {
//	// titleText.setStrokeColor("#000000");
//	// }
//	// titleText.setFontSize(18);
//	// if (key < 4) {
//	// userColorMap.put(tempRevisionHistory.getId(), colors.get(key));
//	// userColorKeyMap.put(tempRevisionHistory.getId(), key);
//	// } else {
//	// userColorMap.put(tempRevisionHistory.getId(), "#000000");
//	// userColorKeyMap.put(tempRevisionHistory.getId(), key);
//	// }
//	// }
//	//
//	// if (tempRevisionHistory.getFromRevision() != 0) {
//	// fromRevisions.add(tempRevisionHistory.getFromRevision());
//	// }
//	//
//	// ViewLabel tempViewLabel = new ViewLabel(
//	// (columnNumMap.get(tempRevisionHistory.getId()) + 1) * Hspace
//	// - leftBackOffset, (rowCount + 1) * Vspace,
//	// "Revision " + tempRevisionHistory.getRevision(),
//	// tempRevisionHistory.getComment(),
//	// tempRevisionHistory.getRevision());
//	//
//	// circles.add(tempViewLabel.getCircle());
//	// tempViewLabel.getCircle().setColor(userColorMap.get(tempRevisionHistory.getId()));
//	//
//	// tempViewLabel.getCircle().addClickHandler(new ClickHandler() {
//	// @Override
//	// public void onClick(ClickEvent event) {
//	// // unhighlight all circles
//	// for (MyCircle circle : circles) {
//	// circle.unHighlight();
//	// }
//	//
//	// // Node selected
//	// MyCircle circle = (MyCircle) event.getSource();
//	// revisionList.setSelectedIndex(circle.getIndex() - 1);
//	//
//	// // highlight selected node
//	// circle.highlight();
//	//
//	// // handler checkout
//	// checkoutHandler.onClick(event, true);
//	// }
//	// });
//	// labelList.add(tempViewLabel);
//	// rowCount++;
//	// }
//	//
//	//
//	// for (RevisionHistory tempRevisionHistory : revisionHistory) {
//	// if (tempRevisionHistory.getFromRevision() != 0) {
//	// ViewLabel fromLabel = labelList.get(tempRevisionHistory
//	// .getFromRevision() - 1);
//	// ViewLabel toLabel = labelList.get(tempRevisionHistory
//	// .getRevision() - 1);
//	// Connection tempConnection = new Connection(fromLabel, toLabel);
//	//
//	// tempConnection.append(canvas);
//	//
//	// }
//	// }
//	//
//
//	class TreeDemoNode extends Node {
//		protected TreeDemoNode() {
//			super();
//		}
//
//		protected final native int id() /*-{
//			return this.id || -1;
//		}-*/;
//
//		protected final native int id(int id) /*-{
//			return this.id = id;
//		}-*/;
//
//		protected final native void setAttr(String name, JavaScriptObject value) /*-{
//			this[name] = value;
//		}-*/;
//
//		protected final native double setAttr(String name, double value) /*-{
//			return this[name] = value;
//		}-*/;
//
//		protected final native JavaScriptObject getObjAttr(String name) /*-{
//			return this[name];
//		}-*/;
//
//		protected final native double getNumAttr(String name) /*-{
//			return this[name];
//		}-*/;
//	}
//}
