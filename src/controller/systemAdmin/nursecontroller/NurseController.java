package controller.systemAdmin.nursecontroller;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.nursecontroller.customernurseset.CustomerNurseSetController;
import controller.systemAdmin.nursecontroller.nurseitem.NurseItemController;
import controller.systemAdmin.nursecontroller.nurselevel.NurseLevelController;
import controller.systemAdmin.nursecontroller.nurserecord.NurseRecordController;

public class NurseController {

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("护理管理");
            System.out.println("1、护理级别");
            System.out.println("2、护理项目");
            System.out.println("3、客户护理设置");
            System.out.println("4、护理记录");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        NurseLevelController.menu();
                        break;
                    case "2":
                        NurseItemController.menu();
                        break;
                    case "3":
                        CustomerNurseSetController.menu();
                        break;
                    case "4":
                        NurseRecordController.menu();
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
