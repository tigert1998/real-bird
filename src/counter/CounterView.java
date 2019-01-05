package counter;

import oglutils.*;

public class CounterView {
    private CounterController counterController;
    private Picture[] numbers;

    public CounterView(CounterController counterController, Picture[] numbers) {
        this.counterController = counterController;
        this.numbers = numbers;
    }

    public void draw() {

    }
}
