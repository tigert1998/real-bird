package bird;

import utils.*;

import java.util.*;

public class BirdController {
    private static final float JUMP_SPEED = .7f;
    private static final float FALLING_CONSTANT = 3.1f;
    private static final float MIN_SPEED = -1.3f;
    private static final int TOTAL_POSTURES = 3;
    private static final float SWITCH_POSTURE_DELTA_TIME = 0.1f;
    private static final float RAISE_HEAD_THRESHOLD_SPEED = 0.4f;

    private static final float BIRD_WIDTH =
            2.f * Settings.getBirdPosturePositions()[0].width / Settings.getSkylinePosition().width;
    private static final float BIRD_HEIGHT =
            2.f * Settings.getBirdPosturePositions()[0].height / Settings.getSkylinePosition().height;

    private boolean tapped = false;
    private float vertSpeed = 0;
    private float vertPosition = 0;
    private int postureID = 0;
    private float currentDeltaTime = 0;

    private boolean isStarted = false;
    private PhysicsPlaygroundHandler handler;

    private void updateHandler() {
        handler.convexPoints = PhysicsPlaygroundHandler.constructConvexPoints(
                -BIRD_WIDTH / 2.f, -BIRD_HEIGHT / 2.f + getVertPosition(),
                BIRD_WIDTH, BIRD_HEIGHT);
        Point origin = new Point(0, getVertPosition());
        for (int i = 0; i < handler.convexPoints.size(); i++) {
            handler.convexPoints.set(i, MathComplement.rotate(handler.convexPoints.get(i), origin, getAngle()));
        }
    }

    public BirdController() {
        handler = PhysicsPlayground.shared.addHandler(true, new ArrayList<>());
        updateHandler();
    }

    public void tap() {
        tapped = true;
    }

    public void setStarted(boolean isStarted) {
        tapped = false;
        vertSpeed = 0;
        vertPosition = 0;
        postureID = 0;
        currentDeltaTime = 0;
        this.isStarted = isStarted;
    }

    public boolean getStarted() {
        return isStarted;
    }

    public Void elapse(Float time) {
        if (isStarted) {
            currentDeltaTime += time;
            if (currentDeltaTime > SWITCH_POSTURE_DELTA_TIME) {
                int deltaPostureID = (int) Math.floor(currentDeltaTime / SWITCH_POSTURE_DELTA_TIME);
                if (vertSpeed > MIN_SPEED) {
                    postureID += deltaPostureID;
                    postureID %= TOTAL_POSTURES;
                }
                currentDeltaTime -= SWITCH_POSTURE_DELTA_TIME * deltaPostureID;
            }
            if (tapped) {
                vertSpeed = JUMP_SPEED;
            }
            vertPosition += vertSpeed * time;
            vertSpeed -= FALLING_CONSTANT * time;
            vertSpeed = Math.max(vertSpeed, MIN_SPEED);
            tapped = false;
        } else {
            currentDeltaTime += time;
            vertPosition = (float) Math.sin(currentDeltaTime * 3) * 0.03f;
        }
        updateHandler();
        return null;
    }

    float getVertPosition() {
        return vertPosition;
    }

    int getPostureID() {
        if (!isStarted) return 1;
        return postureID;
    }

    float getAngle() {
        if (!isStarted)
            return 0;
        if (vertSpeed >= RAISE_HEAD_THRESHOLD_SPEED)
            return (float) Math.PI * 0.1f;
        return (float) Math.PI * 0.6f * (vertSpeed - MIN_SPEED) / (RAISE_HEAD_THRESHOLD_SPEED - MIN_SPEED)
                - 0.5f * (float) Math.PI;
    }
}
