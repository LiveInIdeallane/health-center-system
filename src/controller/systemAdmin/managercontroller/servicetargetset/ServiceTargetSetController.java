package controller.systemAdmin.managercontroller.servicetargetset;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.support.HealthManagerSupport;
import entity.CustomerS.Customer;
import entity.UserS.User;
import service.CustomerS.CustomerService;
import service.CustomerS.CustomerServiceImpl;
import service.UserS.UserService;
import service.UserS.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class ServiceTargetSetController {

    private static final UserService userService = new UserServiceImpl();
    private static final CustomerService customerService = new CustomerServiceImpl();

    private static final int ROLE_HEALTH_MANAGER = 2;

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("设置服务对象");
            System.out.println("1、查询健康管家");
            System.out.println("2、给管家添加服务客户");
            System.out.println("3、查看/移除管家的服务客户");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        listManagers();
                        break;
                    case "2":
                        addServiceCustomer();
                        break;
                    case "3":
                        removeServiceCustomer();
                        break;
                    case "0":
                        return;
                    default:
                        System.out.println("输入有误，请重新输入！");
                }
            } catch (Exception e) {
                ConsoleTools.printError(e);
            }
        }
    }

    private static void listManagers() {
        String name = ConsoleTools.input("健康管家姓名（模糊，回车不过滤）：");
        List<User> result = new ArrayList<>();
        for (User u : userService.findByNickname(name)) {
            if (u.getRoleId() != null && u.getRoleId() == ROLE_HEALTH_MANAGER) {
                result.add(u);
            }
        }
        if (result.isEmpty()) {
            System.out.println("没有符合条件的健康管家！");
            return;
        }
        System.out.printf("%-5s%-12s%-14s%-14s%-6s%n", "ID", "姓名", "登录编码", "手机号", "服务客户数");
        for (User u : result) {
            System.out.printf("%-5d%-12s%-14s%-14s%-6d%n",
                    u.getId(),
                    u.getNickname(),
                    u.getUsername(),
                    u.getPhoneNumber(),
                    serviceCustomers(u.getId()).size());
        }
    }

    private static void addServiceCustomer() throws Exception {
        int managerId = ConsoleTools.inputInt("请输入健康管家ID：");
        User manager = userService.findOne(managerId);
        if (manager == null || manager.getRoleId() == null
                || manager.getRoleId() != ROLE_HEALTH_MANAGER) {
            System.out.println("该健康管家不存在！");
            return;
        }
        String name = ConsoleTools.input("客户姓名（模糊，回车不过滤）：");
        List<Customer> candidates = new ArrayList<>();
        for (Customer c : freeCustomers()) {
            if (!name.isEmpty() && (c.getCustomer_name() == null
                    || !c.getCustomer_name().contains(name))) {
                continue;
            }
            candidates.add(c);
        }
        if (candidates.isEmpty()) {
            System.out.println("没有符合条件的无管家客户！");
            return;
        }
        HealthManagerSupport.printCustomers(candidates);
        int customerId = ConsoleTools.inputInt("请选择客户ID：");
        Customer selected = null;
        for (Customer c : candidates) {
            if (c.getId() != null && c.getId() == customerId) {
                selected = c;
                break;
            }
        }
        if (selected == null) {
            System.out.println("所选客户不可用！");
            return;
        }
        selected.setUser_id(managerId);
        System.out.println(customerService.updateCustomer(selected) ? "设置成功！" : "设置失败！");
    }

    private static void removeServiceCustomer() throws Exception {
        int managerId = ConsoleTools.inputInt("请输入健康管家ID：");
        User manager = userService.findOne(managerId);
        if (manager == null) {
            System.out.println("该健康管家不存在！");
            return;
        }
        List<Customer> customers = serviceCustomers(managerId);
        if (customers.isEmpty()) {
            System.out.println("该健康管家暂无服务客户！");
            return;
        }
        HealthManagerSupport.printCustomers(customers);
        int customerId = ConsoleTools.inputInt("请选择要移除的客户ID：");
        Customer selected = null;
        for (Customer c : customers) {
            if (c.getId() != null && c.getId() == customerId) {
                selected = c;
                break;
            }
        }
        if (selected == null) {
            System.out.println("所选客户不在该管家的服务列表中！");
            return;
        }
        if (!ConsoleTools.confirm("确认将客户【" + selected.getCustomer_name()
                + "】移出该管家的服务列表？(y/n)：")) {
            System.out.println("已取消移除！");
            return;
        }
        selected.setUser_id(null);
        System.out.println(customerService.updateCustomer(selected) ? "移除成功！" : "移除失败！");
    }

    private static List<Customer> freeCustomers() {
        List<Customer> result = new ArrayList<>();
        for (Customer c : customerService.findAllCustomers()) {
            if ((c.getIs_deleted() == null || c.getIs_deleted() == 0) && c.getUser_id() == null) {
                result.add(c);
            }
        }
        return result;
    }

    private static List<Customer> serviceCustomers(Integer managerId) {
        List<Customer> result = new ArrayList<>();
        for (Customer c : customerService.findAllCustomers()) {
            if ((c.getIs_deleted() == null || c.getIs_deleted() == 0)
                    && managerId != null && managerId.equals(c.getUser_id())) {
                result.add(c);
            }
        }
        return result;
    }
}
