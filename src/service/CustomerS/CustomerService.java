package service.CustomerS;

import entity.CustomerS.Customer;
import entity.PreferenceS.Preference;

import java.time.LocalDate;
import java.util.List;

/**
 * 客户业务接口（合并版）。
 * 保留了两套命名风格：
 *  - 组员风格：createCustomer / updateCustomer / deleteCustomer / findCustomerById / findAllCustomers
 *  - 你的风格：save / update / delete / findOne / findAll
 * 功能等价的方法，在实现类里做"转调"。
 */
public interface CustomerService {

    // ========== 组员风格（保留） ==========
    boolean createCustomer(Customer customer);
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(Customer customer);        // 注意：参数是 Customer
    Customer findCustomerById(Integer id);
    List<Customer> findAllCustomers();

    /** 组员风格的模糊查询 */
    List<Customer> search(String customerName, String contact_tel, String idcard, String building_no);

    // ========== 你的风格（保留） ==========
    boolean save(Customer customer);
    boolean update(Customer customer);
    boolean delete(Integer id);

    Customer findOne(Integer id);
    Customer findByIdCard(String idcard);
    List<Customer> findByName(String name);
    List<Customer> findAll();
    List<Customer> findPage(int pageNum, int pageSize);
    int count();
    List<Customer> findExpiringBefore(LocalDate date);

    List<Customer> findByUserId(Integer userId);
    boolean assignCustomerToUser(Integer customerId, Integer userId);
    boolean removeCustomerFromUser(Integer customerId);



    Preference findPreferenceByCustomerId(Integer customerId);
    boolean savePreferences(Integer customerId, List<Preference> preferences);
    boolean deletePreferences(Integer customerId);
}