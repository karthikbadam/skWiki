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
import gwt.g2d.client.graphics.shapes.ShapeBuilder;
import gwt.g2d.shared.Color;
import gwt.g2d.shared.math.Rectangle;
import gwt.g2d.shared.math.Vector2;

import java.util.ArrayList;

import com.google.gwt.canvas.dom.client.Context2d.LineJoin;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mogaleaf.client.common.widgets.ColorHandler;
import com.mogaleaf.client.common.widgets.SimpleColorPicker;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;

public class TouchPad extends Surface implements AttachedPanel {

	/**
	 * History Handling
	 **/
	CanvasToolbar toolbar;
	
	TouchPad surface = this;

	private HistoryManager myHistoryManager;
	
	private HistoryManager myHistoryManagerRedoStack;

	
	static int WIDTH = 1100;
	static int HEIGHT = 630;

	private int strokeSize = 2;
	private Color currentColor = KnownColor.BLACK;
	private int eraserSize = 5;

	private Vector2 lastPosition = new Vector2();
	
	int left;
	int top;
	int right;
	int bottom;

	int windowWidth;
	int windowHeight;
	
	String uuid;
	String uid;
	
	int oldx = -1;
	int oldy = -1;
	
	private ArrayList<CanvasToolbar> toolbars;
	Boolean leftMouseDown = false; 
	
	
	public TouchPad(String uuid, String uid, float scaleWidth, float scaleHeight,  int windowWidth, int windowHeight) {
		super((int) (5*windowWidth * scaleWidth/6), (int) (5*windowHeight/6 * scaleHeight));
		WIDTH = (int) (5*windowWidth/6  * scaleWidth); 
		HEIGHT =(int) (5*windowHeight/6 * scaleHeight);
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.uuid = uuid;
		this.uid = uid;
		this.fillBackground(KnownColor.TRANSPARENT);
		myHistoryManager = new HistoryManager(this);
		myHistoryManagerRedoStack = new HistoryManager(this);
	}

