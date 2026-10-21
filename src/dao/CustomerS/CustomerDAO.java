package dao.CustomerS;

import entity.CustomerS.Customer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface CustomerDAO {

    // ========== 你的命名（保留） ==========
    boolean save(Customer customer) throws SQLException;
    boolean update(Customer customer) throws SQLException;
    boolean delete(Integer id) throws SQLException;

    Customer findOne(Integer id) throws SQLException;
    Customer findByIdCard(String idcard) throws SQLException;
    Customer findByBedId(Integer bedId) throws SQLException;
    List<Customer> findByName(String name) throws SQLException;
    List<Customer> findByRoomNo(String roomNo) throws SQLException;
    List<Customer> findByBuildingNo(String buildingNo) throws SQLException;
    List<Customer> findByLevelId(Integer levelId) throws SQLException;
    List<Customer> findByUserId(Integer userId) throws SQLException;
    List<Customer> findAll() throws SQLException;
    List<Customer> findPage(int offset, int limit) throws SQLException;
    int count() throws SQLException;
    List<Customer> findExpiringBefore(LocalDate date) throws SQLException;

    // ========== 组员的命名（保留） ==========
    boolean saveCustomer(Customer customer) throws SQLException;
    boolean updateCustomer(Customer customer) throws SQLException;
    boolean deleteCustomer(Integer id) throws SQLException;
    Customer findCustomer(Integer id) throws SQLException;
    List<Customer> findAllCustomers() throws SQLException;

    // ========== 共同的多条件搜索 ==========
    List<Customer> search(String customerName, String contact_tel, String idcard, String building_no)
            throws SQLException, InstantiationException, IllegalAccessException;
}