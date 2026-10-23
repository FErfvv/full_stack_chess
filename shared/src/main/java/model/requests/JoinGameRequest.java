package model.requests;

public record JoinGameRequest(String playerColor, String gameID) implements RequestInterface {
    @Override
    public boolean isValidRequest() {
        return playerColor != null && gameID != null;
    }
}
