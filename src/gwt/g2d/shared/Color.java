/*
 * Copyright 2009 Hao Nguyen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gwt.g2d.shared;

import gwt.g2d.client.math.MathHelper;
import java.io.Serializable;
import java.util.Arrays;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;


/**
 * Stores a color. Color is immutable.
 * 
 * @author hao1300@gmail.com
 */
public class Color implements Serializable {	
	protected static final double DEFAULT_ALPHA = 1.0;
	
	private static final long serialVersionUID = 5370658935618812361L;
	private final String colorCode;
	public final int red, green, blue;
	public final double alpha;
	
	/**
	 * Constructor.
	 * 
	 * @param colorCode the string representing this color.
	 * @param red red channel [0-255]
	 * @param green green channel [0-255]
	 * @param blue blue channel [0-255]
	 * @param alpha alpha channel [0.0, 1.0]
	 */
	protected Color(String colorCode, int red, int green, int blue, double alpha) {
		this.colorCode = colorCode;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
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
	public Color(int red, int green, int blue, double alpha) {
		this(new StringBuilder(21)
				.append("rgba(")
				.append(red).append(',')
				.append(green).append(',')
				.append(blue).append(',')
				.append(alpha).append(')')
				.toString(), 
				red, green, blue, alpha);
	}
	
	/**
	 * Construct a web-safe color. The color code is represented in the form
	 * "#RRGGBB" which represents an RGB color using hexadecimal numbers.
	 * 
	 * @param red red channel [0-255]
	 * @param green green channel [0-255]
	 * @param blue blue channel [0-255]
	 */
	public Color(int red, int green, int blue) {
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
	 * Linear interpolation between two colors.
	 * 
	 * @param value1
	 * @param value2
	 * @param amount the amount to interpolate [0.0, 1.0].
	 * @return a new interpolated color.
	 */
	public static final Color lerp(Color value1, Color value2, double amount) {
		return new Color(
				(int) MathHelper.lerp(value1.getR(), value2.getR(), amount),
				(int) MathHelper.lerp(value1.getG(), value2.getG(), amount),
				(int) MathHelper.lerp(value1.getB(), value2.getB(), amount),
				MathHelper.lerp(value1.getAlpha(), value2.getAlpha(), amount));
	}
	
	
	/**
	 * Linear interpolation between two colors with alpha premultiplication for better
	 * graphical effects.
	 * 
	 * @param value1
	 * @param value2
	 * @param amount the amount to interpolate [0.0, 1.0].
	 * @return a new interpolated color.
	 */
	public static final Color lerpPremultiplied(Color c1, Color c2, double amount) {
		double alpha = MathHelper.lerp(c1.getAlpha(), c2.getAlpha(), amount);
		return new Color(
				(int) (MathHelper.lerp(c1.red * c1.alpha, c2.red * c2.alpha, amount)/alpha),
				(int) (MathHelper.lerp(c1.green * c1.alpha, c2.green * c2.alpha, amount)/alpha),
				(int) (MathHelper.lerp(c1.blue * c1.alpha, c2.blue * c2.alpha, amount)/alpha),
				alpha);
	}
	
	
	/**
	 * Smooth interpolation between two colors.
	 * 
	 * @param value1
	 * @param value2
	 * @param amount the amount to interpolate [0.0, 1.0].
	 * @return a new interpolated color.
	 */
	public static final Color smoothStep(Color value1, Color value2, double amount) {
		return new Color(
				(int) MathHelper.smoothStep(value1.getR(), value2.getR(), amount),
				(int) MathHelper.smoothStep(value1.getG(), value2.getG(), amount),
				(int) MathHelper.smoothStep(value1.getB(), value2.getB(), amount),
				MathHelper.smoothStep(value1.getAlpha(), value2.getAlpha(), amount));
	}
	
	/**
	 * Gets the string representation of the color.
	 */
	public final String getColorCode() {
		return colorCode;
	}
	
	/**
	 * Gets the value of the red channel.
	 * 
	 * @return a value between 0 to 255, inclusive.
	 */
	public final int getR() {
		return red;
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
	 * Gets the value of the blue channel.
	 * 
	 * @return a value between 0 to 255, inclusive.
	 */
	public final int getB() {
		return blue;
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
	 * Return the color code (identical to getColorCode).
	 */
	@Override
	public final String toString() {
		return getColorCode();
	}
	
	
	// regular expressions for parsing a color
	private static RegExp PatternRGBA = RegExp.compile("rgba\\(\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*,\\s*([0-9]*\\.?[0-9]+)\\s*\\)"); // rgba
	private static RegExp PatternRGB = RegExp.compile("rgb\\(\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*\\)"); // rgb
	private static RegExp PatternHex = RegExp.compile("\\#([0-9a-fA-F][0-9a-fA-F])([0-9a-fA-F][0-9a-fA-F])([0-9a-fA-F][0-9a-fA-F])"); // html color code #XXXXXX	
	
	
	
	/**
	 * Parse a color from a color code.
	 */
	public static final Color parseColor(String s) {
		
		// rgba pattern
		MatchResult res = PatternRGBA.exec(s);
		if (res != null && res.getGroupCount() == 5) return new Color(Integer.parseInt(res.getGroup(1)), Integer.parseInt(res.getGroup(2)), Integer.parseInt(res.getGroup(3)), Double.parseDouble(res.getGroup(4)));
		
		// rgb pattern
		res = PatternRGB.exec(s);
		if (res != null && res.getGroupCount() == 4) return new Color(Integer.parseInt(res.getGroup(1)), Integer.parseInt(res.getGroup(2)), Integer.parseInt(res.getGroup(3)));
		
		// hex pattern
		res = PatternHex.exec(s);
		if (res != null && res.getGroupCount() == 4) return new Color(Integer.parseInt(res.getGroup(1), 16), Integer.parseInt(res.getGroup(2), 16), Integer.parseInt(res.getGroup(3), 16));
		
		// no match
		return null;
	}
	
	@Override
	public final boolean equals(Object obj) {
		return (obj instanceof Color) ? equals((Color) obj) : false;
	}
	
	public final boolean equals(Color rhs) {
		return getR() == rhs.getR() && getG() == rhs.getG() && getB() == rhs.getB()
				&& getAlpha() == rhs.getAlpha();
	}
	
	@Override
	public final int hashCode() {
		return Arrays.hashCode(new double[]{getHexValue(red, green, blue), alpha});
	}
	
	/**
	 * Gets the integer value of the given rgb value.
	 */
	private final int getHexValue(int r, int g, int b) {
		return ((r << 16) & 0xFF0000) | ((g << 8) & 0xFF00) | (b & 0xFF);
	}
}
