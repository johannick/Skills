package concurrency.philosopher.processor;

import concurrency.philosopher.APhilosopher;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface IProcess{

    public boolean process(@NotNull APhilosopher philosopher);

}