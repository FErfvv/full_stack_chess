package model.requests;

public record RegisterRequest(String username,
                              String password,
                              String email) implements RequestInterface {
    @Override
    public boolean isValidRequest() {
        return username != null && password != null && email != null;
    }
}
