package dao.UserS;

import entity.UserS.User;
import utils.CRUDUtils;
import utils.JdbcUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public int save(User user) {
        String sql = "insert into user(create_time, create_by, update_time, update_by, is_deleted, "
                + "nickname, username, password, sex, email, phone_number, role_id) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            LocalDateTime now = LocalDateTime.now();
            stat.setObject(1, user.getCreateTime() == null ? now : user.getCreateTime());
            stat.setObject(2, user.getCreateBy() == null ? 0 : user.getCreateBy());
            stat.setObject(3, user.getUpdateTime() == null ? now : user.getUpdateTime());
            stat.setObject(4, user.getUpdateBy() == null ? 0 : user.getUpdateBy());
            stat.setObject(5, user.getIsDeleted() == null ? 0 : user.getIsDeleted());
            stat.setString(6, user.getNickname());
            stat.setString(7, user.getUsername());
            stat.setString(8, user.getPassword());
            stat.setObject(9, user.getSex());
            stat.setString(10, user.getEmail());
            stat.setString(11, user.getPhoneNumber());
            stat.setObject(12, user.getRoleId());
            int rows = stat.executeUpdate();
            if (rows > 0) {
                rs = stat.getGeneratedKeys();
                if (rs.next()) user.setId(rs.getInt(1));
            }
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 先手动关 rs 和 stat
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            // 再关连接
            JdbcUtils.close();
        }
    }

    @Override
    public int update(User user) {
        String sql = "update user set update_time=?, nickname=?, username=?, sex=?, email=?, phone_number=?, role_id=? where id=?";
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            stat.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stat.setString(2, user.getNickname());
            stat.setString(3, user.getUsername());
            stat.setObject(4, user.getSex());
            stat.setString(5, user.getEmail());
            stat.setString(6, user.getPhoneNumber());
            stat.setObject(7, user.getRoleId());
            stat.setInt(8, user.getId());
            return stat.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 先关 stat
            if (stat != null) {
                try { stat.close(); } catch (SQLException ignored) {}
            }
            // 再关连接
            JdbcUtils.close();
        }
    }

    @Override
    public int delete(Integer id) {
        String sql = "update user set is_deleted=1 where id=?";
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            stat.setInt(1, id);
            return stat.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stat != null) {
                try { stat.close(); } catch (SQLException ignored) {}
            }
            JdbcUtils.close();   // 无参
        }
    }

    @Override
    public User findOne(Integer id) {
        return queryOne("select * from user where id=? and is_deleted=0", id);
    }

    @Override
    public User findByUsername(String username) {
        String sql = "select * from user where username=?";
        Object[] params = {username};
        List<User> list = null;
        try {
            list = CRUDUtils.select(sql,params, User.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        queryOne("select * from user where username=? and is_deleted=0", username);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public User findByPhone(String phoneNumber) {
        return queryOne("select * from user where phone_number=? and is_deleted=0", phoneNumber);
    }

    @Override
    public User findByEmail(String email) {
        return queryOne("select * from user where email=? and is_deleted=0", email);
    }

    @Override
    public List<User> findByNickname(String nickname) {
        return queryList("select * from user where nickname like concat('%',?,'%') and is_deleted=0 order by id desc", nickname);
    }

    @Override
    public List<User> findAll() {
        return queryList("select * from user where is_deleted=0 order by id desc");
    }

    @Override
    public List<User> findByRoleId(Integer roleId) {
        return queryList("select * from user where role_id=? and is_deleted=0 order by id desc", roleId);
    }

    @Override
    public List<User> findPage(int offset, int limit) {
        return queryList("select * from user where is_deleted=0 order by id desc limit ?,?", offset, limit);
    }

    @Override
    public int count() {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement("select count(*) from user where is_deleted=0");
            rs = stat.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            // 再关连接
            JdbcUtils.close();
        }
        return 0;
    }

    // ========== 私有辅助 ==========

    private User queryOne(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) stat.setObject(i + 1, params[i]);
            rs = stat.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            // 再关连接
            JdbcUtils.close();
        }
        return null;
    }

    private List<User> queryList(String sql, Object... params) {
        List<User> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) stat.setObject(i + 1, params[i]);
            rs = stat.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            // 再关连接
            JdbcUtils.close();
        }
        return list;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setCreateTime(rs.getObject("create_time", Date.class));
        u.setCreateBy(rs.getObject("create_by", Integer.class));
        u.setUpdateTime(rs.getObject("update_time", Date.class));
        u.setUpdateBy(rs.getObject("update_by", Integer.class));
        u.setIsDeleted(rs.getInt("is_deleted"));
        u.setNickname(rs.getString("nickname"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setSex(rs.getInt("sex"));
        u.setEmail(rs.getString("email"));
        u.setPhoneNumber(rs.getString("phone_number"));
        u.setRoleId(rs.getInt("role_id"));
        return u;
    }
}