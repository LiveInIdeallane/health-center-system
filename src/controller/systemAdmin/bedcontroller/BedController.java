package controller.systemAdmin.bedcontroller;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.bedcontroller.bedmanage.BedManageController;
import controller.systemAdmin.bedcontroller.bedmap.BedMapController;

public class BedController {

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("床位管理");
            System.out.println("1、床位示意图");
            System.out.println("2、床位管理");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        BedMapController.show();
                        break;
                    case "2":
                        BedManageController.menu();
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
