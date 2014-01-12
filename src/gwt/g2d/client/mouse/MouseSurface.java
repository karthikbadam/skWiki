package gwt.g2d.client.mouse;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

import com.google.gwt.canvas.dom.client.Context2d;
import gwt.g2d.client.graphics.ImageData;
import gwt.g2d.client.graphics.Surface;
import gwt.g2d.shared.Color;
import gwt.g2d.shared.math.Vector2;

/**
 * A mouse surface is tied to a normal surface, and registers different shapes that can be checked for
 * clicks or hovers form the mouse. This system works with any complex shape, and will detect hovers/clicks on
 * all non-transparent areas in the drawn shape or image.
 * @author Karel
 *
 */
public class MouseSurface {
	
	// handlers
	private class Handlers {
		public SurfaceClickHandler clickHandler;
		public SurfaceMouseOutHandler mouseOutHandler;
		public SurfaceMouseOverHandler mouseOverHandler;
		public SurfaceMouseMoveHandler mouseMoveHandler;
	};
	
	// the surface we are tied to
	Surface fOriginalSurface;
	
	// original context
	Context2d fOriginalContext;
	
	// the temporary surface which is used to draw on during mouse registration
	public Surface fTempSurface = null;
	
	// current id
	Long fCurrentId = null;
	
	// map of id to handlers
	Map<Long, Handlers> fIdToHandlers = new HashMap<Long, Handlers>();
	
	// map of id to surface (used for checking against this specific collision)
	Map<Long, Surface> fIdToSurface = new HashMap<Long, Surface>();
	
	// drag handlers
	Vector<SurfaceMouseDragHandler> fDragHandlers = new Vector<SurfaceMouseDragHandler>();
	
	// the id where the drag begin
	Long fStartDragId = null;
	
	// the position where the drag began
	Vector2 fStartDragLoc = null;
	
	// list of id's in the order in which they were drawn - used to make sure that only the topmost object is actually triggered
	List<Long> fIds = new LinkedList<Long>();
	
	// last color
	Color fCurrentColor = new Color(200, 200, 0);
	
	// last id that was hovered by the mouse
	Long fLastId = null;
	
	// does this surface has move handlers - otherwise, don't perform the calculations
	boolean fHasMoveHandlers = false;
	
	
	// create a new mouse surface
	public MouseSurface(Surface originalSurface) {
		fOriginalSurface = originalSurface;
		fOriginalContext = fOriginalSurface.getContext();
	}
	
	
	// start registering a new clickable surface
	public void startRegister(Long id) {
		
		// set id
		fCurrentId = id;
		
		// surface
		Surface surface = null;
		
		// doesn't exist yet - create a new canvas
		if (!fIdToSurface.containsKey(id)) {
			surface = new Surface(fOriginalSurface.getCoordinateSpaceWidth(), fOriginalSurface.getCoordinateSpaceHeight());
			fIdToSurface.put(id,  surface);
			fIdToHandlers.put(id, new Handlers());
			fIds.add(id);
		}
		
		// exist - get the existing canvas
		else {
			
			// get the surface
			surface = fIdToSurface.get(id);
			surface.clear();
			
			// find the position of this id, and put it at the end of our draw list
			fIds.remove(id);
			fIds.add(id);
		}
		
		// start drawing on the temp surface now
		fTempSurface = surface;
		fOriginalSurface.replaceContext(fTempSurface.getContext());
	}
	
	
	// stop registering
	public void stopRegister(boolean draw) {
		
		// invalid - surface doesn't exist
		if (fTempSurface == null) return;
		
		// switch back to the original context
		fOriginalSurface.replaceContext(fOriginalContext);
		if (draw) fOriginalSurface.drawImage(fTempSurface.getCanvasElement(), 0, 0, fTempSurface.getCoordinateSpaceWidth(), fTempSurface.getCoordinateSpaceHeight());
		
		// done
		fCurrentId = null;
		fTempSurface = null;
		

	}
	
	
	// next color
	private void generateNextColor() {
		fCurrentColor = new Color(fCurrentColor.red, fCurrentColor.green, fCurrentColor.blue+1);
		if (fCurrentColor.blue > 255) {
			fCurrentColor = new Color(fCurrentColor.red, fCurrentColor.green+1, 0);
			if (fCurrentColor.green > 255) {
				fCurrentColor = new Color(fCurrentColor.red+1, 0, 0);
				if (fCurrentColor.red > 255) {
					fCurrentColor = new Color(0, 0, 0); // reset back to zero
				}
			}
		}
	}
	
	
	// active?
	public boolean isActive() {
		return fCurrentId != null;
	}
	
	
	// add click handler
	public void addClickHandler(SurfaceClickHandler handler) {
		if (fCurrentId == null) return;
		Handlers handlers = fIdToHandlers.get(fCurrentId);
		if (handlers == null) return;
		handlers.clickHandler = handler;
	}
	
	
	// add mouse down handler
	public void addMouseDragHandler(SurfaceMouseDragHandler handler) {
		fDragHandlers.add(handler);
	}
	
