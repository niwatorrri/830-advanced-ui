import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class BoundaryRectangle extends Rectangle {
	private static final long serialVersionUID = 1L;

	// Different constructors
	public BoundaryRectangle(BoundaryRectangle r) {
		super(r);
	}
	public BoundaryRectangle(Rectangle2D r) {
		super(r.getBounds());
	}
	public BoundaryRectangle(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	public BoundaryRectangle(double x, double y, double w, double h) {
		super((int) x, (int) y, (int) Math.ceil(w), (int) Math.ceil(h));
	}
	public BoundaryRectangle() {
		super();
	}

	// For easier print
	public String toString() {
		return String.format("x=%d, y=%d, width=%d, height=%d",
			x, y, width, height);
	}
}