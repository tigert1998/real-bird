import java.util.*;
import java.util.function.*;

public class Clock {
    private List<Function<Float, Void>> functions = new ArrayList<Function<Float, Void>>();

    public void register(Function<Float, Void> function) {
        functions.add(function);
    }

    public void elapse(float time) {
        functions.forEach(function -> {
            function.apply(time);
        });
    }
}
