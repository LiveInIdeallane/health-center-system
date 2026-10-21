package service.LoginServiceS;


import dao.UserS.UserDAO;
import dao.UserS.UserDAOImpl;
import entity.UserS.User;

import java.sql.SQLException;

public class LoginServiceImpl implements LoginService {

    private final UserDAO userDao = new UserDAOImpl();

    @Override
    public User login(String username, String password) throws SQLException {
        //DAO层查询用户
        User user = userDao.findByUsername(username);
        if(user == null){
            return null;
        }
        //逻辑删除校验，账号禁用判断
        if(user.getIsDeleted() != null && user.getIsDeleted() == 1){
            throw new RuntimeException("该账号已被禁用，无法登录！");
        }
        //密码校验
        if(!user.getPassword().equals(password)){
            return null;
        }
        return user;
    }
}

