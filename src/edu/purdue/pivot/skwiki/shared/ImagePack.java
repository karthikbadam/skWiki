package edu.purdue.pivot.skwiki.shared;

import java.io.Serializable;


public class ImagePack implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 379553067944469640L;
	public double leftX;
	public double topY;
	public int width;
	public int height;
	public int layoutHeight;
	public int layoutWidth;

	public String URL;
	public String id;
	
	public ImagePack()
	{
		
	}

	public ImagePack(String URL, double leftX, double topY, int width, int height,
			int layoutWidth, int layoutHeight, String uuid) {
		
		this.URL = URL;
		this.leftX = leftX;
		this.topY = topY;
		this.width = width;
		this.height = height;
		this.layoutHeight = layoutHeight;
		this.layoutWidth = layoutWidth;
		this.id = uuid;
	}
	
	
}
