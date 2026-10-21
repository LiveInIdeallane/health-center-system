package controller.systemAdmin.support;

import controller.systemAdmin.ConsoleTools;
import entity.CustomerS.Customer;

import java.util.List;

public class HealthManagerSupport {

    public static void printCustomers(List<Customer> list) {
        System.out.printf("%-5s%-10s%-8s%-8s%-8s%-12s%-12s%n",
                "ID", "姓名", "年龄", "性别", "床位", "健康管家ID", "护理级别ID");
        for (Customer c : list) {
            System.out.printf("%-5d%-10s%-8s%-8s%-8s%-12s%-12s%n",
                    c.getId(),
                    c.getCustomer_name(),
                    c.getCustomer_age() == null ? "-" : String.valueOf(c.getCustomer_age()),
                    ConsoleTools.sexText(c.getCustomer_sex()),
                    c.getBed_id() == null ? "-" : String.valueOf(c.getBed_id()),
                    c.getUser_id() == null ? "-" : String.valueOf(c.getUser_id()),
                    c.getLevel_id() == null ? "-" : String.valueOf(c.getLevel_id()));
        }
    }
}
