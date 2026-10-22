package model.requests;

public record NewGameRequest(String gameName) implements RequestInterface {
    @Override
    public boolean isValidRequest() {
        return gameName != null;
    }
}
