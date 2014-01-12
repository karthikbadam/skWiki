package gwt.g2d.client.mouse;

import gwt.g2d.shared.math.Vector2;

public interface SurfaceMouseDragHandler {
	public void onDragStart(Vector2 start, Long startId);
	public void onDragChange(Vector2 start, Vector2 stop, Long startId, Long stopId);
	public void onDragStop(Vector2 start, Vector2 stop, Long startId, Long stopId);
}
