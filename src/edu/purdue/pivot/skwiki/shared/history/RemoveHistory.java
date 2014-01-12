package edu.purdue.pivot.skwiki.shared.history;

import gwt.g2d.client.graphics.KnownColor;
import gwt.g2d.client.graphics.Surface;
import gwt.g2d.shared.math.Rectangle;

public class RemoveHistory extends AbstractHistory {

	// private Eraser eraser;
	public int eraserSize;

	// Point position;

	public RemoveHistory() {
		eraserSize = 5;
	}

	public RemoveHistory(Point root, int eraserSize) {
		super();
		this.position = root;
		this.eraserSize = eraserSize;
	}

	public String getColor() {

		return "null";
	}

	@Override
	public String getType() {

		return "RemoveHistory";
	}

	@Override
	public void perform(Surface surface) {

		eraserSize = 20;
		Rectangle rectangle = new Rectangle(position.getVector2().getX(),
				position.getVector2().getY(), eraserSize, +eraserSize);
		surface.setFillStyle(KnownColor.WHITE).fillRectangle(rectangle);
	}

	@Override
	public String toString() {
		String returnString = null;

		returnString = position.toString() + "    " + "NULL    " + "    "
				+ "NULL     " + "REMOVE";

		return returnString;
	}

}
