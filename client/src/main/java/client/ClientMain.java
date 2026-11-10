package client;

import chess.*;
import exceptions.BasicResponseException;

public class ClientMain {
    public static void main(String[] args) throws BasicResponseException {
        String serverUrl = "http://localhost:8080";
        ClientUI client = new ClientUI(serverUrl);
        client.run();
    }
}
