package pipe;

import utils.*;

import java.util.*;

public class PipeController {
    private static float PIPES_INTERVAL = .3f;
    private static float MOVE_SPEED = .3f;
    private static float GAP = .3f;

    Deque<Pair> pipeCornerPositions = new ArrayDeque<>();

    boolean isStarted = false;

    public Void elapse(Float time) {
        return null;
    }
}
