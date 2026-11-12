package exceptions;

/**
 * Indicates the player already joined that game
 */
public class AlreadyJoinedException extends BasicResponseException {
    public AlreadyJoinedException() {
        super("Error: you have already joined this game. You cannot join again.", 403);
    }
}
