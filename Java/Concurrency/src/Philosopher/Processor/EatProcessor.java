
public class EatProcessor implements IProcessor<boolean, Philosopher>{

    @Override
    public boolean process(Philosopher philosopher){
        return philosopher.eat();
    }

}