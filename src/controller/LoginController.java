package controller;

import controller.systemAdmin.SystemAdminMainView;
import controller.healthManager.NurseView;
import entity.UserS.User;
import service.UserS.UserService;
import service.UserS.UserServiceImpl;
import utils.Param;


import java.util.Scanner;

/**
 * 登录 Controller：只负责接收账号密码、调用 service 验证、记录当前登录人。
 * 业务校验（账号是否存在、是否冻结、密码是否正确）在 UserServiceImpl 中。
 */
public class LoginController {

    private static  Scanner scanner = new Scanner(System.in);
    private static  UserService userService = new UserServiceImpl();
    private static final int MAX_ATTEMPT = 3;

    //当前登录用户，退住/外出审核时作为审批人
    private static User currentUser;


    //这里的登录添加了老师没有的验证登录次数的限制
    public static void login() {
        for (int i = 1; i <= MAX_ATTEMPT; i++) {
            System.out.println("=========== 东软颐养中心管理系统 ===========");
            System.out.print("请输入登录编码：");
            String username = scanner.nextLine();
            System.out.print("请输入登录密码：");
            String password = scanner.nextLine();

            try {
                User user = userService.login(username, password);
                currentUser = user;
                Param.loggingUser = user;
                System.out.println("登录成功！欢迎您，" + user.getNickname());
                //获取用户类型进行跳转到不同类型用户的主页菜单
                Integer roleid = currentUser.getRoleId();
                if (roleid == 1) {
                    SystemAdminMainView.menu();
                }else if (roleid == 2) {
                    NurseView.nurseMenu();
                }
                return;
            } catch (Exception e) {
                System.out.println("登录失败：" + e.getMessage()
                        + "（还可尝试 " + (MAX_ATTEMPT - i) + " 次）");
            }
        }
        System.out.println("失败次数过多，系统退出！");
        System.exit(0);
    }



    //调用此方法从login获得用户类型，在MenuView中先调用此方法获得用户类型再根据用户类型写死菜单
    public static User getCurrentUser() {
        return currentUser;
    }



    //这个是ai给出来的目前没看出有什么用，先放这里吧
    public static String currentUserName() {
        User u = currentUser;
        if (u == null) {
            return "system";
        }
        return u.getNickname() == null ? u.getUsername() : u.getNickname();
    }



}
