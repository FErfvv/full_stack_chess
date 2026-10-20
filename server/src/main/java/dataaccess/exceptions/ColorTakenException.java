package dataaccess.exceptions;

/**
 * Indicates the color requested was full
 */
public class ColorTakenException extends Exception {
    public ColorTakenException(String message) {
        super(message);
    }

    public ColorTakenException(String message, Throwable ex) {
        super(message, ex);
    }
}