	// add mouse over handler
	public void addMouseOverHandler(SurfaceMouseOverHandler handler) {
		if (fCurrentId == null) return;
		Handlers handlers = fIdToHandlers.get(fCurrentId);
		if (handlers == null) return;
		handlers.mouseOverHandler = handler;
		fHasMoveHandlers = true;
	}
	
	
	// add mouse out handler
	public void addMouseOutHandler(SurfaceMouseOutHandler handler) {
		if (fCurrentId == null) return;
		Handlers handlers = fIdToHandlers.get(fCurrentId);
		if (handlers == null) return;
		handlers.mouseOutHandler = handler;
		fHasMoveHandlers = true;
	}
	
	
	// add mouse move handler
	public void addMouseMoveHandler(SurfaceMouseMoveHandler handler) {
		if (fCurrentId == null) return;
		Handlers handlers = fIdToHandlers.get(fCurrentId);
		if (handlers == null) return;
		handlers.mouseMoveHandler = handler;
		fHasMoveHandlers = true;
	}
	
	
	/**
	 * We clicked on the canvas - check where we clicked exactly.
	 */
	public void onClick(int x, int y) {
		
		// get all different canvases and check for a collision
		ListIterator<Long> it = fIds.listIterator(fIds.size());
		while (it.hasPrevious()) {
			Long id = it.previous();
			Surface surface = fIdToSurface.get(id);
			
			// get the color at the given location
			ImageData data = surface.getImageData(x, y, 1, 1);
			double alpha = data.getAlpha(0, 0);
			
			// hit is not transparent - we got a hit!
			if (alpha > Double.MIN_VALUE) {
				
				// look up color
				Handlers handlers = fIdToHandlers.get(id);
				if (handlers != null && handlers.clickHandler != null) handlers.clickHandler.onClick(new Vector2(x, y), id);
				return;

			}
		}
	}
	
	
	/**
	 * We put our mouse down on the canvas - check where we clicked exactly.
	 */
	public void onMouseDown(int x, int y) {
		
		// perform one single onMouseMove call to detect where we are
		fStartDragLoc = new Vector2(x, y);
		onMouseMove(x, y);
		
		// set the id matched by onMouseMove
		fStartDragId = fLastId;
		
		// let all handlers know
		for (SurfaceMouseDragHandler handler : fDragHandlers) {
			handler.onDragStart(fStartDragLoc, fStartDragId);
		}
	}
	
	
	/**
	 * We lifted up our mouse on the canvas - check where we clicked exactly.
	 */
	public void onMouseUp(int x, int y) {
		
		// let all handlers know
		for (SurfaceMouseDragHandler handler : fDragHandlers) {
			handler.onDragStop(fStartDragLoc, new Vector2(x, y), fStartDragId, fLastId); 
		}
		
		// disable the onMouseMove calls again by setting fStartDragLoc to null
		fStartDragId = null;
		fStartDragLoc = null;
	}
	
	
	/**
	 * We're entering the canvas - check if we entered immediately on an object.
	 */
	public void onMouseOver(int x, int y) {
		if (!fHasMoveHandlers) return;
		
		// get all different canvases and check for a collision
		ListIterator<Long> it = fIds.listIterator(fIds.size());
		while (it.hasPrevious()) {
			Long id = it.previous();
			Surface surface = fIdToSurface.get(id);
			
			// get the color at the given location
			ImageData data = surface.getImageData(x, y, 1, 1);
			double alpha = data.getAlpha(0, 0);
			
			// hit is not transparent - we got a hit!
			if (alpha > Double.MIN_VALUE) {
				
				// we entered on an object, signal it
				Handlers handlers = fIdToHandlers.get(id);
				if (handlers != null && handlers.mouseOverHandler != null) handlers.mouseOverHandler.onMouseOver(new Vector2(x, y), id);
				fLastId = id;
				return;
			}
		}
	}
	
