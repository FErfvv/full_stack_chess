package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import exceptions.BasicResponseException;
import jdk.jshell.spi.ExecutionControl;
import model.AuthData;
import model.requests.*;
import model.responses.GameListResponseData;
import model.responses.ListGamesResponse;

import java.net.*;
import java.net.http.*;
import java.util.ArrayList;
import java.util.List;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private AuthData authData;

    public ServerFacade(String url) {
        serverUrl = url;
        authData = null;
    }

    public void register(String[] params) throws BasicResponseException {
        HttpRequest request = buildRequest(
                "POST",
                "/user",
                null,
                new RegisterRequest(params[0], params[1], params[2]));
        HttpResponse<String> response = sendRequest(request);
        authData = handleResponse(response, AuthData.class, null);
    }

    public void login(String[] params) throws BasicResponseException {
        HttpRequest request = buildRequest(
                "POST",
                "/session",
                null,
                new LoginRequest(params[0], params[1]));
        HttpResponse<String> response = sendRequest(request);
        authData = handleResponse(response, AuthData.class, null);
    }

    public void logout() throws BasicResponseException {
        HttpRequest request = buildRequest(
                "DELETE",
                "/session",
                authData.authToken(),
                null);
        HttpResponse<String> response = sendRequest(request);
        handleResponse(response, null, null);
    }

    public int createGame(String[] params) throws BasicResponseException {
        HttpRequest request = buildRequest(
                "POST",
                "/game",
                authData.authToken(),
                new NewGameRequest(params[0]));
        HttpResponse<String> response = sendRequest(request);
        return Integer.parseInt((String) handleResponse(response, ArrayList.class, new String[]{"gameID"}).getFirst());
    }

    public ListGamesResponse listGames() throws BasicResponseException {
        HttpRequest request = buildRequest(
                "GET",
                "/game",
                authData.authToken(),
                null);
        HttpResponse<String> response = sendRequest(request);
        return handleResponse(response, ListGamesResponse.class, null);
    }

    public void joinGame(String playerColor, String gameID) throws BasicResponseException {
        HttpRequest request = buildRequest(
                "PUT",
                "/game",
                authData.authToken(),
                new JoinGameRequest(playerColor, gameID));

        HttpResponse<String> response = sendRequest(request);
        handleResponse(response, null, null);
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

    public <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass, String[] itemsToExtract) throws BasicResponseException {
        if (!isSuccessful(response.statusCode())) {
            if (response.body() != null) {
                throw new BasicResponseException(response.body(), response.statusCode());
            }
            throw new BasicResponseException("Error in receiving request", response.statusCode());
        }

        if (itemsToExtract != null && responseClass == ArrayList.class) {
            ArrayList<String> values = new ArrayList<>();
            for (String item : itemsToExtract) {
                JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
                values.add(jsonObject.get(item).getAsString());
            }
            return (T) values;
        } else if (response.body() != null && responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }


    private boolean isSuccessful(int statusCode) {
        return statusCode == 200;
    }
}