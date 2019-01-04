package oglutils;

import java.nio.file.*;

import static org.lwjgl.opengl.GL41C.*;

public class Picture {
    private static final Path SHADERS_PATH = Paths.get("/Users/tigertang/Projects/real-bird/shaders");

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

    /**
     * Generate a sub-picture of a already generated picture. Share the same texture with the former one.
     * @param picture original picture
     * @param x x of lower left corner
     * @param y y of lower left corner
     * @param width width of sub-picture. the number of pixels in a row
     * @param height height of sub-picture. the number of pixels in a column
     */
    public Picture(Picture picture, int x, int y, int width, int height) {
        texture = picture.texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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

    public void place(float x, float y, float width, float height,
                      float backgroundWidth, float backgroundHeight, float distance, float angle) {
        Pair origin = new Pair(x + width / 2, y + height / 2);
        Pair rightTop = rotate(new Pair(x + width, y + height), origin, angle);
        Pair leftTop = rotate(new Pair(x, y + height), origin, angle);
        Pair leftBottom = rotate(new Pair(x, y), origin, angle);
        Pair rightBottom = rotate(new Pair(x + width, y), origin, angle);

        Pair divider = new Pair(backgroundWidth / 2, backgroundHeight / 2);
        rightTop.divide(divider);
        leftTop.divide(divider);
        leftBottom.divide(divider);
        rightBottom.divide(divider);

        float[] vertices = {
                leftBottom.x, leftBottom.y, -distance, getTexCoordX(), getTexCoordY(),
                rightBottom.x, rightBottom.y, -distance, getTexCoordX() + getTexCoordW(), getTexCoordY(),
                leftTop.x, leftTop.y, -distance, getTexCoordX(), getTexCoordY() + getTexCoordH(),
                rightTop.x, rightTop.y, -distance, getTexCoordX() + getTexCoordW(), getTexCoordY() + getTexCoordH()
        };

        generateVertexBuffers(vertices);
    }

    public void place(float x, float y, float width, float height, float distance) {
        releaseVertexBuffers();

        float[] vertices = {
                x, y, -distance, getTexCoordX(), getTexCoordY(),
                x + width, y, -distance, getTexCoordX() + getTexCoordW(), getTexCoordY(),
                x, y + height, -distance, getTexCoordX(), getTexCoordY() + getTexCoordH(),
                x + width, y + height, -distance, getTexCoordX() + getTexCoordW(), getTexCoordY() + getTexCoordH()
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

    private static class Pair {
        float x;
        float y;
        private Pair(float x, float y) {
            this.x = x;
            this.y = y;
        }
        private void divide(Pair divider) {
            this.x = this.x / divider.x;
            this.y = this.y / divider.y;
        }
    }

    private static Pair rotate(Pair p, Pair o, float theta) {
        double cos = Math.cos(theta), sin = Math.sin(theta);
        double x = (p.x - o.x) * cos - (p.y - o.y) * sin + o.x;
        double y = (p.x - o.x) * sin + (p.y - o.y) * cos + o.y;
        return new Pair((float) x, (float) y);
    }

    public Texture getTexture() {
        return texture;
    }
}
