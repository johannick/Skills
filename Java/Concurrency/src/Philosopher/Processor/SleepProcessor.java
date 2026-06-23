
public class SleepProcessor implements IProcessor<boolean, Philosopher>{

    @Override
    public boolean process(Philosopher philosopher){
        philosopher.takeLeft();
        return philosopher.hasLeftFork();
    }

}