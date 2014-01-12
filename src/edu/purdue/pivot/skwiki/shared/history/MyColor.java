package edu.purdue.pivot.skwiki.shared.history;


import java.io.Serializable;
import java.util.Arrays;
import gwt.g2d.client.math.MathHelper;

/**
 * Stores a color. Color is immutable.
 * 
 * @author hao1300@gmail.com
 */
public class MyColor implements Serializable {	
	protected static  double DEFAULT_ALPHA = 1.0;
	
	private static  long serialVersionUID = 5370658935618812361L;
	/**
	 * Linear interpolation between two colors.
	 * 
	 * @param value1
	 * @param value2
	 * @param amount the amount to interpolate [0.0, 1.0].
	 * @return a new interpolated color.
	 */
	public static final MyColor lerp(MyColor value1, MyColor value2, double amount) {
		return new MyColor(
				(int) MathHelper.lerp(value1.getR(), value2.getR(), amount),
				(int) MathHelper.lerp(value1.getG(), value2.getG(), amount),
				(int) MathHelper.lerp(value1.getB(), value2.getB(), amount),
				MathHelper.lerp(value1.getAlpha(), value2.getAlpha(), amount));
	}
	/**
	 * Smooth interpolation between two colors.
	 * 
	 * @param value1
	 * @param value2
	 * @param amount the amount to interpolate [0.0, 1.0].
	 * @return a new interpolated color.
	 */
	public static final MyColor smoothStep(MyColor value1, MyColor value2, double amount) {
		return new MyColor(
				(int) MathHelper.smoothStep(value1.getR(), value2.getR(), amount),
				(int) MathHelper.smoothStep(value1.getG(), value2.getG(), amount),
				(int) MathHelper.smoothStep(value1.getB(), value2.getB(), amount),
				MathHelper.smoothStep(value1.getAlpha(), value2.getAlpha(), amount));
	}
	private  String colorCode;
	
	private  int red, green, blue;
	private  double alpha;
	
	/**
	 * Constructor.
	 * 
	 * @param colorCode the string representing this color.
	 * @param red red channel [0-255]
	 * @param green green channel [0-255]
	 * @param blue blue channel [0-255]
	 * @param alpha alpha channel [0.0, 1.0]
	 */
	
	public MyColor()
	{
		this.colorCode = "BLACK";
		this.red = 0;
		this.green = 0;
		this.blue = 0;
		this.alpha = 0.0;
		
	}
	
	/**
	 * Construct a web-safe color. The color code is represented in the form
	 * "#RRGGBB" which represents an RGB color using hexadecimal numbers.
	 * 
	 * @param red red channel [0-255]
	 * @param green green channel [0-255]
	 * @param blue blue channel [0-255]
	 */
	public MyColor(int red, int green, int blue) {
		StringBuilder stringBuilder = new StringBuilder("#000000");
		String hexString = Integer.toHexString(getHexValue(red, green, blue));
		colorCode = stringBuilder.replace(
				stringBuilder.length() - hexString.length(), 
				stringBuilder.length(), 
				hexString).toString();
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = DEFAULT_ALPHA;
	}
	
	/**
	 * Represents a color that contains transparency.
	 * The color code is represented in the form "rgba(r,g,b,a)".
	 * 
	 * @param red red channel [0-255]
	 * @param green green channel [0-255]
	 * @param blue blue channel [0-255]
	 * @param alpha alpha channel [0.0, 1.0]
	 */
	public MyColor(int red, int green, int blue, double alpha) {
		this(new StringBuilder(21)
				.append("rgba(")
				.append(red).append(',')
				.append(green).append(',')
				.append(blue).append(',')
				.append(alpha).append(')')
				.toString(), 
				red, green, blue, alpha);
	}
	
	protected MyColor(String colorCode, int red, int green, int blue, double alpha) {
		this.colorCode = colorCode;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public final boolean equals(MyColor rhs) {
		return getR() == rhs.getR() && getG() == rhs.getG() && getB() == rhs.getB()
				&& getAlpha() == rhs.getAlpha();
	}
	
	@Override
	public final boolean equals(Object obj) {
		return (obj instanceof MyColor) ? equals((MyColor) obj) : false;
	}
	
	/**
	 * Gets the value of the alpha channel.
	 * 
	 * @return a value between 0.0 to 1.0, inclusive.
	 */
	public final double getAlpha() {
		return alpha;
	}
	
	/**
	 * Gets the value of the blue channel.
	 * 
	 * @return a value between 0 to 255, inclusive.
	 */
	public final int getB() {
		return blue;
	}
	
	/**
	 * Gets the string representation of the color.
	 */
	public final String getColorCode() {
		return colorCode;
	}
	
	/**
	 * Gets the value of the green channel.
	 * 
	 * @return a value between 0 to 255, inclusive.
	 */
	public final int getG() {
		return green;
	}
	
	/**
	 * Gets the integer value of the given rgb value.
	 */
	private final int getHexValue(int r, int g, int b) {
		return ((r << 16) & 0xFF0000) | ((g << 8) & 0xFF00) | (b & 0xFF);
	}
	
	/**
	 * Gets the value of the red channel.
	 * 
	 * @return a value between 0 to 255, inclusive.
	 */
	public final int getR() {
		return red;
	}
	
	@Override
	public final int hashCode() {
		return Arrays.hashCode(new double[]{getHexValue(getR(), getG(), getB()), getAlpha()});
	}
	
	@Override
	public final String toString() {
		return getColorCode();
	}
}
