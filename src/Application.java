import java.awt.*;
import java.nio.file.*;

import bird.*;
import counter.*;
import ground.*;
import oglutils.*;
import org.lwjgl.opengl.*;
import pipe.*;
import utils.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL41C.*;


public class Application {
    private static final int WIDTH = 432;
    private static final int HEIGHT = 768;
    private static final Path RESOURCE_PATH = Paths.get("resources");
    private static final PhysicsPlayground physicsPlayground = PhysicsPlayground.shared;

    private State state = State.READY;

    private long window;

    private Picture skyline;

    private Clock clock = new Clock();

    private BirdController birdController;
    private BirdView birdView;
    private GroundController groundController;
    private GroundView groundView;
    private PipeController pipeController;
    private PipeView pipeView;
    private CounterController counterController;
    private CounterView counterView;

    private void setState(State state) {
        this.state = state;
        birdController.setState(state);
        pipeController.setState(state);
        groundController.setState(state);
        counterController.setState(state);
    }

    private void initGL() {
        if (!glfwInit())
            throw new IllegalStateException("Fail to initialize GLFW");
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        this.window = glfwCreateWindow(WIDTH, HEIGHT, "Real Bird", 0, 0);
        if (this.window == 0)
            throw new IllegalStateException("Fail to initialize GLFW window");

        glfwSetKeyCallback(this.window, (window, key, scanCode, action, mods) -> {
            if (key == 'Q')
                System.exit(0);
            else if (key == ' ') {
                if (state == State.READY)
                    setState(State.STARTED);
                birdController.tap();
            } else if (key == 'R') {
                if (state == State.ENDED)
                    setState(State.READY);
            }
        });
        glfwMakeContextCurrent(this.window);
        glfwShowWindow(this.window);

        GL.createCapabilities();
    }

    private void loadResources() {
        Picture resources = new Picture(RESOURCE_PATH.resolve("textures.png"));
        Rectangle skylinePosition = Settings.getSkylinePosition();

        skyline = new Picture(resources, skylinePosition);
        skyline.place(-1, -1, 2, 2, .5f);

        birdController = new BirdController();
        birdView = new BirdView(birdController,
                Picture.picturesFromPositions(resources, Settings.getBirdPosturePositions()));

        groundController = new GroundController();
        groundView = new GroundView(groundController,
                new Picture(resources, Settings.getGroundPosition()));

        pipeController = new PipeController(Void -> {
            counterController.addCount();
            return null;
        });
        pipeView = new PipeView(pipeController,
                new Picture(resources, Settings.getPipeUpPosition()),
                new Picture(resources, Settings.getPipeDownPosition()));

        counterController = new CounterController();
        counterView = new CounterView(counterController,
                Picture.picturesFromPositions(resources, Settings.getBigNumberPositions()));

        clock.register(birdController::elapse);
        clock.register(groundController::elapse);
        clock.register(pipeController::elapse);
    }

    private void renderLoop() {
        glEnable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0, 1.0f, 0, 1.0f);

        Float lastTime = null;
        while (!glfwWindowShouldClose(this.window)) {
            float currentTime = (float) glfwGetTime();
            if (lastTime != null) clock.elapse(currentTime - lastTime);
            lastTime = currentTime;

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            skyline.draw();
            groundView.draw();
            pipeView.draw();
            birdView.draw();
            counterView.draw();

            if (state == State.STARTED && physicsPlayground.hit()) {
                setState(State.ENDED);
            }

            glfwSwapBuffers(this.window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        Application app = new Application();

        app.initGL();
        app.loadResources();

        app.renderLoop();
    }
}
