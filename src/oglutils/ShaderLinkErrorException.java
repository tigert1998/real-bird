package oglutils;

import java.nio.file.*;

public class ShaderLinkErrorException extends Exception {
    public ShaderLinkErrorException(Path vsPath, Path fsPath, String log) {
        super("oglutils.Shader at \"" + vsPath.toString() + "\" and \"" +
                fsPath.toString() + "\" links incorrectly with the following cause: \n" + log);
    }
}
