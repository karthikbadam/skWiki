package gwt.g2d.client.graphics;

import gwt.g2d.shared.Color;

import com.google.gwt.canvas.dom.client.CanvasPixelArray;

/**
 * ImageData wrapper around the GWT ImageData, so that it can work with Color instead of
 * the incredibly inwieldy getRedAt etc functions.
 * @author Karel
 *
 */
public class ImageData {
	
	
	// image data object
	com.google.gwt.canvas.dom.client.ImageData imageData;
	
	
	/**
	 * Create an ImageData object.
	 */
	public ImageData(com.google.gwt.canvas.dom.client.ImageData imageData) {
		this.imageData = imageData;
	}
	
	/**
	 * Returns the original image data.
	 */
	public com.google.gwt.canvas.dom.client.ImageData getGWTImageData() {
		return imageData;
	}
	
	
	/**
	 * Set the color at a given location.
	 */
	public void setColor(int x, int y, Color color) {
		imageData.setRedAt(color.red, x, y);
		imageData.setGreenAt(color.green, x, y);
		imageData.setBlueAt(color.blue, x, y);
		imageData.setAlphaAt((int)(color.alpha * 255), x, y);
	}
	
	
	/**
	 * Get the color at a given location.
	 */
	public Color getColor(int x, int y) {
		return new Color(imageData.getRedAt(x,  y), imageData.getGreenAt(x,  y), imageData.getBlueAt(x,  y), imageData.getAlphaAt(x, y) / 255.0);
	}
	
	
	/**
	 * Get the alpha at a given location.
	 */
	public double getAlpha(int x, int y) {
		return imageData.getAlphaAt(x,  y) / 255.0;
	}
	
	
	/**
	 * Return the raw pixel array.
	 */
	public CanvasPixelArray getData() {
		return imageData.getData();
	}

}
