import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * PacMan class who create the object wish is playable by the player.
 * 
 * @authors : Naveau Simon, Travaillé Loïc.
 */
public class Pacman extends SimpleFigure {

	Color pacmanColor = Color.YELLOW;
	int size;
	boolean isAlive;
	int pacmanDir = -1;

	/**
	 * PacMan constructor who set his attributes. Call the constructor of element.
	 * 
	 * @param coordX  Coordinate X of PacMan on the JPanel
	 * @param coordY  Coordinate Y of PacMan on the JPanel
	 * @param size    The size of PacMan on the JPanel
	 * @param isAlive This boolean tell if PacMan is actually playable and alive or
	 *                not.
	 * @param dir     The actual direction of PacMan [-1==Stop; 0==Up; 1==Down;
	 *                2==Left; 3==Right].
	 * @param color   The actual color of PacMan displayed =.
	 */
	public Pacman(int coordX, int coordY, int size, boolean isAlive, int dir, Color color) {
		super(size, size, coordX, coordY, color);
		assert coordX >= 0 || coordY >= 0 : "Pre Pacman constructor : Coordinate < 0";
		assert size >= 0 : "Pre Pacman constructor : Size < 0";
		assert dir == -1 : "Pre Pacman constructor : PacMan moving at creation";

		this.isAlive = isAlive;
		this.size = size;
		this.pacmanDir = dir;
		try {
			invariant();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Make PacMan moving one Tile up of his actual position.
	 */
	public void moveUp() {
		this.move(0, -this.getSize() * 2);
		try {
			invariant();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Make PacMan moving one Tile down of his actual position.
	 */
	public void moveDown() {
		this.move(0, this.getSize() * 2);
		try {
			invariant();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Make PacMan moving one Tile right of his actual position.
	 */
	public void moveRight() {
		this.move(this.getSize() * 2, 0);
		try {
			invariant();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Make PacMan moving one Tile left of his actual position.
	 */
	public void moveLeft() {
		this.move(-this.getSize() * 2, 0);
		try {
			invariant();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Invariant checking the PacMan class
	 * 
	 * @throws Exception Can launch different exceptions about the Pacman class.
	 */
	protected void invariant() throws Exception {
		if (size < 0) {
			throw new Exception("Pacman size error");
		}
		if (pacmanDir != -1 && pacmanDir != 0 && pacmanDir != 1 && pacmanDir != 2 && pacmanDir != 3) {
			throw new Exception("Pacman direction error");
		}
	}

	@Override
	protected Shape makeShape() {
		return new Ellipse2D.Double(getX(), getY(), getSize(), getSize());
	}

	// JavaDoc is useless on getters and setters
	public Color getPacmanColor() {
		return pacmanColor;
	}

	public void setPacmanColor(Color pacmanColor) {
		this.pacmanColor = pacmanColor;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public int getDir() {
		return this.pacmanDir;
	}

	public void setDir(int dir) {
		this.pacmanDir = dir;
	}
}
