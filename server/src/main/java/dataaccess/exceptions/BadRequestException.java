package dataaccess.exceptions;

/**
 * Indicates there was an error connecting to the database
 */
public class BadRequestException extends BasicResponseException {
    public BadRequestException() {
        super("Error: bad request", 400);
    }
}
