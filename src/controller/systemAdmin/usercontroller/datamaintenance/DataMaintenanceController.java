package controller.systemAdmin.usercontroller.datamaintenance;

import controller.LoginController;
import controller.systemAdmin.ConsoleTools;
import entity.MenuS.Menu;
import entity.RoleS.Role;
import entity.UserS.User;
import service.MenuS.MenuService;
import service.MenuS.MenuServiceImpl;
import service.RoleS.RoleService;
import service.RoleS.RoleServiceImpl;
import service.UserS.UserService;
import service.UserS.UserServiceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataMaintenanceController {

    private static final UserService userService = new UserServiceImpl();
    private static final RoleService roleService = new RoleServiceImpl();
    private static final MenuService menuService = new MenuServiceImpl();

    private static final int ROLE_ADMIN = 1;
    private static final int SEX_FEMALE = 0;
    private static final int SEX_MALE = 1;

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("基础数据维护");
            System.out.println("1、查询用户列表");
            System.out.println("2、新增用户");
            System.out.println("3、修改用户");
            System.out.println("4、删除用户");
            System.out.println("5、查询角色列表");
            System.out.println("6、新增角色");
            System.out.println("7、修改角色");
            System.out.println("8、删除角色");
            System.out.println("9、分配菜单");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        listUsers();
                        break;
                    case "2":
                        addUser();
                        break;
                    case "3":
                        updateUser();
                        break;
                    case "4":
                        deleteUser();
                        break;
                    case "5":
                        listRoles();
                        break;
                    case "6":
                        addRole();
                        break;
                    case "7":
                        updateRole();
                        break;
                    case "8":
                        deleteRole();
                        break;
                    case "9":
                        assignMenus();
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

    private static void listUsers() {
        String name = ConsoleTools.input("真实姓名（模糊，回车不过滤）：");
        List<User> users = name.isEmpty() ? userService.findAll() : userService.findByNickname(name);
        if (users.isEmpty()) {
            System.out.println("没有符合条件的用户！");
            return;
        }
        System.out.printf("%-5s%-12s%-14s%-6s%-14s%-20s%-14s%n",
                "ID", "姓名", "登录编码", "性别", "手机号", "邮箱", "角色");
        for (User u : users) {
            System.out.printf("%-5d%-12s%-14s%-6s%-14s%-20s%-14s%n",
                    u.getId(),
                    u.getNickname(),
                    u.getUsername(),
                    sexText(u.getSex()),
                    u.getPhoneNumber(),
                    u.getEmail(),
                    roleName(u.getRoleId()));
        }
    }

    private static void addUser() {
        User user = new User();
        user.setNickname(ConsoleTools.input("真实姓名："));
        user.setUsername(ConsoleTools.input("登录编码："));
        String phone = ConsoleTools.input("手机号：");
        if (phone.isEmpty()) {
            System.out.println("手机号不能为空！");
            return;
        }
        user.setPhoneNumber(phone);
        String sex = ConsoleTools.input("性别(0女 1男)：");
        if (!"0".equals(sex) && !"1".equals(sex)) {
            System.out.println("性别输入有误！");
            return;
        }
        user.setSex(Integer.valueOf(sex));
        user.setEmail(ConsoleTools.input("邮箱(可空)："));
        Integer roleId = chooseRole();
        if (roleId == null) {
            return;
        }
        user.setRoleId(roleId);
        if (user.getNickname().isEmpty() || user.getUsername().isEmpty()) {
            System.out.println("真实姓名和登录编码不能为空！");
            return;
        }
        try {
            userService.save(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("新增成功！默认密码为手机号后6位：" + user.getPassword());
    }

    private static void updateUser() {
        int id = ConsoleTools.inputInt("请输入要修改的用户ID：");
        User old = userService.findOne(id);
        if (old == null) {
            System.out.println("用户不存在！");
            return;
        }
        User user = new User();
        user.setId(old.getId());
        user.setIsDeleted(old.getIsDeleted());
        user.setPassword(old.getPassword());
        user.setNickname(ConsoleTools.keep("真实姓名(" + old.getNickname() + ")：", old.getNickname()));
        user.setUsername(ConsoleTools.keep("登录编码(" + old.getUsername() + ")：", old.getUsername()));
        user.setPhoneNumber(ConsoleTools.keep("手机号(" + old.getPhoneNumber() + ")：", old.getPhoneNumber()));
        String sex = ConsoleTools.input("性别(0女 1男)(" + old.getSex() + ")：");
        user.setSex(sex.isEmpty() ? old.getSex() : Integer.valueOf(sex));
        user.setEmail(ConsoleTools.keep("邮箱(" + old.getEmail() + ")：", old.getEmail()));
        Integer roleId = chooseRole();
        if (roleId == null) {
            return;
        }
        user.setRoleId(roleId);
        System.out.println(userService.update(user) ? "修改成功！" : "修改失败！");
    }

    private static void deleteUser() {
        int id = ConsoleTools.inputInt("请输入要删除的用户ID：");
        User user = userService.findOne(id);
        if (user == null) {
            System.out.println("用户不存在！");
            return;
        }
        if (Integer.valueOf(id).equals(currentUserId())) {
            System.out.println("不能删除当前登录用户！");
            return;
        }
        if (!ConsoleTools.confirm("确认删除用户【" + user.getNickname() + "】？(y/n)：")) {
            System.out.println("已取消删除！");
            return;
        }
        System.out.println(userService.delete(id) ? "删除成功！" : "删除失败！");
    }

    private static void listRoles() {
        List<Role> roles = roleService.findAll();
        if (roles.isEmpty()) {
            System.out.println("暂无角色数据！");
            return;
        }
        System.out.printf("%-5s%-16s%-10s%-10s%n", "ID", "角色名", "用户数", "菜单数");
        for (Role role : roles) {
            System.out.printf("%-5d%-16s%-10d%-10d%n",
                    role.getId(),
                    role.getName(),
                    userService.findByRoleId(role.getId()).size(),
                    roleService.findMenuIdsByRoleId(role.getId()).size());
        }
    }

    private static void addRole() {
        String name = ConsoleTools.input("角色名称：");
        if (name.isEmpty()) {
            System.out.println("角色名称不能为空！");
            return;
        }
        Role role = new Role();
        role.setName(name);
        role.setIsDeleted(0);
        role.setUpdateBy(currentUserId());
        System.out.println(roleService.save(role) ? "新增成功！" : "新增失败！");
    }

    private static void updateRole() {
        int id = ConsoleTools.inputInt("请输入要修改的角色ID：");
        Role role = roleService.findOne(id);
        if (role == null) {
            System.out.println("角色不存在！");
            return;
        }
        String name = ConsoleTools.keep("角色名称(" + role.getName() + ")：", role.getName());
        role.setName(name);
        role.setUpdateBy(currentUserId());
        System.out.println(roleService.update(role) ? "修改成功！" : "修改失败！");
    }

    private static void deleteRole() {
        int id = ConsoleTools.inputInt("请输入要删除的角色ID：");
        Role role = roleService.findOne(id);
        if (role == null) {
            System.out.println("角色不存在！");
            return;
        }
        if (Integer.valueOf(ROLE_ADMIN).equals(role.getId())) {
            System.out.println("系统内置管理员角色不能删除！");
            return;
        }
        if (!ConsoleTools.confirm("确认删除角色【" + role.getName() + "】？(y/n)：")) {
            System.out.println("已取消删除！");
            return;
        }
        System.out.println(roleService.delete(id) ? "删除成功！" : "删除失败！");
    }

    private static void assignMenus() {
        int roleId = ConsoleTools.inputInt("请输入角色ID：");
        Role role = roleService.findOne(roleId);
        if (role == null) {
            System.out.println("角色不存在！");
            return;
        }
        List<Menu> allMenus = menuService.findAll();
        if (allMenus.isEmpty()) {
            System.out.println("暂无菜单数据！");
            return;
        }
        List<Integer> selected = roleService.findMenuIdsByRoleId(roleId);
        System.out.println("当前角色【" + role.getName() + "】已拥有菜单标记为 * ：");
        System.out.printf("%-6s%-6s%-14s%-20s%n", "ID", "选择", "菜单标题", "菜单路径");
        for (Menu menu : allMenus) {
            System.out.printf("%-6d%-6s%-14s%-20s%n",
                    menu.getId(),
                    selected.contains(menu.getId()) ? "*" : "",
                    menu.getTitle(),
                    menu.getPath());
        }
        String ids = ConsoleTools.input("请输入要授权的菜单ID（多个用逗号分隔，回车表示清空授权）：");
        List<Integer> menuIds = new ArrayList<>();
        if (!ids.isEmpty()) {
            for (String s : ids.split("[,，]")) {
                String trim = s.trim();
                if (!trim.isEmpty()) {
                    menuIds.add(Integer.valueOf(trim));
                }
            }
        }
        System.out.println(roleService.assignMenus(roleId, menuIds) ? "分配成功！" : "分配失败！");
    }

    private static Integer chooseRole() {
        List<Role> roles = roleService.findAll();
        if (roles.isEmpty()) {
            System.out.println("暂无角色数据，请先维护角色！");
            return null;
        }
        System.out.printf("%-6s%-16s%n", "ID", "角色名");
        for (Role role : roles) {
            System.out.printf("%-6d%-16s%n", role.getId(), role.getName());
        }
        int roleId = ConsoleTools.inputInt("请选择角色ID：");
        if (roleService.findOne(roleId) == null) {
            System.out.println("所选角色不存在！");
            return null;
        }
        return roleId;
    }

    private static String roleName(Integer roleId) {
        if (roleId == null) {
            return "-";
        }
        Role role = roleService.findOne(roleId);
        return role == null ? "-" : role.getName();
    }

    private static String sexText(Integer sex) {
        if (sex == null) {
            return "-";
        }
        return sex == SEX_MALE ? "男" : (sex == SEX_FEMALE ? "女" : "-");
    }

    private static Integer currentUserId() {
        User u = LoginController.getCurrentUser();
        return u == null ? null : u.getId();
    }

}
