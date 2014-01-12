package edu.purdue.pivot.skwiki.shared.history;

import gwt.g2d.client.graphics.Surface;

import java.io.Serializable;

public class AbstractHistory implements Serializable {

	public int historyNumber = 0;
	int INFINITY = 100000;
	public double leftDistance = INFINITY;
	public double rightDistance = INFINITY;
	public Point distance = new Point(0, 0);
	// Color color = KnownColor.BLACK;

	public Point position;

	public AbstractHistory() {

		position = new Point(0, 0);
	}

	public Point getPosition() {
		return position;
	}

	public String getType() {
		return "Abstract";
	}

	public void perform(Surface surface) {

	}

}
