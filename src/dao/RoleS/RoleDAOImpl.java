package dao.RoleS;

import entity.RoleS.Role;
import utils.JdbcUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色表 role 的数据访问实现类。
 * 严格按六大步骤：加载驱动 → 建立连接 → 创建执行对象 → 执行 SQL → 处理结果集 → 关闭资源
 */
public class RoleDAOImpl implements RoleDAO {

    @Override
    public int save(Role role) {
        String sql = "insert into role(id, create_time, update_time, update_by, is_deleted, name) values (?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = JdbcUtils.getConnection();                       // 2. 建立连接
            int newId = getNextId(conn);
            stat = conn.prepareStatement(sql);                // 3. 创建执行对象
            LocalDateTime now = LocalDateTime.now();
            stat.setInt(1, newId);
            stat.setObject(2, role.getCreateTime() == null ? now : role.getCreateTime());
            stat.setObject(3, role.getUpdateTime());
            stat.setObject(4, role.getUpdateBy());
            stat.setObject(5, role.getIsDeleted() == null ? 0 : role.getIsDeleted());
            stat.setString(6, role.getName());
            int rows = stat.executeUpdate();                  // 4. 执行 SQL
            if (rows > 0) role.setId(newId);                  // 5. 处理结果
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            JdbcUtils.close();
        }
    }

    private synchronized int getNextId(Connection conn) throws SQLException {
        int newId = 1;
        PreparedStatement ps = null;
        ResultSet r = null;
        try {
            ps = conn.prepareStatement("select ifnull(max(id),0)+1 from role");
            r = ps.executeQuery();
            if (r.next()) newId = r.getInt(1);
        } finally {
            if (r != null) r.close();
            if (ps != null) ps.close();
        }
        return newId;
    }

    @Override
    public int update(Role role) {
        String sql = "update role set update_time=?, update_by=?, name=? where id=? and is_deleted=0";
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            stat.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stat.setObject(2, role.getUpdateBy());
            stat.setString(3, role.getName());
            stat.setInt(4, role.getId());
            return stat.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            JdbcUtils.close();
        }
    }

    @Override
    public int delete(Integer id) {
        String sql = "update role set is_deleted=1 where id=?";
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
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            JdbcUtils.close();
        }
    }

    @Override
    public Role findOne(Integer id) {
        String sql = "select * from role where id=? and is_deleted=0";
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            stat.setInt(1, id);
            rs = stat.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            JdbcUtils.close();
        }
        return null;
    }

    @Override
    public Role findByName(String name) {
        String sql = "select * from role where name=? and is_deleted=0";
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            stat.setString(1, name);
            rs = stat.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            JdbcUtils.close();
        }
        return null;
    }

    @Override
    public List<Role> findAll() {
        List<Role> list = new ArrayList<>();
        String sql = "select * from role where is_deleted=0 order by id desc";
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            rs = stat.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            JdbcUtils.close();
        }
        return list;
    }

    @Override
    public List<Role> findPage(int offset, int limit) {
        List<Role> list = new ArrayList<>();
        String sql = "select * from role where is_deleted=0 order by id desc limit ?,?";
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            stat.setInt(1, offset);
            stat.setInt(2, limit);
            rs = stat.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
                if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
                if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
                JdbcUtils.close();

        }
        return list;
    }

    @Override
    public int count() {
        String sql = "select count(*) from role where is_deleted=0";
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            rs = stat.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            JdbcUtils.close();
        }
        return 0;
    }

    private Role mapRow(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setId(rs.getInt("id"));
        role.setCreateTime(rs.getObject("create_time", LocalDateTime.class));
        role.setUpdateTime(rs.getObject("update_time", LocalDateTime.class));
        role.setUpdateBy(rs.getObject("update_by", Integer.class));
        role.setIsDeleted(rs.getInt("is_deleted"));
        role.setName(rs.getString("name"));
        return role;
    }
}