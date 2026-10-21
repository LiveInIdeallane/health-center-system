package service.UserS;

import entity.UserS.User;

import java.sql.SQLException;
import java.util.List;

/**
 * 用户业务接口。
 * 关联功能：
 * - 用户-角色：新增/修改时校验角色存在，按角色查用户
 */
public interface UserService {

    /** 新增用户（校验账号、手机号、邮箱不重复；密码默认手机号后6位） */
    boolean save(User user) throws SQLException;

    /** 修改用户（校验账号、手机号、邮箱不重复） */
    boolean update(User user);

    /** 逻辑删除用户 */
    boolean delete(Integer id);

    /** 按主键查 */
    User findOne(Integer id);

    /** 登录：按账号查，校验密码 */
    User login(String username, String password);

    /** 按账号查 */
    User findByUsername(String username);

    /** 按手机号查（注册判重用） */
    User findByPhone(String phoneNumber);

    /** 按真实姓名模糊查（健康管家列表） */
    List<User> findByNickname(String nickname);

    /** 按角色查用户（角色-用户关联） */
    List<User> findByRoleId(Integer roleId);

    /** 查所有用户 */
    List<User> findAll();

    /** 分页查 */
    List<User> findPage(int pageNum, int pageSize);

    /** 统计总数 */
    int count();
}