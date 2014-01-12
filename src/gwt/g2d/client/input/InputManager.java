/*
 * Copyright 2010 Hao Nguyen
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
package gwt.g2d.client.input;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Interface for defining a manager that manages an input device, allowing the
 * user to query the state of the input device at each point in time without
 * the use of a handler or listener. 
 * 
 * @author hao1300@gmail.com
 */
public abstract class InputManager<T extends HasHandlers> {
	private boolean preventDefault;
	
	/**
	 * Let this InputManager manages the given object's input state.
	 * A InputManager can only manages one object at a time. To manage a 
	 * different object, call {@link #unmanage()} first. 
	 *  
	 * @param obj
	 */
	public abstract void manage(T obj);
	
	/**
	 * Unmanages the managed object.
	 * If no object is being managed, then calling this should have no effect.
	 */
	public abstract void unmanage();
	
	
	/**
	 * Sets whether or not to prevent the default action from taking place.
	 * true if the default action is to be prevented.
	 * Default: false.
	 * 
	 * @param preventDefault
	 */
	public void setPreventDefault(boolean preventDefault) {
		this.preventDefault = preventDefault;
	}
	
	/**
	 * Gets whether or not to prevent the default action from taking place.
	 */
	public boolean isPreventDefault() {
		return preventDefault;
	}
	
	/**
	 * Notifies this InputManager that an event is being handled. 
	 * This prevents the default action from taking place if isPreventDefault() 
	 * is true.
	 */
	protected <H extends EventHandler> void handleEvent(DomEvent<H> e) {
		if (isPreventDefault()) {
			e.preventDefault();
		}
	}
}
