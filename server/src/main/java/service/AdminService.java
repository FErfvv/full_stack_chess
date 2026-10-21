package service;

import dataaccess.DataAccess;

public class AdminService {
    private final DataAccess myDataAccess;

    public AdminService(DataAccess myDataAccess) {
        this.myDataAccess = myDataAccess;
    }

    public void clearDb() {
        myDataAccess.clear();
    }
}
