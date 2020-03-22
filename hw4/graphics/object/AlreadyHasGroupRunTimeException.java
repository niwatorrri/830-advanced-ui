package graphics.object;

public class AlreadyHasGroupRunTimeException extends RuntimeException {
    /**
     * AlreadyHasGroupRunTimeException class:
     * Thrown when an object wants to be added to two groups
     */
    private static final long serialVersionUID = 1L;
    public AlreadyHasGroupRunTimeException(String message) {
        super(message);
    }
    public AlreadyHasGroupRunTimeException() {
        super("Object is already in a group");
    }
}
