package oglutils;

import java.nio.file.*;

public class ShaderCompileErrorException extends Exception {
    public ShaderCompileErrorException(Path path, String log) {
        super("oglutils.Shader at \"" + path.toString() + "\" compiles incorrectly with the following cause: \n" + log);
    }
}
