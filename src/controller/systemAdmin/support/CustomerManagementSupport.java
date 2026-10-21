package controller.systemAdmin.support;

import entity.CustomerS.Customer;
import service.CustomernurseitemS.CustomernurseitemService;
import service.CustomernurseitemS.CustomernurseitemServiceImpl;
import service.CustomerS.CustomerService;
import service.CustomerS.CustomerServiceImpl;

import java.util.ArrayList;
import java.util.List;

public final class CustomerManagementSupport {

    private static final CustomerService CUSTOMER_SERVICE = new CustomerServiceImpl();
    private static final CustomernurseitemService NURSE_ITEM_SERVICE = new CustomernurseitemServiceImpl();

    private CustomerManagementSupport() {
    }

    public static String customerName(Integer customerId) {
        if (customerId == null) {
            return "";
        }
        Customer c = CUSTOMER_SERVICE.findCustomerById(customerId);
        return c == null ? "" : c.getCustomer_name();
    }

    public static List<Integer> nurseCustomerIds() {
        List<Integer> ids = NURSE_ITEM_SERVICE.findCustomerIdsWithNurseItems();
        return ids == null ? new ArrayList<>() : ids;
    }

    public static boolean isNurseCustomer(Customer c) {
        return nurseCustomerIds().contains(c.getId());
    }

    public static boolean isNurseCustomer(Integer customerId) {
        return nurseCustomerIds().contains(customerId);
    }

}
