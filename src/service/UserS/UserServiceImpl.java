package service.UserS;

import dao.RoleS.RoleDAO;
import dao.RoleS.RoleDAOImpl;
import dao.UserS.UserDAO;
import dao.UserS.UserDAOImpl;
import entity.RoleS.Role;
import entity.UserS.User;

import java.sql.SQLException;
import java.util.List;

/**
 * 用户业务实现类。
 * 关联功能：
 * - 用户-角色：新增/修改时校验角色存在
 */
public class UserServiceImpl implements UserService {

    private UserDAO userDAO = new UserDAOImpl();
    private RoleDAO roleDAO = new RoleDAOImpl();

    // ===================== 增 =====================

    @Override
    public boolean save(User user) throws SQLException {
        // 1. 校验账号不重复
        if (userDAO.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("账号已存在：" + user.getUsername());
        }
        // 2. 校验手机号不重复
        if (userDAO.findByPhone(user.getPhoneNumber()) != null) {
            throw new RuntimeException("手机号已存在：" + user.getPhoneNumber());
        }
        // 3. 校验邮箱不重复（邮箱非空时）
        if (user.getEmail() != null && !user.getEmail().isEmpty()
                && userDAO.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("邮箱已存在：" + user.getEmail());
        }
        // 4. 校验角色存在（用户-角色关联）
        if (user.getRoleId() == null) {
            throw new RuntimeException("必须指定系统角色");
        }
        Role role = roleDAO.findOne(user.getRoleId());
        if (role == null) {
            throw new RuntimeException("角色不存在，roleId=" + user.getRoleId());
        }
        // 5. 密码默认设置为手机号后6位（需求 5.1 附录）
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            String phone = user.getPhoneNumber();
            if (phone != null && phone.length() >= 6) {
                user.setPassword(phone.substring(phone.length() - 6));
            } else {
                user.setPassword("123456"); // 兜底
            }
        }
        return userDAO.save(user) > 0;
    }

    // ===================== 改 =====================

    @Override
    public boolean update(User user) {
        User exist = userDAO.findOne(user.getId());
        if (exist == null) {
            throw new RuntimeException("用户不存在，id=" + user.getId());
        }
        // 校验账号不重复（排除自己）
        User byUsername = null;
        try {
            byUsername = userDAO.findByUsername(user.getUsername());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (byUsername != null && !byUsername.getId().equals(user.getId())) {
            throw new RuntimeException("账号已被占用：" + user.getUsername());
        }
        // 校验手机号不重复（排除自己）
        User byPhone = userDAO.findByPhone(user.getPhoneNumber());
        if (byPhone != null && !byPhone.getId().equals(user.getId())) {
            throw new RuntimeException("手机号已被占用：" + user.getPhoneNumber());
        }
        // 校验角色存在
        if (user.getRoleId() != null && roleDAO.findOne(user.getRoleId()) == null) {
            throw new RuntimeException("角色不存在，roleId=" + user.getRoleId());
        }
        return userDAO.update(user) > 0;
    }

    // ===================== 删 =====================

    @Override
    public boolean delete(Integer id) {
        return userDAO.delete(id) > 0;
    }

    // ===================== 查 =====================

    @Override
    public User findOne(Integer id) {
        return userDAO.findOne(id);
    }

    @Override
    public User login(String username, String password) {
        User user = null;
        try {
            user = userDAO.findByUsername(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (user == null) {
            throw new RuntimeException("账号不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("密码错误");
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        try {
            return userDAO.findByUsername(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findByPhone(String phoneNumber) {
        return userDAO.findByPhone(phoneNumber);
    }

    @Override
    public List<User> findByNickname(String nickname) {
        return userDAO.findByNickname(nickname);
    }

    @Override
    public List<User> findByRoleId(Integer roleId) {
        return userDAO.findByRoleId(roleId);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public List<User> findPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return userDAO.findPage(offset, pageSize);
    }

    @Override
    public int count() {
        return userDAO.count();
    }
}