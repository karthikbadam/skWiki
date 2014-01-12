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
package gwt.g2d.client.util;

/**
 * A simple count-down that counts the number of times that tick() is called.
 * 
 * @author hao1300@gmail.com
 */
public class CountDown {
	private int maxCount;
	private int count;
	
	public CountDown(int count) {
		this.maxCount = count;
	}
	
	/**
	 * Decrement the count down.
	 * 
	 * @return true if tick() has been called for at least as many times
	 * 				 the number of count down specified. 
	 */
	public final boolean tick() {
		++count;
		return isCompleted();
	}
	
	/**
	 * Checks whether the count down has been completed.
	 * 
	 * @return true if the count down is completed.
	 */
	public final boolean isCompleted() {
		return count >= maxCount;
	}
	
	/**
	 * Gets the percentage of down down that this has completed.
	 */
	public final double getPercent() {
		return count / (double) maxCount;
	}
	
	/**
	 * Resets the count.
	 */
	public final void reset() {
		count = 0;
	}
}
