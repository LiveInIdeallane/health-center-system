package controller.systemAdmin.customercontroller.backdown;

import controller.LoginController;
import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.support.CustomerManagementSupport;
import entity.BackdownS.Backdown;
import entity.CustomerS.Customer;
import service.BackdownS.BackdownService;
import service.BackdownS.BackdownServiceImpl;
import service.BedS.BedService;
import service.BedS.BedServiceImpl;
import service.CustomerS.CustomerService;
import service.CustomerS.CustomerServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class BackdownController {

    private static final BackdownService backdownService = new BackdownServiceImpl();
    private static final CustomerService customerService = new CustomerServiceImpl();
    private static final BedService bedService = new BedServiceImpl();

    private static final int RETREAT_NORMAL = 1;
    private static final int RETREAT_DEATH = 2;
    private static final int RETREAT_KEEP_BED = 3;
    private static final int AUDIT_PASS = 1;
    private static final int AUDIT_REJECT = 2;

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("退住登记");
            System.out.println("1、浏览退住登记信息");
            System.out.println("2、按客户姓名查询");
            System.out.println("3、审核退住申请");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        printBackdowns(backdownService.findAllBackdowns());
                        break;
                    case "2":
                        searchByName();
                        break;
                    case "3":
                        auditBackdown();
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

    private static void searchByName() {
        String name = ConsoleTools.input("客户姓名（模糊）：");
        List<Backdown> filtered = new ArrayList<>();
        for (Backdown b : backdownService.findAllBackdowns()) {
            String cName = CustomerManagementSupport.customerName(b.getCustomer_id());
            if (cName.contains(name)) {
                filtered.add(b);
            }
        }
        printBackdowns(filtered);
    }

    private static void printBackdowns(List<Backdown> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("暂无退住登记信息！");
            return;
        }
        System.out.printf("%-5s%-10s%-12s%-10s%-16s%-8s%-10s%-12s%n",
                "ID", "客户ID", "客户姓名", "退住时间", "退住类型", "状态", "审批人", "审批时间");
        for (Backdown b : list) {
            System.out.printf("%-5d%-10d%-12s%-10s%-16s%-8s%-10s%-12s%n",
                    b.getId(),
                    b.getCustomer_id(),
                    CustomerManagementSupport.customerName(b.getCustomer_id()),
                    ConsoleTools.fmt(b.getRetreattime()),
                    retreatTypeText(b.getRetreattype()),
                    ConsoleTools.auditText(b.getAuditstatus(), AUDIT_PASS),
                    b.getAuditperson(),
                    ConsoleTools.fmt(b.getAudittime()));
        }
    }

    private static void auditBackdown() {
        int id = ConsoleTools.inputInt("请输入要审核的退住登记ID：");
        Backdown b = backdownService.findBackdownById(id);
        if (b == null) {
            System.out.println("退住登记不存在！");
            return;
        }
        if (b.getAuditstatus() != null && b.getAuditstatus() != 0) {
            System.out.println("该申请已审核，不能重复审核！");
            return;
        }
        System.out.println("1、通过    2、不通过");
        int result = ConsoleTools.inputInt("请选择审核结果：");
        if (result != AUDIT_PASS && result != AUDIT_REJECT) {
            System.out.println("输入有误！");
            return;
        }
        b.setAuditstatus(result);
        b.setAuditperson(LoginController.currentUserName());
        b.setAudittime(new Date());

        if (!backdownService.updateBackdown(b)) {
            System.out.println("审核失败！");
            return;
        }
        if (result == AUDIT_PASS
                && (Objects.equals(b.getRetreattype(), RETREAT_NORMAL)
                || Objects.equals(b.getRetreattype(), RETREAT_DEATH))) {
            Customer c = customerService.findCustomerById(b.getCustomer_id());
            if (c != null && c.getBed_id() != null) {
                bedService.changeBedStatus(c.getBed_id(), 1);
            }
        }
        System.out.println("审核完成！");
    }

    private static String retreatTypeText(Integer type) {
        if (type == null) {
            return "";
        }
        switch (type) {
            case RETREAT_NORMAL:
                return "正常退住";
            case RETREAT_DEATH:
                return "死亡退住";
            case RETREAT_KEEP_BED:
                return "保留床位";
            default:
                return "未知";
        }
    }

}
