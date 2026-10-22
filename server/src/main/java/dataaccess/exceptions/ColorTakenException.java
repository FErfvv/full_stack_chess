package dataaccess.exceptions;

/**
 * Indicates the color requested was full
 */
public class ColorTakenException extends BasicResponseException {
    public ColorTakenException() {
        super("Error: The requested color was already taken", 403);
    }
}
