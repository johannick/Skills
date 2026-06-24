package concurrency.philosopher;

import concurrency.philosopher.process.*;

import java.util.ArrayList;

public class Philosopher extends APhilosopher implements Runnable {

    private int eatCounter;
    private final int id;
    private int state;
    private final Fork first;
    private final Fork second;
    private final ArrayList<String> states = new ArrayList<String>(3);
    private final ArrayList<IProcess> life = new ArrayList<IProcess>(3);

    public int getId(){
        return id;
    }

    public int getEatCounter(){
        return eatCounter;
    }

    @Override
    public boolean hasFirstFork() {

        return first.getOwnerId() == id;
    }

    public boolean hasSecondFork() {

        return second.getOwnerId() == id;
    }

    public boolean takeFirst(){

        return first.tryTake(id);
    }

    public boolean takeSecond(){

        return second.tryTake(id);
    }

    public boolean eat(){

        if (hasFirstFork() && hasSecondFork()) {
            ++eatCounter;
            first.tryRelease(id);
            second.tryRelease(id);
            return true;
        }
        return false;
    }

    public Philosopher(int identifier, Fork first, Fork second) {
        this.id = identifier;
        this.first = first;
        this.second = second;
        this.state = 0;
        this.eatCounter = 0;
        this.life.add(new SleepProcess());
        this.life.add(new ThinkProcess());
        this.life.add(new EatProcess());
        this.states.add("SLEEP");
        this.states.add("THINK");
        this.states.add(" EAT ");
    }

    @Override
    public String getState(){

        return states.get(state);
    }

    @Override
    public void run(){
        if (life.get(state).process(this))
            state = (state + 1) % states.size();
    }

}