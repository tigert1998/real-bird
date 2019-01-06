package oglutils;

import java.nio.*;
import java.nio.file.*;

import org.lwjgl.stb.*;
import utils.*;

import static org.lwjgl.opengl.GL41C.*;

public class Texture {
    static private class LoadImageResult {
        ByteBuffer byteBuffer;
        int width, height, nrChannels;
        private LoadImageResult(ByteBuffer byteBuffer, int width, int height, int nrChannels) {
            this.byteBuffer = byteBuffer;
            this.width = width;
            this.height = height;
            this.nrChannels = nrChannels;
        }
    }

    private int textureID, width, height, nrChannels;

    private LoadImageResult loadImage(Path path) {
        int[] widthPointer = new int[1], heightPointer = new int[1], nrChannelsPointer = new int[1];
        ByteBuffer byteBuffer = UniformReader.getByteBuffer(path);
        if (byteBuffer == null) return null;
        byteBuffer = STBImage.stbi_load_from_memory(byteBuffer, widthPointer, heightPointer, nrChannelsPointer, 0);
        return new LoadImageResult(byteBuffer, widthPointer[0], heightPointer[0], nrChannelsPointer[0]);
    }

    private void genTextureID(ByteBuffer byteBuffer, int width, int height, int nrChannels) {
        this.textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, this.textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        if (nrChannels == 3)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, byteBuffer);
        else if (nrChannels == 4)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBuffer);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public Texture(Path path) {
        STBImage.stbi_set_flip_vertically_on_load(true);

        LoadImageResult loadImageResult = loadImage(path);
        if (loadImageResult == null)
            throw new IllegalArgumentException("oglutils.Texture at \"" + path + "\" is not accessible.");

        width = loadImageResult.width;
        height = loadImageResult.height;
        nrChannels = loadImageResult.nrChannels;
        ByteBuffer byteBuffer = loadImageResult.byteBuffer;

        genTextureID(byteBuffer, width, height, nrChannels);
    }

    public int getTextureID() {
        return textureID;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getNrChannels() {
        return nrChannels;
    }
}
