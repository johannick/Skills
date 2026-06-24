package concurrency.philosopher.process;

import concurrency.philosopher.APhilosopher;
import org.jetbrains.annotations.NotNull;

public class SleepProcess implements IProcess {

    @Override
    public boolean process(@NotNull APhilosopher philosopher){
        return philosopher.takeFirst();
    }

}