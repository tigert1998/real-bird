import java.util.*;
import java.util.function.*;

public class Clock {
    private List<Function<Double, Void>> functions = new ArrayList<Function<Double, Void>>();

    public void register(Function<Double, Void> function) {
        functions.add(function);
    }

    public void elapse(double time) {
        functions.forEach(function -> {
            function.apply(time);
        });
    }
}
