import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Canvas is a class to allow for simple graphical drawing on a canvas.
 */
public class Canvas {

	// -------------------------------------------------------------------------
	// Static part
	// -------------------------------------------------------------------------
	/**
	 * The canvas initial width
	 */
	public static final int WIDTH = 700;

	/***
	 * The canvas name
	 */
	public static final String TITLE = "PacMan";

	/**
	 * The canvas initial height
	 */
	public static final int HEIGHT = 700;

	/**
	 * The canvas initial background color
	 */
	public static final Color BACKGROUND = Color.BLACK;

	public Pacman pacman = null;

	public Tile[] grid = null;
	/**
	 * this class unique instance (singleton)
	 */
	private static Canvas instance;

	/**
	 * Factory method to get the canvas singleton object.
	 *
	 * @return the canvas instance
	 */
	public static Canvas getCanvas() {
		if (Canvas.instance == null) {
			try {
				Canvas.instance = new Canvas();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!Canvas.instance.frame.isVisible()) {
			Canvas.instance.frame.setVisible(true);
		}
		return Canvas.instance;
	}

	// -------------------------------------------------------------------------
	// Instance part
	// -------------------------------------------------------------------------
	/**
	 * the frame that allows to display this canvas
	 */
	private final JFrame frame;

	/**
	 * the panel where the shares are displayed
	 */
	private final CanvasPane canvas;

	/**
	 * the button which closes the window
	 */
	// private final JButton exit;

	/**
	 * the reference objects used as keys in the shapes map in the order of their
	 * addition (cf {@link #draw()})
	 */
	private final Queue<Object> objects;

	/**
	 * The shapes to be displayed on the canvas at the next {@link #redraw()}
	 * invocation. Keys: reference objects associated to the shapes
	 */
	private final Map<Object, ColoredShape> shapes;

	/**
	 * the UP, DOWN, LEFT, RIGHT keys states: true if they are currently being
	 * pressed
	 */
	private boolean upPressed, downPressed, leftPressed, rightPressed;

	private JPanel panel;
	private JPanel right;
	private JPanel left;
	private JPanel black;
	private JPanel img;
	private JLabel image;
	private JLabel score;
	private JLabel nbLives;
	private JLabel nbLevel;

	private Canvas() throws IOException {

		this.img = new JPanel();
		this.image = new JLabel(new ImageIcon("croix.png"));
		this.img.add(this.image);
		this.img.setBackground(Color.BLACK);

		this.img.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}

			public void mouseExited(MouseEvent e) {
				image.setIcon(new ImageIcon("croix.png"));
			}

			public void mouseEntered(MouseEvent e) {
				image.setIcon(new ImageIcon("croix2.png"));
			}
		});

		this.score = new JLabel();
		this.score.setForeground(Color.WHITE);
		this.score.setBackground(Color.BLACK);

		this.nbLevel = new JLabel();
		this.nbLevel.setForeground(Color.WHITE);
		this.nbLevel.setBackground(Color.BLACK);

		this.nbLives = new JLabel();
		this.nbLives.setForeground(Color.WHITE);
		this.nbLives.setBackground(Color.BLACK);

		this.panel = new JPanel(new BorderLayout());

		this.right = new JPanel(new BorderLayout());
		this.left = new JPanel(new BorderLayout());
		this.left.setBackground(Color.BLACK);

		this.black = new JPanel();
		this.black.setBackground(Color.BLACK);

		this.objects = new ConcurrentLinkedQueue<>();
		this.shapes = new ConcurrentHashMap<>();

		this.canvas = new CanvasPane();

		this.panel.add(this.canvas, BorderLayout.CENTER);

		this.right.add(this.img, BorderLayout.NORTH);
		this.right.add(this.black, BorderLayout.CENTER);

		this.left.add(this.score, BorderLayout.NORTH);
		this.left.add(this.nbLevel, BorderLayout.CENTER);
		this.left.add(this.nbLives, BorderLayout.SOUTH);

		this.panel.add(this.right, BorderLayout.EAST);
		this.panel.add(this.left, BorderLayout.WEST);

		this.frame = new JFrame();
		this.frame.setTitle(TITLE);
		this.frame.setContentPane(panel);

		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setUndecorated(true);
		this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		this.frame.pack();

		this.canvas.addKeyListener(new KeyboardListener());
		this.canvas.setFocusable(true);

	}

	public void drawScore(int score) {
		this.score.setText("Score : " + score);
		this.score.setForeground(Color.WHITE);
	}

	public void drawLives(int nbLives) {
		this.nbLives.setText("Lives Left : " + nbLives);
		this.nbLives.setForeground(Color.WHITE);
	}

	public void drawLevel(int nbLevel) {
		this.nbLevel.setText("Level : " + nbLevel);
		this.nbLevel.setForeground(Color.WHITE);
	}

	public void setPacman(Pacman pac) {
		this.pacman = pac;
	}

	public void setGrid(Tile[] newGrid) {
		this.grid = newGrid;
	}

	// -------------------------------------------------------------------------
	// Draw
	// -------------------------------------------------------------------------
	/**
	 * Redraw all shapes currently on the Canvas.
	 */
	public void redraw() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				canvas.repaint();
			}
		});
	}

	/**
	 * Wait for a specified number of milliseconds before finishing. This provides
	 * an easy way to specify a small delay which can be used when producing
	 * animations.
	 *
	 * @param milliseconds the delay to wait for in milliseconds
	 */
	public void wait(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {
			// ignoring exception at the moment
		}
	}

	/**
	 * Draw a given shape onto the canvas.
	 *
	 * @param referenceObject an object to define identity for this shape
	 * @param color           the color of the shape
	 * @param shape           the shape object to be drawn on the canvas
	 */
	public void draw(Object referenceObject, Color color, Shape shape) {
		this.objects.remove(referenceObject); // just in case it was already there
		this.objects.add(referenceObject); // add at the end
		this.shapes.put(referenceObject, new ColoredShape(shape, color));
	}

	/**
	 * Draw a given text onto the canvas.
	 *
	 * @param referenceObject an object to define identity for this shape
	 * @param color           the color of the text
	 * @param text            the text
	 * @param x               the x location of the text
	 * @param y               the y location of the text
	 */
	public void drawString(Object referenceObject, Color color, String text, int x, int y) {
		this.objects.remove(referenceObject); // just in case it was already there
		this.objects.add(referenceObject); // add at the end
		this.shapes.put(referenceObject, new ColoredShape(text, x, y, color));
	}

	/**
	 * Erase a given shape's from the screen.
	 *
	 * @param referenceObject the shape object to be erased
	 */
	public void erase(Object referenceObject) {
		this.objects.remove(referenceObject);
		this.shapes.remove(referenceObject);
	}

	// -------------------------------------------------------------------------
	// Key pressed accessors
	// -------------------------------------------------------------------------
	/**
	 * Check whether the UP key is currently pressed
	 *
	 * @return true if the UP key is currently pressed
	 */
	public boolean isUpPressed() {
		return this.upPressed;
	}

	/**
	 * Check whether the DOWN key is currently pressed
	 *
	 * @return true if the DOWN key is currently pressed
	 */
	public boolean isDownPressed() {
		return this.downPressed;
	}

	/**
	 * Check whether the LEFT key is currently pressed
	 *
	 * @return true if the LEFT key is currently pressed
	 */
	public boolean isLeftPressed() {
		return this.leftPressed;
	}

	/**
	 * Check whether the RIGHT key is currently pressed
	 *
	 * @return true if the RIGHT key is currently pressed
	 */
	public boolean isRightPressed() {
		return this.rightPressed;
	}

	// -------------------------------------------------------------------------
	// Inner classes
	// -------------------------------------------------------------------------
	/**
	 * Inner class CanvasPane - the actual canvas component contained in the Canvas
	 * frame. This is essentially a JPanel with added capability to refresh the
	 * shapes drawn on it.
	 */
	private class CanvasPane extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.setColor(BACKGROUND);
			g.fillRect(0, 0, getWidth(), getHeight());
			for (Object shape : objects) {
				shapes.get(shape).draw((Graphics2D) g);
			}
		}
	}

	/**
	 * Inner class ColoredShape. Represents a shape or text and its color
	 */
	private class ColoredShape {

		private Shape shape; // the shape if it exists
		private String text; // the text if it exists
		private int x, y; // the text location
		private Color color; // the text or shape color

		/**
		 * Initialize a {@link ColoredShape} instance composed of a shape and a color
		 */
		public ColoredShape(Shape shape, Color color) {
			this.shape = shape;
			this.color = color;
		}

		/**
		 * Initialize a {@link ColoredShape} instance composed of a text and a color
		 */
		public ColoredShape(String text, int x, int y, Color color) {
			this.text = text;
			this.x = x;
			this.y = y;
			this.color = color;
		}

		/**
		 * Draw the shape using the given graphic object
		 *
		 * @param graphic AWT graphic object
		 */
		public void draw(Graphics2D graphic) {
			graphic.setColor(color);
			if (shape != null) {
				graphic.fill(shape);
			} else if (text != null) {
				graphic.drawString(text, x, y);
			}
		}
	}

	/**
	 * Inner class KeyboardListener - listens for the UP, DOWN, RIGHT, LEFT keys.
	 */
	private class KeyboardListener extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent event) {
			switch (event.getKeyCode()) {
			case KeyEvent.VK_UP:
				Tile check = getTile(Canvas.getCanvas().pacman.getX() - (grid[0].size / 4),
						Canvas.getCanvas().pacman.getY() - grid[0].size - (grid[0].size / 4));
				if (!check.isAWall()) {
					Canvas.getCanvas().pacman.setDir(0);
					upPressed = true;
				}
				break;
			case KeyEvent.VK_DOWN:
				Tile check1 = getTile(Canvas.getCanvas().pacman.getX() - (grid[0].size / 4),
						Canvas.getCanvas().pacman.getY() + grid[0].size - (grid[0].size / 4));
				if (!check1.isAWall()) {
					Canvas.getCanvas().pacman.setDir(1);
					downPressed = true;
				}
				break;
			case KeyEvent.VK_LEFT:
				Tile check2 = getTile(Canvas.getCanvas().pacman.getX() - grid[0].size - (grid[0].size / 4),
						Canvas.getCanvas().pacman.getY() - (grid[0].size / 4));
				if (!check2.isAWall()) {
					Canvas.getCanvas().pacman.setDir(2);
					leftPressed = true;
				}
				break;
			case KeyEvent.VK_RIGHT:
				Tile check3 = getTile(Canvas.getCanvas().pacman.getX() - (grid[0].size / 4) + grid[0].size,
						Canvas.getCanvas().pacman.getY() - (grid[0].size / 4));
				if (!check3.isAWall()) {
					Canvas.getCanvas().pacman.setDir(3);
					rightPressed = true;
				}
				break;
			}
		}

		public Tile getTile(int coordX, int coordY) {
			for (Tile tile : grid) {
				if (tile.getX() == coordX && tile.getY() == coordY) {
					return tile;
				}
			}
			return null;
		}

		@Override
		public void keyReleased(KeyEvent event) {
			switch (event.getKeyCode()) {
			case KeyEvent.VK_UP:
				upPressed = false;
				break;
			case KeyEvent.VK_DOWN:
				downPressed = false;
				break;
			case KeyEvent.VK_LEFT:
				leftPressed = false;
				break;
			case KeyEvent.VK_RIGHT:
				rightPressed = false;
				break;
			}
		}
	}
}
