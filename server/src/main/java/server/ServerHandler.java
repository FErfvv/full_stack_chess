package server;

import com.google.gson.Gson;
import model.AuthToken;
import model.UserData;
import model.requests.RegisterRequest;
import service.UserService;

public class ServerHandler {

    private static UserService userService = new UserService();

    public static AuthToken register(String registerData) {
        AuthToken registerResponse = null;
        try {
            Gson gson = new Gson();
            RegisterRequest myData = gson.fromJson(registerData, RegisterRequest.class);
            registerResponse = userService.register(myData);
        } catch (Exception e) {
            System.out.println("Exception " + e + ": Error in converting json to object");
        }

        return registerResponse;
    }
}
