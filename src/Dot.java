import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 * The class who create the dotes for the PacMan game. Extends Element.
 * 
 * @authors : Naveau Simon, Travaillé Loïc.
 */
public class Dot extends Element {

	boolean isEat = false;
	boolean isSuper;

	/**
	 * This is the constructor of the dot class
	 * 
	 * @param coordX    The position X of the dot in the JPanel
	 * @param coordY    The position Y of the dot in the JPanel
	 * @param size      The size of a dot on the JPanel
	 * @param value     The value add to the game score when this dot is eat by
	 *                  PacMan
	 * @param elemColor The color of the dot.
	 * @param isSuper   If this boolean is at true. This dot is a SuperDot and when
	 *                  she is ate PacMan is super.
	 */
	public Dot(int coordX, int coordY, int size, int value, Color elemColor, boolean isSuper) {
		super(coordX, coordY, size, value, elemColor);
//		assert coordX >= 0 : "Pre DotConstructor : coordX < 0";
//		assert coordY >= 0 : "Pre DotConstructor : coordY < 0";
		assert size >= 0 : "Pre DotConstructor : size < 0";
		this.isSuper = isSuper;
		try {
			invariant();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Shape makeShape() {
		return new Ellipse2D.Double(getX(), getY(), getSize(), getSize());
	}

	/**
	 * Invariant checking the level class
	 * 
	 * @throws Exception Can launch different exceptions about the level class.
	 */
	protected void invariant() throws Exception {
		if (size < 0) {
			throw new Exception("Dot size error");
		}
		if (value < 0) {
			throw new Exception("Dot value error");
		}
	}

	// JavaDoc useless on getters and setters
	public boolean isEat() {
		return isEat;
	}

	public void setEat(boolean isEat) {
		this.isEat = isEat;
	}

	public boolean isSuper() {
		return isSuper;
	}

	public void setSuper(boolean isSuper) {
		this.isSuper = isSuper;
	}

}
