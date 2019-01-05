package pipe;

import utils.*;

import java.util.*;

public class PipeController {
    final static float VERT_GAP = .45f;
    private final static float MOVE_SPEED = .4f;
    private final static float HORI_GAP = 1.2f;
    private final static float MAX_RAND_Y = .4f;
    private final static int QUEUE_SIZE = 4;

    final static float PIPE_WIDTH =
            2.f * Settings.getPipeUpPosition().width / Settings.getSkylinePosition().width;
    final static float PIPE_DOWN_HEIGHT =
            2.f * Settings.getPipeDownPosition().height / Settings.getSkylinePosition().height;
    final static float PIPE_UP_HEIGHT =
            2.f * Settings.getPipeUpPosition().height / Settings.getSkylinePosition().height;

    private final static PhysicsPlayground physicsPlayground = PhysicsPlayground.shared;

    // left bottom corner of pipe down
    Deque<Point> pipeCornerPositions = new ArrayDeque<>();
    Deque<PhysicsPlaygroundHandler> pipeDownHandlers = new ArrayDeque<>();
    Deque<PhysicsPlaygroundHandler> pipeUpHandlers = new ArrayDeque<>();

    private static List<Point> constructConvexPoints(float x, float y, float width, float height) {
        List<Point> convexPoints = new ArrayList<>();
        convexPoints.add(new Point(x, y));
        convexPoints.add(new Point(x + width, y));
        convexPoints.add(new Point(x + width, y + height));
        convexPoints.add(new Point(x, y + height));
        return convexPoints;
    }

    private boolean isStarted = false;

    private static float randomPipeY() {
        return (float) Math.random() * MAX_RAND_Y - 1;
    }

    private void fillPipeDeque() {
        if (pipeCornerPositions.isEmpty())
            pipeCornerPositions.addLast(new Point(1 + HORI_GAP, randomPipeY()));
        while (pipeCornerPositions.size() < QUEUE_SIZE)
            randomlyAddPipe();
    }

    private void randomlyAddPipe() {
        Point last = pipeCornerPositions.getLast();
        Point newLeftBottomCorner = new Point(last.x + HORI_GAP, randomPipeY());
        pipeCornerPositions.addLast(newLeftBottomCorner);
        pipeDownHandlers.addLast(physicsPlayground.addHandler(false, constructConvexPoints(
                newLeftBottomCorner.x, newLeftBottomCorner.y,
                PIPE_WIDTH, PIPE_DOWN_HEIGHT
        )));
        pipeUpHandlers.addLast(physicsPlayground.addHandler(false, constructConvexPoints(
                newLeftBottomCorner.x, newLeftBottomCorner.y + PIPE_DOWN_HEIGHT + VERT_GAP,
                PIPE_WIDTH, PIPE_UP_HEIGHT
        )));
    }

    private void clearRedundantPipes() {
        while (!pipeCornerPositions.isEmpty() && pipeCornerPositions.getFirst().x < -1 -PIPE_WIDTH) {
            pipeCornerPositions.pollFirst();
            pipeUpHandlers.pollFirst();
            pipeDownHandlers.pollFirst();
        }
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
        clearRedundantPipes();
        fillPipeDeque();
        return null;
    }
}
