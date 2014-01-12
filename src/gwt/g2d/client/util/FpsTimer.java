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

import java.util.LinkedList;

import com.google.gwt.user.client.Timer;

/**
 * A fixed FPS Timer. The desired FPS may or may not be reached depending on 
 * the complexity of each loop.
 * 
 * The implementation of this class is based on part of utils3d.js in WebGL's 
 * Hello World demo.
 * 
 * @author hao1300@gmail.com
 */
public abstract class FpsTimer {
	private static final int FRAMERATE_UPDATE_INTERVAL = 500; 
	private FpsTimerImpl timer = new FpsTimerImpl();
	private Timer fpsUpdateTimer = new Timer() {
		@Override
		public void run() {
			float tot = 0;
	    for (Float fps : timer.framerates) {
	      tot += fps;
	    }
	    fps = tot / timer.framerates.size();
		}
	};
	private float fps;
	private int desiredFps;
	
	public FpsTimer() {
		this(30);
	}
	
	public FpsTimer(int desiredFps) {
		this.desiredFps = desiredFps;
	}
	
	/**
	 * Starts the FPS timer.
	 */
	public void start() {
		timer.renderTime = System.currentTimeMillis();
		timer.scheduleRepeating(1000 / desiredFps);
		fpsUpdateTimer.scheduleRepeating(FRAMERATE_UPDATE_INTERVAL);
	}

	/**
	 * Cancels the FPS timer.
	 */
	public void cancel() {
		timer.cancel();
		fpsUpdateTimer.cancel();
	}

	/**
	 * Sets the desired frame rate per seconds.
	 * 
	 * @param desiredFps
	 */
	public void setDesiredFps(int desiredFps) {
		this.desiredFps = desiredFps;
	}

	/**
	 * Gets the desired frame rate per seconds.
	 */
	public int getDesiredFps() {
		return desiredFps;
	}
	
	/**
	 * Gets the current FPS, which may be different from the desired fps.
	 */
	public float getFps() {
		return fps;
	}
	
	/**
	 * Overrides this method to perform update per frame.
	 */
	public abstract void update();
	
	/**
	 * Helper class for checking the FPS.
	 */
	public class FpsTimerImpl extends Timer {
		private LinkedList<Float> framerates = new LinkedList<Float>();
    private long renderTime;
    
		@Override
		public void run() {
			long newTime = System.currentTimeMillis();
			int t = (int) (newTime - this.renderTime);
			float framerate = 1000f / t;
			framerates.addLast(framerate);
			while (framerates.size() > desiredFps) {
				framerates.removeFirst();
			}
			renderTime = newTime;
			update();
		}
	}
}
