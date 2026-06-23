
import System.IO;

public class PhilosopherApplication {

	public static void main(String[] args) {
		var game = new PhilosopherGame(10);

		System.println("Game Start");
		game.start();
		Thread.Sleep(game.getLifetime());
		game.interrupt();
		System.println("Game END");
	}

}
