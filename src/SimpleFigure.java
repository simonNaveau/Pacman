
import java.awt.Color;
import java.awt.Shape;

/**
 * An abstract figure composed of a single shape
 *
 * @inv getColor() != null
 */
public abstract class SimpleFigure extends Figure {

	private Color color; // the shape color

	/**
	 * Initialize the figure properties
	 *
	 * @param width  the width of the figure bounding rectangle in pixels
	 * @param height the height of the figure bounding rectangle in pixels
	 * @param x      the x location of the upper left corner of the figure bounding
	 *               rectangle in pixels
	 * @param y      the y location of the upper left corner of the figure bounding
	 *               rectangle in pixels
	 * @param color  the shape color
	 *
	 * @pre width >= 0 && height >= 0 && color != null
	 */
	public SimpleFigure(int width, int height, int x, int y, Color color) {
		super(width, height, x, y);
		assert color != null : "color is null";
		this.color = color;
		// this.simpleFigureInvariant();
	}

	// ------------------------------------------------------------------------
	// Draw
	// ------------------------------------------------------------------------
	/**
	 * {@inheritDoc }
	 */
	@Override
	public void draw() {
		Canvas canvas = Canvas.getCanvas();
		canvas.draw(this, this.getColor(), makeShape());
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	public void erase() {
		Canvas canvas = Canvas.getCanvas();
		canvas.erase(this);
	}

	/**
	 * Make a shape that represents this figure from the current location and size
	 *
	 * @return shape representing this figure
	 */
	protected abstract Shape makeShape();

	// ------------------------------------------------------------------------
	// Getters
	// ------------------------------------------------------------------------
	/**
	 * Give the figure color
	 *
	 * @return the figure color
	 */
	public Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color = color;
		System.out.println(this.color);
	}

//	// ------------------------------------------------------------------------
//	// Invariant
//	// ------------------------------------------------------------------------
//	/**
//	 * Check the class invariant
//	 */
//	protected void simpleFigureInvariant() {
//		super.invariant();
//		assert this.color != null : "Invariant violated: color is null";
//	}
}
