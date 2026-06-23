package concurrency.philosopher;

import java.util.concurrent.atomic.AtomicInteger;

public class Fork {

    private final int id;
    private final AtomicInteger owner = new AtomicInteger(-1);

    public int getId() {
        return id;
    }

    public Fork(int identifier) {

        id = identifier;
    }

    public int getOwnerId() {

        return owner.intValue();
    }

    public boolean tryTake(int philosopherId){

        return owner.compareAndSet(-1, philosopherId);
    }

    public boolean tryRelease(int philosopherId){

        return owner.compareAndSet(philosopherId, -1);
    }
}