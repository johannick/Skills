

public class DisplayProcessor implements IProcessor<PhilosopherGame, PhilosopherGame> {

    private final char separator = '|';

    @Override
    public PhilosopherGame process(PhilosopherGame game){

        System.out.print(separator);
        for (Philosopher philosopher : game.getPhilosophers()) {
            System.out.printl(printableId(philosopher) + separator);
        }
        System.out.println();
        System.out.print(separator);
        for (Philosopher philosopher : game.getPhilosophers()) {
            System.out.printl(printableState(philosopher) + separator);
        }
        System.out.println();
        return game;
    }

    private String printableState(Philosopher philosopher){
        return String.format("%5i", philosopher.getId());
    }

    private String printableId(Philosopher philosopher){
        return String.format("%5s", philosopher.getId().toString());
    }

}