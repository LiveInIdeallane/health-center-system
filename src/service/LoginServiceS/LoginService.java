package service.LoginServiceS;


import entity.UserS.User;

import java.sql.SQLException;

public interface LoginService {
    User login(String username, String password) throws SQLException;
}
