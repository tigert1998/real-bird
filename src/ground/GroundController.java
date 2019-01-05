package ground;

import utils.*;

public class GroundController {
    private final static float GROUND_HEIGHT =
            2.f * Settings.getGroundPosition().height / Settings.getSkylinePosition().height;
    private final static float MOVE_SPEED = .8f;
    private float anchor = 0;

    public GroundController() {
        PhysicsPlayground.shared.addHandler(false,
                PhysicsPlaygroundHandler.constructConvexPoints(-1, -1, 2, GROUND_HEIGHT));
    }

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
