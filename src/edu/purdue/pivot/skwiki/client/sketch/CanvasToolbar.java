package edu.purdue.pivot.skwiki.client.sketch;

import gwt.g2d.shared.Color;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mogaleaf.client.common.widgets.ColorHandler;
import com.mogaleaf.client.common.widgets.SimpleColorPicker;
import com.smartgwt.client.types.HeaderControls;
import com.smartgwt.client.widgets.HeaderControl;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Slider;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.ValueChangedEvent;
import com.smartgwt.client.widgets.events.ValueChangedHandler;

public class CanvasToolbar extends Composite {

	private EventHandler handler = new EventHandler();
	// private TouchPad canvas;

	private ImgButton color;
	private ImgButton strokeSize;
	private ImgButton eraser;
	private ImgButton undo;
	private ImgButton redo;
	private ImgButton clear;
	private ImgButton resize;

	static CanvasToolbar toolbarInstance = null;

	public ArrayList<TouchPad> touchpads = new ArrayList<TouchPad>();

	com.smartgwt.client.widgets.Window colorWin = new com.smartgwt.client.widgets.Window();
	com.smartgwt.client.widgets.Window sliderWin = new com.smartgwt.client.widgets.Window();

	/* slider for strokeSize */
	Slider hSlider = new Slider("Stroke Size");
	HorizontalPanel sliderPanel = new HorizontalPanel();

	// public CanvasToolbar(TouchPad canvas) {
	// FlexTable basePanel = new FlexTable();
	// initWidget(basePanel);
	// this.canvas = canvas;
	//
	// basePanel.setWidget(0, 0, color = createImgButton("color.png"));
	// basePanel.setWidget(1, 0,
	// strokeSize = createImgButton("strokeSize.png"));
	// basePanel.setWidget(2, 0, eraser = createImgButton("eraser.png"));
	// basePanel.setWidget(3, 0, undo = createImgButton("undo.png"));
	// basePanel.setWidget(4, 0, redo = createImgButton("redo.png"));
	// }

	public CanvasToolbar() {
		FlexTable basePanel = new FlexTable();
		initWidget(basePanel);

		basePanel.setWidget(0, 0, color = createImgButton("color.png"));
		basePanel.setWidget(1, 0,
				strokeSize = createImgButton("strokeSize.png"));
		basePanel.setWidget(2, 0, eraser = createImgButton("eraser.png"));
		basePanel.setWidget(3, 0, undo = createImgButton("undo.png"));
		basePanel.setWidget(4, 0, redo = createImgButton("redo.png"));

		/* slider for the stroke size */
		hSlider.setVertical(false);
		hSlider.setMinValue(1);
		hSlider.setMaxValue(50);
		hSlider.setWidth(500);
		hSlider.setNumValues(50);
		hSlider.setValue(2);
		hSlider.addValueChangedHandler(new ValueChangedHandler() {
			@Override
			public void onValueChanged(ValueChangedEvent event) {
				int strokeSize1 = event.getValue();
				for (int i = 0; i < touchpads.size(); i++) {
					touchpads.get(i).changeStrokeSize(strokeSize1);
				}
			}
		});

		sliderPanel.add(hSlider);
		sliderWin.setTitle("Stroke Size");
		sliderWin.setShowMinimizeButton(true);
		HeaderControl close = new HeaderControl(HeaderControl.CLOSE,
				new com.smartgwt.client.widgets.events.ClickHandler() {
					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						sliderWin.hide();
					}

				});

		close.setWidth(25);
		close.setHeight(25);
		sliderWin.setHeaderControls(HeaderControls.HEADER_LABEL, close);


		sliderWin.setCanDragReposition(true);
		sliderWin.setCanDragResize(true);
		sliderWin.addItem(sliderPanel);
		sliderWin.setAutoSize(true);
		sliderWin.setCanDragReposition(true);
		sliderWin.setCanDragResize(true);
		
		/* settings window for color palette */
		colorWin.setTitle("Color Palette");
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
				Color currentColor = new Color(tempColor.getRed(), tempColor
						.getGreen(), tempColor.getBlue());
				for (int i = 0; i < touchpads.size(); i++) {
					touchpads.get(i).changeColor(currentColor);
				}
			}
		});

		
		HeaderControl closeColorWin = new HeaderControl(HeaderControl.CLOSE,
				new com.smartgwt.client.widgets.events.ClickHandler() {
					@Override
					public void onClick(
							com.smartgwt.client.widgets.events.ClickEvent event) {
						colorWin.hide();
					}

				});

		closeColorWin.setWidth(25);
		closeColorWin.setHeight(25);
		colorWin.setHeaderControls(HeaderControls.HEADER_LABEL, closeColorWin);

		picker.setHeight("250px");
		picker.setWidth("160px");

		colorWin.addItem(new SimplePanel(picker));
		
		colorWin.setCanDragReposition(true);
		colorWin.setCanDragResize(true);
		colorWin.setAutoSize(true);
		colorWin.setCanDragReposition(true);
		colorWin.setCanDragResize(true);
		
	}

	
	
	public static CanvasToolbar getInstance() {
		if (toolbarInstance == null) {
			toolbarInstance = new CanvasToolbar();
		}
		return toolbarInstance;
	}

	public void addTouchPad(TouchPad canvas) {
		touchpads.add(canvas);
	}

	private ImgButton createImgButton(String string) {
		ImgButton button = new ImgButton();
		button.setWidth(48);
		button.setHeight(48);
		button.setSrc(string);
		button.setLabelHPad(5);
		button.setLabelVPad(10);
		button.addClickHandler(handler);
		return button;
	}

	private class EventHandler implements
			com.smartgwt.client.widgets.events.ClickHandler, KeyUpHandler {

		@Override
		public void onClick(
				final com.smartgwt.client.widgets.events.ClickEvent event) {
			Widget sender = (Widget) event.getSource();

			if (sender == color) {
				colorWin.show();
				colorWin.setLeft(800);
				colorWin.setTop(60);
				colorWin.getHeader().setHeight(28);
			} else if (sender == strokeSize) {
				sliderWin.show();
				sliderWin.setLeft(800 - sliderWin.getWidth() - 10);
				sliderWin.setTop(500 - sliderWin.getHeight() - 20);
				sliderWin.getHeader().setHeight(28);
			} else if (sender == eraser) {
				for (int i = 0; i < touchpads.size(); i++) {
					touchpads.get(i).erase();
				}
			} else if (sender == undo) {
				for (int i = 0; i < touchpads.size(); i++) {
					if (touchpads.get(i).inFocus == true) {
						touchpads.get(i).undo();
					}
				}
			} else if (sender == redo) {
				for (int i = 0; i < touchpads.size(); i++) {
					if (touchpads.get(i).inFocus == true) {
						touchpads.get(i).redo();
					}
				}
			} else if (sender == clear) {
				for (int i = 0; i < touchpads.size(); i++) {
					if (touchpads.get(i).inFocus == true) {
						touchpads.get(i).clear();
					}
				}
			} else if (sender == resize) {
				// canvas.resize();
			}
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {
		}
	}

	public void setFocus() {
		for (int i = 0; i < touchpads.size(); i++) {
			touchpads.get(i).inFocus = false;
		}
	}

}
