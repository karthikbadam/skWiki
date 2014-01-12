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
 * A simple cycle tracker that counts for a given number of ticks per cycle.
 * 
 * @author hao1300@gmail.com
 */
public class Cycle {
	private CountDown countDown;
	private int numCycle;
	
	/**
	 * Creates an infinite cycle that counts for the given number of ticks per 
	 * cycle.
	 */
	public Cycle(int count) {
		this(count, -1);
	}
	
	/**
	 * Creates a cycle tracker that runs for the given number of cycles.
	 * 
	 * @param count
	 */
	public Cycle(int count, int numCycle) {
		this.countDown = new CountDown(count);
		this.numCycle = numCycle;
	}
	
	/**
	 * Perform a tick in the cycle.
	 * 
	 * @return true if the cycle has completed.
	 */
	public final boolean cycleTick() {
		if (countDown.isCompleted() && (numCycle != 0)) {
			countDown.reset();
			if (numCycle > 0) {
				numCycle--;
			}
		}
		return countDown.tick();
	}
	
	/**
	 * Resets the count down in this cycle.
	 */
	public final void resetTick() {
		countDown.reset();
	}
}
