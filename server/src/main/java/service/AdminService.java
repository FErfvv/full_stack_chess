package service;

import dataaccess.DataAccess;
import dataaccess.exceptions.DataAccessException;

public class AdminService {
    private final DataAccess myDataAccess;

    public AdminService(DataAccess myDataAccess) {
        this.myDataAccess = myDataAccess;
    }

    public void clearDb() throws DataAccessException {
        myDataAccess.clear();
    }
}
