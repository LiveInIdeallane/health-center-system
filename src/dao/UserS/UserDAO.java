package dao.UserS;

import entity.UserS.User;
import entity.UserS.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

    int save(User user);                       // 新增用户
    int update(User user);                     // 修改用户
    int delete(Integer id);                    // 逻辑删除（is_deleted=1）
    User findOne(Integer id);                  // 按 id 查
    User findByUsername(String username) throws SQLException;      // 按账号查（登录用）
    User findByPhone(String phoneNumber);      // 按手机号查（判重用）
    User findByEmail(String email);            // 按邮箱查（查重/找回密码用）
    List<User> findByNickname(String nickname);   // 按真实姓名模糊查（健康管家列表搜索用）
    List<User> findAll();                      // 查所有
    List<User> findByRoleId(Integer roleId);   // 按角色查
    List<User> findPage(int offset, int limit);// 分页查询
    int count();                               // 统计总数（配合分页）

}
