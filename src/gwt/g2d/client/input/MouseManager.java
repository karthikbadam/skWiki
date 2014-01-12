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

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Manages the state of the mouse on an object. 
 * 
 * @author hao1300@gmail.com
 */
public class MouseManager extends InputManager<HasAllMouseHandlers> 
		implements MouseDownHandler, MouseMoveHandler, MouseOutHandler, 
		MouseOverHandler, MouseUpHandler, MouseWheelHandler {
	private final Set<Integer> pressedKeys = new HashSet<Integer>();
	private int clientX, clientY, screenX, screenY, x, y;
	private int wheelDelta;
	private final List<HandlerRegistration> handlers = 
			new ArrayList<HandlerRegistration>(5);  
	
	@Override
	public void manage(HasAllMouseHandlers obj) {
		if (!handlers.isEmpty()) {
			throw new IllegalStateException(
					"This MouseManager is already managing another object.");
		}
		handlers.add(obj.addMouseDownHandler(this));
		handlers.add(obj.addMouseMoveHandler(this));
		handlers.add(obj.addMouseOutHandler(this));
		handlers.add(obj.addMouseOverHandler(this));
		handlers.add(obj.addMouseUpHandler(this));
		handlers.add(obj.addMouseWheelHandler(this));
	}

	@Override
	public void unmanage() {
		wheelDelta = 0;
		clientX = 0;
		clientY = 0;
		screenX = 0;
		screenY = 0;
		x = 0;
		y = 0;
		pressedKeys.clear();
		for (HandlerRegistration handler : handlers) {
			handler.removeHandler();
		}
		handlers.clear();
	}
	
	/**
	 * Returns true if the given mouse button is down. The mouse buttons are
   * {@link com.google.gwt.dom.client.NativeEvent#BUTTON_LEFT},
   * {@link com.google.gwt.dom.client.NativeEvent#BUTTON_RIGHT},
   * {@link com.google.gwt.dom.client.NativeEvent#BUTTON_MIDDLE}
   * 
	 * @param button
	 */
	public boolean isButtonDown(int button) {
		return pressedKeys.contains(button);
	}

	/**
   * Gets the mouse x-position within the browser window's client area.
   * 
   * @return the mouse x-position
   */
	public int getClientX() {
		return clientX;
	}
	
	/**
   * Gets the mouse y-position within the browser window's client area.
   * 
   * @return the mouse y-position
   */
	public int getClientY() {
		return clientY;
	}
	
	/**
   * Gets the mouse x-position on the user's display.
   * 
   * @return the mouse x-position
   */
  public int getScreenX() {
    return screenX;
  }

  /**
   * Gets the mouse y-position on the user's display.
   * 
   * @return the mouse y-position
   */
  public int getScreenY() {
    return screenY;
  }

  /**
   * Gets the mouse x-position relative to the event's current target element.
   * 
   * @return the relative x-position
   */
  public int getX() {
    return x;
  }

  /**
   * Gets the mouse y-position relative to the event's current target element.
   * 
   * @return the relative y-position
   */
  public int getY() {
    return y;
  }
	
	@Override
	public void onMouseDown(MouseDownEvent event) {
		pressedKeys.add(event.getNativeButton());
		handleMouseEvent(event);
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		handleMouseEvent(event);
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		handleMouseEvent(event);
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		handleMouseEvent(event);
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		pressedKeys.remove(event.getNativeButton());
		handleMouseEvent(event);
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		wheelDelta += event.getDeltaY();
		handleMouseEvent(event);
	}
	
	/**
	 * Notifies this MouseManager that a MouseEvent is being handled.
	 * Stores the mouse position and prevent the default action from taking 
	 * place if isPreventDefault() is true.
	 * 
	 * @param <H>
	 * @param event
	 */
	private <H extends EventHandler> void handleMouseEvent(MouseEvent<H> event) {
		clientX = event.getClientX();
		clientY = event.getClientY();
		screenX = event.getScreenX();
		screenY = event.getScreenY();
		x = event.getX();
		y = event.getY();
		handleEvent(event);
	}
}
