package counter;

public class CounterController {
    private int counter = 0;

    public void addCounter() {
        counter++;
    }

    int getCounter() {
        return counter;
    }
}
