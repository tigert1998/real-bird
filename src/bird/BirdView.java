package bird;

import oglutils.*;

public class BirdView {
    private BirdController birdController;
    private Picture[] postures;
    private float widthInCoord, heightInCoord;

    public BirdView(BirdController birdController, Picture[] postures, float widthInCoord, float heightInCoord) {
        this.birdController = birdController;
        this.postures = postures;
        this.widthInCoord = widthInCoord;
        this.heightInCoord = heightInCoord;
    }

    public void draw() {
        int postureID = birdController.getPostureID();
        Picture posture = postures[postureID];
        posture.place(-widthInCoord / 2, birdController.getVertPosition() - heightInCoord / 2,
                widthInCoord, heightInCoord, 0.5f);
        posture.draw();
    }
}
