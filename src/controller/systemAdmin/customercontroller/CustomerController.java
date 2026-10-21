package controller.systemAdmin.customercontroller;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.customercontroller.backdown.BackdownController;
import controller.systemAdmin.customercontroller.checkin.CheckinController;
import controller.systemAdmin.customercontroller.outward.OutwardController;

/**
 * 客户管理 Controller（控制台）。
 * 菜单：1入住登记 2退住登记 3外出登记，具体实现分别在 checkin、backdown、outward 子包中。
 *
 * 分层约定：controller 只做界面交互、收集并校验参数、调用 service、展示结果；
 * 业务规则（释放床位、改床位状态、逻辑删除等）全部放在 service 层，controller 不写 SQL。
 */
public class CustomerController {

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("客户管理");
            System.out.println("1、入住登记");
            System.out.println("2、退住登记");
            System.out.println("3、外出登记");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            switch (select) {
                case "1":
                    CheckinController.menu();
                    break;
                case "2":
                    BackdownController.menu();
                    break;
                case "3":
                    OutwardController.menu();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("输入有误，请重新输入！");
            }
        }
    }

}
