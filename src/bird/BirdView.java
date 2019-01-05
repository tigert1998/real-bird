package bird;

import oglutils.*;
import utils.*;

public class BirdView {
    private static final float backgroundWidth = Settings.getSkylinePosition().width;
    private static final float backgroundHeight = Settings.getSkylinePosition().height;

    private BirdController birdController;
    private Picture[] postures;

    public BirdView(BirdController birdController, Picture[] postures) {
        this.birdController = birdController;
        this.postures = postures;
    }

    public void draw() {
        int postureID = birdController.getPostureID();
        Picture posture = postures[postureID];
        float postureWidth = posture.getWidth();
        float postureHeight = posture.getHeight();
        posture.place(
                -postureWidth / 2,
                -postureHeight / 2 + birdController.getVertPosition() * backgroundHeight / 2.f,
                postureWidth, postureHeight,
                backgroundWidth, backgroundHeight, -.5f, birdController.getAngle());
        posture.draw();
    }
}
