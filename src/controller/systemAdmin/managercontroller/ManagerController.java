package controller.systemAdmin.managercontroller;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.managercontroller.serviceattention.ServiceAttentionController;
import controller.systemAdmin.managercontroller.servicetargetset.ServiceTargetSetController;

public class ManagerController {

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("健康管家");
            System.out.println("1、设置服务对象");
            System.out.println("2、服务关注");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        ServiceTargetSetController.menu();
                        break;
                    case "2":
                        ServiceAttentionController.menu();
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
