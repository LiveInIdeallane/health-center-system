package controller.healthManager;

import controller.LoginController;
import entity.BackdownS.Backdown;
import entity.CustomernurseitemS.Customernurseitem;
import entity.NurserecordS.Nurserecord;
import entity.OutwardS.Outward;
import entity.UserS.User;
import entity.RoleS.Role;
import entity.MenuS.Menu;
import entity.CustomerS.Customer;
import entity.PreferenceS.Preference;

import service.BackdownS.BackdownService;
import service.BackdownS.BackdownServiceImpl;
import service.CustomernurseitemS.CustomernurseitemService;
import service.CustomernurseitemS.CustomernurseitemServiceImpl;
import service.NurserecordS.Nurserecordservice;
import service.NurserecordS.NurserecordserviceImpl;
import service.OutwardS.OutwardService;
import service.OutwardS.OutwardServiceImpl;
import service.UserS.UserService;
import service.UserS.UserServiceImpl;
import service.RoleS.RoleService;
import service.RoleS.RoleServiceImpl;
import service.MenuS.MenuService;
import service.MenuS.MenuServiceImpl;
import service.CustomerS.CustomerService;
import service.CustomerS.CustomerServiceImpl;

import java.sql.SQLException;


import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * 主菜单控制器：提供四个模块的菜单入口。
 * 由 MainView.menu() 根据动态菜单列表分发调用。
 */
public class MainController {

    private static Scanner sc = new Scanner(System.in);

    private static UserService userService = new UserServiceImpl();
    private static RoleService roleService = new RoleServiceImpl();
    private static MenuService menuService = new MenuServiceImpl();
    private static CustomerService customerService = new CustomerServiceImpl();
    private static OutwardService outwardService = new OutwardServiceImpl();
    private static BackdownService backdownService = new BackdownServiceImpl();

    private static Nurserecordservice nurserecordService = new NurserecordserviceImpl();
    private static CustomernurseitemService customernurseitemService = new CustomernurseitemServiceImpl();

    // ===================== 1. 用户管理 =====================

