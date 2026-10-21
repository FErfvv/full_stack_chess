package service;

import dataaccess.exceptions.UsernameAlreadyTakenException;
import model.AuthToken;
import model.requests.RegisterRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService service = new UserService();

    @Test
    void registerNew() throws UsernameAlreadyTakenException {
        String username = "jeff";
        RegisterRequest request = new RegisterRequest(username, "password", "jeff@gmail.com");
        assertDoesNotThrow(() -> service.register(request));
    }

    @Test
    void registerDuplicateUsername() throws UsernameAlreadyTakenException {
        String username = "john";
        RegisterRequest request = new RegisterRequest(username, "password", "jeff@gmail.com");
        AuthToken expected = new AuthToken(username, "randomStringAuth");
        assertEquals(expected.username(), service.register(request).username());
        assertThrows(UsernameAlreadyTakenException.class, () -> service.register(request));
    }
}