package bird;

import oglutils.*;

public class BirdView {
    private BirdController birdController;
    private Picture[] postures;
    private int backgroundWidth, backgroundHeight;

    public BirdView(BirdController birdController, Picture[] postures, int backgroundWidth, int backgroundHeight) {
        this.birdController = birdController;
        this.postures = postures;
        this.backgroundWidth = backgroundWidth;
        this.backgroundHeight = backgroundHeight;
    }

    public void draw() {
        int postureID = birdController.getPostureID();
        Picture posture = postures[postureID];
        float postureWidth = posture.getWidth();
        float postureHeight = posture.getHeight();
        posture.place(-postureWidth / 2, -postureHeight / 2 + birdController.getVertPosition() * backgroundHeight,
                postureWidth, postureHeight,
                backgroundWidth, backgroundHeight, -.5f, birdController.getAngle());
        posture.draw();
    }
}