	/**
	 * We left the surface - make sure we send the last selected object a message.
	 */
	public void onMouseOut(int x, int y) {
		if (!fHasMoveHandlers) return;
		
		// no entity selected when we left the surface - don't do anything
		if (fLastId == null) return;
		
		// look up color
		Handlers handlers = fIdToHandlers.get(fLastId);
		if (handlers != null && handlers.mouseOutHandler != null) handlers.mouseOutHandler.onMouseOut(new Vector2(x, y));
		fLastId = null;
	}
	
	/**
	 * We moved the mouse over the canvas - check for collision with a registered object.
	 */
	public void onMouseMove(int x, int y) {
		if (!fHasMoveHandlers && fStartDragLoc == null) return;
		
		// get all different canvases and check for a collision
		boolean hit = false;
		ListIterator<Long> it = fIds.listIterator(fIds.size());
		while (it.hasPrevious()) {
			Long id = it.previous();
			Surface surface = fIdToSurface.get(id);
			
			// get the color at the given location
			ImageData data = surface.getImageData(x, y, 1, 1);
			double alpha = data.getAlpha(0, 0);
			
			// hit is not transparent - we got a hit!
			if (alpha > Double.MIN_VALUE) {
				hit = true;
				
				// we were hovering over another id and now we're hovering over nothing or a new id - send a mouse out event
				if (fLastId != null && fLastId != id) {
					Handlers handlers = fIdToHandlers.get(fLastId);
					if (handlers != null && handlers.mouseOutHandler != null) handlers.mouseOutHandler.onMouseOut(new Vector2(x, y));
					fLastId = null;
					
				}
				
				// we enter a new object
				if (fLastId == null) {
					fLastId = id;
					Handlers handlers = fIdToHandlers.get(fLastId);
					if (handlers != null && handlers.mouseOverHandler != null) handlers.mouseOverHandler.onMouseOver(new Vector2(x, y), fLastId);
				}
				
				// we were already on this object, but we moved the mouse
				else {
					Handlers handlers = fIdToHandlers.get(fLastId);
					if (handlers != null && handlers.mouseMoveHandler != null) handlers.mouseMoveHandler.onMouseMove(new Vector2(x, y), fLastId);
				}
				
				// only process one hit
				break;
			}
		}
		
		// update drag
		if (fStartDragLoc != null) {
			for (SurfaceMouseDragHandler handler : fDragHandlers) {
				handler.onDragChange(fStartDragLoc, new Vector2(x, y), fStartDragId, fLastId); 
			}
		}
		
		// we got here - this means there was no hit
		// if we were above an object in the previous cycle, let it know we left
		if (!hit) {
			Handlers handlers = fIdToHandlers.get(fLastId);
			if (handlers != null && handlers.mouseOutHandler != null) handlers.mouseOutHandler.onMouseOut(new Vector2(x, y));
			fLastId = null;
		}
	}
}
