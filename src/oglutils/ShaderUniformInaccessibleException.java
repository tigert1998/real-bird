package oglutils;

public class ShaderUniformInaccessibleException extends Exception {
    public ShaderUniformInaccessibleException(String identifier) {
        super("Uniform variable \"" + identifier + "\" is inaccessible.");
    }
}
