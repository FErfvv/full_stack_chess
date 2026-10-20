package dataaccess;

/**
 * Indicates the game requested does not exist
 */
public class NoGameException extends Exception {
    public NoGameException(String message) {
        super(message);
    }

    public NoGameException(String message, Throwable ex) {
        super(message, ex);
    }
}
