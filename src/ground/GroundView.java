package ground;

import oglutils.*;
import utils.*;

public class GroundView {
    private static final float backgroundWidth = Settings.getSkylinePosition().width;
    private static final float backgroundHeight = Settings.getSkylinePosition().height;

    private GroundController groundController;
    private Picture picture;

    public GroundView(GroundController groundController, Picture picture) {
        this.groundController = groundController;
        this.picture = picture;
    }

    public void draw() {
        float anchor = groundController.getAnchor();
        picture.place(
                -backgroundWidth / 2.f - anchor * backgroundWidth / 2.f, -backgroundHeight / 2.f,
                backgroundWidth, picture.getHeight(),
                backgroundWidth, backgroundHeight, 0, 0);
        picture.draw();
        picture.place(
                backgroundWidth / 2.f - anchor * backgroundWidth / 2.f, -backgroundHeight / 2.f,
                backgroundWidth, picture.getHeight(),
                backgroundWidth, backgroundHeight, 0, 0);
        picture.draw();
    }
}
