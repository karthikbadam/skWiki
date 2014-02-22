package edu.purdue.pivot.skwiki.client.sketch;

import edu.purdue.pivot.skwiki.shared.CanvasPack;
import edu.purdue.pivot.skwiki.shared.DataPack;
import edu.purdue.pivot.skwiki.shared.history.AbstractHistory;
import edu.purdue.pivot.skwiki.shared.history.AddHistory;
import edu.purdue.pivot.skwiki.shared.history.HistoryManager;
import edu.purdue.pivot.skwiki.shared.history.MyColor;
import edu.purdue.pivot.skwiki.shared.history.PathHeadHistory;
import edu.purdue.pivot.skwiki.shared.history.Point;
import edu.purdue.pivot.skwiki.shared.history.RemoveHistory;
import gwt.g2d.client.graphics.KnownColor;
import gwt.g2d.client.graphics.Surface;
import gwt.g2d.shared.Color;
import gwt.g2d.shared.math.Rectangle;
import gwt.g2d.shared.math.Vector2;
import java.util.ArrayList;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.canvas.dom.client.Context2d.LineJoin;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mogaleaf.client.common.widgets.ColorHandler;
import com.mogaleaf.client.common.widgets.SimpleColorPicker;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;

public class TouchPad extends Surface implements AttachedPanel {

	CanvasToolbar toolbar;
	TouchPad surface = this;

	/* history stack */
	private HistoryManager myHistoryManager;
	private HistoryManager myHistoryManagerRedoStack;
	
	Boolean inFocus = false; 

	static int WIDTH = 1100;
	static int HEIGHT = 630;

	/* strokeSize, currentColor, and eraserSize */
	private int strokeSize = 2;
	private Color currentColor = KnownColor.BLACK;
	private int eraserSize = 5;

//	/* slider for strokeSize */
//	Slider hSlider = new Slider("Stroke Size");
//	HorizontalPanel sliderPanel = new HorizontalPanel();
//	Logger logger = Logger.getLogger("Skwiki");

//	com.smartgwt.client.widgets.Window colorWin = new com.smartgwt.client.widgets.Window();
//	com.smartgwt.client.widgets.Window sliderWin = new com.smartgwt.client.widgets.Window();

	int left;
	int top;
	int right;
	int bottom;

	int windowWidth;
	int windowHeight;

	/* canvas identifiers */
	String uuid;
	String uid;

	/* variables to store previous x and y */
	double oldx = -1;
	double oldy = -1;
	
	/* indexes of touch start events */
	ArrayList<Integer> indexes = new ArrayList<Integer>();
	ArrayList<Integer> strokeSizes = new ArrayList<Integer>();
	ArrayList<String> colorCache = new ArrayList<String>();
	ArrayList<Vector2> cache = new ArrayList<Vector2>();

	//private ArrayList<CanvasToolbar> toolbars;
	Boolean leftMouseDown = false;

	/* buffer 4 points before drawing a curve */
	Vector2 p0 = new Vector2();
	Vector2 p1 = new Vector2();
	Vector2 p2 = new Vector2();
	Vector2 p3 = new Vector2();
	int strokePointCount = 0;
	int bufferCount = 3;
	ArrayList<Vector2> buffList = new ArrayList<Vector2>();

	/* context2d */
	Context2d canvas_context;

	public TouchPad(String uuid, String uid, float scaleWidth,
			float scaleHeight, int windowWidth, int windowHeight) {
		super((int) (5 * windowWidth * scaleWidth / 6),
				(int) (5 * windowHeight / 6 * scaleHeight));
		WIDTH = (int) (5 * windowWidth / 6 * scaleWidth);
		HEIGHT = (int) (5 * windowHeight / 6 * scaleHeight);
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.uuid = uuid;
		this.uid = uid;
		this.fillBackground(KnownColor.TRANSPARENT);
		myHistoryManager = new HistoryManager(this);
		myHistoryManagerRedoStack = new HistoryManager(this);
		canvas_context = this.getContext();
	}

