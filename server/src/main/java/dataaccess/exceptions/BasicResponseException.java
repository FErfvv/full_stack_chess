package dataaccess.exceptions;

import com.google.gson.Gson;

import java.util.Map;

public class BasicResponseException extends Exception {

    private final int httpCode;

    public BasicResponseException(String message, int httpCode) {
        super(message);
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getJsonMessage() {
        return new Gson().toJson(Map.of("message", getMessage()));
    }

}
