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
package gwt.g2d.client.math;

/**
 * Utility class that provides helper methods for performing math calculation
 * in addition to those in the Math class.
 * 
 * @author hao1300@gmail.com
 */
public final class MathHelper {	
  public static final double PI_OVER_2 = Math.PI / 2.0;
  public static final double PI_OVER_4 = Math.PI / 4.0;
  public static final double TWO_PI = Math.PI * 2.0;
  
  private MathHelper() {
  }
  
  /**
   * Smooth interpolation using Catmull-Rom spline.
   * 
   * @param value1
   * @param value2
   * @param value3
   * @param value4
   * @param amount the amount to interpolate [0.0, 1.0].
   * @return the interpolated value.
   */
  public static final double catmullRom(double value1, double value2, double value3, 
  		double value4, double amount) {
    return (0.5 * (2.0 * value2 +
        (value3 - value1) * amount +
        (2.0 * value1 - 5.0 * value2 + 4.0 * value3 - value4) * square(amount) +
        (3.0 * value2 - value1 - 3.0 * value3 + value4) * cube(amount)));
  }
  
  /**
   * Clamp the given value in the range [min, max].
   * 
   * @param value
   * @param min the minimum value that the given value can take (inclusive).
   * @param max the maximum value that the given value can take (inclusive).
   * @return return the value in [min, max]
   */
  public static final double clamp(double value, double min, double max) {
    return (value > max) ? max : ((value < min) ? min : value);
  }
  
  /**
   * Returns the cube of a value.
   * 
   * @param value the value to raise to the cubic power.
   * @return the cubic power of the given value.
   */
  public static final double cube(double value) {
  	return value * value * value;
  }
  
  /**
   * Gets the absolute distance between two values.
   * 
   * @param value1
   * @param value2
   * @return the absolute distance between value1 and value2.
   */
  public static final double distance(double value1, double value2) {
    return Math.abs(value1 - value2);
  }
  
  /**
   * Interpolate using a Hermite spline.
   * 
   * @param value1
   * @param tangent1
   * @param value2
   * @param tangent2
   * @param amount the amount to interpolate [0.0, 1.0].
   * @return the interpolated value.
   */
  public static final double hermite(double value1, double tangent1, double value2, 
  		double tangent2, double amount) {
    if (amount == 0.0) {
      return value1;
    } else if (amount == 1.0) { 
      return value2;
    } else {
      return (2 * value1 - 2 * value2 + tangent2 + tangent1) * cube(amount) 
      		+ (3 * value2 - 3 * value1 - 2 * tangent1 - tangent2) * square(amount) 
      		+ tangent1 * amount + value1;
    }
  }
  
  /**
   * Linear interpolation between two values.
   * 
   * @param value1
   * @param value2
   * @param amount the amount to interpolate [0.0, 1.0].
   * @return the interpolated value.
   */
  public static final double lerp(double value1, double value2, double amount) {
    return value1 + (value2 - value1) * amount;
  }
  
  /**
   * Smooth interpolation between two values.
   * 
   * @param value1
   * @param value2
   * @param amount the amount to interpolate [0.0, 1.0].
   * @return the interpolated value.
   */
  public static final double smoothStep(double value1, double value2, double amount) {
  	if (amount == 0.0) {
      return value1;
    } else if (amount == 1.0) { 
      return value2;
    } else {
	  	return (2 * value1 - 2 * value2) * cube(amount) 
					+ (3 * value2 - 3 * value1) * square(amount) + value1;
    }
  }
  
  /**
   * Returns the square of a value.
   * 
   * @param value the value to square.
   * @return the square of the given value.
   */
  public static final double square(double value) {
  	return value * value;
  }
}