    public static void userMenu() {
        while (true) {
            System.out.println("\n--- 用户管理 ---");
            System.out.println("1. 用户 CRUD");
            System.out.println("2. 角色管理");
            System.out.println("3. 菜单管理");
            System.out.println("0. 返回上级");
            System.out.print("请选择：");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": userCrudMenu(); break;      // 原 userMenu 内容改名
                    case "2": roleMenu(); break;          // 已有
                    case "3": menuMenu(); break;          // 已有
                    case "0": return;
                    default: System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    /** 原来的 userMenu 内容，改成 userCrudMenu */
    private static void userCrudMenu() {
        while (true) {
            System.out.println("\n--- 用户 CRUD ---");
            System.out.println("1. 新增用户");
            System.out.println("2. 修改用户");
            System.out.println("3. 删除用户");
            System.out.println("4. 查询所有用户");
            System.out.println("5. 按角色查用户");
            System.out.println("0. 返回上级");
            System.out.print("请选择：");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": addUser(); break;
                    case "2": updateUser(); break;
                    case "3": deleteUser(); break;
                    case "4": listUsers(); break;
                    case "5": listUsersByRole(); break;
                    case "0": return;
                    default: System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    private static void addUser() {
        User u = new User();
        System.out.print("真实姓名："); u.setNickname(sc.nextLine());
        System.out.print("系统账号："); u.setUsername(sc.nextLine());
        System.out.print("密码："); u.setPassword(sc.nextLine());
        System.out.print("性别(0女 1男)：");
        u.setSex(Integer.parseInt(sc.nextLine()));
        System.out.print("邮箱："); u.setEmail(sc.nextLine());
        System.out.print("电话："); u.setPhoneNumber(sc.nextLine());
        System.out.print("角色ID："); u.setRoleId(Integer.parseInt(sc.nextLine()));
        try {
            System.out.println(userService.save(u) ? "新增成功" : "新增失败");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateUser() {
        System.out.print("用户 id：");
        Integer id = Integer.parseInt(sc.nextLine());
        User u = userService.findOne(id);
        if (u == null) { System.out.println("用户不存在"); return; }
        System.out.print("新真实姓名(" + u.getNickname() + ")：");
        String s = sc.nextLine(); if (!s.isEmpty()) u.setNickname(s);
        System.out.print("新电话(" + u.getPhoneNumber() + ")：");
        s = sc.nextLine(); if (!s.isEmpty()) u.setPhoneNumber(s);
        System.out.println(userService.update(u) ? "修改成功" : "修改失败");
    }

    private static void deleteUser() {
        System.out.print("用户 id：");
        String input = sc.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("id 不能为空");
            return;
        }
        Integer id;
        try {
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("id 必须是数字");
            return;
        }
        System.out.println(userService.delete(id) ? "删除成功" : "删除失败");
    }

    private static void listUsers() {
        List<User> list = userService.findAll();
        for (User u : list) System.out.println("  " + u);
    }

    private static void listUsersByRole() {
        System.out.print("角色ID：");
        Integer roleId = Integer.parseInt(sc.nextLine());
        List<User> list = userService.findByRoleId(roleId);
        for (User u : list) System.out.println("  " + u);
    }

    // ===================== 2. 角色管理 =====================

    public static void roleMenu() {
        while (true) {
            System.out.println("\n--- 角色管理 ---");
            System.out.println("1. 新增角色");
            System.out.println("2. 修改角色");
            System.out.println("3. 删除角色");
            System.out.println("4. 查询所有角色");
            System.out.println("5. 给角色分配菜单");
            System.out.println("6. 查看角色的菜单");
            System.out.println("0. 返回上级");
            System.out.print("请选择：");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": addRole(); break;
                    case "2": updateRole(); break;
                    case "3": deleteRole(); break;
                    case "4": listRoles(); break;
                    case "5": assignMenus(); break;
                    case "6": viewRoleMenus(); break;
                    case "0": return;
                    default: System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    private static void addRole() {
        Role r = new Role();
        System.out.print("角色名："); r.setName(sc.nextLine());
        System.out.println(roleService.save(r) ? "新增成功，id=" + r.getId() : "新增失败");
    }

    private static void updateRole() {
        System.out.print("角色 id：");
        Integer id = Integer.parseInt(sc.nextLine());
        Role r = roleService.findOne(id);
        if (r == null) { System.out.println("角色不存在"); return; }
        System.out.print("新角色名(" + r.getName() + ")：");
        String s = sc.nextLine(); if (!s.isEmpty()) r.setName(s);
        System.out.println(roleService.update(r) ? "修改成功" : "修改失败");
    }

    private static void deleteRole() {
        System.out.print("角色 id：");
        Integer id = Integer.parseInt(sc.nextLine());
        System.out.println(roleService.delete(id) ? "删除成功" : "删除失败");
    }

    private static void listRoles() {
        List<Role> list = roleService.findAll();
        for (Role r : list) System.out.println("  " + r);
    }

    private static void assignMenus() {
        System.out.print("角色 id：");
        Integer roleId = Integer.parseInt(sc.nextLine());
        System.out.println("所有菜单：");
        for (Menu m : menuService.findAll()) {
            System.out.println("  " + m.getId() + " - " + m.getTitle());
        }
        System.out.print("要分配的菜单 id（逗号分隔，如 1,2,3）：");
        String[] arr = sc.nextLine().split(",");
        List<Integer> menuIds = new ArrayList<>();
        for (String s : arr) {
            if (!s.trim().isEmpty()) menuIds.add(Integer.parseInt(s.trim()));
        }
        System.out.println(roleService.assignMenus(roleId, menuIds) ? "分配成功" : "分配失败");
    }

    private static void viewRoleMenus() {
        System.out.print("角色 id：");
        Integer roleId = Integer.parseInt(sc.nextLine());
        List<Menu> menus = roleService.findMenusByRoleId(roleId);
        for (Menu m : menus) System.out.println("  " + m);
    }

    // ===================== 3. 菜单管理 =====================

    public static void menuMenu() {
        while (true) {
            System.out.println("\n--- 菜单管理 ---");
            System.out.println("1. 新增菜单");
            System.out.println("2. 修改菜单");
            System.out.println("3. 删除菜单");
            System.out.println("4. 查询所有菜单");
            System.out.println("5. 查询一级菜单");
            System.out.println("6. 按父级查子菜单");
            System.out.println("0. 返回上级");
            System.out.print("请选择：");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": addMenu(); break;
                    case "2": updateMenu(); break;
                    case "3": deleteMenu(); break;
                    case "4": listMenus(); break;
                    case "5": listTopMenus(); break;
                    case "6": listMenusByParent(); break;
                    case "0": return;
                    default: System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    private static void addMenu() {
        Menu m = new Menu();
        System.out.print("一级菜单索引："); m.setMenusIndex(sc.nextLine());
        System.out.print("菜单标题："); m.setTitle(sc.nextLine());
        System.out.print("菜单图标："); m.setIcon(sc.nextLine());
        System.out.print("菜单路径："); m.setPath(sc.nextLine());
        System.out.print("父级ID（0 表示顶级）：");
        String s = sc.nextLine();
        if (!s.isEmpty()) m.setParentId(Integer.parseInt(s));
        System.out.println(menuService.save(m) ? "新增成功" : "新增失败");
    }

    private static void updateMenu() {
        System.out.print("菜单 id：");
        Integer id = Integer.parseInt(sc.nextLine());
        Menu m = menuService.findOne(id);
        if (m == null) { System.out.println("菜单不存在"); return; }
        System.out.print("新标题(" + m.getTitle() + ")：");
        String s = sc.nextLine(); if (!s.isEmpty()) m.setTitle(s);
        System.out.print("新路径(" + m.getPath() + ")：");
        s = sc.nextLine(); if (!s.isEmpty()) m.setPath(s);
        System.out.println(menuService.update(m) ? "修改成功" : "修改失败");
    }

    private static void deleteMenu() {
        System.out.print("菜单 id：");
        Integer id = Integer.parseInt(sc.nextLine());
        System.out.println(menuService.delete(id) ? "删除成功" : "删除失败");
    }

    private static void listMenus() {
        for (Menu m : menuService.findAll()) System.out.println("  " + m);
    }

    private static void listTopMenus() {
        for (Menu m : menuService.findTopMenus()) System.out.println("  " + m);
    }

    private static void listMenusByParent() {
        System.out.print("父级 id：");
        Integer pid = Integer.parseInt(sc.nextLine());
        for (Menu m : menuService.findByParentId(pid)) System.out.println("  " + m);
    }

    // ===================== 4. 客户管理 =====================

    public static void customerMenu() {
        while (true) {
            System.out.println("\n--- 客户管理 ---");
            System.out.println("1. 新增客户");
            System.out.println("2. 修改客户");
            System.out.println("3. 删除客户");
            System.out.println("4. 按姓名查客户");
            System.out.println("5. 查询所有客户");
            System.out.println("6. 给健康管家分配客户");
            System.out.println("7. 解除健康管家与客户的关联");
            System.out.println("8. 外出审核");                  // ← 新增
            System.out.println("9. 退住审核");                  // ← 新增
            System.out.println("10. 膳食管理（客户饮食偏好）");  // ← 新增
            System.out.println("0. 返回上级");
            System.out.print("请选择：");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": addCustomer(); break;
                    case "2": updateCustomer(); break;
                    case "3": deleteCustomer(); break;
                    case "4": findCustomerByName(); break;
                    case "5": listCustomers(); break;
                    case "6": assignCustomerToUser(); break;
                    case "7": removeCustomerFromUser(); break;
                    case "8": outwardMenuForAdmin(); break;
                    case "9": backdownMenuForAdmin(); break;        // 新方法
                    case "10": preferenceMenu(); break;      // 新方法
                    case "0": return;
                    default: System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    /** 管理员：退住申请 */
    private static void backdownMenuForAdmin() {
        while (true) {
            System.out.println("\n--- 退住申请 ---");
            System.out.println("1. 提交退住申请");
            System.out.println("2. 浏览退住申请列表");
            System.out.println("0. 返回上级");
            System.out.print("请选择：");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": addBackdownByAdmin(); break;
                    case "2": listBackdownByAdmin(); break;
                    case "0": return;
                    default: System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    /** 管理员：提交退住申请（可替任何客户提交） */
    private static void addBackdownByAdmin() {
        System.out.print("客户姓名（模糊，直接回车查全部）：");
        String name = sc.nextLine().trim();
        List<Customer> all = customerService.findAll();
        List<Customer> list = new ArrayList<>();
        for (Customer c : all) {
            if (name.isEmpty() || (c.getCustomer_name() != null && c.getCustomer_name().contains(name))) {
                list.add(c);
            }
        }
        if (list.isEmpty()) { System.out.println("未找到客户"); return; }
        for (Customer c : list) {
            System.out.println("  id=" + c.getId() + ", 姓名=" + c.getCustomer_name());
        }

        System.out.print("请选择客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());

        Backdown b = new Backdown();
        b.setIs_delete(0);
        b.setCustomer_id(customerId);
        System.out.print("退住类型(0正常退住 1死亡退住 2保留床位)：");
        b.setRetreattype(Integer.parseInt(sc.nextLine()));
        System.out.print("退住原因："); b.setRetreatreason(sc.nextLine());
        System.out.print("退住时间(yyyy-MM-dd)：");
        b.setRetreattime(java.sql.Date.valueOf(sc.nextLine()));
        b.setAuditstatus(0);   // 已提交
        b.setAuditperson(null);
        b.setAudittime(null);

        boolean ok = backdownService.createBackdown(b);
        System.out.println(ok ? "退住申请已提交" : "提交失败");
    }

    /** 管理员：浏览所有退住申请 */
    private static void listBackdownByAdmin() {
        List<Backdown> list = backdownService.findAllBackdowns();
        if (list.isEmpty()) { System.out.println("暂无退住申请"); return; }
        boolean found = false;
        for (Backdown b : list) {
            if (b.getIs_delete() == null || b.getIs_delete() == 0) {
                System.out.println("  申请id=" + b.getId()
                        + ", 客户id=" + b.getCustomer_id()
                        + ", 退住类型=" + b.getRetreattype()
                        + ", 退住原因=" + b.getRetreatreason()
                        + ", 退住时间=" + b.getRetreattime()
                        + ", 审批状态=" + b.getAuditstatus());
                found = true;
            }
        }
        if (!found) System.out.println("暂无退住申请");
    }

    /** 管理员：外出申请 */
    private static void outwardMenuForAdmin() {
        while (true) {
            System.out.println("\n--- 外出申请 ---");
            System.out.println("1. 提交外出申请");
            System.out.println("2. 浏览外出申请列表");
            System.out.println("3. 登记回院时间");
            System.out.println("0. 返回上级");
            System.out.print("请选择：");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": addOutwardByAdmin(); break;
                    case "2": listOutwardByAdmin(); break;
                    case "3": updateReturnTimeByAdmin(); break;
                    case "0": return;
                    default: System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    /** 管理员：提交外出申请（可替任何客户提交） */
    private static void addOutwardByAdmin() {
        System.out.print("客户姓名（模糊，直接回车查全部）：");
        String name = sc.nextLine().trim();
        List<Customer> all = customerService.findAll();
        List<Customer> list = new ArrayList<>();
        for (Customer c : all) {
            if (name.isEmpty() || (c.getCustomer_name() != null && c.getCustomer_name().contains(name))) {
                list.add(c);
            }
        }
        if (list.isEmpty()) { System.out.println("未找到客户"); return; }
        for (Customer c : list) {
            System.out.println("  id=" + c.getId() + ", 姓名=" + c.getCustomer_name());
        }

        System.out.print("请选择客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());

        Outward o = new Outward();
        o.setIs_delete(0);
        o.setCustomer_id(customerId);
        System.out.print("外出事由："); o.setOutgoingreason(sc.nextLine());
        System.out.print("外出时间(yyyy-MM-dd)：");
        o.setOutgoingtime(java.sql.Date.valueOf(sc.nextLine()));
        System.out.print("预计回院时间(yyyy-MM-dd)：");
        o.setExpectedreturntime(java.sql.Date.valueOf(sc.nextLine()));
        System.out.print("陪同人："); o.setEscorted(sc.nextLine());
        System.out.print("与老人关系："); o.setRelation(sc.nextLine());
        System.out.print("陪同人电话："); o.setEscortedtel(sc.nextLine());
        o.setAuditstatus(0);   // 已提交
        o.setAuditperson(null);
        o.setAudittime(null);

        boolean ok = outwardService.CreateOutward(o);
        System.out.println(ok ? "外出申请已提交" : "提交失败");
    }

    /** 管理员：浏览所有外出申请 */
    private static void listOutwardByAdmin() {
        List<Outward> outs = outwardService.GetOutwards();
        if (outs.isEmpty()) { System.out.println("暂无外出申请"); return; }
        boolean found = false;
        for (Outward o : outs) {
            if (o.getIs_delete() == null || o.getIs_delete() == 0) {
                System.out.println("  申请id=" + o.getId()
                        + ", 客户id=" + o.getCustomer_id()
                        + ", 事由=" + o.getOutgoingreason()
                        + ", 外出=" + o.getOutgoingtime()
                        + ", 预计回院=" + o.getExpectedreturntime()
                        + ", 实际回院=" + o.getActualreturntime()
                        + ", 审批状态=" + o.getAuditstatus());
                found = true;
            }
        }
        if (!found) System.out.println("暂无外出申请");
    }

    /** 管理员：登记回院时间 */
    private static void updateReturnTimeByAdmin() {
        List<Outward> outs = outwardService.GetOutwards();
        if (outs.isEmpty()) { System.out.println("暂无外出申请"); return; }
        for (Outward o : outs) {
            if (o.getIs_delete() == null || o.getIs_delete() == 0) {
                System.out.println("  申请id=" + o.getId()
                        + ", 客户id=" + o.getCustomer_id()
                        + ", 外出=" + o.getOutgoingtime()
                        + ", 实际回院=" + o.getActualreturntime());
            }
        }
        System.out.print("要登记回院时间的申请 id：");
        Integer oid = Integer.parseInt(sc.nextLine());

        Outward target = null;
        for (Outward o : outs) {
            if (oid.equals(o.getId())) { target = o; break; }
        }
        if (target == null) { System.out.println("未找到该申请"); return; }

        System.out.print("实际回院时间(yyyy-MM-dd)：");
        Date t = java.sql.Date.valueOf(sc.nextLine());

        target.setActualreturntime(t);
        boolean ok = outwardService.UpdateOutward(target);
        System.out.println(ok ? "已登记" : "登记失败");
    }

    private static void addCustomer() {
        Customer c = new Customer();
        System.out.print("客户姓名："); c.setCustomer_name(sc.nextLine());
        System.out.print("身份证："); c.setIdcard(sc.nextLine());
        System.out.print("性别(0男 1女)：");
        c.setCustomer_sex(Integer.parseInt(sc.nextLine()));
        System.out.print("联系电话："); c.setContact_tel(sc.nextLine());
        System.out.print("房间号："); c.setRoom_no(sc.nextLine());
        System.out.print("入住时间(yyyy-MM-dd)：");
        c.setCheckin_date(java.sql.Date.valueOf(sc.nextLine()));
        System.out.print("合同到期(yyyy-MM-dd)：");
        c.setExpiration_date(java.sql.Date.valueOf(sc.nextLine()));
        System.out.println(customerService.save(c) ? "新增成功" : "新增失败");
    }

    private static void updateCustomer() {
        System.out.print("客户 id：");
        Integer id = Integer.parseInt(sc.nextLine());
        Customer c = customerService.findOne(id);
        if (c == null) { System.out.println("客户不存在"); return; }
        System.out.print("新姓名(" + c.getCustomer_name() + ")：");
        String s = sc.nextLine(); if (!s.isEmpty()) c.setCustomer_name(s);
        System.out.print("新电话(" + c.getContact_tel() + ")：");
        s = sc.nextLine(); if (!s.isEmpty()) c.setContact_tel(s);
        System.out.println(customerService.update(c) ? "修改成功" : "修改失败");
    }

    private static void deleteCustomer() {
        System.out.print("客户 id：");
        Integer id = Integer.parseInt(sc.nextLine());
        System.out.println(customerService.delete(id) ? "删除成功" : "删除失败");
    }

    private static void findCustomerByName() {
        System.out.print("姓名关键字：");
        String name = sc.nextLine();
        for (Customer c : customerService.findByName(name)) System.out.println("  " + c);
    }

    private static void listCustomers() {
        for (Customer c : customerService.findAll()) System.out.println("  " + c);
    }

    private static void assignCustomerToUser() {
        System.out.print("客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());
        System.out.print("健康管家 id：");
        Integer userId = Integer.parseInt(sc.nextLine());
        System.out.println(customerService.assignCustomerToUser(customerId, userId) ? "分配成功" : "分配失败");
    }

    private static void removeCustomerFromUser() {
        System.out.print("客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());
        System.out.println(customerService.removeCustomerFromUser(customerId) ? "解除成功" : "解除失败");
    }

    // ===================== 4. 健康管家管理 =====================

    // ===================== 4. 健康管家管理 =====================

    public static void healthNurseMenu() {
        while (true) {
            System.out.println("\n--- 健康管家与护理管理 ---");
            System.out.println("1. 查询健康管家列表");
            System.out.println("2. 设置服务对象");
            System.out.println("3. 日常护理（代管生成记录）"); // ← 新增
            System.out.println("4. 服务对象护理记录查看");     // ← 新增
            System.out.println("0. 返回上级");
            System.out.print("请选择：");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": listHealthNurses(); break;
                    case "2": setServiceTarget(); break;
                    case "3": adminDailyCare(); break;      // ← 调新增的方法
                    case "4": adminNurseRecord(); break;    // ← 调新增的方法
                    case "0": return;
                    default: System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    // ↓↓↓ 以下是新增的两个方法（把护工的逻辑搬过来） ↓↓↓

    /** 管理员：日常护理 */
    private static void adminDailyCare() {
        // 管理员可以查所有客户
        System.out.print("客户姓名（模糊，直接回车查全部）：");
        String name = sc.nextLine().trim();
        List<Customer> all = customerService.findAll();
        List<Customer> list = new ArrayList<>();
        for (Customer c : all) {
            if (name.isEmpty() || (c.getCustomer_name() != null && c.getCustomer_name().contains(name))) {
                list.add(c);
            }
        }
        if (list.isEmpty()) { System.out.println("未找到客户"); return; }
        for (Customer c : list) {
            System.out.println("  id=" + c.getId() + ", 姓名=" + c.getCustomer_name());
        }

        System.out.print("请选择客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());

        // 查护理项目
        List<Customernurseitem> items = new ArrayList<>();
        try {
            for (Customernurseitem it : customernurseitemService.findAllCustomernurseitem()) {
                if (customerId.equals(it.getCustomer_id())
                        && (it.getIs_deleted() == null || it.getIs_deleted() == 0)) {
                    items.add(it);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (items.isEmpty()) { System.out.println("该客户暂无护理项目"); return; }
        for (Customernurseitem it : items) {
            System.out.println("  项目id=" + it.getItem_id() + ", 数量=" + it.getNurse_number());
        }

        System.out.print("请选择护理项目 id：");
        Integer itemId = Integer.parseInt(sc.nextLine());
        System.out.print("护理数量：");
        Integer count = Integer.parseInt(sc.nextLine());

        Nurserecord record = new Nurserecord();
        record.setIs_deleted(0);
        record.setCustomer_id(customerId);
        record.setItem_id(itemId);
        record.setNursing_time(new java.util.Date());
        record.setNursing_content("管理员代管护理");
        record.setNursing_count(count);
        record.setUser_id(LoginController.getCurrentUser().getId()); // 记录操作人

        boolean ok = nurserecordService.addNurserecord(record);
        if (ok) {
            for (Customernurseitem it : items) {
                if (it.getItem_id().equals(itemId)) {
                    it.setNurse_number(it.getNurse_number() - count);
                    customernurseitemService.updateCustomernurseitem(it);
                    break;
                }
            }
            System.out.println("护理记录生成成功！已扣减对应护理数量。");
        } else {
            System.out.println("护理记录生成失败！");
        }
    }

    /** 管理员：查看服务对象护理记录 */
    private static void adminNurseRecord() {
        System.out.print("客户姓名（模糊，直接回车查全部）：");
        String name = sc.nextLine().trim();
        List<Customer> all = customerService.findAll();
        List<Customer> list = new ArrayList<>();
        for (Customer c : all) {
            if (name.isEmpty() || (c.getCustomer_name() != null && c.getCustomer_name().contains(name))) {
                list.add(c);
            }
        }
        if (list.isEmpty()) { System.out.println("未找到客户"); return; }
        for (Customer c : list) {
            System.out.println("  id=" + c.getId() + ", 姓名=" + c.getCustomer_name());
        }

        System.out.print("请选择客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());

        List<Nurserecord> records = new ArrayList<>();
        try {
            for (Nurserecord r : nurserecordService.findAllNurserecord()) {
                if (customerId.equals(r.getCustomer_id())
                        && (r.getIs_deleted() == null || r.getIs_deleted() == 0)) {
                    records.add(r);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (records.isEmpty()) { System.out.println("该客户暂无护理记录"); return; }
        for (Nurserecord r : records) {
            System.out.println("  记录id=" + r.getId()
                    + ", 项目id=" + r.getItem_id()
                    + ", 时间=" + r.getNursing_time()
                    + ", 数量=" + r.getNursing_count()
                    + ", 内容=" + r.getNursing_content());
        }
    }

    /** 查询健康管家列表（按姓名模糊） */
    private static void listHealthNurses() {
        System.out.print("管家姓名（模糊，直接回车查全部）：");
        String name = sc.nextLine().trim();
        List<User> nurses = userService.findByRoleId(2);   // role_id=2 是健康管家
        boolean found = false;
        for (User u : nurses) {
            if (name.isEmpty() || (u.getNickname() != null && u.getNickname().contains(name))) {
                System.out.println("  id=" + u.getId()
                        + ", 姓名=" + u.getNickname()
                        + ", 电话=" + u.getPhoneNumber());
                found = true;
            }
        }
        if (!found) System.out.println("未找到健康管家");
    }

    /** 设置服务对象 */
    private static void setServiceTarget() {
        List<User> nurses = userService.findByRoleId(2);
        if (nurses.isEmpty()) {
            System.out.println("暂无健康管家");
            return;
        }
        System.out.println("管家列表：");
        for (User u : nurses) {
            System.out.println("  id=" + u.getId() + ", 姓名=" + u.getNickname());
        }
        System.out.print("请选择管家 id：");
        Integer nurseId = Integer.parseInt(sc.nextLine());

        while (true) {
            System.out.println("\n--- 设置服务对象 ---");
            System.out.println("1. 查询无管家客户");
            System.out.println("2. 把客户分配给该管家");
            System.out.println("3. 查看该管家的服务客户");
            System.out.println("4. 移除某个客户");
            System.out.println("0. 返回");
            System.out.print("请选择：");

            String c = sc.nextLine().trim();
            try {
                switch (c) {
                    case "1": listNoNurseCustomers(); break;
                    case "2": assignToNurse(nurseId); break;
                    case "3": listNurseCustomers(nurseId); break;
                    case "4": removeFromNurse(nurseId); break;
                    case "0": return;
                    default: System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    /** 查无管家客户（user_id = -1） */
    private static void listNoNurseCustomers() {
        List<Customer> all = customerService.findAll();
        boolean found = false;
        for (Customer cu : all) {
            if (cu.getUser_id() != null && cu.getUser_id() == -1) {
                System.out.println("  id=" + cu.getId()
                        + ", 姓名=" + cu.getCustomer_name()
                        + ", 房间号=" + cu.getRoom_no());
                found = true;
            }
        }
        if (!found) System.out.println("暂无无管家客户");
    }

    /** 把客户分配给管家 */
    private static void assignToNurse(Integer nurseId) {
        System.out.print("要分配的客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());
        System.out.println(customerService.assignCustomerToUser(customerId, nurseId) ? "分配成功" : "分配失败");
    }

    /** 查管家的服务客户 */
    private static void listNurseCustomers(Integer nurseId) {
        List<Customer> list = customerService.findByUserId(nurseId);
        if (list.isEmpty()) { System.out.println("该管家暂无服务客户"); return; }
        for (Customer cu : list) {
            System.out.println("  id=" + cu.getId()
                    + ", 姓名=" + cu.getCustomer_name()
                    + ", 房间号=" + cu.getRoom_no());
        }
    }

    /** 移除管家的某个客户 */
    private static void removeFromNurse(Integer nurseId) {
        System.out.print("要移除的客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());
        System.out.println(customerService.removeCustomerFromUser(customerId) ? "移除成功" : "移除失败");
    }

// ===================== 客户管理：外出审核 / 退住审核 / 膳食 =====================

    /** 外出审核 */
    private static void auditOutward() {
        System.out.println("--- 外出审核（待审核的申请） ---");
        // 你 Service 里要能查所有待审核（auditstatus=0）的外出申请
        // 例：List<Outward> list = outwardService.getPendingAudits();
        // 下面给的是伪代码框架，按你 Service 实际方法名改
        System.out.println("功能待补充：查询 auditstatus=0 的外出申请，选择某条进行审核");
    }

    /** 退住审核 */
    private static void auditBackdown() {
        System.out.println("--- 退住审核（待审核的申请） ---");
        System.out.println("功能待补充：查询 auditstatus=0 的退住申请，选择某条进行审核");
    }

    /** 膳食管理（客户饮食偏好） */
    private static void preferenceMenu() {
        while (true) {
            System.out.println("\n--- 膳食管理（客户饮食偏好） ---");
            System.out.println("1. 查看某客户的偏好");
            System.out.println("2. 设置某客户的偏好");
            System.out.println("0. 返回上级");
            System.out.print("请选择：");

            String c = sc.nextLine().trim();
            try {
                switch (c) {
                    case "1": viewPreference(); break;
                    case "2": savePreference(); break;
                    case "0": return;
                    default: System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    private static void viewPreference() {
        System.out.print("客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());
        Preference p = customerService.findPreferenceByCustomerId(customerId);
        System.out.println(p == null ? "该客户暂无偏好" : p);
    }

    private static void savePreference() {
        System.out.print("客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());
        Preference p = new Preference();
        System.out.print("饮食喜好："); p.setPreferences(sc.nextLine());
        System.out.print("注意事项："); p.setAttention(sc.nextLine());
        System.out.print("备注：");     p.setRemark(sc.nextLine());
        List<Preference> list = new ArrayList<>();
        list.add(p);
        System.out.println(customerService.savePreferences(customerId, list) ? "保存成功" : "保存失败");
    }


}