	//slider
	Slider hSlider = new Slider("Stroke Size");
	HorizontalPanel sliderPanel = new HorizontalPanel();
    
	
	public TouchPad(String uuid, String uid, ArrayList<CanvasToolbar> toolbars2, int windowWidth, int windowHeight) {
		super(windowWidth/3, windowHeight/3);
		WIDTH = windowWidth/3; 
		HEIGHT = windowHeight/3;
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
		this.toolbars = toolbars2;
		toolbar = new CanvasToolbar(this);
		
		//hSlider for the stroke size
		hSlider.setVertical(false);  
        hSlider.setMinValue(1);  
        hSlider.setMaxValue(50);
        hSlider.setWidth(500);
        hSlider.setNumValues(50);  
        hSlider.setValue(strokeSize);
        hSlider.addValueChangedHandler(new ValueChangedHandler() {  
            @Override
			public void onValueChanged(ValueChangedEvent event) {  
               strokeSize = event.getValue();
            }  
        });  
        
        sliderPanel.add(hSlider);
        sliderWin.setTitle("Stroke Size");
        sliderWin.setShowMinimizeButton(true);
        sliderWin.addCloseClickHandler(new  com.smartgwt.client.widgets.events.CloseClickHandler() {
           
			@Override
			public void onCloseClick(CloseClickEvent event) {
				sliderWin.hide();
			}
        });
        
        sliderWin.setCanDragReposition(true);  
        sliderWin.setCanDragResize(true);  
        sliderWin.addItem(sliderPanel);
        sliderWin.setAutoSize(true);
		
        //add focus handler that works now
		this.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				for (CanvasToolbar temp_toolbar : toolbars) {
					temp_toolbar.setVisible(false);
				}
				
				toolbar.setVisible(true);
				
			}
			
		});
		
		// needs a mouse handler for M.S. Surface
		//TODO need a permanent solution for this
		this.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				
				leftMouseDown = true;
				
				// surface.setStyleName("gwt-TouchPanelWidget-focus");
				if (erase == true) {

				} else {
					surface.getElement().getStyle().setCursor(Cursor.CROSSHAIR);
					// UpdateSize();
					
					int x = event.getX();
					int y = event.getY();
					
					oldx = x;
					oldy = y;
					
					PathHeadHistory aPathHeadHistory = new PathHeadHistory(
							new Point(x, y), strokeSize, currentColor);
					myHistoryManager.addHistory(aPathHeadHistory);
					lastPosition.set(x, y);
					
				}	
				
			}

		});

		
		//mouse move handler
		this.addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				
				int x = event.getX();
				int y = event.getY();
				
				if (erase && leftMouseDown) {

					eraserSize = 20;
					Rectangle rectangle = new Rectangle(x, y, eraserSize,
							+eraserSize);
					surface.save().setFillStyle(KnownColor.WHITE)
							.fillRectangle(rectangle);
					RemoveHistory aRemoveHistory = new RemoveHistory(new Point(
							x, y), eraserSize);
					myHistoryManager.addHistory(aRemoveHistory);

				} else if (leftMouseDown){

					if (isCursorResize(x, y)) {

					} else {
						surface.getElement().getStyle().setCursor(Cursor.CROSSHAIR);
						surface.save();
						surface.setLineJoin(LineJoin.ROUND);
						surface.setStrokeStyle(currentColor);
						surface.setLineWidth(strokeSize);
						surface.setFillStyle(currentColor);

						int newx = (int) (x * 0.3 + oldx * 0.7);
						int newy = (int) (y * 0.3 + oldy * 0.7);

						surface.strokeShape(new ShapeBuilder().drawLineSegment(
								oldx, oldy, newx, newy).build());

						AddHistory addHistory = new AddHistory(new Point(
								lastPosition), new Point(newx, newy),
								strokeSize, currentColor);
						myHistoryManager.addHistory(addHistory);

						// draw();
						surface.restore();
						lastPosition.set(newx, newy);

						oldx = newx;
						oldy = newy;
					}
				}
				event.preventDefault();
				event.stopPropagation();
			}
		});
		
		
		this.addMouseUpHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				
				leftMouseDown = false;
				event.preventDefault();
				surface.setStyleName("gwt-TouchPanelWidget");
				if (erase == true) {

				} else {
					surface.getElement().getStyle().setCursor(Cursor.DEFAULT);
				}
				event.stopPropagation();
				//erase = false;
			
			}
			
		});
	}

	public void draw() {
		surface.clear();
		int historySize = myHistoryManager.historys.size();
		int start = 0;
		for (AbstractHistory addHistory : myHistoryManager.historys) {
			if (addHistory.getType() == "AddHistory") {

				Point position = ((AddHistory) addHistory).position;
				Point endPos = ((AddHistory) addHistory).endPos;

				MyColor pathColor = ((AddHistory) addHistory).pathColor;
				double oldx = position.getVector2().getX();
				double oldy = position.getVector2().getY();

				double x = 0.3 * endPos.getVector2().getX() + 0.7 * oldx;
				double y = 0.3 * endPos.getVector2().getY() + 0.7 * oldy;

				strokeSize = ((AddHistory) addHistory).strokeSize;
				surface.save().setLineJoin(LineJoin.ROUND);
				surface.setStrokeStyle(
						new Color(pathColor.getR(), pathColor.getG(), pathColor
								.getB())).setLineWidth(strokeSize);
				surface.strokeShape(new ShapeBuilder().drawLineSegment(
						position.getVector2(), endPos.getVector2()).build());

				surface.restore();

			} else if (addHistory.getType() == "PathHeadHistory") {

			}
		}
	}

	public CanvasToolbar getToolbar() {
		return toolbar;
	}

	protected boolean isCursorResize(int x, int y) {
		
		return false;
	}

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

	public void clearSurface() {
		myHistoryManager.moveToState(0.00);
		this.clear();
	}

	public void moveToState(double statePercent) {
		myHistoryManager.moveToState(statePercent);
	}

	public void renewImage() {
		clearSurface();
		myHistoryManager.moveToState(1.00);
	}

	public void renewImage(ArrayList<AbstractHistory> newHistorys) {
		myHistoryManager.clearHistory();
		for (AbstractHistory tempHistory : newHistorys) {
			myHistoryManager.addHistory(tempHistory);
		}
		clearSurface();
		myHistoryManager.moveToState(1.00);
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
		
		final DialogBox dialog = new DialogBox();
		final SimpleColorPicker picker = new SimpleColorPicker();

		picker.addListner(new ColorHandler() {
			@Override
			public void newColorSelected(String color) {
				color = color.replace("#", "");
				edu.purdue.pivot.skwiki.client.sketch.colorpicker.Color tempColor = new edu.purdue.pivot.skwiki.client.sketch.colorpicker.Color();
				try {
					tempColor.setHex(color);
				} catch (Exception e) {
					e.printStackTrace();
				}
				currentColor = new Color(tempColor.getRed(), tempColor
						.getGreen(), tempColor.getBlue());
				dialog.hide();
			}
		});
		picker.setWidth("200px");
		picker.setHeight("200px");
		dialog.add(new SimplePanel(picker));
		dialog.show();
		dialog.setAutoHideEnabled(true);
		dialog.setPopupPosition(windowWidth - 250, 50);
		dialog.getElement().getStyle().setZIndex(10);
		// dialog.center();

	}

	final com.smartgwt.client.widgets.Window sliderWin = new com.smartgwt.client.widgets.Window();
    
	public void changeStrokeSize() {
		if (currentColor == KnownColor.WHITE) {
			currentColor = KnownColor.BLACK;
			return;
		}
		
		if (erase) {
			erase = false;
			return;
		}
		
		sliderWin.show();
		sliderWin.setLeft(windowWidth - sliderWin.getWidth() - 10);
		sliderWin.setTop(windowHeight - sliderWin.getHeight() - 20);

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
		
		for (int i = size - 1; i > size - removeSize; i-- ) {
			AbstractHistory tempHistory = myHistoryManager.historys.remove(i);
			myHistoryManagerRedoStack.historys.add(tempHistory);
		}
		
		renewImage();
		//clearSurface();
		//currentState = currentState - 0.03;
		//myHistoryManager.moveToState(currentState);
	}

	public void redo() {
		int removeSize = 20;
		int redoStackSize = myHistoryManagerRedoStack.historys.size();
		
		if (redoStackSize == 0) {
			return;
		}
		
		for (int i = redoStackSize - 1; i > redoStackSize - removeSize; i--) {
			AbstractHistory tempHistory = myHistoryManagerRedoStack.historys.remove(i);
			myHistoryManager.addHistory(tempHistory);
		}
		renewImage();
	}

	@Override
	public String getID() {
		return uuid;
	}

	boolean erase = false;

	public void erase() {
//		if (erase == false) {
//			erase = true;
//		} else {
//			erase = false;
//		}
		
		if (currentColor == KnownColor.WHITE) {
			currentColor = KnownColor.BLACK;
		} else {
			currentColor = KnownColor.WHITE;
		}
		
	}

	public void resize() {
		surface.setSize(surface.getOffsetWidth() + 50,
				surface.getOffsetHeight() + 50);
		surface.renewImage();
	}

}
