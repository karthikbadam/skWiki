package gwt.g2d.client.graphics.shapes;

/**
 * The shape registration is returned when a shape is registered with a surface.
 * To remove the shape from the surface (and not forward events anymore),
 * call the removeShape function.
 * @author Karel
 *
 */
public interface ShapeRegistration {
	public void removeShape();
}
