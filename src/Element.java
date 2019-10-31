import java.awt.Color;

/**
 * Element class extended by Dots and Ghost. Represent all the elements on the
 * board excepted PacMan and the Tiles
 * 
 * @authors : Naveau Simon, Travaillé Loïc.
 */
public abstract class Element extends SimpleFigure {

	Color color;
	int size;
	int value;

	/**
	 * Constructor of the Element class
	 * 
	 * @param coordX    Coordinate X of the element on the JPanel
	 * @param coordY    Coordinate Y of the element on the JPanel
	 * @param size      The size of the element on the JPanel
	 * @param value     The value added at the game score when the element is ate by
	 *                  PacMan
	 * @param elemColor The color of the element on the JPanel
	 */
	public Element(int coordX, int coordY, int size, int value, Color elemColor) {
		super(size, size, coordX, coordY, elemColor);
		assert coordX >= 0 || coordY >= 0 : "Pre Elem constructor : Coordinate < 0";
		assert size >= 0 : "Pre Elem constructor : Size < 0";
		assert value >= 0 : "Pre Elem constructor : Value < 0";
		assert elemColor != null : "Pre Elem constructor : color == null";

		this.value = value;
		this.color = elemColor;
		this.size = size;

		try {
			invariant();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Invariant checking the Element class
	 * 
	 * @throws Exception Can launch different exceptions about the Element class.
	 */
	protected void invariant() throws Exception {
		if (size < 0) {
			throw new Exception("Elem size error");
		}
		if (value < 0) {
			throw new Exception("Elem value error");
		}
	}

	// JavaDoc useless on getters and setters
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
