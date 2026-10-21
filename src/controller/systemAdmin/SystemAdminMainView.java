package controller.systemAdmin;

import controller.systemAdmin.bedcontroller.BedController;
import controller.systemAdmin.customercontroller.CustomerController;
import controller.systemAdmin.managercontroller.ManagerController;
import controller.systemAdmin.nursecontroller.NurseController;
import controller.systemAdmin.usercontroller.UserController;

/**
 * 主界面 Controller：登录成功后的主菜单，只做跳转。
 */
public class SystemAdminMainView {

    public static void menu() {
        while (true) {
            System.out.println("********************************");
            System.out.println("      东软颐养中心管理系统");
            System.out.println("********************************");
            System.out.println("1、客户管理");
            System.out.println("2、用户管理");
            System.out.println("3、床位管理");
            System.out.println("4、护理管理");
            System.out.println("5、健康管家");
            System.out.println("0、退出系统");
            String select = ConsoleTools.input("请输入序号：");
            switch (select) {
                case "1":
                    CustomerController.menu();
                    break;
                case "2":
                    UserController.menu();
                    break;
                case "3":
                    BedController.menu();
                    break;
                case "4":
                    NurseController.menu();
                    break;
                case "5":
                    ManagerController.menu();
                    break;
                case "0":
                    System.out.println("感谢使用，再见！");
                    System.exit(0);
                    break;
                default:
                    System.out.println("输入有误，请重新输入！");
            }
        }
    }

}
