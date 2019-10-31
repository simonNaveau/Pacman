import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Level class. This object regroup all the elements of 
 * @authors : Naveau Simon, Travaillé Loïc.  
 */
public class Level {
	int powerDuration;
	int endPower;
	int score = 0;
	int lostLife = 0;
	int levelLife = 0;
	boolean superPower = false;
	boolean play = false;
	int size;
	String mazePath;
	Tile[] grid;
	Ghost[] ghostList;
	ArrayList<Dot> dotList = new ArrayList<Dot>();
	Pacman pacman;
	int ghostLastDir[];

	/**
	 * Constructor of the Level class
	 * @param powerDuration The duration in second of the superPower. Cumulative
	 * @param size The size of the Tile who compose the grid.
	 * @param mazePath The path to the ".txt" file witch contain the maze configuration for this level.
	 */
	public Level(int powerDuration, int size, String mazePath) {
		assert powerDuration > 0 : "Pre Level Constructor : powerDuration < 0";
		assert size > 0 : "Pre Level Constructor : size < 0";
		assert !mazePath.equals("") :"Pre Level Constructor : mazePath == \"\" ";
		
		this.powerDuration = powerDuration;
		this.size = size;
		this.mazePath = mazePath;
	
		try {
			invariant();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This function create the PacMan object for this level.
	 */
	private void initPacman() {
		
		boolean trouve = false;
		int i = grid.length-1;
		Tile LastTile = null;
		while (trouve == false || i > 0) {
			if (!grid[i].isAWall) {
				LastTile = grid[i];
				trouve = true;
			}
			i--;
		}
		this.pacman = new Pacman(LastTile.getX() + (this.size / 4), LastTile.getY() + (this.size / 4), this.size / 2, true, -1, Color.YELLOW);
		Canvas.getCanvas().setPacman(this.pacman);
		
		assert this.pacman != null : "Post initPacman(): Error pacman object is null after initialisation";
	}
	
	
	/**
	 * This function initialize the Ghost list for the level and the ghostLastDir.
	 */
	private void initGhosts() {
		assert grid.length > 0 && grid != null : "Pre initGhosts : Wrong grid initialization";
		boolean trouve = false;
		int i = 0;
		Tile firstTile = null;
		while (trouve == false || i < this.grid.length) {
			if (!grid[i].isAWall) {
				firstTile = grid[i];
				trouve = true;
			}
			i++;
		}

		this.ghostList = new Ghost[4];
		this.ghostList[0] = new Ghost(firstTile.getX() + (this.size / 4), firstTile.getY() + (this.size / 4), this.size / 2, 500, Color.PINK);
		this.ghostList[1] = new Ghost(firstTile.getX() + (this.size / 4), firstTile.getY() + (this.size / 4),
				this.size / 2, 500, Color.GREEN);
		this.ghostList[2] = new Ghost(firstTile.getX() + (this.size / 4), firstTile.getY() + (this.size / 4),
				this.size / 2, 500, Color.CYAN);
		this.ghostList[3] = new Ghost(firstTile.getX() + (this.size / 4), firstTile.getY() + (this.size / 4),
				this.size / 2, 500, Color.ORANGE);
		this.ghostLastDir = new int[ghostList.length];

		for (int j = 0; j < ghostList.length; j++) {
			while (!canMove(ghostList[j], ghostLastDir[j])) {
				ghostLastDir[j] = chooseRandomDirection(-1);
			}
		}
		assert ghostLastDir != null : "Post initGhosts : ghostLastDir == null";
		assert ghostList != null : "Post initGhosts : ghostList == null";
		assert ghostLastDir.length == ghostList.length : "Post initGhosts : ghostLastDir.length != ghostList.length";
	}

	/**
	 * This function initialize the Dot list for this level.
	 */
	private void initDots() {
		assert grid.length > 0 && grid != null : "Pre initDots : Error on grid initialization";
		for (Tile tile : grid) {
			if (!tile.isAWall()) {
				if (Math.random() * 100 <= 2) {
					int dotSize = this.size / 2;
					this.dotList.add(new Dot((tile.getX() + (this.size / 2)) - (dotSize / 2), (tile.getY() + (this.size / 2)) - (dotSize / 2), dotSize, 100, Color.RED, true));
				} else {
					int dotSize = this.size / 4;
					this.dotList.add(new Dot((tile.getX() + (this.size / 2)) - (dotSize / 2),
							(tile.getY() + (this.size / 2)) - (dotSize / 2), dotSize, 100, Color.WHITE, false));
				}

			}
		}

		assert dotList != null : "Post initDots : dotList == null";
	}

	/**
	 * This function initialize the maze of the level
	 * @param path The path to the maze configuration file
	 */
	private void initGrid(String path) {
		assert !path.equals("") : "Pre initGrid : Maze path == \"\" ";
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int) dimension.getHeight();
		int width = (int) dimension.getWidth();
		Scanner sc = null;
		String comma = ",";
		String un = "1";
		int nbLines = 0;
		int nbColumns = 0;
		int line = 0;
		int column = 0;
		int tmpX = 0;
		int tmpY = 0;
		try {
			try {
				sc = new Scanner(new File(path));
				while (sc.hasNextLine()) {
					for (char c : sc.next().toCharArray()) {
						if (c == comma.charAt(0)) {
							nbLines = nbLines + 1;
						} else if (nbLines == 0) {
							nbColumns = nbColumns + 1;
						}
					}
				}
				this.grid = new Tile[nbLines * nbColumns];
			} finally {
				if (sc != null)
					sc.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		tmpX = (width - nbColumns * size) / 2;
		tmpY = (height - nbLines * size) / 2;

		try {
			try {
				sc = new Scanner(new File(path));
				int cpt = 0;
				while (sc.hasNextLine()) {
					for (char c : sc.next().toCharArray()) {
						if (c == comma.charAt(0)) {
							line = line + 1;
							column = 0;
						} else if (c == un.charAt(0)) {
							Tile t = new Tile(size, tmpX + column * size, tmpY + line * size, Color.BLUE, true);
							grid[cpt] = t;
							cpt++;
							column = column + 1;
						} else {
							Tile t = new Tile(size, tmpX + column * size, tmpY + line * size, Color.BLACK, false);
							grid[cpt] = t;
							cpt++;
							column = column + 1;
						}
					}
				}
				Canvas.getCanvas().setGrid(this.grid);
			} finally {
				if (sc != null)
					sc.close();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		assert grid != null : "Post initGrid : grid creation error";
	}

	/**
	 * This method refresh the element display on the screen.
	 */
	public void drawstart() {
		assert grid != null : "Pre drawStart : grid == null";
		assert dotList != null : "Pre drawStart : dotList == null";
		assert ghostList != null : "Pre drawStart : ghostList == null";
		for (Tile fig : grid) {
			fig.draw();
		}

		for (Dot dot : dotList) {
			dot.draw();
		}
		for (Ghost g : ghostList) {
			g.draw();
		}
		pacman.draw();
	}
	
	/**
	 * This method refresh the dots display on the screen.
	 */
	public void drawDots() {
		assert dotList != null : "Pre drawDots : dotList == null";
		for (Dot dot : dotList) {
			dot.draw();
		}
	}

	/**
	 * This method refresh the ghosts display on the screen.
	 */
	public void drawghost() {
		assert ghostList != null : "Pre drawGhost : ghostList == null";
		for (Ghost g : ghostList) {
			g.draw();
		}
	}

	/**
	 * This method choose randomly a new direction but can't return the direction in parameter
	 * @param dirToSkip The direction to not return
	 * @return An int who represent the new direction
	 */
	private int chooseRandomDirection(int dirToSkip) {
		assert dirToSkip == -1 || dirToSkip == 0 || dirToSkip == 1 || dirToSkip == 2 || dirToSkip == 3 : "Pre chooseRandomDirection : Error in direction to skip";
		switch (dirToSkip) {
		case 0:
			int newDir0 = (int) (Math.random() * 4);
			while (newDir0 == 1 || newDir0 == 0) {
				newDir0 = (int) (Math.random() * 4);
			}
			return newDir0;
		case 1:
			int newDir1 = (int) (Math.random() * 4);
			while (newDir1 == 0 || newDir1 == 1) {
				newDir1 = (int) (Math.random() * 4);
			}
			return newDir1;
		case 2:
			int newDir2 = (int) (Math.random() * 4);
			while (newDir2 == 2 || newDir2 == 3) {
				newDir2 = (int) (Math.random() * 4);
			}
			return newDir2;
		case 3:
			int newDir3 = (int) (Math.random() * 4);
			while (newDir3 == 2 || newDir3 == 3) {
				newDir3 = (int) (Math.random() * 4);
			}
			return newDir3;

		default:
			int newDir = (int) (Math.random() * 4);
			return newDir;
		}
	}

	/**
	 * This function search in the tile list the one who match with the coordinates in parameter.
	 * @param coordX The x coordinate of the tile to find
	 * @param coordY The y coordinate of the tile to find
	 * @return The Tile corresponding or null
	 */
	public Tile getTile(int coordX, int coordY) {
		assert grid != null : "Pre getTile : grid == null";
		for (Tile tile : grid) {
			if (tile.getX() == coordX && tile.getY() == coordY) {
				return tile;
			}
		}
		return null;
	}
	
	/**
	 * This method made the ghosts of the level moving and eating. It check if itch ghost can continue to move in the same direction 
	 * or choose a new direction if it can't. The ghosts have a chance of 50 % to change of direction at each intersection.
	 */
	public void moveGhosts() {
		assert ghostLastDir.length >= 0 : "Pre moveGhost : ghostLastDir error";
		assert ghostList.length >= 0 : "Pre moveGhost : ghostList error";
		for (int i = 0; i < ghostList.length; i++) {
			if (canMove(ghostList[i], ghostLastDir[i])) {

				switch (ghostLastDir[i]) {
				case 0:

					if (Math.random() * 2 > 1) {
						if (Math.random() * 2 > 1) {
							if (canMove(ghostList[i], 2)) {
								ghostLastDir[i] = 2;
								ghostList[i].moveRight();
								break;
							}
							if (canMove(ghostList[i], 3)) {
								ghostLastDir[i] = 3;
								ghostList[i].moveLeft();
								break;
							}
						}
					}

					ghostList[i].moveUp();
					break;
				case 1:
					if (Math.random() * 2 > 1) {
						if (Math.random() * 2 > 1) {
							if (canMove(ghostList[i], 2)) {
								ghostLastDir[i] = 2;
								ghostList[i].moveRight();
								break;
							}
							if (canMove(ghostList[i], 3)) {
								ghostLastDir[i] = 3;
								ghostList[i].moveLeft();
								break;
							}
						}
					}
					ghostList[i].moveDown();
					break;
				case 2:
					if (Math.random() * 2 > 1) {
						if (Math.random() * 2 > 1) {
							if (canMove(ghostList[i], 0)) {
								ghostLastDir[i] = 0;
								ghostList[i].moveUp();
								break;
							}
							if (canMove(ghostList[i], 1)) {
								ghostLastDir[i] = 1;
								ghostList[i].moveDown();
								break;
							}
						}
					}
					ghostList[i].moveRight();
					break;
				case 3:
					if (Math.random() * 2 > 1) {
						if (Math.random() * 2 > 1) {
							if (canMove(ghostList[i], 0)) {
								ghostLastDir[i] = 0;
								ghostList[i].moveUp();
								break;
							}
							if (canMove(ghostList[i], 1)) {
								ghostLastDir[i] = 1;
								ghostList[i].moveDown();
								break;
							}
						}
					}
					ghostList[i].moveLeft();
					break;
				default:
					break;
				}
				if(!superPower) {
					if (ghostList[i].eat(this.pacman)) {
						this.lostLife++;
						this.pacRespawn();
						if (lostLife == this.levelLife) {
							this.stopLevel();
						}
					}
				}else {
					eatGhost();
				}
				
			} else {

				int test = ghostLastDir[i];

				while (!canMove(ghostList[i], ghostLastDir[i])) {
					ghostLastDir[i] = chooseRandomDirection(test);
				}
				switch (ghostLastDir[i]) {
				case 0:
					ghostList[i].moveUp();
					break;
				case 1:
					ghostList[i].moveDown();
					break;
				case 2:
					ghostList[i].moveRight();
					break;
				case 3:
					ghostList[i].moveLeft();
					break;
				default:
					break;
				}
				if(!superPower) {
					if (ghostList[i].eat(this.pacman)) {
						this.lostLife++;
						this.pacRespawn();
						if (lostLife == this.levelLife) {
							this.stopLevel();
						}
					}
				}else {
					eatGhost();
				}
			}
		}
	}
	
	/**
	 * This method allow PacMan to eat a ghost in the same Tile as him. If a ghost is ate, he respawn.
	 */
	public void eatGhost() {
		
		assert ghostList.length > 0 : "Pre eatGhost : Error ghostList.length < 0 ";
		assert pacman != null : "Pre eatGhost : Error pacman not init";
		for (Ghost g : ghostList) {
			if (pacman.getX()==g.getX() && pacman.getY()==g.getY()) {
				this.score += g.getValue();
				g.setAlive(false);
				int ghostIndex = -1;
				for (int i = 0; i < ghostList.length; i++) {
					if(ghostList[i].getX() == g.getX() && ghostList[i].getY() == g.getY() ) {
						ghostIndex = i;
					}
				}
				ghostRespawn(ghostIndex);
			}
		}

		try {
			invariant();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The method make PacMan respawn. He respawn where he start the level
	 */
	public void pacRespawn() {
		assert pacman != null : "Pre pacRespawn : Error pacman not init";
		assert pacman.isAlive() == false : "Pre eatGhost : Error pacman is not dead";
		
		boolean trouve = false;
		pacman.setAlive(true);
		int i = grid.length-1;
		Tile LastTile = null;
		while (trouve == false || i > 0) {
			if (!grid[i].isAWall) {
				LastTile = grid[i];
				trouve = true;
			}
			i--;
		}
		this.pacman.changeX(LastTile.getX() + (this.size / 4));
		this.pacman.changeY(LastTile.getY() + (this.size / 4));
		this.pacman.setDir(-1);
		this.pacman.draw();
		Canvas.getCanvas().redraw();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * The method make a ghost respawn. In the half of the grid where pacMan is not.
	 * @param index
	 */
	public void ghostRespawn(int index) {
		boolean trouve = false;
		int i = 0;
		Tile spawn = null;
		while (trouve == false || i < grid.length) {
			if (!grid[i].isAWall) {
				spawn = grid[i];
				trouve = true;
			}
			i++;
		}
		if(pacman.getY() > spawn.getX()/2) { //Si pacman est dans la moitier supp du tableau, le fantome spawn dans la motier inf
			trouve = false;
			i = grid.length-1;
			while (trouve == false || i > 0) {
				if (!grid[i].isAWall) {
					spawn = grid[i];
					trouve = true;
				}
				i--;
			}
		}
		ghostList[index].changeX(spawn.getX() + (this.size / 4));
		ghostList[index].changeY(spawn.getY() + (this.size / 4));
		while (!canMove(ghostList[index], ghostLastDir[index])) {
			ghostLastDir[index] = -1;
			ghostLastDir[index] = chooseRandomDirection(-1);
		}
		ghostList[index].draw();
		Canvas.getCanvas().redraw();

	}
	
	/**
	 * Check if an element can move in a direction.
	 * @param elem The element to check
	 * @param direction The direction to check
	 * @return true if the way is open else false
	 */
	private boolean canMove(Element elem, int direction) {
		assert direction >= -1 && direction <= 3 : "Pre canMove : Error in direction";
		assert elem != null : "Pre canMove : Error on the element";
		switch (direction) {
		case 0:
			Tile check = getTile(elem.getX() - (this.size / 4), elem.getY() - this.size - (this.size / 4));
			if (check.isAWall())
				return false;
			return true;
		case 1:
			Tile check1 = getTile(elem.getX() - (this.size / 4), elem.getY() + this.size - (this.size / 4));
			if (check1.isAWall())
				return false;
			return true;
		case 2:
			Tile check2 = getTile(elem.getX() - (this.size / 4) + this.size, elem.getY() - (this.size / 4));
			if (check2.isAWall())
				return false;
			return true;
		case 3:
			Tile check3 = getTile(elem.getX() - this.size - (this.size / 4), elem.getY() - (this.size / 4));
			if (check3.isAWall())
				return false;
			return true;

		default:
			return false;
		}
	}
	
	/**
	 * This method allow PacMan to eat a dots in the same Tile as him. If a dot is ate, she is destroyed.
	 */
	public void pacmanEat() {
		assert pacman != null : "Pre pacmanEat : pacman == null";
		assert dotList != null : "Pre pacmanEat : dotList == null";
		int pacmanX = this.pacman.getX()-(this.size / 4);
		int pacmanY = this.pacman.getY()-(this.size / 4);

		for (Dot dot : dotList) {
			if (dot.getX()-(this.size / 2)+(dot.getSize()/2) == pacmanX && dot.getY()-(this.size / 2)+(dot.getSize()/2) == pacmanY && !dot.isEat()) {
				this.score += dot.getValue();
				dot.setEat(true);
				this.setSuperPower(dot.isSuper());
				this.dotList.remove(dot);
				Canvas.getCanvas().erase(dot);
				Canvas.getCanvas().redraw();
				break;
			}
		}

		try {
			invariant();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is the main function of the level. It launch the Level and made it playable.
	 * @param remainingLife The life remaining to finish this level.
	 * @return An array as {score, lives lost}
	 */
	public int[] startLevel(int remainingLife) {
		assert remainingLife > 0 : "Pre startLevel : remainingLife <= 0";
		initGrid(mazePath);
		initDots();
		initGhosts();
		initPacman();
		this.play = true;
		this.levelLife = remainingLife;
		this.drawstart();
		while (this.play) {
			this.movePacman();
			this.moveGhosts();
			Canvas.getCanvas().redraw();
			try {
				Thread.sleep(180);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			if(superPower) {
				int currentTime = (int) (System.currentTimeMillis()/1000);
				if(currentTime > endPower) {
					this.ghostList[0].setColor(Color.PINK);
					this.ghostList[1].setColor(Color.GREEN);
					this.ghostList[2].setColor(Color.CYAN);
					this.ghostList[3].setColor(Color.ORANGE);
					this.superPower = false;
					
				}
			}
			
			
			
			
			if(this.dotList.isEmpty()) stopLevel();
			if(this.levelLife - lostLife == 0) stopLevel();
			try {
				invariant();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new int[] { score, lostLife };
	}

	/**
	 * Stop the level and print the results in the cmd
	 */
	public void stopLevel() {
		this.play = false;
		System.out.println("Level ended : Your score for this level is " + score);
		System.out.println("");
	}

	/**
	 * This method made the PacMan of the level moving and eating. It check if he can continue to move in the same direction 
	 * or choose stop.
	 */
	public void movePacman() {
		assert pacman != null : "Pre movePacman : pacman == null";
			if (canPacmanMove(this.pacman.getDir())) {

				switch (this.pacman.getDir()) {
				case -1:
					System.out.println("Wait");
					
					if(!superPower) {
						for (Ghost g : ghostList) {
							if (g.eat(this.pacman)) {
								this.lostLife++;
								this.pacRespawn();
								if (lostLife == this.levelLife) {
									this.stopLevel();
								}
							}
						}
					}else {
						eatGhost();
					}
					
					
					break;
				case 0:
					this.pacman.moveUp();
					if(!superPower) {
						for (Ghost g : ghostList) {
							if (g.eat(this.pacman)) {
								this.lostLife++;
								this.pacRespawn();
								if (lostLife == this.levelLife) {
									this.stopLevel();
								}
							}
						}
					}else {
						eatGhost();
					}
					break;
				case 1:
					this.pacman.moveDown();
					if(!superPower) {
						for (Ghost g : ghostList) {
							if (g.eat(this.pacman)) {
								this.lostLife++;
								this.pacRespawn();
								if (lostLife == this.levelLife) {
									this.stopLevel();
								}
							}
						}
					}else {
						eatGhost();
					}
					break;
				case 2:
					this.pacman.moveLeft();
					if(!superPower) {
						for (Ghost g : ghostList) {
							if (g.eat(this.pacman)) {
								this.lostLife++;
								this.pacRespawn();
								if (lostLife == this.levelLife) {
									this.stopLevel();
								}
							}
						}
					}else {
						eatGhost();
					}
					break;
				case 3:
					this.pacman.moveRight();
					if(!superPower) {
						for (Ghost g : ghostList) {
							if (g.eat(this.pacman)) {
								this.lostLife++;
								this.pacRespawn();
								if (lostLife == this.levelLife) {
									this.stopLevel();
								}
							}
						}
					}else {
						eatGhost();
					}
					break;
				default:
					break;
				}
				pacmanEat();
			}
		}

	/**
	 * Check if PacMan can move in a direction.
	 * @param pacmanDir The direction to check
	 * @return true if the way is open else false
	 */
	private boolean canPacmanMove(int pacmanDir) {
		assert pacmanDir >= -1 && pacmanDir <= 3 : "Pre canPacmanMove : Error in direction";
		assert pacman != null : "Pre canPacmanMove : pacman == null";
		switch (pacmanDir) {
		case 0:
			Tile check = getTile(this.pacman.getX() - (this.size / 4), this.pacman.getY() - this.size - (this.size / 4));
			if (check.isAWall())
				return false;
			return true;
		case 1:
			Tile check1 = getTile(this.pacman.getX() - (this.size / 4), this.pacman.getY() + this.size - (this.size / 4));
			if (check1.isAWall())
				return false;
			return true;
		case 2:
			Tile check2 = getTile(this.pacman.getX() - this.size - (this.size / 4), this.pacman.getY() - (this.size / 4));
			if (check2.isAWall())
				return false;
			return true;
		case 3:
			Tile check3 = getTile(this.pacman.getX() - (this.size / 4) + this.size, this.pacman.getY() - (this.size / 4));
			if (check3.isAWall())
				return false;
			return true;

		default:
			return false;
		}
	}

	/**
	 * Change the state of the super boolean and change the super remaining time in consequences.
	 * @param superPow The new state of superPower
	 */
	public void setSuperPower(boolean superPow) {
		if(superPower) {
			if(superPow) {
				for (Ghost g : ghostList) {
					g.setColor(Color.BLUE);
				}
				Canvas.getCanvas().redraw();
				this.endPower = ((int) (System.currentTimeMillis()/1000))+powerDuration+(endPower-(int) (System.currentTimeMillis()/1000));
			}
		}else {
			if(superPow) {
				for (Ghost g : ghostList) {
					g.setColor(Color.BLUE);
				}
				Canvas.getCanvas().redraw();
				this.endPower = ((int) (System.currentTimeMillis()/1000))+powerDuration;
				this.superPower = superPow;
			}
		}
	}

	/**
	 * Invariant checking the level class
	 * @throws Exception Can launch different exceptions about the level class.
	 */
	private void invariant() throws Exception {
		if(powerDuration < 0) {
			throw new Exception("Wrong Level powerDuration");
		}
		if(score < 0) {
			throw new Exception("Wrong Level score");
		}
		if(size < 1) {
			throw new Exception("Level size error");
		}
		if(dotList == null) {
			throw new Exception("Level dotListe error");
		}
	}

	//JavaDoc useless on getters and setters
	public int getPowerDuration() {
		return powerDuration;
	}

	public void setPowerDuration(int powerDuration) {
		this.powerDuration = powerDuration;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isSuperPower() {
		return superPower;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Tile[] getGrid() {
		return grid;
	}

	public void setGrid(Tile[] grid) {
		this.grid = grid;
	}

	public Pacman getPacman() {
		return pacman;
	}

	public void setPacman(Pacman pacman) {
		this.pacman = pacman;
	}
	
	public Ghost[] getGhostList() {
		return ghostList;
	}

	public void setGhostList(Ghost[] ghostList) {
		this.ghostList = ghostList;
	}

	public ArrayList<Dot> getDotList() {
		return dotList;
	}

	public void setDotList(ArrayList<Dot> dotList) {
		this.dotList = dotList;
	}
}
