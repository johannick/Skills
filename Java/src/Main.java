
import concurrency.philosopher.*;

void main() {
    var game = new PhilosopherGame(10, 5000);
    long startTime = System.currentTimeMillis();

    IO.println("Game Start");
    while ((System.currentTimeMillis() - startTime) < game.getLifetime()){
        game.run();
        game.display();
    }
    game.stop();
    IO.println("Game Report");
    game.report();
}