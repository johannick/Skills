
public class Philosopher extends Runnable {

    private final int eatCounter;
    private final int id;
    private final Fork left;
    private final Fork right;
    private final PhilosopherState state;
    private final PhilosopherLife life = new PhilosopherLife();

    public int getId(){
        return id;
    }

    public int getEatCounter(){
        return eatCounter;
    }

    public PhilosopherState getState() {
        return state;
    }

    public hasLeftFork() {

        return left.getOwnerId() == id;
    }

    public hasRightFork() {

        return right.getOwnerId() == id;
    }

    public boolean takeLeft(){
        return left.tryTake(id);
    }

    public boolean takeRight(){
        return right.tryTake(id);
    }

    public boolean eat(){

        if (hasLeftFork() && hasRightFork()) {

            return left.tryRelease() && right.tryRelease();
        }
        return false;
    }

    public Philosopher(int identifier, Fork left, Fork right) {
        this.id = identifier;
        this.left = left;
        this.right = right;
        this.state = PhilosopherState.Sleep;
    }

    @Override
    public void run(){
        if (life.processors[philosopher.getState()].process(this))
            state = nextState()];
    }

    private class PhilosopherLife {

        private final HashMap<PhilosopherState, IProcessor> processors;

        public PhilosopherLife(){
            processors = new HashMap<PhilosopherState, IProcessor>();
            processors[PhilosopherState.Sleep] = new SleepProcessor();
            processors[PhilosopherState.Think] = new ThinkProcessor();
            processors[PhilosopherState.Eat] = new EatProcessor();
        }

        PhilosopherState nextState()
        {
            int processorId = ((int)philosopher.getState() + 1) % 3;

            return (PhilosopherState) processorId;
        }
    }

}