package client;

import exceptions.BasicResponseException;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:8080");
    }

    @BeforeAll
    static void clearDB() throws BasicResponseException {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    public void register() {
        String[] goodParams = new String[]{"user1", "user1", "user1@gmail.com"};
        Assertions.assertDoesNotThrow(() -> serverFacade.register(goodParams));
        String[] badParams = new String[]{null, "user1", "user1@gmail.com"};
        Assertions.assertThrows(BasicResponseException.class, () -> serverFacade.register(badParams));
    }

    @Test
    @Order(2)
    public void logout() {
        Assertions.assertDoesNotThrow(() -> serverFacade.logout());
    }

    @Test
    @Order(3)
    public void login() {
        String[] goodParams = new String[]{"user1", "user1", "user1@gmail.com"};
        Assertions.assertDoesNotThrow(() -> serverFacade.login(goodParams));
        String[] badParams = new String[]{"notLoggedIn", "user1", "user1@gmail.com"};
        Assertions.assertThrows(BasicResponseException.class, () -> serverFacade.login(badParams));
    }

    @Test
    @Order(4)
    public void createGame() {
        String[] goodParams = new String[]{"myGame"};
        Assertions.assertDoesNotThrow(() -> serverFacade.createGame(goodParams));
        String[] badParams = new String[]{null};
        Assertions.assertThrows(BasicResponseException.class, () -> serverFacade.createGame(badParams));
    }

    @Test
    @Order(5)
    public void listGames() {
        Assertions.assertDoesNotThrow(() -> serverFacade.listGames());
    }

    @Test
    @Order(6)
    public void joinGame() throws BasicResponseException {
        String[] goodParams = new String[]{"WHITE", "1"};
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(goodParams[0], goodParams[1]));
    }


}
