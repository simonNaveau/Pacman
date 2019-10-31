
/**
 * An abstract figure that can be manipulated and drawn on a frame.
 *
 * @inv getWidth() >= 0 && getHeight() >= 0
 */
public abstract class Figure {

	private int x; // the x location of the upper left corner of the figure bounding rectangle in
					// pixels
	private int y; // the y location of the upper left corner of the figure bounding rectangle in
					// pixels
	private int width; // the width of the figure bounding rectangle in pixels
	private int height; // the height of the figure bounding rectangle in pixels

	/**
	 * Initialize the figure default properties
	 */
	public Figure() {
		try {
			invariant();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the figure properties
	 *
	 * @param width  the width of the figure bounding rectangle in pixels
	 * @param height the height of the figure bounding rectangle in pixels
	 * @param x      the x location of the upper left corner of the figure bounding
	 *               rectangle in pixels
	 * @param y      the y location of the upper left corner of the figure bounding
	 *               rectangle in pixels
	 *
	 * @pre width >= 0 && height >= 0
	 */
	public Figure(int width, int height, int x, int y) {
		assert width >= 0 && height >= 0 : "Wrong dimensions";

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		try {
			invariant();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------
	// Draw
	// ------------------------------------------------------------------------
	/**
	 * Draw the figure with the current properties on the canvas.
	 */
	public abstract void draw();

	/**
	 * Erase the current figure from the canvas.
	 */
	public abstract void erase();

	// ------------------------------------------------------------------------
	// Getters
	// ------------------------------------------------------------------------
	/**
	 * Give the width of the figure bounding rectangle in pixels
	 *
	 * @return the figure width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Give the height of the figure bounding rectangle in pixels
	 *
	 * @return the figure height
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Give the x location of the upper left corner of the figure bounding rectangle
	 * in pixels
	 *
	 * @return the figure x location
	 */
	public int getX() {
		return this.x;
	}

	public void changeX(int x) {
		this.x = x;
	}

	public void changeY(int y) {
		this.y = y;
	}

	/**
	 * Give the y location of the upper left corner of the figure bounding rectangle
	 * in pixels
	 *
	 * @return the figure y location
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Check whether the figure is inside the canvas
	 *
	 * @return true if the figure is inside the canvas
	 */
	public boolean isInside() {
		// Canvas canvas = Canvas.getCanvas();
		return this.x >= 0 && this.x + this.width < Canvas.WIDTH && this.y >= 0 && this.y + this.height < Canvas.HEIGHT;
	}

	/**
	 * Check whether the current and the given figures have a non null intersection
	 *
	 * @param other a shape
	 * @return true if the shapes have a non null intersection
	 */
	public boolean intersects(Figure other) {
		return ((this.x < other.x && this.x + this.width > other.x)
				|| (other.x < this.x && other.x + other.width > this.x))
				&& ((this.y < other.y && this.y + this.height > other.y)
						|| (other.y < this.y && other.y + other.height > this.y));
	}

	// ------------------------------------------------------------------------
	// Setters
	// ------------------------------------------------------------------------
	/**
	 * Change the location of the upper left corner of the figure bounding rectangle
	 *
	 * @param dx number of pixels to move the figure to the right (dx>0) or left
	 *           (dx<0)
	 * @param dy number of pixels to move the figure to the bottom (dy>0) or top
	 *           (dy<0)
	 */
	public void move(int dx, int dy) {
		this.x += dx;
		this.y += dy;
		draw();
		try {
			invariant();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Move the figure in an autonomous way (i.e. the dx and dy moves are chosen by
	 * the object itself)
	 */
	public void move() {
		// do nothing
		try {
			invariant();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------
	// Invariant
	// ------------------------------------------------------------------------
	/**
	 * Check the class invariant
	 */
	protected void invariant() throws Exception {
		assert this.width >= 0 && this.height >= 0 : "Invariant violated: wrong dimensions";
	}
}
