
public class ThinkProcessor implements IProcessor<boolean, Philosopher>{

    @Override
    public boolean process(Philosopher philosopher){
        philosopher.takeRight();
        return philosopher.hasRightFork();
    }

}