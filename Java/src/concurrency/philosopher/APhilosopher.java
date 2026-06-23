package concurrency.philosopher;

import java.lang.String;


public abstract class APhilosopher {
    public abstract int getId();
    public abstract boolean hasFirstFork();
    public abstract boolean hasSecondFork();
    public abstract boolean takeFirst();
    public abstract boolean takeSecond();
    public abstract boolean eat();
    public abstract String getState();
    public abstract int getEatCounter();
}
