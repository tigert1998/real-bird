package counter;

public class CounterController {
    private int count = 0;

    public void addCount() {
        count++;
    }

    int getCount() {
        return count;
    }
}
