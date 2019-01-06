package utils;

import org.lwjgl.*;

import java.io.*;
import java.nio.*;
import java.nio.file.*;

public class UniformReader {
    static public InputStream getInputStream(Path path) {
        InputStream inputStream = UniformReader.class.getResourceAsStream("/" + path.toString());
        if (inputStream == null) {
            // not in jar
            Path realPath = Paths.get(System.getProperty("user.dir"), path.toString());
            try {
                inputStream = new FileInputStream(realPath.toString());
            } catch (Exception exception) {
                inputStream = null;
            }
        }
        return inputStream;
    }

    static public byte[] getByteArray(Path path) {
        try {
            return getInputStream(path).readAllBytes();
        } catch (Exception exception) {
            return null;
        }
    }

    static public ByteBuffer getByteBuffer(Path path) {
        byte[] bytes = getByteArray(path);
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        return byteBuffer;
    }
}