	public TouchPad(String uuid, String uid,
			ArrayList<CanvasToolbar> toolbars2, int windowWidth,
			int windowHeight) {

		super(windowWidth / 2, windowHeight / 2);
		WIDTH = windowWidth / 2;
		HEIGHT = windowHeight / 2;

		erase = false;

		this.uuid = uuid;
		this.uid = uid;

		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;

		this.fillBackground(KnownColor.TRANSPARENT);

		myHistoryManager = new HistoryManager(this);
		myHistoryManagerRedoStack = new HistoryManager(this);

		left = this.getAbsoluteLeft();
		top = this.getAbsoluteTop();
		right = left + WIDTH;
		bottom = top + HEIGHT;

//		this.toolbars = toolbars2;
//		toolbar = new CanvasToolbar(this);
		
		toolbar = CanvasToolbar.getInstance();
		toolbar.addTouchPad(this);
		
		canvas_context = this.getContext();

		/* add focus handler that works now */
		this.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				toolbar.setFocus();
				inFocus = true; 
			}
		});

		/* needs a mouse handler for M.S. Surface */

		this.addTouchStartHandler(new TouchStartHandler() {

			@Override
			public void onTouchStart(TouchStartEvent event) {
				myHistoryManagerRedoStack.clearHistory();
				leftMouseDown = true;

				if (erase == true) {

				} else {
					surface.getElement().getStyle().setCursor(Cursor.CROSSHAIR);

					int x = event.getTouches().get(0).getPageX()
							- surface.getAbsoluteLeft();
					int y = event.getTouches().get(0).getPageY()
							- surface.getAbsoluteTop();

					oldx = x;
					oldy = y;

					PathHeadHistory aPathHeadHistory = new PathHeadHistory(
							new Point(x, y), strokeSize, currentColor);
					myHistoryManager.addHistory(aPathHeadHistory);

					/* touch start */
					if (strokePointCount == 0) {
						p1.x = x;
						p1.y = y;
						p2.x = x;
						p2.y = y;
						strokePointCount++;
						strokePointCount++;
						canvas_context.setStrokeStyle(currentColor
								.getColorCode());
						canvas_context.setFillStyle(currentColor.getColorCode());
						canvas_context
								.setLineWidth(((double) strokeSize) * 0.7);
						canvas_context.beginPath();
						canvas_context.arc(x, y, ((double) strokeSize) * 0.7,
								0, 2 * Math.PI);
						canvas_context.fill();
						indexes.add(cache.size());
						cache.add(new Vector2(x, y));
						strokeSizes.add(strokeSize);
						colorCache.add(currentColor.getColorCode());
					}

				}
				event.preventDefault();
				event.stopPropagation();
			}
		});

		/* touch move handler */
		this.addTouchMoveHandler(new TouchMoveHandler() {
			@Override
			public void onTouchMove(TouchMoveEvent event) {

				double x = event.getTouches().get(0).getPageX()
						- surface.getAbsoluteLeft();
				double y = event.getTouches().get(0).getPageY()
						- surface.getAbsoluteTop();

				if (erase && leftMouseDown) {

					eraserSize = 20;
					Rectangle rectangle = new Rectangle(x, y, eraserSize,
							+eraserSize);
					surface.save().setFillStyle(KnownColor.WHITE)
							.fillRectangle(rectangle);
					RemoveHistory aRemoveHistory = new RemoveHistory(new Point(
							(int) x, (int) y), eraserSize);
					myHistoryManager.addHistory(aRemoveHistory);

				} else if (leftMouseDown) {

					AddHistory addHistory = new AddHistory(new Point((int) x,
							(int) y), new Point((int) x, (int) y), strokeSize,
							currentColor);
					myHistoryManager.addHistory(addHistory);

					if (strokePointCount == 2) {

						/* set stroke color and shadow */
						canvas_context.setStrokeStyle(currentColor
								.getColorCode());
						canvas_context.setFillStyle(currentColor.getColorCode());
						canvas_context
								.setLineWidth(((double) strokeSize) * 0.5);
						canvas_context.setLineCap(LineCap.ROUND);
						canvas_context.setLineJoin(LineJoin.ROUND);
						canvas_context
								.setShadowBlur(((double) strokeSize) * 0.3);
						canvas_context.setShadowColor(currentColor
								.getColorCode());

						/* latest touch point */
						p3.x = x;
						p3.y = y;
						p0.x = p1.x + (p1.x - p2.x);
						p0.y = p1.y + (p1.y - p2.y);
						strokePointCount++;

						/* buffer list */
						get_buffer(p0, p1, p2, p3, bufferCount);

						bufferCount = buffList.size();
						oldx = (int) buffList.get(0).x;
						oldy = (int) buffList.get(0).y;
						canvas_context.beginPath();
						canvas_context.moveTo(oldx, oldy);
						cache.add(new Vector2(oldx, oldy));
						
						/*
						 * draw all interpolated points found through Hermite
						 * interpolation
						 */
						int i = 0;
						for (i = 1; i < bufferCount - 2; i++) {
							x = buffList.get(i).x;
							y = buffList.get(i).y;
							cache.add(new Vector2(x, y));
							
							double nextx = buffList.get(i + 1).x;
							double nexty = buffList.get(i + 1).y;
							double c = (x + nextx) / 2;
							double d = (y + nexty) / 2;
							canvas_context.quadraticCurveTo(x, y, c, d);

						}
						x = buffList.get(i).x;
						y = buffList.get(i).y;
						cache.add(new Vector2(x, y));
						
						double nextx = buffList.get(i + 1).x;
						double nexty = buffList.get(i + 1).y;
						cache.add(new Vector2(nextx, nexty));
						
						canvas_context.quadraticCurveTo(x, y, nextx, nexty);

						/* adaptive buffering! based on distance between points */
						int distance = (int) Math.sqrt(Math.pow(x - p2.x, 2)
								+ Math.pow(y - p2.y, 2));
						bufferCount = ((int) distance / 4) > 2 ? (int) distance / 4
								: 3;
						bufferCount = bufferCount < 10 ? bufferCount : 10;

						// logger.log(Level.SEVERE, "bufferCount "+
						// bufferCount);
					} else {

						/* update the touch point list */
						p0.x = p1.x;
						p0.y = p1.y;
						p1.x = p2.x;
						p1.y = p2.y;
						p2.x = p3.x;
						p2.y = p3.y;
						p3.x = x;
						p3.y = y;

						get_buffer(p0, p1, p2, p3, bufferCount);

						/*
						 * draw all interpolated points found through Hermite
						 * interpolation
						 */
						bufferCount = buffList.size();
						int i = 1;
						for (i = 1; i < bufferCount - 2; i++) {
							x = buffList.get(i).x;
							y = buffList.get(i).y;
							cache.add(new Vector2(x, y));
							
							double nextx = buffList.get(i + 1).x;
							double nexty = buffList.get(i + 1).y;
							double c = (x + nextx) / 2;
							double d = (y + nexty) / 2;
							canvas_context.quadraticCurveTo(x, y, c, d);
						}
						x = buffList.get(i).x;
						y = buffList.get(i).y;
						cache.add(new Vector2(x, y));
						
						double nextx = buffList.get(i + 1).x;
						double nexty = buffList.get(i + 1).y;
						cache.add(new Vector2(nextx, nexty));
						
						canvas_context.quadraticCurveTo(x, y, nextx, nexty);
						canvas_context.stroke();
						surface.restore();

						/* adaptive buffering using Hermite interpolation */
						int distance = (int) Math.sqrt(Math.pow(x - p2.x, 2)
								+ Math.pow(y - p2.y, 2));
						bufferCount = ((int) distance / 4) > 2 ? (int) distance / 4
								: 3;
						bufferCount = bufferCount < 10 ? bufferCount : 10;
						// logger.log(Level.SEVERE, "bufferCount "+
						// bufferCount);
					}
				}
				event.preventDefault();
				event.stopPropagation();
			}
		});

		this.addTouchEndHandler(new TouchEndHandler() {

			@Override
			public void onTouchEnd(TouchEndEvent event) {
				leftMouseDown = false;
				event.preventDefault();
				surface.setStyleName("gwt-TouchPanelWidget");
				if (erase == true) {

				} else {
					surface.getElement().getStyle().setCursor(Cursor.DEFAULT);
				}
				event.stopPropagation();
				// erase = false;
				strokePointCount = 0;
				bufferCount = 3;

			}
		});
	}

	/* Cubic Hermite interpolation */
	private void get_buffer(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3,
			int bufferCount) {

		buffList.clear();
		double steps = 0;
		double step = 0.0;
		double t = 0.0;
		double tt = 0.0;
		double ttt = 0.0;
		double p0x = p0.x;
		double p1x = p1.x;
		double p2x = p2.x;
		double p3x = p3.x;
		double p0y = p0.y;
		double p1y = p1.y;
		double p2y = p2.y;
		double p3y = p3.y;

		int ipps = bufferCount;

		int i = 0;
		steps = ipps - 1;
		if (steps > 0) {
			step = 1.0 / steps;
			buffList.add(new Vector2(p1.x, p1.y));

			for (i = 1; i < steps; ++i) {
				t += step;
				tt = t * t;
				ttt = tt * t;
				buffList.add(new Vector2(0.5 * ((2 * p1x) + (-p0x + p2x) * t
						+ (2 * p0x - 5 * p1x + 4 * p2x - p3x) * tt + (-p0x + 3
						* p1x - 3 * p2x + p3x)
						* ttt), 0.5 * ((2 * p1y) + (-p0y + p2y) * t
						+ (2 * p0y - 5 * p1y + 4 * p2y - p3y) * tt + (-p0y + 3
						* p1y - 3 * p2y + p3y)
						* ttt)));
			}
			buffList.add(new Vector2(p2.x, p2.y));
		}
	}

