package dao.RoleMenuS;

import entity.RoleMenuS.RoleMenu;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleMenuDAOImpl implements RoleMenuDAO {

    @Override
    public int save(RoleMenu roleMenu) {
        String sql = "insert into rolemenu(id, role_id, menu) values (?,?,?)";
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = JdbcUtils.getConnection();
            int newId = getNextId(conn);
            stat = conn.prepareStatement(sql);
            stat.setInt(1, newId);
            stat.setInt(2, roleMenu.getRoleId());
            stat.setInt(3, roleMenu.getMenuId());
            int rows = stat.executeUpdate();
            if (rows > 0) roleMenu.setId(newId);
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
            ps = conn.prepareStatement("select ifnull(max(id),0)+1 from rolemenu");
            r = ps.executeQuery();
            if (r.next()) newId = r.getInt(1);
        } finally {
            if (r != null) r.close();
            if (ps != null) ps.close();
        }
        return newId;
    }

    @Override
    public int deleteByRoleId(Integer roleId) {
        return executeUpdate("delete from rolemenu where role_id=?", roleId);
    }

    @Override
    public int deleteByMenuId(Integer menuId) {
        return executeUpdate("delete from rolemenu where menu=?", menuId);
    }

    @Override
    public int deleteByRoleIdAndMenuId(Integer roleId, Integer menuId) {
        return executeUpdate("delete from rolemenu where role_id=? and menu=?", roleId, menuId);
    }

    @Override
    public RoleMenu findByRoleIdAndMenuId(Integer roleId, Integer menuId) {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement("select * from rolemenu where role_id=? and menu=?");
            stat.setInt(1, roleId);
            stat.setInt(2, menuId);
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
    public List<RoleMenu> findByRoleId(Integer roleId) {
        return queryList("select * from rolemenu where role_id=? order by id", roleId);
    }

    @Override
    public List<RoleMenu> findByMenuId(Integer menuId) {
        return queryList("select * from rolemenu where menu=? order by id", menuId);
    }

    private int executeUpdate(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) stat.setObject(i + 1, params[i]);
            return stat.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            JdbcUtils.close();
        }
    }

    private List<RoleMenu> queryList(String sql, Object... params) {
        List<RoleMenu> list = new ArrayList<>();
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
            JdbcUtils.close();
        }
        return list;
    }

    private RoleMenu mapRow(ResultSet rs) throws SQLException {
        RoleMenu rm = new RoleMenu();
        rm.setId(rs.getInt("id"));
        rm.setRoleId(rs.getInt("role_id"));
        rm.setMenuId(rs.getInt("menu"));
        return rm;
    }
}