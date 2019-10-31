import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * This class create the Tile objects. Extends SimpleFigure.
 * 
 * @authors : Naveau Simon, Travaillé Loïc.
 *
 */
public class Tile extends SimpleFigure {

	int size;
	Color tileColor;
	boolean isAWall;

	/**
	 * Tile constructor method. Call the SimpleElement constructor.
	 * 
	 * @param size    the size of the Tile on the JPanel.
	 * @param coordX  Coordinate X of the Tile on the JPanel
	 * @param coordY  Coordinate Y of the Tile on the JPanel
	 * @param color   The color of the Tile displayed (BLUE if a wall and BLACK if
	 *                not).
	 * @param isAWall Tell if the Tile is a wall.
	 */
	public Tile(int size, int coordX, int coordY, Color color, boolean isAWall) {
		// asserts are defined on the Element constructor.
		super(size, size, coordX, coordY, color);
		this.size = size;
		this.tileColor = color;
		this.isAWall = isAWall;

		try {
			invariant();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Invariant checking the Tile class
	 * 
	 * @throws Exception Can launch different exceptions about the Tile class.
	 */
	protected void invariant() throws Exception {
		if (size < 0) {
			throw new Exception("Error tile size < 0");
		}
	}

	@Override
	protected Shape makeShape() {
		return new Rectangle2D.Double(getX(), getY(), getSize(), getSize());
	}

	// JavaDoc useless on getters and setters
	public Color getTileColor() {
		return tileColor;
	}

	public void setTileColor(Color tileColor) {
		this.tileColor = tileColor;
	}

	public boolean isAWall() {
		return isAWall;
	}

	public void setAWall(boolean isAWall) {
		this.isAWall = isAWall;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
