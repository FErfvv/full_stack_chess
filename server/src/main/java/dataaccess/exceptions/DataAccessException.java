package dataaccess.exceptions;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends BasicResponseException {
    public DataAccessException(String descriptionOfError) {
        super(String.format("Error: %s", descriptionOfError), 500);
    }
}
