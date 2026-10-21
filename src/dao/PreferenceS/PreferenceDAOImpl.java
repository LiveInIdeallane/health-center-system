package dao.PreferenceS;

import entity.PreferenceS.Preference;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 顾客偏好表 preference 的数据访问实现类。
 * <p>
 * preference 表字段：
 * id, customer_id, preferences, attention, remark, is_deleted
 * <p>
 * 关联关系：
 * - customer_id 关联 customer.id
 */
public class PreferenceDAOImpl implements PreferenceDAO {

    private Connection conn;
    private PreparedStatement stat;
    private ResultSet rs;

    // ===================== 增 =====================

    @Override
    public int save(Preference preference) {
        String sql = "insert into preference(id, customer_id, preferences, attention, remark, is_deleted) "
                + "values (?,?,?,?,?,?)";
        try {
            conn = JdbcUtils.getConnection();

            int newId = getNextId();

            stat = conn.prepareStatement(sql);
            stat.setInt(1, newId);
            stat.setObject(2, preference.getCustomerId());
            stat.setObject(3, preference.getPreferences());
            stat.setObject(4, preference.getAttention());
            stat.setObject(5, preference.getRemark());
            stat.setObject(6, preference.getIsDeleted() == null ? 0 : preference.getIsDeleted());

            int rows = stat.executeUpdate();
            if (rows > 0) {
                preference.setId(newId);
            }
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 先关 rs（这个 save 方法里实际上没用到 rs，但为了统一，还是判 null）
            if (rs != null) {
                try { rs.close(); } catch (SQLException ignored) {}
            }
            // 再关 stat
            if (stat != null) {
                try { stat.close(); } catch (SQLException ignored) {}
            }
            // 最后关连接
            JdbcUtils.close();
        }
    }

    @Override
    public int batchSave(List<Preference> preferences) {
        if (preferences == null || preferences.isEmpty()) {
            return 0;
        }
        String sql = "insert into preference(id, customer_id, preferences, attention, remark, is_deleted) "
                + "values (?,?,?,?,?,?)";
        int total = 0;
        try {
            conn = JdbcUtils.getConnection();

            int nextId = getNextId();

            stat = conn.prepareStatement(sql);
            for (Preference p : preferences) {
                stat.setInt(1, nextId);
                stat.setObject(2, p.getCustomerId());
                stat.setObject(3, p.getPreferences());
                stat.setObject(4, p.getAttention());
                stat.setObject(5, p.getRemark());
                stat.setObject(6, p.getIsDeleted() == null ? 0 : p.getIsDeleted());
                stat.addBatch();
                p.setId(nextId);
                nextId++;
            }
            int[] rows = stat.executeBatch();
            for (int r : rows) {
                total += r;
            }
            return total;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            // 先关 stat
            if (stat != null) {
                try { stat.close(); } catch (SQLException ignored) {}
            }
            // 再关连接
            JdbcUtils.close();
        }
    }

    private synchronized int getNextId() throws SQLException {
        int newId = 1;
        PreparedStatement ps = conn.prepareStatement("select ifnull(max(id),0)+1 from preference");
        ResultSet r = ps.executeQuery();
        if (r.next()) {
            newId = r.getInt(1);
        }
        r.close();
        ps.close();
        return newId;
    }

    // ===================== 改 =====================

    @Override
    public int update(Preference preference) {
        String sql = "update preference set customer_id=?, preferences=?, attention=?, remark=? "
                + "where id=? and is_deleted=0";
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            stat.setObject(1, preference.getCustomerId());
            stat.setObject(2, preference.getPreferences());
            stat.setObject(3, preference.getAttention());
            stat.setObject(4, preference.getRemark());
            stat.setInt(5, preference.getId());
            return stat.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            // 先关 stat
            if (stat != null) {
                try { stat.close(); } catch (SQLException ignored) {}
            }
            // 再关连接
            JdbcUtils.close();
        }
    }

    // ===================== 删（逻辑删除） =====================

    @Override
    public int delete(Integer id) {
        String sql = "update preference set is_deleted=1 where id=?";
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
    public int deleteByCustomerId(Integer customerId) {
        String sql = "update preference set is_deleted=1 where customer_id=?";
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            stat.setObject(1, customerId);
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

    // ===================== 查 =====================

    @Override
    public Preference findOne(Integer id) {
        String sql = "select * from preference where id=? and is_deleted=0";
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            stat.setInt(1, id);
            rs = stat.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
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
        return null;
    }

    @Override
    public Preference findByCustomerId(Integer customerId) {
        String sql = "select * from preference where customer_id=? and is_deleted=0";
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            stat.setInt(1, customerId);
            rs = stat.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
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
        return null;
    }

    // ===================== 私有辅助 =====================

    private Preference mapRow(ResultSet rs) throws SQLException {
        Preference p = new Preference();
        p.setId(rs.getInt("id"));
        p.setCustomerId(rs.getObject("customer_id", Integer.class));
        p.setPreferences(rs.getString("preferences"));
        p.setAttention(rs.getString("attention"));
        p.setRemark(rs.getString("remark"));
        p.setIsDeleted(rs.getInt("is_deleted"));
        return p;
    }
}