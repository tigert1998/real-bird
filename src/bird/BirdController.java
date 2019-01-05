package bird;

import utils.*;

import java.util.*;

public class BirdController {
    private static final float JUMP_SPEED = .9f;
    private static final float FALLING_CONSTANT = 3.2f;
    private static final float MIN_SPEED = -1.4f;
    private static final int TOTAL_POSTURES = 3;
    private static final float SWITCH_POSTURE_DELTA_TIME = 0.1f;
    private static final float RAISE_HEAD_THRESHOLD_SPEED = 0.4f;

    private static final float HIT_SIDE_LENGTH = Settings.BIRD_RELATIVE_HEIGHT;

    private boolean tapped = false;
    private float vertSpeed = 0;
    private float vertPosition = 0;
    private int postureID = 0;
    private float currentDeltaTime = 0;

    private PhysicsPlaygroundHandler handler;

    private void updateHandler() {
        handler.convexPoints = PhysicsPlaygroundHandler.constructConvexPoints(
                (-HIT_SIDE_LENGTH / 2.f),
                (-HIT_SIDE_LENGTH / 2.f + getVertPosition()),
                HIT_SIDE_LENGTH,
                HIT_SIDE_LENGTH);
    }

    public BirdController() {
        handler = PhysicsPlayground.shared.addHandler(true, new ArrayList<>());
        updateHandler();
    }

    private State state = State.READY;

    public void setState(State state) {
        this.state = state;
        switch (state) {
            case READY:
                return;
            case STARTED:
                tapped = false;
                vertSpeed = 0;
                vertPosition = 0;
                postureID = 0;
                currentDeltaTime = 0;
            case ENDED:
                tapped = false;
                vertSpeed = 0;
        }
    }

    public void tap() {
        tapped = true;
    }

    public Void elapse(Float time) {
        switch (state) {
            case READY:
                currentDeltaTime += time;
                vertPosition = (float) Math.sin(currentDeltaTime * 3) * 0.03f;
                updateHandler();
                return null;
            case STARTED:
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
                updateHandler();
                return null;
            case ENDED:
                vertSpeed -= FALLING_CONSTANT * time;
                vertSpeed = Math.max(vertSpeed, MIN_SPEED);
                vertPosition += vertSpeed * time;
                vertPosition = Math.max(vertPosition, Settings.GROUND_RELATIVE_HEIGHT - 1);
                return null;
        }
        return null;
    }

    float getVertPosition() {
        return vertPosition;
    }

    int getPostureID() {
        switch (state) {
            case READY:
                return 1;
            default:
                return postureID;
        }
    }

    float getAngle() {
        switch (state) {
            case READY:
                return 0;
            default:
                if (vertSpeed >= RAISE_HEAD_THRESHOLD_SPEED)
                    return (float) Math.PI * 0.1f;
                return (float) Math.PI * 0.6f * (vertSpeed - MIN_SPEED) / (RAISE_HEAD_THRESHOLD_SPEED - MIN_SPEED)
                        - 0.5f * (float) Math.PI;
        }
    }
}
