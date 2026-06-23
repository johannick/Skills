package concurrency.philosopher.processor;

import concurrency.philosopher.APhilosopher;
import org.jetbrains.annotations.NotNull;

public class ThinkProcess implements IProcess {

    @Override
    public boolean process(@NotNull APhilosopher philosopher){
        return philosopher.takeSecond();
    }

}