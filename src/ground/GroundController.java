package ground;

public class GroundController {
    private final static float MOVE_SPEED = .8f;
    private float anchor = 0;

    public Void elapse(Float time) {
        anchor += MOVE_SPEED * time;
        if (anchor > 1) {
            anchor = anchor - (float) Math.floor(anchor);
        }
        return null;
    }

    float getAnchor() {
        return anchor;
    }
}
