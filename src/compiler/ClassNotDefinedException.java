package compiler;

public class ClassNotDefinedException extends RuntimeException {
    public ClassNotDefinedException(String message) {
        super(message);
    }
}
