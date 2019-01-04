package pipe;

import oglutils.*;

public class PipeView {
    private Picture pipeUp, pipeDown;
    private PipeController pipeController;

    public PipeView(PipeController pipeController, Picture pipeUp, Picture pipeDown) {
        this.pipeController = pipeController;
        this.pipeUp = pipeUp;
        this.pipeDown = pipeDown;
    }

    public void draw() {
        pipeController.pipeCornerPositions.forEach(pair -> {
            pipeDown.place(pair.x, pair.y, PipeController.PIPE_WIDTH, PipeController.PIPE_DOWN_HEIGHT, 0.3f);
            pipeDown.draw();
            pipeUp.place(pair.x, pair.y + PipeController.PIPE_DOWN_HEIGHT + PipeController.VERT_GAP,
                    PipeController.PIPE_WIDTH, PipeController.PIPE_UP_HEIGHT, 0.3f);
            pipeUp.draw();
        });
    }
}
