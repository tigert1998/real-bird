package counter;

import utils.*;

public class CounterController {
    private int count = 0;

    public void addCount() {
        count++;
    }

    int getCount() {
        return count;
    }

    public void setState(State state) {
        if (state == State.READY) {
            count = 0;
        }
    }
}
