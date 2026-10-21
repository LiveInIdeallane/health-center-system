package controller.systemAdmin.customercontroller.outward;

import controller.LoginController;
import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.support.CustomerManagementSupport;
import entity.CustomerS.Customer;
import entity.OutwardS.Outward;
import service.BedS.BedService;
import service.BedS.BedServiceImpl;
import service.CustomerS.CustomerService;
import service.CustomerS.CustomerServiceImpl;
import service.OutwardS.OutwardService;
import service.OutwardS.OutwardServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OutwardController {

    private static final OutwardService outwardService = new OutwardServiceImpl();
    private static final CustomerService customerService = new CustomerServiceImpl();
    private static final BedService bedService = new BedServiceImpl();

    private static final int AUDIT_PASS = 1;
    private static final int AUDIT_REJECT = 2;
    private static final int BED_STATUS_OUT = 3;

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("外出登记");
            System.out.println("1、浏览外出登记信息");
            System.out.println("2、按客户姓名查询");
            System.out.println("3、审核外出申请");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        printOutwards(outwardService.GetOutwards());
                        break;
                    case "2":
                        searchByName();
                        break;
                    case "3":
                        auditOutward();
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
        List<Outward> filtered = new ArrayList<>();
        for (Outward o : outwardService.GetOutwards()) {
            String cName = CustomerManagementSupport.customerName(o.getCustomer_id());
            if (cName.contains(name)) {
                filtered.add(o);
            }
        }
        printOutwards(filtered);
    }

    private static void printOutwards(List<Outward> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("暂无外出登记信息！");
            return;
        }
        System.out.printf("%-5s%-10s%-12s%-12s%-12s%-16s%-8s%-10s%-12s%n",
                "ID", "客户ID", "客户姓名", "外出时间", "预计返回", "外出事由", "状态", "审批人", "审批时间");
        for (Outward o : list) {
            System.out.printf("%-5d%-10d%-12s%-12s%-12s%-16s%-8s%-10s%-12s%n",
                    o.getId(),
                    o.getCustomer_id(),
                    CustomerManagementSupport.customerName(o.getCustomer_id()),
                    ConsoleTools.fmt(o.getOutgoingtime()),
                    ConsoleTools.fmt(o.getExpectedreturntime()),
                    o.getOutgoingreason(),
                    ConsoleTools.auditText(o.getAuditstatus(), AUDIT_PASS),
                    o.getAuditperson(),
                    ConsoleTools.fmt(o.getAudittime()));
        }
    }

    private static void auditOutward() {
        int id = ConsoleTools.inputInt("请输入要审核的外出登记ID：");
        Outward o = outwardService.GetOutwardById(id);
        if (o == null) {
            System.out.println("外出登记不存在！");
            return;
        }
        if (o.getAuditstatus() != null && o.getAuditstatus() != 0) {
            System.out.println("该申请已审核，不能重复审核！");
            return;
        }
        System.out.println("1、通过    2、不通过");
        int result = ConsoleTools.inputInt("请选择审核结果：");
        if (result != AUDIT_PASS && result != AUDIT_REJECT) {
            System.out.println("输入有误！");
            return;
        }
        o.setAuditstatus(result);
        o.setAuditperson(LoginController.currentUserName());
        o.setAudittime(new Date());

        if (!outwardService.UpdateOutward(o)) {
            System.out.println("审核失败！");
            return;
        }
        if (result == AUDIT_PASS) {
            Customer c = customerService.findCustomerById(o.getCustomer_id());
            if (c != null && c.getBed_id() != null) {
                bedService.changeBedStatus(c.getBed_id(), BED_STATUS_OUT);
            }
        }
        System.out.println("审核完成！");
    }

}
