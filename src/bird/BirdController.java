package bird;

public class BirdController {
    private static final float JUMP_SPEED = .7f;
    private static final float FALLING_CONSTANT = 3.1f;
    private static final float MIN_SPEED = -1.3f;
    private static final int TOTAL_POSTURES = 3;
    private static final float SWITCH_POSTURE_DELTA_TIME = 0.1f;
    private static final float RAISE_HEAD_THRESHOLD_SPEED = 0.4f;

    private boolean tapped = false;
    private float vertSpeed = 0;
    private float vertPosition = 0;
    private int postureID = 0;
    private float currentDeltaTime = 0;

    public void tap() {
        tapped = true;
    }

    public void clear() {
        tapped = false;
        vertSpeed = 0;
        vertPosition = 0;
        postureID = 0;
        currentDeltaTime = 0;
    }

    public Void elapse(Float time) {
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
        return null;
    }

    float getVertPosition() {
        return vertPosition;
    }

    int getPostureID() {
        return postureID;
    }

    float getAngle() {
        if (vertSpeed >= RAISE_HEAD_THRESHOLD_SPEED)
            return (float) Math.PI * 0.1f;
        return (float) Math.PI * 0.6f * (vertSpeed - MIN_SPEED) / (RAISE_HEAD_THRESHOLD_SPEED - MIN_SPEED)
                - 0.5f * (float) Math.PI;
    }
}
