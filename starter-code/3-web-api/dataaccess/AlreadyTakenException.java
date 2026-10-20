package dataaccess;

/**
 * Indicates the position requested was already taken in the game
 */
public class AlreadyTakenException extends Exception {
    public AlreadyTakenException(String message) {
        super(message);
    }

    public AlreadyTakenException(String message, Throwable ex) {
        super(message, ex);
    }
}
