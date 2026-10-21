package service.CustomerS;

import dao.CustomerS.CustomerDAO;
import dao.CustomerS.CustomerDAOImpl;
import dao.PreferenceS.PreferenceDAO;
import dao.PreferenceS.PreferenceDAOImpl;
import dao.UserS.UserDAO;
import dao.UserS.UserDAOImpl;
import entity.CustomerS.Customer;
import entity.PreferenceS.Preference;
import entity.UserS.User;
import utils.JdbcUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDAO customerDAO = new CustomerDAOImpl();
    private PreferenceDAO preferenceDAO = new PreferenceDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();

    // ========================================================
    // 组员风格（真实逻辑）
    // ========================================================

    @Override
    public boolean createCustomer(Customer customer) {
        try {
            return customerDAO.saveCustomer(customer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        try {
            return customerDAO.updateCustomer(customer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteCustomer(Customer customer) {
        try {
            preferenceDAO.deleteByCustomerId(customer.getId());
            return customerDAO.deleteCustomer(customer.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer findCustomerById(Integer id) {
        try {
            return customerDAO.findCustomer(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findAllCustomers() {
        try {
            return customerDAO.findAllCustomers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findByUserId(Integer userId) {

        //查询某个管家服务的客户列表，只涉及一条查询语句
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            List<Customer> customers = customerDAO.findByUserId(userId);
            JdbcUtils.getConnection().commit();
            return customers;
        } catch (SQLException e) {
            try {
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close();
        }

    }

    @Override
    public List<Customer> search(String customerName, String contact_tel, String idcard, String building_no) {
        try {
            return customerDAO.search(customerName, contact_tel, idcard, building_no);
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // ========================================================
    // 你的风格（转调组员的）
    // ========================================================

    @Override
    public boolean save(Customer customer) {
        return createCustomer(customer);
    }

    @Override
    public boolean update(Customer customer) {
        return updateCustomer(customer);
    }

    @Override
    public boolean delete(Integer id) {
        try {
            preferenceDAO.deleteByCustomerId(id);
            return customerDAO.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer findOne(Integer id) {
        return findCustomerById(id);
    }

    @Override
    public Customer findByIdCard(String idcard) {
        try {
            return customerDAO.findByIdCard(idcard);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findByName(String name) {
        try {
            return customerDAO.findByName(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findAll() {
        return findAllCustomers();
    }

    @Override
    public List<Customer> findPage(int pageNum, int pageSize) {
        try {
            return customerDAO.findPage((pageNum - 1) * pageSize, pageSize);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int count() {
        try {
            return customerDAO.count();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findExpiringBefore(LocalDate date) {
        try {
            return customerDAO.findExpiringBefore(date);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean assignCustomerToUser(Integer customerId, Integer userId) {
        try {
            Customer c = customerDAO.findOne(customerId);
            if (c == null) throw new RuntimeException("客户不存在");
            User u = userDAO.findOne(userId);
            if (u == null) throw new RuntimeException("用户不存在");
            c.setUser_id(userId);
            return customerDAO.update(c);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeCustomerFromUser(Integer customerId) {
        try {
            Customer c = customerDAO.findOne(customerId);
            if (c == null) throw new RuntimeException("客户不存在");
            c.setUser_id(-1);
            return customerDAO.update(c);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Preference findPreferenceByCustomerId(Integer customerId) {
        return preferenceDAO.findByCustomerId(customerId);
    }

    @Override
    public boolean savePreferences(Integer customerId, List<Preference> preferences) {
        preferenceDAO.deleteByCustomerId(customerId);
        if (preferences == null || preferences.isEmpty()) return true;
        for (Preference p : preferences) {
            p.setCustomerId(customerId);
            preferenceDAO.save(p);
        }
        return true;
    }

    @Override
    public boolean deletePreferences(Integer customerId) {
        return preferenceDAO.deleteByCustomerId(customerId) > 0;
    }
}