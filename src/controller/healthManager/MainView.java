package controller.healthManager;
import controller.BedView;
import java.util.Scanner;

/**
 * 管理员主菜单：5 个主模块。
 */
public class MainView {

    public static void menu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== 东软颐养中心管理系统 =====");
            System.out.println("1. 客户管理");
            System.out.println("2. 床位管理");
            System.out.println("3. 护理管理");
            System.out.println("4. 健康管家");
            System.out.println("5. 用户管理");
            System.out.println("0. 退出");
            System.out.print("请选择：");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": MainController.customerMenu(); break;
                    case "2": BedView.menu(); break;
                    case "3":
                        System.out.println("【护理管理模块】待组员补充入口");
                        break;
                    case "4": MainController.healthNurseMenu(); break;
                    case "5": MainController.userMenu(); break;
                    case "0":
                        System.out.println("已退出系统。");
                        System.exit(0);
                    default:
                        System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }
}