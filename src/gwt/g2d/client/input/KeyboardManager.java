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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Manages the state of the keyboard on an object. 
 * 
 * @author hao1300@gmail.com
 */
public class KeyboardManager extends InputManager<HasAllKeyHandlers> 
		implements KeyDownHandler, KeyPressHandler, KeyUpHandler {
	private final Set<Integer> pressedKeys = new HashSet<Integer>();
	private final List<HandlerRegistration> handlers = 
			new ArrayList<HandlerRegistration>(3);  
	
	@Override
	public void manage(HasAllKeyHandlers obj) {
		if (!handlers.isEmpty()) {
			throw new IllegalStateException(
					"This KeyboardManager is already managing another object.");
		}
		handlers.add(obj.addKeyDownHandler(this));
		handlers.add(obj.addKeyUpHandler(this));
		handlers.add(obj.addKeyUpHandler(this));
	}

	@Override
	public void unmanage() {
		pressedKeys.clear();
		for (HandlerRegistration handler : handlers) {
			handler.removeHandler();
		}
		handlers.clear();
	}
	
	/**
	 * Returns true if the given keycode is held down.
	 * These key codes are enumerated in the {@link KeyCodes} class.
	 * 
	 * @param keycode
	 */
	public boolean isButtonDown(int keycode) {
		return pressedKeys.contains(keycode);
	}
	
	@Override
	public void onKeyDown(KeyDownEvent event) {
		pressedKeys.add(event.getNativeKeyCode());
		handleEvent(event);
	}
	
	@Override
	public void onKeyPress(KeyPressEvent event) {
		pressedKeys.add(event.getNativeEvent().getKeyCode());
		handleEvent(event);
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		pressedKeys.remove(event.getNativeKeyCode());
		handleEvent(event);
	}
}
