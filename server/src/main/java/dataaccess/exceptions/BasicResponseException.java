package dataaccess.exceptions;

import java.util.Map;

public class BasicException extends Exception {

    private final int httpCode;

    public BasicException(String message, int httpCode) {
        super(message);
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public Map<String, String> getJsonMessage() {
        return Map.of("message", getMessage());
    }

}
