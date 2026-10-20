package dataaccess.exceptions;

/**
 * Indicates the username was already taken
 */
public class UsernameAlreadyTakenException extends Exception {
    public UsernameAlreadyTakenException(String message) {
        super(message);
    }

    public UsernameAlreadyTakenException(String message, Throwable ex) {
        super(message, ex);
    }
}
