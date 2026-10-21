package dao.CustomerS;

import dao.CustomerS.CustomerDAO;
import entity.CustomerS.Customer;
import utils.CRUDUtils;
import utils.JdbcUtils;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public boolean saveCustomer(Customer customer) throws SQLException {
        String sql = "insert into customer(is_deleted,customer_name,customer_age,customer_sex,idcard,room_no,building_no,checkin_date,expiration_date,contact_tel,bed_id,psychosomatic_state,attention,birthday,height,weight,blood_type,filepath,user_id,level_id,family_member) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] object = {
                customer.getIs_deleted(), customer.getCustomer_name(), customer.getCustomer_age(),
                customer.getCustomer_sex(), customer.getIdcard(), customer.getRoom_no(),
                customer.getBuilding_no(), toSqlDate(customer.getCheckin_date()),
                toSqlDate(customer.getExpiration_date()), customer.getContact_tel(),
                customer.getBed_id(), customer.getPsychosomatic_state(), customer.getAttention(),
                toSqlDate(customer.getBirthday()), customer.getHeight(), customer.getWeight(),
                customer.getBlood_type(), customer.getFilepath(), customer.getUser_id(),
                customer.getLevel_id(), customer.getFamily_member()
        };
        return CRUDUtils.update(sql, object);
    }

    @Override
    public boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customer SET is_deleted = ?, customer_name = ?, customer_age = ?, customer_sex = ?, idcard = ?, room_no = ?, building_no = ?, checkin_date = ?, expiration_date = ?, contact_tel = ?, bed_id = ?, psychosomatic_state = ?, attention = ?, birthday = ?, height = ?, weight = ?, blood_type = ?, filepath = ?, user_id = ?, level_id = ?, family_member = ? where id = ?";
        Object[] object = {
                customer.getIs_deleted(), customer.getCustomer_name(), customer.getCustomer_age(),
                customer.getCustomer_sex(), customer.getIdcard(), customer.getRoom_no(),
                customer.getBuilding_no(), toSqlDate(customer.getCheckin_date()),
                toSqlDate(customer.getExpiration_date()), customer.getContact_tel(),
                customer.getBed_id(), customer.getPsychosomatic_state(), customer.getAttention(),
                toSqlDate(customer.getBirthday()), customer.getHeight(), customer.getWeight(),
                customer.getBlood_type(), customer.getFilepath(), customer.getUser_id(),
                customer.getLevel_id(), customer.getFamily_member(), customer.getId()
        };
        return CRUDUtils.update(sql, object);
    }

    private java.sql.Date toSqlDate(java.util.Date date) {
        return date == null ? null : new java.sql.Date(date.getTime());
    }

    @Override
    public boolean deleteCustomer(Integer id) throws SQLException {
        String sql = "UPDATE customer SET is_deleted = 1 WHERE id = ?";
        Object[] object = {id};
        return CRUDUtils.update(sql, object);
    }

    @Override
    public Customer findCustomer(Integer id) throws SQLException {
        String sql = "SELECT * FROM customer WHERE id = ?";
        Object[] object = {id};
        List<Customer> customers = CRUDUtils.select(sql, object, Customer.class);
        return customers.size() > 0 ? customers.get(0) : null;
    }

    @Override
    public List<Customer> findAllCustomers() throws SQLException {
        String sql = "SELECT * FROM customer";
        Object[] object = {};
        return CRUDUtils.select(sql, object, Customer.class);
    }

    @Override
    public List<Customer> findByUserId(Integer userId) throws SQLException {
        String sql = "SELECT * FROM customer WHERE user_id = ? AND is_deleted = 0";
        Object[] object = {userId};
        return CRUDUtils.select(sql, object, Customer.class);
    }

    @Override
    public List<Customer> search(String customerName, String contact_tel, String idcard, String building_no) throws SQLException, InstantiationException, IllegalAccessException {
        String sql = "SELECT * FROM customer WHERE 1=1 ";
        List<Object> list = new ArrayList<>();
        if (customerName != null && !"".equals(customerName)) {
            sql += " AND customer_name like ? ";
            list.add("%" + customerName + "%");
        }
        if (contact_tel != null && !"".equals(contact_tel)) {
            sql += " AND contact_tel like ? ";
            list.add("%" + contact_tel + "%");
        }
        if (idcard != null && !"".equals(idcard)) {
            sql += " AND idcard like ? ";
            list.add("%" + idcard + "%");
        }
        if (building_no != null && !"".equals(building_no)) {
            sql += " AND building_no like ? ";
            list.add("%" + building_no + "%");
        }
        return CRUDUtils.select(sql, list.toArray(), Customer.class);
    }

    @Override
    public boolean save(Customer customer) throws SQLException {
        return saveCustomer(customer);
    }

    @Override
    public boolean update(Customer customer) throws SQLException {
        return updateCustomer(customer);
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        return deleteCustomer(id);
    }

    @Override
    public Customer findOne(Integer id) throws SQLException {
        return findCustomer(id);
    }

    @Override
    public Customer findByIdCard(String idcard) throws SQLException {
        String sql = "SELECT * FROM customer WHERE idcard = ?";
        Object[] object = {idcard};
        List<Customer> customers = CRUDUtils.select(sql, object, Customer.class);
        return customers.size() > 0 ? customers.get(0) : null;
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        return findAllCustomers();
    }

    // ========================================================
    // 你的其他查询（组员没有的，用 JDBC 直接写）
    // ========================================================


    @Override
    public Customer findByBedId(Integer bedId) throws SQLException {
        return queryOne("select * from customer where bed_id=? and is_deleted=0", bedId);
    }

    @Override
    public List<Customer> findByName(String name) throws SQLException {
        return queryList("select * from customer where customer_name like concat('%',?,'%') and is_deleted=0 order by id desc", name);
    }

    @Override
    public List<Customer> findByRoomNo(String roomNo) throws SQLException {
        return queryList("select * from customer where room_no=? and is_deleted=0 order by id desc", roomNo);
    }

    @Override
    public List<Customer> findByBuildingNo(String buildingNo) throws SQLException {
        return queryList("select * from customer where building_no=? and is_deleted=0 order by id desc", buildingNo);
    }

    @Override
    public List<Customer> findByLevelId(Integer levelId) throws SQLException {
        return queryList("select * from customer where level_id=? and is_deleted=0 order by id desc", levelId);
    }


    @Override
    public List<Customer> findPage(int offset, int limit) throws SQLException {
        return queryList("select * from customer where is_deleted=0 order by id desc limit ?,?", offset, limit);
    }

    @Override
    public int count() throws SQLException {
        String sql = "select count(*) from customer where is_deleted=0";
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            rs = stat.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            JdbcUtils.close();
        }
        return 0;
    }

    @Override
    public List<Customer> findExpiringBefore(LocalDate date) throws SQLException {
        return queryList("select * from customer where expiration_date < ? and is_deleted=0 order by expiration_date asc", date);
    }

    // ========================================================
    // 私有辅助（JDBC 直接写）
    // ========================================================

    private Customer queryOne(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) stat.setObject(i + 1, params[i]);
            rs = stat.executeQuery();
            if (rs.next()) return mapRow(rs);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            JdbcUtils.close();
        }
        return null;
    }

    private List<Customer> queryList(String sql, Object... params) throws SQLException {
        List<Customer> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            stat = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) stat.setObject(i + 1, params[i]);
            rs = stat.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (stat != null) try { stat.close(); } catch (SQLException ignored) {}
            JdbcUtils.close();
        }
        return list;
    }

    private Customer mapRow(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setId(rs.getInt("id"));
        c.setIs_deleted(rs.getObject("is_deleted", Integer.class));
        c.setCustomer_name(rs.getString("customer_name"));
        c.setCustomer_age(rs.getObject("customer_age", Integer.class));
        c.setCustomer_sex(rs.getObject("customer_sex", Integer.class));
        c.setIdcard(rs.getString("idcard"));
        c.setRoom_no(rs.getString("room_no"));
        c.setBuilding_no(rs.getString("building_no"));
        c.setCheckin_date(rs.getDate("checkin_date"));
        c.setExpiration_date(rs.getDate("expiration_date"));
        c.setContact_tel(rs.getString("contact_tel"));
        c.setBed_id(rs.getObject("bed_id", Integer.class));
        c.setPsychosomatic_state(rs.getString("psychosomatic_state"));
        c.setAttention(rs.getString("attention"));
        c.setBirthday(rs.getDate("birthday"));
        c.setHeight(rs.getString("height"));
        c.setWeight(rs.getString("weight"));
        c.setBlood_type(rs.getString("blood_type"));
        c.setFilepath(rs.getString("filepath"));
        c.setUser_id(rs.getObject("user_id", Integer.class));
        c.setLevel_id(rs.getObject("level_id", Integer.class));
        c.setFamily_member(rs.getString("family_member"));
        return c;
    }
}