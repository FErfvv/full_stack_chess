package client;

import com.google.gson.Gson;
import exceptions.BasicResponseException;
import model.AuthData;
import model.requests.RegisterRequest;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CommandHandler {
    private static ServerFacade facade;
    private static String serverUrl;

    public CommandHandler(String url) {
        serverUrl = url;
        facade = new ServerFacade(serverUrl);
    }

    public boolean register(String[] params) throws BasicResponseException {
        HttpRequest request = facade.buildRequest(
                "POST",
                "/user",
                null,
                new RegisterRequest(params[0], params[1], params[2]));
        var response = facade.sendRequest(request);
        if (response.statusCode() == 200) {
            System.out.println("REGISTERED!!");
            System.out.println(response.body());
            return true;
        }
        return false;
    }


}
