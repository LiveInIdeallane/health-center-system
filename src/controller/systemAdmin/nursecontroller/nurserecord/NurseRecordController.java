package controller.systemAdmin.nursecontroller.nurserecord;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.support.NurseManagementSupport;
import entity.CustomerS.Customer;
import entity.NursecontentS.Nursecontent;
import entity.NurserecordS.Nurserecord;
import service.CustomerS.CustomerService;
import service.CustomerS.CustomerServiceImpl;
import service.NursecontentS.Nursecontentservice;
import service.NursecontentS.NursecontentserviceImpl;
import service.NurserecordS.Nurserecordservice;
import service.NurserecordS.NurserecordserviceImpl;

import java.util.ArrayList;
import java.util.List;

public class NurseRecordController {

    private static final Nursecontentservice nursecontentService = new NursecontentserviceImpl();
    private static final Nurserecordservice nurserecordService = new NurserecordserviceImpl();
    private static final CustomerService customerService = new CustomerServiceImpl();

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("护理记录");
            System.out.println("1、查询客户信息列表");
            System.out.println("2、查询客户护理记录");
            System.out.println("3、移除护理记录");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        NurseManagementSupport.listCustomers();
                        break;
                    case "2":
                        listRecords();
                        break;
                    case "3":
                        removeRecord();
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

    private static void listRecords() throws Exception {
        int customerId = ConsoleTools.inputInt("请输入客户ID：");
        Customer customer = customerService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("客户不存在！");
            return;
        }
        List<Nurserecord> records = new ArrayList<>();
        for (Nurserecord record : nurserecordService.findAllNurserecord()) {
            if (Integer.valueOf(customerId).equals(record.getCustomer_id())
                    && (record.getIs_deleted() == null || record.getIs_deleted() == 0)) {
                records.add(record);
            }
        }
        if (records.isEmpty()) {
            System.out.println("该客户暂无护理记录！");
            return;
        }
        System.out.printf("%-5s%-16s%-20s%-8s%-10s%-10s%n",
                "ID", "护理项目", "护理时间", "数量", "护理人ID", "内容");
        for (Nurserecord record : records) {
            Nursecontent content = nursecontentService.findNursecontentByid(record.getItem_id());
            System.out.printf("%-5d%-16s%-20s%-8d%-10s%-10s%n",
                    record.getId(),
                    content == null ? "-" : content.getNursing_name(),
                    ConsoleTools.fmt(record.getNursing_time()),
                    record.getNursing_count(),
                    record.getUser_id() == null ? "-" : record.getUser_id(),
                    record.getNursing_content());
        }
    }

    private static void removeRecord() throws Exception {
        int id = ConsoleTools.inputInt("请输入要移除的护理记录ID：");
        Nurserecord record = nurserecordService.findnurserecordById(id);
        if (record == null) {
            System.out.println("护理记录不存在！");
            return;
        }
        if (!ConsoleTools.confirm("确认移除该护理记录？(y/n)：")) {
            System.out.println("已取消移除！");
            return;
        }
        record.setIs_deleted(1);
        System.out.println(nurserecordService.updateNurserecord(record) ? "移除成功！" : "移除失败！");
    }
}
