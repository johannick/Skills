package concurrency.philosopher;

import java.util.ArrayList;

public class PhilosopherGame implements  Runnable {

    private final ArrayList<Fork> forks;
    private final ArrayList<APhilosopher> philosophers;
    private final ArrayList<Thread> lives;
    private final int lifetime;

    public int getLifetime(){
        return lifetime;
    }

    public PhilosopherGame(int numberPhilosophers, int lifetimeMilliSeconds){

        forks = new ArrayList<Fork>(numberPhilosophers);
        philosophers = new ArrayList<APhilosopher>(numberPhilosophers);
        lives = new ArrayList<Thread>(numberPhilosophers);
        lifetime = lifetimeMilliSeconds;

        for (int i = 0; i < numberPhilosophers; ++i) {
            forks.add(new Fork(i));
        }

        boolean lefter = false;

        for (int i = 0; i < numberPhilosophers; ++i){
            Fork left = forks.get(i);
            Fork right = forks.get((i + 1) % numberPhilosophers);

            if (lefter)
                philosophers.add(new Philosopher(i, left, right));
            else
                philosophers.add(new Philosopher(i, right, left));
            lefter = !lefter;
        }
    }

    @Override
    public void run()  {

        for (APhilosopher philosopher : philosophers) {
            var thread = new Thread((Runnable) philosopher);

            lives.add(thread);
            thread.start();
        }
        try {
        for (Thread live : lives) {

                live.join();
        }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop(){
        for (Thread life : lives) {
            life.interrupt();
        }
    }

    public void display(){

        char separator = '|';
        System.out.print(separator);
        for (APhilosopher philosopher : philosophers) {
            System.out.print(philosopher.getState() + separator);
        }
        System.out.println();
        System.out.print(separator);
        for (Fork fork : forks) {
            System.out.print(printableId(fork.getOwnerId(), 5) + separator);
        }
        System.out.println();
    }

    private String printableId(int identifier, int padding){
        return String.format("%" + padding + "s", Integer.toString(identifier));
    }

    public void report(){

        char separator = '|';
        System.out.print(separator);
        for (APhilosopher philosopher : philosophers) {
            System.out.print(printableId(philosopher.getId(), 8) + separator);
        }
        System.out.println();
        System.out.print(separator);
        for (APhilosopher philosopher : philosophers) {
            System.out.print("EAT:" + printableId(philosopher.getEatCounter(), 4) + separator);
        }
        System.out.println();
    }
}