//	public CanvasToolbar getToolbar() {
//		return toolbar;
//	}

	public void updateDataPack(DataPack data, int startIndex) {
		int i = 0;
		for (AbstractHistory tempHistory : myHistoryManager.historys) {
			if (i >= startIndex)
				data.updatedHistory.add(tempHistory);
			i++;
		}
	}

	@Override
	public Surface scale(double scale) {
		return this.scale(scale, scale);
	}

	public void updateDataPack(CanvasPack data, int startIndex) {
		int i = 0;
		for (AbstractHistory tempHistory : myHistoryManager.historys) {
			if (i >= startIndex)
				data.updatedHistory.add(tempHistory);
			i++;
		}
	}

	/* when the sketch is downloaded */
	public void renewImage(ArrayList<AbstractHistory> newHistorys) {
		myHistoryManager.clearHistory();
		indexes.clear();
		cache.clear();
		colorCache.clear();
		strokeSizes.clear();
		
		for (AbstractHistory tempHistory : newHistorys) {
			myHistoryManager.addHistory(tempHistory);
		}

		surface.clear();
		int start = 0;
		for (start = 0; start < myHistoryManager.historys.size(); start++) {

			AbstractHistory history = myHistoryManager.historys.get(start);
			if (history.getType() == "AddHistory") {

				Point endPos = ((AddHistory) history).endPos;
				MyColor pathColor = ((AddHistory) history).pathColor;
				strokeSize = ((AddHistory) history).strokeSize;
				double x = endPos.getVector2().getX();
				double y = endPos.getVector2().getY();

				if (strokePointCount == 2) {
					canvas_context.setStrokeStyle(pathColor.getColorCode());
					canvas_context.setFillStyle(pathColor.getColorCode());
					canvas_context.setLineWidth(((double) strokeSize) * 0.5);
					canvas_context.setLineCap(LineCap.ROUND);
					canvas_context.setLineJoin(LineJoin.ROUND);
					canvas_context.setShadowBlur(((double) strokeSize) * 0.3);
					canvas_context.setShadowColor(pathColor.getColorCode());
					
					/* get the x, y */
					p3.x = x;
					p3.y = y;
					p0.x = p1.x + (p1.x - p2.x);
					p0.y = p1.y + (p1.y - p2.y);
					strokePointCount++;

					
					get_buffer(p0, p1, p2, p3, bufferCount);
					bufferCount = buffList.size();
					oldx = (int) buffList.get(0).x;
					oldy = (int) buffList.get(0).y;
					canvas_context.beginPath();
					canvas_context.moveTo(oldx, oldy);
					cache.add(new Vector2(oldx, oldy));
					
					/*
					 * draw all interpolated points found through Hermite
					 * interpolation
					 */
					int i = 1;
					for (i = 1; i < bufferCount - 2; i++) {
						x = buffList.get(i).x;
						y = buffList.get(i).y;
						cache.add(new Vector2(x, y));
						double nextx = buffList.get(i + 1).x;
						double nexty = buffList.get(i + 1).y;
						double c = (x + nextx) / 2;
						double d = (y + nexty) / 2;
						canvas_context.quadraticCurveTo(x, y, c, d);
					}
					x = buffList.get(i).x;
					y = buffList.get(i).y;
					cache.add(new Vector2(x, y));
					double nextx = buffList.get(i + 1).x;
					double nexty = buffList.get(i + 1).y;
					cache.add(new Vector2(nextx, nexty));
					
					canvas_context.quadraticCurveTo(x, y, nextx, nexty);
					
					/* adaptive buffering using Hermite interpolation */
					int distance = (int) Math.sqrt(Math.pow(x - p2.x, 2)
							+ Math.pow(y - p2.y, 2));
					bufferCount = ((int) distance / 4) > 2 ? (int) distance / 4
							: 3;
					bufferCount = bufferCount < 10 ? bufferCount : 10;
					
				} else {

					/* update the touch point list */
					p0.x = p1.x;
					p0.y = p1.y;
					p1.x = p2.x;
					p1.y = p2.y;
					p2.x = p3.x;
					p2.y = p3.y;
					p3.x = x;
					p3.y = y;

					get_buffer(p0, p1, p2, p3, bufferCount);

					/*
					 * draw all interpolated points found through Hermite
					 * interpolation
					 */
					bufferCount = buffList.size();
					int i = 1;
					for (i = 1; i < bufferCount - 2; i++) {
						x = buffList.get(i).x;
						y = buffList.get(i).y;
						cache.add(new Vector2(x, y));
						double nextx = buffList.get(i + 1).x;
						double nexty = buffList.get(i + 1).y;
						double c = (x + nextx) / 2;
						double d = (y + nexty) / 2;
						canvas_context.quadraticCurveTo(x, y, c, d);

					}
					x = buffList.get(i).x;
					y = buffList.get(i).y;
					cache.add(new Vector2(x, y));
					
					double nextx = buffList.get(i + 1).x;
					double nexty = buffList.get(i + 1).y;
					cache.add(new Vector2(nextx, nexty));
					
					canvas_context.quadraticCurveTo(x, y, nextx, nexty);
					canvas_context.stroke();
					surface.restore();

					/* adaptive buffering using Hermite interpolation */
					int distance = (int) Math.sqrt(Math.pow(x - p2.x, 2)
							+ Math.pow(y - p2.y, 2));
					bufferCount = ((int) distance / 4) > 2 ? (int) distance / 4
							: 3;
					bufferCount = bufferCount < 10 ? bufferCount : 10;
					// logger.log(Level.SEVERE, "bufferCount "+
					// bufferCount);
				}

			} else if (history.getType() == "PathHeadHistory") {
				
				MyColor pathColor = ((PathHeadHistory) history).pathColor;
				strokeSize = ((PathHeadHistory) history).strokeSize;
				
				Point position = ((PathHeadHistory) history).position;
				double x = position.getVector2().getX();
				double y = position.getVector2().getY();
				strokePointCount = 0;

				/* initialize the points */
				p1.x = x;
				p1.y = y;
				p2.x = x;
				p2.y = y;
				strokePointCount++;
				strokePointCount++;
				
				/* add index to indexes list */
				indexes.add(cache.size());
				cache.add(new Vector2(x, y));
				strokeSizes.add(strokeSize);
				colorCache.add(pathColor.getColorCode());
				
				/* set stroke color, size, and shadow */
				canvas_context.setStrokeStyle(pathColor.getColorCode());
				canvas_context.setFillStyle(pathColor.getColorCode());
				canvas_context.setLineWidth(((double) strokeSize) * 0.4);
				canvas_context.beginPath();
				canvas_context.arc(x, y, ((double) strokeSize) * 0.4, 0,
						2 * Math.PI);
				canvas_context.fill();

				bufferCount = 3;

			}
		}
		strokePointCount = 0;
	}

	private void redrawfromCache() {
		surface.clear();
		
		for (int i = 0; i < indexes.size(); i++) {
			int index = indexes.get(i);
			Vector2 touchStart = cache.get(index);
			String color = colorCache.get(i);
			int strokeSize = strokeSizes.get(i);
			double x = touchStart.getX();
			double y = touchStart.getY();
			
			if (index > 0) {
				canvas_context.stroke();
				surface.restore();
			}
			
			canvas_context.setStrokeStyle(color);
			canvas_context.setFillStyle(color);
			canvas_context.setLineWidth(((double) strokeSize) * 0.4);
			canvas_context.beginPath();
			canvas_context.arc(x, y, ((double) strokeSize) * 0.4, 0,
					2 * Math.PI);
			canvas_context.fill();
			
			
			canvas_context.beginPath();
			canvas_context.setStrokeStyle(color);
			canvas_context.setFillStyle(color);
			canvas_context.setLineWidth(((double) strokeSize));
			canvas_context.setLineCap(LineCap.ROUND);
			canvas_context.setLineJoin(LineJoin.ROUND);
			canvas_context.setShadowBlur(((double) strokeSize) * 0.3);
			canvas_context.setShadowColor(color);
			canvas_context.moveTo(x, y);
			
			Vector2 touchMove;
			
			for (int j = index+1; j < cache.size() - 1; j++) {
				if (i == indexes.size()-1) {
					
				} else if ( j < indexes.get(i + 1) - 1) {
					
				} else {
					canvas_context.stroke();
					break;
				}
				
				touchMove = cache.get(j);
				x = touchMove.getX();
				y = touchMove.getY();
				double nextx = cache.get(j+1).getX();
				double nexty = cache.get(j+1).getY();
				double c = (x + nextx) / 2;
				double d = (y + nexty) / 2;
				canvas_context.quadraticCurveTo(x, y, c, d);
			}
			canvas_context.stroke();
			
		}
	}

	public int getHistorySize() {
		return myHistoryManager.historys.size();
	}

	public int chopHistory(float factor) {
		int removeNumber = Math.round(myHistoryManager.historys.size()
				* (1 - factor));

		int i;
		for (i = 0; i < removeNumber; i++) {
			myHistoryManager.historys
					.remove(myHistoryManager.historys.size() - 1);
		}
		return removeNumber;
	}

	public TouchPad getDrawingSurface() {
		return this;
	}

	public void UpdateSize() {
		WIDTH = this.getCoordinateSpaceWidth();
		HEIGHT = this.getCoordinateSpaceHeight();
	}

	protected void setColor(int red, int green, int blue) {
		currentColor = new Color(red, green, blue);
	}


	public void changeColor() {
//		colorWin.show();
//		colorWin.setLeft(windowWidth - colorWin.getWidth() - 10);
//		colorWin.setTop(60);
//		colorWin.getHeader().setHeight(28);
	}

	public void changeColor(Color tempColor) {
		currentColor = tempColor;
	}

	public void changeStrokeSize() {
//		if (currentColor == KnownColor.WHITE) {
//			currentColor = KnownColor.BLACK;
//			return;
//		}
//
//		if (erase) {
//			erase = false;
//			return;
//		}
//
//		sliderWin.show();
//		sliderWin.setLeft(windowWidth - sliderWin.getWidth() - 10);
//		sliderWin.setTop(windowHeight - sliderWin.getHeight() - 20);
//		sliderWin.getHeader().setHeight(28);
//
//		erase = false;

	}

	public void changeStrokeSize(int tempStrokeSize) {
		if (currentColor == KnownColor.WHITE) {
			currentColor = KnownColor.BLACK;
		}
		
		strokeSize = tempStrokeSize; 
		erase = false;
	}

	
	public void changeOpacity() {

	}

	double currentState = 1.0;

	public void undo() {
		int size = myHistoryManager.historys.size();

		int removeSize = 20;
		if (size < removeSize) {
			return;
		}

		for (int i = size - 1; i > size - removeSize; i--) {
			AbstractHistory tempHistory = myHistoryManager.historys.remove(i);
			myHistoryManagerRedoStack.historys.add(tempHistory);
		}

		redrawfromCache();
		// renewImage();
		// clearSurface();
		// currentState = currentState - 0.03;
		// myHistoryManager.moveToState(currentState);
	}

	public void redo() {
		int removeSize = 20;
		int redoStackSize = myHistoryManagerRedoStack.historys.size();

		if (redoStackSize == 0) {
			return;
		}

		for (int i = redoStackSize - 1; i > redoStackSize - removeSize; i--) {
			AbstractHistory tempHistory = myHistoryManagerRedoStack.historys
					.remove(i);
			myHistoryManager.addHistory(tempHistory);
		}
		redrawfromCache();
	}

	public void redraw() {
		redrawfromCache();
	}

	@Override
	public String getID() {
		return uuid;
	}

	boolean erase = false;

	public void erase() {
		// if (erase == false) {
		// erase = true;
		// } else {
		// erase = false;
		// }

		if (currentColor == KnownColor.WHITE) {
			currentColor = KnownColor.BLACK;
		} else {
			currentColor = KnownColor.WHITE;
		}
	}
}
