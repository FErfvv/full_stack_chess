package dataaccess.exceptions;

/**
 * Indicates the username was already taken
 */
public class UsernameAlreadyTakenException extends BasicResponseException {
    public UsernameAlreadyTakenException(String username) {
        super(String.format("Error: the username: %s has already been taken", username), 403);
    }
}
