package counter;

import oglutils.*;
import utils.Settings;

public class CounterView {
    private static final float GAP = .1f;
    private static final float COUNTER_Y = .7f;

    private CounterController counterController;
    private Picture[] numbers;

    public CounterView(CounterController counterController, Picture[] numbers) {
        this.counterController = counterController;
        this.numbers = numbers;
    }

    public void draw() {
        int count = counterController.getCount();
        String str = String.valueOf(count);
        int totalDigits = str.length();
        float totalWidth =
                Settings.BIG_NUMBER_RELATIVE_WIDTH + GAP * (totalDigits - 1);
        for (int i = 0; i < str.length(); i++) {
            int digit = str.charAt(i) - '0';
            float x = -totalWidth / 2 + i * GAP;
            numbers[digit].place(
                    x, COUNTER_Y,
                    Settings.BIG_NUMBER_RELATIVE_WIDTH, Settings.BIG_NUMBER_RELATIVE_HEIGHT,
                    -.6f);
            numbers[digit].draw();
        }
    }
}
