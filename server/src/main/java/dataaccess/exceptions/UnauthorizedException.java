package dataaccess.exceptions;

/**
 * Indicates there was an error connecting to the database
 */
public class UnauthorizedException extends BasicResponseException {
    public UnauthorizedException(String message) {
        super(String.format("Error: %s", message), 401);
    }
}
