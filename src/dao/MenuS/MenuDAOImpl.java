package dao.MenuS;

import entity.MenuS.Menu;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuDAOImpl implements MenuDAO {

    @Override
    public int save(Menu menu) {
        String sql = "insert into menu(id, menus_index, title, icon, path, parent_id) values (?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = JdbcUtils.getConnection();
            int newId = getNextId(conn);
            stat = conn.prepareStatement(sql);
            stat.setInt(1, newId);
            stat.setString(2, menu.getMenusIndex());
            stat.setString(3, menu.getTitle());
            stat.setString(4, menu.getIcon());
            stat.setString(5, menu.getPath());
            stat.setObject(6, menu.getParentId());
            int rows = stat.executeUpdate();
            if (rows > 0) menu.setId(newId);
            return rows;
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

    private synchronized int getNextId(Connection conn) throws SQLException {
        int newId = 1;
        PreparedStatement ps = null;
        ResultSet r = null;
        try {
            ps = conn.prepareStatement("select ifnull(max(id),0)+1 from menu");
            r = ps.executeQuery();
            if (r.next()) newId = r.getInt(1);
        } finally {
            if (r != null) r.close();
            if (ps != null) ps.close();
        }
        return newId;
    }

    @Override
    public int update(Menu menu) {
        String sql = "update menu set menus_index=?, title=?, icon=?, path=?, parent_id=? where id=?";
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            stat.setString(1, menu.getMenusIndex());
            stat.setString(2, menu.getTitle());
            stat.setString(3, menu.getIcon());
            stat.setString(4, menu.getPath());
            stat.setObject(5, menu.getParentId());
            stat.setInt(6, menu.getId());
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
        String sql = "delete from menu where id=?";
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
            // 先关 stat
            if (stat != null) {
                try { stat.close(); } catch (SQLException ignored) {}
            }
            // 再关连接
            JdbcUtils.close();
        }
    }

    @Override
    public Menu findOne(Integer id) {
        return queryOne("select * from menu where id=?", id);
    }

    @Override
    public Menu findByPath(String path) {
        return queryOne("select * from menu where path=?", path);
    }

    @Override
    public List<Menu> findAll() {
        return queryList("select * from menu order by parent_id, menus_index");
    }

    @Override
    public List<Menu> findTopMenus() {
        return queryList("select * from menu where parent_id is null or parent_id=0 order by menus_index");
    }

    @Override
    public List<Menu> findByParentId(Integer parentId) {
        return queryList("select * from menu where parent_id=? order by menus_index", parentId);
    }

    private Menu queryOne(String sql, Object... params) {
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
            if (rs != null) try {
                rs.close();
            } catch (SQLException ignored) {
            }
            if (stat != null) try {
                stat.close();
            } catch (SQLException ignored) {
            }
            JdbcUtils.close();
        }
        return null;
    }

    private List<Menu> queryList(String sql, Object... params) {
        List<Menu> list = new ArrayList<>();
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

    private Menu mapRow(ResultSet rs) throws SQLException {
        Menu m = new Menu();
        m.setId(rs.getInt("id"));
        m.setMenusIndex(rs.getString("menus_index"));
        m.setTitle(rs.getString("title"));
        m.setIcon(rs.getString("icon"));
        m.setPath(rs.getString("path"));
        m.setParentId(rs.getObject("parent_id", Integer.class));
        return m;
    }
}