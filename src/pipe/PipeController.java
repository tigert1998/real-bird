package pipe;

import utils.*;

import java.util.*;

public class PipeController {
    final static float VERT_GAP = .45f;
    private final static float MOVE_SPEED = .3f;
    private final static float HORI_GAP = 1.2f;
    private final static float MAX_RAND_Y = .4f;
    private final static int QUEUE_SIZE = 4;

    final static float PIPE_WIDTH =
            2.f * Settings.getPipeUpPosition().width / Settings.getSkylinePosition().width;
    final static float PIPE_DOWN_HEIGHT =
            2.f * Settings.getPipeDownPosition().height / Settings.getSkylinePosition().height;
    final static float PIPE_UP_HEIGHT =
            2.f * Settings.getPipeUpPosition().height / Settings.getSkylinePosition().height;

    // left bottom corner of pipe down
    Deque<Pair> pipeCornerPositions = new ArrayDeque<>();

    private boolean isStarted = false;

    private static float randomPipeY() {
        return (float) Math.random() * MAX_RAND_Y - 1;
    }

    private void fillPipeDeque() {
        if (pipeCornerPositions.isEmpty())
            pipeCornerPositions.addLast(new Pair(1 + HORI_GAP, randomPipeY()));
        while (pipeCornerPositions.size() < QUEUE_SIZE)
            randomlyAddPipe();
    }

    private void randomlyAddPipe() {
        Pair last = pipeCornerPositions.getLast();
        pipeCornerPositions.addLast(new Pair(last.x + HORI_GAP, randomPipeY()));
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
        if (isStarted) {
            fillPipeDeque();
        }
    }

    public Void elapse(Float time) {
        if (!isStarted) return null;
        pipeCornerPositions.forEach(pair -> {
            pair.x -= time * MOVE_SPEED * 2;
        });
        while (!pipeCornerPositions.isEmpty() && pipeCornerPositions.getFirst().x < -1 -PIPE_WIDTH)
            pipeCornerPositions.pollFirst();
        fillPipeDeque();
        return null;
    }
}
