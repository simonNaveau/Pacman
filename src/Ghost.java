import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 * This class create the ghosts objects. Extends Element.
 * 
 * @authors : Naveau Simon, Travaillé Loïc.
 *
 */
public class Ghost extends Element {

	boolean isAlive = true;

	/**
	 * Constructor of the class Ghost. Call the constructor of element.
	 * 
	 * @param coordX    Coordinate X of the ghost on the JPanel
	 * @param coordY    Coordinate Y of the ghost on the JPanel
	 * @param size      The size of the ghost on the JPanel
	 * @param value     The value added at the game score when the ghost is eated by
	 *                  PacMan
	 * @param elemColor The color of the ghost display on the screen
	 */
	public Ghost(int coordX, int coordY, int size, int value, Color elemColor) {
		// assert are defined on the Element class constructor
		super(coordX, coordY, size, value, elemColor);
	}

	/**
	 * Make the ghost moving one Tile up of his actual position.
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
	 * Make the ghost moving one Tile down of his actual position.
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
	 * Make the ghost moving one Tile right of his actual position.
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
	 * Make the ghost moving one Tile left of his actual position.
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
	 * This function enable PacMan to eat the ghost when his super mode is
	 * activated.
	 * 
	 * @param pacman The PacMan object active on the game.
	 * @return Return of the ghost is eated by PacMan
	 */
	public boolean eat(Pacman pacman) {
		if (this.getX() == pacman.getX() && this.getY() == pacman.getY()) {
			pacman.setAlive(false);
			return true;
		}
		return false;

	}

	@Override
	protected Shape makeShape() {
		return new Rectangle2D.Double(getX(), getY(), getSize(), getSize());

	}

	// JavaDoc useless on getters and setters
	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

}