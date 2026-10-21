package model.requests;

public record AuthRequest(String authToken) implements RequestInterface {
    @Override
    public boolean isValidRequest() {
        return authToken != null;
    }
}
