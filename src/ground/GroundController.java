package ground;

import utils.*;

public class GroundController {
    private final static float MOVE_SPEED = .8f;
    private float anchor = 0;

    private State state = State.READY;

    public void setState(State state) {
        this.state = state;
    }

    public GroundController() {
        // ground
        PhysicsPlayground.shared.addHandler(false,
                PhysicsPlaygroundHandler.constructConvexPoints(-1, -1,
                        2, Settings.GROUND_RELATIVE_HEIGHT));
        // sky
        PhysicsPlayground.shared.addHandler(false,
                PhysicsPlaygroundHandler.constructConvexPoints(-1, 1,
                        2, Settings.GROUND_RELATIVE_HEIGHT));
    }

    public Void elapse(Float time) {
        switch (state) {
            case READY:
                break;
            case STARTED:
                break;
            case ENDED:
                return null;
        }
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
