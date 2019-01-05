package pipe;

import utils.*;

import java.util.*;

public class PipeController {
    final static float VERT_GAP = .45f;
    private final static float MOVE_SPEED = .8f;
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
    private Deque<PhysicsPlaygroundHandler> pipeDownHandlers = new ArrayDeque<>();
    private Deque<PhysicsPlaygroundHandler> pipeUpHandlers = new ArrayDeque<>();

    private boolean isStarted = false;

    private static float randomPipeY() {
        return (float) Math.random() * MAX_RAND_Y - 1;
    }

    private void fillPipeDeque() {
        if (pipeCornerPositions.isEmpty()) {
            addPipe(new Point(1 + HORI_GAP, randomPipeY()));
        }
        while (pipeCornerPositions.size() < QUEUE_SIZE)
            randomlyAddPipe();
    }

    private void addPipe(Point point) {
        pipeCornerPositions.addLast(point);
        pipeDownHandlers.addLast(physicsPlayground.addHandler(false,
                PhysicsPlaygroundHandler.constructConvexPoints(
                        point.x, point.y,
                        PIPE_WIDTH, PIPE_DOWN_HEIGHT
                )));
        pipeUpHandlers.addLast(physicsPlayground.addHandler(false,
                PhysicsPlaygroundHandler.constructConvexPoints(
                        point.x, point.y + PIPE_DOWN_HEIGHT + VERT_GAP,
                        PIPE_WIDTH, PIPE_UP_HEIGHT
                )));
    }

    private void randomlyAddPipe() {
        Point last = pipeCornerPositions.getLast();
        Point newLeftBottomCorner = new Point(last.x + HORI_GAP, randomPipeY());
        addPipe(newLeftBottomCorner);
    }

    private void clearRedundantPipes() {
        while (!pipeCornerPositions.isEmpty() && pipeCornerPositions.getFirst().x < -1 -PIPE_WIDTH) {
            pipeCornerPositions.pollFirst();
            pipeUpHandlers.pollFirst();
            pipeDownHandlers.pollFirst();
        }
    }

    private void updatePipes(Float time) {
        var cornerPositionsIterator = pipeCornerPositions.iterator();
        var downHandlersIterator = pipeDownHandlers.iterator();
        var upHandlersIterator = pipeUpHandlers.iterator();

        while (cornerPositionsIterator.hasNext()) {
            {
                Point point = cornerPositionsIterator.next();
                point.x -= time * MOVE_SPEED;
            }
            {
                var handler = downHandlersIterator.next();
                handler.convexPoints.forEach(point -> {
                    point.x -= time * MOVE_SPEED;
                });
            }
            {
                var handler = upHandlersIterator.next();
                handler.convexPoints.forEach(point -> {
                    point.x -= time * MOVE_SPEED;
                });
            }
        }
        clearRedundantPipes();
        fillPipeDeque();
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
        if (isStarted) {
            fillPipeDeque();
        }
    }

    public Void elapse(Float time) {
        if (!isStarted) return null;
        updatePipes(time);
        return null;
    }
}
