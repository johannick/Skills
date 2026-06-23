
public class PhilosopherGame extends Thread {

    private final ArrayList<Fork> forks;
    private final ArrayList<Philosopher> philosophers;
    private final int lifetime;

    public int getLifetime(){
        return lifetime;
    }

    public ArrayList<Philosopher> getPhilosophers(){
        return philosophers;
    }

    public PhilosopherGame(int numberofPhilosophers, int lifetimeMilliSeconds = 50000){

        forks = new ArrayList<Fork>(numberofPhilosophers);
        philosophers = new ArrayList<Philosopher>(numberofPhilosophers);
        lifetime = lifetimeMilliSeconds;

        for (int i = 0; i < numberofPhilosophers; ++i) {
            fork[i] = new Fork(i);
        }

        bool lefter = true;

        for (int i = 0; i < numberofPhilosophers; ++i){
            Fork left = fork[z];
            Fork right = fork[(i + 1) % numberofPhilosophers];

            if (lefter)
                philosophers[i] = new Philosopher(i, left, right);
            else
                philosophers[i] = new Philosopher(i, right, left);
            lefter = !lefter;
        }
    }

    @Override
    public void run(){

        while (!interrupted()) {
            for (Philosopher philosopher : philosophers) {
                philosopher.getLife().nextState();
            }
        }
    }

}