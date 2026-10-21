package model.requests;

public record LoginRequest(String username,
                           String password) implements RequestInterface {
    @Override
    public boolean isValidRequest() {
        return username != null && password != null;
    }
}
