/**
 * Main class of the PacMan game. Create the game with all the level choose in
 * the method
 * 
 * @authors : Naveau Simon, Travaillé Loïc.
 */
public class Game {
	int bestScore = 0;
	int nbLifes;
	Level levelList[];
	boolean play = false;

	/**
	 * This is the constructor of the class.
	 * 
	 * @param nbLifes   The numbers of PacMan lives for all the game.
	 * @param levelList The list who contain all levels of the game.
	 */
	public Game(int nbLifes, Level levelList[]) {
		assert nbLifes > 1 : "Pre Game Constructor : nbLifes < 1";
		assert levelList.length > 1 : "Pre Game Constructor : levelList.length < 1";
		assert levelList != null : "Pre Game Constructor : levelList == null";

		this.nbLifes = nbLifes;
		this.levelList = levelList;
		try {
			invariant();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launch the game. All the levels are launch one by one. The score and the
	 * number of lives is update at each end of level.
	 * 
	 * @throws Exception Invariant exception
	 */
	public void startGame() throws Exception {
		assert play == false : "Pre startGame(): play !=  false";
		assert nbLifes != 0 : "Pre startGame(): nbLifes < 0";
		invariant();
		this.play = true;
		while (play) {
			int levelNumber = 1;
			for (Level level : levelList) {
				Canvas.getCanvas().drawLevel(levelNumber);
				Canvas.getCanvas().redraw();
				if (nbLifes > 0) {
					System.out.println(
							"Level " + levelNumber + " started, you have " + getNbLifes() + " lifes remaining"); // Afficher
																													// le
																													// numéro
																													// du
																													// niveau
																													// dans
																													// la
																													// fenetre
					int[] lvlResult = level.startLevel(getNbLifes());
					this.bestScore += lvlResult[0]; // Afficher ce score dans la fenêtre aussi
					setNbLifes(getNbLifes() - lvlResult[1]); // Afficher nbLifes soit avec des chiffres ou avec des
																// cercles jaune en dessous du platal.

					if (nbLifes <= 0) {
						stopGame();
					}
					levelNumber++;
				}
			}
			if (nbLifes > 0) {
				stopGame();
			}
			invariant();
		}

	}

	/**
	 * Stop the game. That display the report of the game.
	 * 
	 * @throws Exception Invariant exception
	 */
	public void stopGame() throws Exception {
		invariant();
		assert play == true : "Pre stopGame(): play !=  true";
		this.play = false;
		if (nbLifes > 0) {
			System.out
					.println("Congratulation you won the game with " + nbLifes + " lifes and a score of " + bestScore);
		} else {
			System.out.println("You loose !!!"); // afficher game over et donner le score
			System.out.println("Your score is " + bestScore);
		}
		invariant();
	}

	/**
	 * Invariant checking the game class
	 * 
	 * @throws Exception Can launch different exceptions about the game class.
	 */
	private void invariant() throws Exception {
		if (bestScore < 0) {
			throw new Exception("Wrong Game bestScore");
		}
		if (nbLifes < 0) {
			throw new Exception("Wrong Game nbLifes");
		}
		if (levelList.length < 0) {
			throw new Exception("Wrong Game LvlList length");
		}
		if (levelList == null) {
			throw new Exception("Wrong Game LvlList");
		}
	}

	// JavaDoc useless on getters and setters

	public int getBestScore() {
		return bestScore;
	}

	public void setBestScore(int bestScore) {
		this.bestScore = bestScore;
	}

	public int getNbLifes() {
		return nbLifes;
	}

	public void setNbLifes(int nbLifes) {
		this.nbLifes = nbLifes;
	}

}
