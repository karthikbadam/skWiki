package edu.purdue.pivot.skwiki.shared.history;

import gwt.g2d.client.graphics.KnownColor;

public class PathProfile {

	int stokeSize;
	KnownColor pathColor;
	KnownColor startPoint;

	public PathProfile(int stokeSize, KnownColor pathColor) {

		this.stokeSize = stokeSize;
		this.pathColor = pathColor;

	}

	public KnownColor getPathColor() {
		return pathColor;
	}

	public int getStokeSize() {
		return stokeSize;
	}

	public void setPathColor(KnownColor pathColor) {
		this.pathColor = pathColor;
	}

	public void setStokeSize(int stokeSize) {
		this.stokeSize = stokeSize;
	}

}
