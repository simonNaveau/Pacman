
public class yoloMain {
	public static void main(String[] args) {
		int nbLives = 3;
		Level lvlTest = new Level(5, 38, "plateau1.txt");
		Level lvlTest2 = new Level(5, 38, "plateau2.txt");
		Game test = new Game(nbLives, new Level[] { lvlTest, lvlTest2 });
		Canvas.getCanvas().drawLives(nbLives);
		Canvas.getCanvas().redraw();
		try {
			test.startGame();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}