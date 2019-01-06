package pipe;

import utils.*;

import java.util.*;
import java.util.function.*;

public class PipeController {
    final static float VERT_GAP = .45f;
    private final static float MOVE_SPEED = .8f;
    private final static float HORI_GAP = 1.2f;
    private final static float MAX_RAND_Y = -.6f;
    private final static float MIN_RAND_Y = -1.3f;
    private final static int QUEUE_SIZE = 3;

    private final static PhysicsPlayground physicsPlayground = PhysicsPlayground.shared;

    // left bottom corner of pipe down
    Deque<Point> pipeCornerPositions = new ArrayDeque<>();
    private Deque<PhysicsPlaygroundHandler> pipeDownHandlers = new ArrayDeque<>();
    private Deque<PhysicsPlaygroundHandler> pipeUpHandlers = new ArrayDeque<>();
    private Map<Point, Boolean> pipePassed = new HashMap<>();
    private Function<Void, Void> addCountCallback;

    private State state = State.READY;

    private static float randomPipeY() {
        return (float) Math.random() * (MAX_RAND_Y - MIN_RAND_Y) + MIN_RAND_Y;
    }

    public PipeController(Function<Void, Void> addCountCallback) {
        this.addCountCallback = addCountCallback;
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
                        Settings.PIPE_RELATIVE_WIDTH,
                        Settings.PIPE_DOWN_RELATIVE_HEIGHT
                )));
        pipeUpHandlers.addLast(physicsPlayground.addHandler(false,
                PhysicsPlaygroundHandler.constructConvexPoints(
                        point.x,
                        point.y + Settings.PIPE_DOWN_RELATIVE_HEIGHT + VERT_GAP,
                        Settings.PIPE_RELATIVE_WIDTH,
                        Settings.PIPE_UP_RELATIVE_HEIGHT
                )));
        pipePassed.put(point, false);
    }

    private void randomlyAddPipe() {
        Point last = pipeCornerPositions.getLast();
        Point newLeftBottomCorner = new Point(last.x + HORI_GAP, randomPipeY());
        addPipe(newLeftBottomCorner);
    }

    private void clearRedundantPipes() {
        while (!pipeCornerPositions.isEmpty() && pipeCornerPositions.getFirst().x < -1 -Settings.PIPE_RELATIVE_WIDTH) {
            Point point = pipeCornerPositions.pollFirst();
            var handler = pipeUpHandlers.pollFirst();
            PhysicsPlayground.shared.removeHandler(false, handler);
            handler = pipeDownHandlers.pollFirst();
            PhysicsPlayground.shared.removeHandler(false, handler);
            pipePassed.remove(point);
        }
    }

    private void checkBirdPass() {
        for (Point point : pipeCornerPositions) {
            if (pipePassed.get(point)) continue;
            if (point.x < -Settings.PIPE_RELATIVE_WIDTH / 2) {
                pipePassed.put(point, true);
                if (addCountCallback != null) {
                    addCountCallback.apply(null);
                }
            }
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
        checkBirdPass();
    }

    public void setState(State state) {
        this.state = state;
        switch (state) {
            case READY:
                break;
            case STARTED:
                fillPipeDeque();
            case ENDED:
                break;
        }
    }

    public Void elapse(Float time) {
        switch (state) {
            case READY:
                return null;
            case STARTED:
                break;
            case ENDED:
                return null;
        }
        updatePipes(time);
        return null;
    }
}
