package dataaccess.exceptions;

/**
 * Indicates the game requested does not exist
 */
public class NoGameException extends BasicResponseException {
    public NoGameException() {
        super("Error: game doesn't exist", 403);
    }
}
