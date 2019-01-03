import java.nio.file.*;

import bird.BirdController;
import bird.BirdView;
import oglutils.Picture;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL41C.*;


public class Application {
    private static final int WIDTH = 432;
    private static final int HEIGHT = 768;
    private static final Path RESOURCE_PATH = Paths.get("/Users/tigertang/Projects/real-bird/resources");

    private long window;

    private Picture skyline;

    private Clock clock = new Clock();
    private BirdView birdView;
    private BirdController birdController;

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
            else if (key == ' ')
                birdController.tap();
        });
        glfwMakeContextCurrent(this.window);
        glfwShowWindow(this.window);

        GL.createCapabilities();
    }

    private void loadResources() {
        Picture resources = new Picture(RESOURCE_PATH.resolve("textures.png"));
        skyline = new Picture(resources, 0, 0, 144, 256);
        skyline.place(-1, -1, 2, 2, 1);

        birdController = new BirdController();
        birdView = new BirdView(birdController, new Picture[] {
                new Picture(resources, 264, 180, 17, 12),
        }, 34.f / 144, 24.f / 256);

        clock.register(birdController::elapse);
    }

    private void renderLoop() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0, 1.0f, 0, 1.0f);

        Double lastTime = null;
        while (!glfwWindowShouldClose(this.window)) {
            double currentTime = glfwGetTime();
            if (lastTime != null) clock.elapse(currentTime - lastTime);
            lastTime = currentTime;

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            skyline.draw();
            birdView.draw();

            glfwSwapBuffers(this.window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        var app = new Application();

        app.initGL();
        app.loadResources();

        app.renderLoop();
    }
}
