package oglutils;

import java.nio.file.*;

import static org.lwjgl.opengl.GL41C.*;

public class Picture {
    private static final Path SHADERS_PATH = Paths.get("/Users/tigertang/Projects/real-bird/shaders");

    private static Shader shader = null;
    private Texture texture = null;
    private Integer vao = null, vbo = null;

    private float texCoordX = 0, texCoordY = 0, texCoordW = 1, texCoordH = 1; // texture coordinates

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

    public void releaseVertexBuffers() {
        if (vao != null) {
            glDeleteVertexArrays(vao);
            vao = null;
        }
        if (vbo != null) {
            glDeleteBuffers(vbo);
            vbo = null;
        }
    }

    public Picture(Path path) {
        texture = new Texture(path);
    }

    /**
     * Generate a sub-picture of a generated picture. Share the same texture with the former one.
     * @param picture original picture
     * @param x x of left top corner
     * @param y y of left top corner
     * @param width width of sub-picture
     * @param height height of sub-picture
     */
    public Picture(Picture picture, int x, int y, int width, int height) {
        texture = picture.texture;
        texCoordX = (float) x / texture.getWidth();
        texCoordY = (float) y / texture.getHeight();
        texCoordW = (float) width / texture.getWidth();
        texCoordH = (float) height / texture.getHeight();
    }

    public void place(float x, float y, float width, float height, float distance) {
        releaseVertexBuffers();

        float[] vertices = {
                x, y, -distance, texCoordX, texCoordY,
                x + width, y, -distance, texCoordX + texCoordW, texCoordY,
                x, y + height, -distance, texCoordX, texCoordY + texCoordH,
                x + width, y + height, -distance, texCoordX + texCoordW, texCoordY + texCoordH
        };

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
}
