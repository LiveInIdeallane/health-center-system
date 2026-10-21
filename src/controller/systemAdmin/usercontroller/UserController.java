package controller.systemAdmin.usercontroller;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.usercontroller.datamaintenance.DataMaintenanceController;

public class UserController {

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("用户管理");
            System.out.println("1、基础数据维护");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        DataMaintenanceController.menu();
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
}
