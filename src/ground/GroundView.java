package ground;

import oglutils.*;

public class GroundView {
    private GroundController groundController;
    private Picture picture;
    private int backgroundWidth, backgroundHeight;

    public GroundView(GroundController groundController, Picture picture, int backgroundWidth, int backgroundHeight) {
        this.groundController = groundController;
        this.picture = picture;
        this.backgroundWidth = backgroundWidth;
        this.backgroundHeight = backgroundHeight;
    }

    public void draw() {
        float anchor = groundController.getAnchor();
        picture.place(-backgroundWidth / 2.f - anchor * backgroundWidth, -backgroundHeight / 2.f,
                backgroundWidth, picture.getHeight(),
                backgroundWidth, backgroundHeight, 0, 0);
        picture.draw();
        picture.place(backgroundWidth / 2.f - anchor * backgroundWidth, -backgroundHeight / 2.f,
                backgroundWidth, picture.getHeight(),
                backgroundWidth, backgroundHeight, 0, 0);
        picture.draw();
    }
}
