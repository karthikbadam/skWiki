package edu.purdue.pivot.skwiki.client.vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class VectorToolbar extends Composite {
	public interface Images extends ImageBundle {

		AbstractImagePrototype rectangle();

		AbstractImagePrototype polygon();

		AbstractImagePrototype circle();

		AbstractImagePrototype line();

		AbstractImagePrototype curve();

	}

	public interface Strings extends Constants {

		String rectangle();

		String polygon();

		String circle();

		String line();

		String curve();

	}

	private Images images = (Images) GWT.create(Images.class);
	private Strings strings = (Strings) GWT.create(Strings.class);
	private EventHandler handler = new EventHandler();
	private VectorPanel vectorCanvas;

	private ToggleButton circle;
	private ToggleButton rectangle;
	private ToggleButton line;
	private ToggleButton polygon;
	private ToggleButton curve;

	public VectorToolbar(VectorPanel vectorCanvas) {
		VerticalPanel basePanel = new VerticalPanel();
		initWidget(basePanel);
		this.vectorCanvas = vectorCanvas;

		basePanel.add(circle = createToggleButton(images.circle(),
				strings.circle()));
		basePanel.add(rectangle = createToggleButton(images.rectangle(),
				strings.rectangle()));
		basePanel.add(line = createToggleButton(images.line(), strings.line()));
		basePanel.add(polygon = createToggleButton(images.polygon(),
				strings.polygon()));
		basePanel.add(curve = createToggleButton(images.curve(),
				strings.curve()));

	}

	private class EventHandler implements ClickHandler, KeyUpHandler {

		@Override
		public void onClick(final ClickEvent event) {
			Widget sender = (Widget) event.getSource();

			if (sender == circle) {
				vectorCanvas.addCircle();
			} else if (sender == rectangle) {
				vectorCanvas.addRectangle();
			} else if (sender == line) {
				vectorCanvas.addLine();
			} else if (sender == polygon) {
				vectorCanvas.addPolygon();
			} else if (sender == curve) {
				vectorCanvas.addCurve();
			}
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {
		}
	}

	private ToggleButton createToggleButton(final AbstractImagePrototype img,
			final String tip) {
		ToggleButton tb = new ToggleButton(img.createImage());
		tb.addClickHandler(handler);
		tb.setTitle(tip);
		return tb;
	}

}
