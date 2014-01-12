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
package gwt.g2d.client.framework;

import gwt.g2d.client.graphics.Surface;
import gwt.g2d.client.util.FpsTimer;
import gwt.g2d.shared.math.Vector2;

import com.google.gwt.user.client.Timer;

/**
 * Abstract class for running and rendering an application.
 * 
 * This class is deprecated and will be removed in future release. Please
 * consider using {@link FpsTimer} instead.
 * 
 * @author hao1300@gmail.com
 */
@Deprecated
public abstract class AbstractApp {	
	private Surface primarySurface;
	private Timer timer;
	private boolean paused;
	
	public AbstractApp(Surface surface) {
		primarySurface = surface;
	}
	
	public AbstractApp(int width, int height) {
		this(new Surface(width, height));
	}
	
	public AbstractApp(Vector2 size) {
		this(new Surface(size));
	}
	
	/**
	 * Gets the primary surface for the application.
	 */
	public final Surface getPrimarySurface() {
		return primarySurface;
	}
	
	/**
	 * Gets whether the application is paused.
	 */
	public boolean isPaused() {
		return paused;
	}
	
	/**
	 * Sets whether the application is paused.
	 * Pausing the application will not take place immediately; instead, it will 
	 * be paused before the next update loop. 
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	/**
	 * Exits the application.
	 */
	public void exit() {
		timer.cancel();
	}
	
	/**
	 * Runs the application at the desired FPS.
	 * The application will try to call update and draw as many times as the 
	 * given frames per second, but it may be called less often if there is a 
	 * performance hit in either method.
	 * 
	 * @param fps the number of frames per second to run the application at.
	 */
	public void run(int fps) {
		initialize();
		timer = new Timer() {
			@Override
			public void run() {
				if (isPaused()) {
					return;
				}
				update();
			}
		};
		timer.scheduleRepeating(1000 / fps);
	}
	
	/**
	 * Performs initialization logics before the application right before the 
	 * application is run.
	 */
	public abstract void initialize();
	
	/**
	 * Updates the application.
	 * Override this method to perform the actions that occurs every fps.
	 */
	public abstract void update();
}
