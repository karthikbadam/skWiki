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
package edu.purdue.pivot.skwiki.client.sketch;

import gwt.g2d.client.graphics.Surface;
import gwt.g2d.client.util.FpsTimer;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for all demo.
 * 
 * @author hao1300@gmail.com
 */
public abstract class AbstractCanvas extends FpsTimer {
	protected static final int WIDTH = 100, HEIGHT = 100;
	private final Surface surface;
	private final String demoName;
	private final Panel parentContainer;
	
	public AbstractCanvas(String demoName, Panel parentContainer) {
		super(60);
		surface = new Surface(WIDTH, HEIGHT);
		this.demoName = demoName;
		this.parentContainer = parentContainer;
		parentContainer.clear();
	}
	
	/**
	 * Gets the container where this demo is contained.
	 */
	protected void add(Widget child) {
		parentContainer.add(child);
	}
	
	/**
	 * Gets the name of the demo.
	 */
	public String getDemoName() {
		return demoName;
	}

	/**
	 * Gets the surface for the demo.
	 */
	public Surface getPrimarySurface() {
		return surface;
	}
	
	/**
	 * Initializes the demo.
	 */
	public abstract void initialize();
}
