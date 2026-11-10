package client;

import com.google.gson.Gson;
import exceptions.BasicResponseException;
import jdk.jshell.spi.ExecutionControl;
import model.AuthData;
import model.requests.RegisterRequest;

import java.net.*;
import java.net.http.*;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private AuthData authData;

    public ServerFacade(String url) {
        serverUrl = url;
        authData = null;
    }

    public AuthData register(String[] params) throws BasicResponseException {
        HttpRequest request = buildRequest(
                "POST",
                "/user",
                null,
                new RegisterRequest(params[0], params[1], params[2]));
        HttpResponse<String> response = sendRequest(request);
        authData = handleResponse(response, AuthData.class);
        return authData;
    }

    public void login() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not yet implemented");
    }

    public void logout() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not yet implemented");
    }

    public void quit() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not yet implemented");
    }

    public void createGame() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not yet implemented");
    }

    public void listGames() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not yet implemented");
    }

    public void playGame() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not yet implemented");
    }

    public void observeGame() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not yet implemented");
    }

    public HttpResponse<String> sendRequest(HttpRequest request) throws BasicResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new BasicResponseException("Error sending request: " + ex.getMessage(), 500);
        }
    }

    public HttpRequest buildRequest(String method, String path, String authToken, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("Authorization", authToken);
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    public <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws BasicResponseException {
        if (!isSuccessful(response.statusCode())) {
            if (response.body() != null) {
                throw new BasicResponseException(response.body(), response.statusCode());
            }
            throw new BasicResponseException("Error in receiving request", response.statusCode());
        }

        if (response.body() != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }


    private boolean isSuccessful(int statusCode) {
        return statusCode == 200;
    }
}