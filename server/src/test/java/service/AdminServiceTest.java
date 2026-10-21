package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {
    DataAccess testDataAccess = new MemoryDataAccess();
    AdminService service = new AdminService(testDataAccess);

    @Test
    void clearDb() {
        assertDoesNotThrow(() -> service.clearDb());
    }
}