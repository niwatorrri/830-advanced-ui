import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class BoundaryRectangle extends Rectangle {
	private static final long serialVersionUID = 1L;
	public BoundaryRectangle(BoundaryRectangle r) {
		super(r);
	}
	public BoundaryRectangle(Rectangle2D r) {
		super(r.getBounds());
	}
	public BoundaryRectangle(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	public BoundaryRectangle() {
		super();
	}
}