package pipe;

import oglutils.*;
import utils.*;

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
            pipeDown.place(pair.x, pair.y,
                    Settings.PIPE_RELATIVE_WIDTH,
                    Settings.PIPE_DOWN_RELATIVE_HEIGHT, 0.3f);
            pipeDown.draw();
            pipeUp.place(pair.x, pair.y + Settings.PIPE_DOWN_RELATIVE_HEIGHT + PipeController.VERT_GAP,
                    Settings.PIPE_RELATIVE_WIDTH, Settings.PIPE_UP_RELATIVE_HEIGHT, 0.3f);
            pipeUp.draw();
        });
    }
}
