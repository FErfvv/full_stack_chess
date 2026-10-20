package server;

import com.google.gson.Gson;
import model.UserData;

public class ServerHandler {
    public static UserData register(String registerData) {
        UserData myData = null;
        try {
            Gson gson = new Gson();
            myData = gson.fromJson(registerData, UserData.class);
        } catch (Exception e) {
            System.out.println("Exception " + e + ": Error in converting json to object");
        }

        return myData;
    }
}
