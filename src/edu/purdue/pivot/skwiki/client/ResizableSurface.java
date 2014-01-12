package edu.purdue.pivot.skwiki.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;

import gwt.g2d.client.graphics.Surface;

public class ResizableSurface extends Surface {
	private boolean bDragDrop = false;
	private boolean move = false;
	private Element movingPanelElement;
	private List panelResizedListeners = new ArrayList();

	public ResizableSurface(int width, int height) {
		super(width, height);
		
		//listen to mouse-events
        DOM.sinkEvents(this.getElement(),
        		Event.ONMOUSEDOWN |
        		Event.ONMOUSEMOVE | 
                Event.ONMOUSEUP |
                Event.ONMOUSEOVER
        );
	}

	@Override
	public void onBrowserEvent(Event event) {
		final int eventType = DOM.eventGetType(event);
		if (Event.ONMOUSEOVER == eventType) {
        	//show different cursors
            if (isCursorResize(event)) {
                DOM.setStyleAttribute(this.getElement(), "cursor", "se-resize");
            } else if(isCursorMove(event)){
                DOM.setStyleAttribute(this.getElement(),"cursor", "se-resize");
            }else {
                DOM.setStyleAttribute(this.getElement(), "cursor", "default");
            }
        }
        if (Event.ONMOUSEDOWN == eventType) {
            if (isCursorResize(event)) {
            	//enable/disable resize
                if (bDragDrop == false) {
                	bDragDrop = true;

                    DOM.setCapture(this.getElement());
                }
            }else if(isCursorMove(event)){
                DOM.setCapture(this.getElement());
                move = true;
            }
        } else if (Event.ONMOUSEMOVE == eventType) {
        	//reset cursor-type
            if(!isCursorResize(event)&&!isCursorMove(event)){
                DOM.setStyleAttribute(this.getElement(), "cursor", "default");
            }
            
            //calculate and set the new size
            if (bDragDrop == true) {
            	int absX = DOM.eventGetClientX(event);
                int absY = DOM.eventGetClientY(event);
                int originalX = DOM.getAbsoluteLeft(this.getElement());
                int originalY = DOM.getAbsoluteTop(this.getElement());
                
                //do not allow mirror-functionality
                if(absY>originalY && absX>originalX){
                    Integer height = absY-originalY+2;
                    this.setHeight(height + "px");

                    Integer width = absX-originalX+2;
                    this.setWidth(width + "px");
                    notifyPanelResizedListeners(width, height);
                }
            }else if(move == true){
                RootPanel.get().setWidgetPosition(this, DOM.eventGetClientX(event),DOM.eventGetClientY(event));
            }
        } else if (Event.ONMOUSEUP == eventType) {
        	//reset states
            if(move == true){
                move = false;
                DOM.releaseCapture(this.getElement());
            }
            if (bDragDrop == true) {
            	bDragDrop = false;
                DOM.releaseCapture(this.getElement());
            }
        }
	}

	protected boolean isCursorResize(Event event) {
		int cursorY = DOM.eventGetClientY(event);
		int initialY = this.getAbsoluteTop();
		int height = this.getOffsetHeight();

		int cursorX = DOM.eventGetClientX(event);
		int initialX = this.getAbsoluteLeft();
		int width = this.getOffsetWidth();

		// only in bottom right corner (area of 10 pixels in square)
		if (((initialX + width - 10) < cursorX && cursorX <= (initialX + width))
				&& ((initialY + height - 10) < cursorY && cursorY <= (initialY + height)))
			return true;
		else
			return false;
	}

	public void setMovingPanelElement(Element movingPanelElement) {
		this.movingPanelElement = movingPanelElement;
	}

	protected boolean isCursorMove(Event event) {
		if (movingPanelElement != null) {
			int cursorY = DOM.eventGetClientY(event);
			int initialY = movingPanelElement.getAbsoluteTop();
			int cursorX = DOM.eventGetClientX(event);
			int initialX = movingPanelElement.getAbsoluteLeft();

			if (initialY <= cursorY && initialX <= cursorX)
				return true;
			else
				return false;
		} else
			return false;
	}

	public void addPanelResizedListener(PanelResizeListener listener) {
		panelResizedListeners.add(listener);
	}

	/**
	 * Interface function to emit signal
	 */
	private void notifyPanelResizedListeners(Integer width, Integer height) {
		for (Iterator i = panelResizedListeners.iterator(); i.hasNext();) {
			((PanelResizeListener) i.next()).onResized(width, height);
		}
	}

	public interface PanelResizeListener {
		/**
		 * indicates a Panel has been resized
		 * 
		 * @param width
		 * @param height
		 **/
		public void onResized(Integer width, Integer height);

	}
}
