package oglutils;

import java.io.*;
import java.nio.file.*;

import static org.lwjgl.opengl.GL41C.*;

public class Shader {

    private int programID;

    public Shader(Path vsPath, Path fsPath) throws IOException, ShaderCompileErrorException, ShaderLinkErrorException {
        String vsSource = new String(Files.readAllBytes(vsPath));
        String fsSource = new String(Files.readAllBytes(fsPath));
        int vsID = compile(GL_VERTEX_SHADER, vsSource, vsPath);
        int fsID = compile(GL_FRAGMENT_SHADER, fsSource, fsPath);
        programID = link(vsID, fsID, vsPath, fsPath);
    }

    private int compile(int type, String source, Path path) throws ShaderCompileErrorException {
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, source);
        glCompileShader(shaderID);
        int[] successPointer = new int[1];
        glGetShaderiv(shaderID, GL_COMPILE_STATUS, successPointer);
        if (successPointer[0] > 0) return shaderID;
        String log = glGetShaderInfoLog(shaderID);
        throw new ShaderCompileErrorException(path, log);
    }

    private int link(int vsID, int fsID, Path vsPath, Path fsPath) throws ShaderLinkErrorException {
        int programID = glCreateProgram();
        glAttachShader(programID, vsID);
        glAttachShader(programID, fsID);
        glLinkProgram(programID);
        glDeleteShader(vsID);
        glDeleteShader(fsID);
        int[] successPointer = new int[1];
        glGetProgramiv(programID, GL_LINK_STATUS, successPointer);
        if (successPointer[0] > 0) return programID;
        String log = glGetProgramInfoLog(programID);
        throw new ShaderLinkErrorException(vsPath, fsPath, log);
    }

    public void use() {
        glUseProgram(programID);
    }

    public void setUniform(String identifier, int value) throws ShaderUniformInaccessibleException {
        int location = glGetUniformLocation(programID, identifier);
        if (location < 0) throw new ShaderUniformInaccessibleException(identifier);
        glUniform1i(location, value);
    }
}