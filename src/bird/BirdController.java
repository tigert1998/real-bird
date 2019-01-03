package bird;

public class BirdController {
    private final float JUMP_SPEED = 1.0f;
    private final float FALLING_CONSTANT = 4.1f;
    private final float MIN_SPEED = -1.4f;

    private boolean tapped = false;
    private float vertSpeed = 0;
    private float vertPosition = 0;

    public void tap() {
        tapped = true;
    }

    public void clear() {
        tapped = false;
        vertSpeed = 0;
        vertPosition = 0;
    }

    public Void elapse(Double time) {
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
        return 0;
    }
}
