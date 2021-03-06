package oglutils;

import java.awt.*;
import java.nio.file.*;

import static org.lwjgl.opengl.GL41C.*;

import utils.Point;

import static utils.MathComplement.*;

public class Picture {
    private static final Path SHADERS_PATH = Paths.get("shaders");

    private static Shader shader = null;
    private Texture texture;
    private Integer vao = null, vbo = null;

    private int x, y, width, height;

    static {
        Path vsPath = SHADERS_PATH.resolve("picture.vert");
        Path fsPath = SHADERS_PATH.resolve("picture.frag");
        try {
            shader = new Shader(vsPath, fsPath);
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(0);
        }
    }

    public static Picture[] picturesFromPositions(Picture resources, Rectangle[] rectangles) {
        Picture[] result = new Picture[rectangles.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Picture(resources, rectangles[i]);
        }
        return result;
    }

    private void releaseVertexBuffers() {
        if (vao != null) {
            glDeleteVertexArrays(vao);
            vao = null;
        }
        if (vbo != null) {
            glDeleteBuffers(vbo);
            vbo = null;
        }
    }

    private void generateVertexBuffers(float[] vertices) {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES * 5, 0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Float.BYTES * 5, Float.BYTES * 3);

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public Picture(Path path) {
        texture = new Texture(path);
        x = 0;
        y = 0;
        width = texture.getWidth();
        height = texture.getHeight();
    }

    public Picture(Picture picture, Rectangle rectangle) {
        texture = picture.texture;
        x = rectangle.x;
        y = rectangle.y;
        width = rectangle.width;
        height = rectangle.height;
    }

    private float getTexCoordX() {
        return (float) x / texture.getWidth();
    }

    private float getTexCoordY() {
        return (float) y / texture.getHeight();
    }

    private float getTexCoordW() {
        return (float) width / texture.getWidth();
    }

    private float getTexCoordH() {
        return (float) height / texture.getHeight();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void place(float xInScreen, float yInScreen,
                      float widthInScreen, float heightInScreen,
                      float screenWidth, float screenHeight,
                      float z, float angle) {
        Point origin = new Point(xInScreen + widthInScreen / 2, yInScreen + heightInScreen / 2);
        Point rightTop = rotate(new Point(xInScreen + widthInScreen, yInScreen + heightInScreen), origin, angle);
        Point leftTop = rotate(new Point(xInScreen, yInScreen + heightInScreen), origin, angle);
        Point leftBottom = rotate(new Point(xInScreen, yInScreen), origin, angle);
        Point rightBottom = rotate(new Point(xInScreen + widthInScreen, yInScreen), origin, angle);

        Point divider = new Point(screenWidth / 2, screenHeight / 2);
        rightTop.divide(divider);
        leftTop.divide(divider);
        leftBottom.divide(divider);
        rightBottom.divide(divider);

        float[] vertices = {
                leftBottom.x, leftBottom.y, z, getTexCoordX(), getTexCoordY(),
                rightBottom.x, rightBottom.y, z, getTexCoordX() + getTexCoordW(), getTexCoordY(),
                leftTop.x, leftTop.y, z, getTexCoordX(), getTexCoordY() + getTexCoordH(),
                rightTop.x, rightTop.y, z, getTexCoordX() + getTexCoordW(), getTexCoordY() + getTexCoordH()
        };

        generateVertexBuffers(vertices);
    }

    public void place(float x, float y, float width, float height, float z) {
        releaseVertexBuffers();

        float[] vertices = {
                x, y, z, getTexCoordX(), getTexCoordY(),
                x + width, y, z, getTexCoordX() + getTexCoordW(), getTexCoordY(),
                x, y + height, z, getTexCoordX(), getTexCoordY() + getTexCoordH(),
                x + width, y + height, z, getTexCoordX() + getTexCoordW(), getTexCoordY() + getTexCoordH()
        };

        generateVertexBuffers(vertices);
    }

    public void draw() {
        shader.use();
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());

        try {
            shader.setUniform("uSampler", 0);
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(0);
        }

        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
        glBindVertexArray(0);
    }

    public Texture getTexture() {
        return texture;
    }
}
