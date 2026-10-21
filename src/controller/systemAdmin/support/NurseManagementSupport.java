package controller.systemAdmin.support;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.support.CustomerManagementSupport;
import entity.CustomerS.Customer;
import entity.NursecontentS.Nursecontent;
import entity.NurselevelS.Nurselevel;
import service.CustomerS.CustomerService;
import service.CustomerS.CustomerServiceImpl;
import service.NursecontentS.Nursecontentservice;
import service.NursecontentS.NursecontentserviceImpl;
import service.NurselevelS.Nurselevelservice;
import service.NurselevelS.NurselevelserviceImpl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NurseManagementSupport {

    public static final int STATUS_ON = 1;
    public static final int STATUS_OFF = 2;
    public static final int BUY_MONTHS = 3;

    private static final Nurselevelservice nurselevelService = new NurselevelserviceImpl();
    private static final Nursecontentservice nursecontentService = new NursecontentserviceImpl();
    private static final CustomerService customerService = new CustomerServiceImpl();

    public static Integer inputStatus(String tip) {
        String s = ConsoleTools.input(tip);
        if ("1".equals(s)) {
            return STATUS_ON;
        }
        if ("2".equals(s)) {
            return STATUS_OFF;
        }
        return null;
    }

    public static Integer inputStatusFilter(String tip) {
        String s = ConsoleTools.input(tip);
        if (s.isEmpty()) {
            return STATUS_ON;
        }
        if ("0".equals(s)) {
            return null;
        }
        if ("1".equals(s)) {
            return STATUS_ON;
        }
        if ("2".equals(s)) {
            return STATUS_OFF;
        }
        return STATUS_ON;
    }

    public static void printContents(List<Nursecontent> list) {
        System.out.printf("%-5s%-10s%-16s%-10s%-8s%-12s%-10s%-10s%n",
                "ID", "编号", "名称", "价格", "状态", "执行周期", "执行次数", "描述");
        for (Nursecontent content : list) {
            System.out.printf("%-5d%-10s%-16s%-10s%-8s%-12s%-10s%-10s%n",
                    content.getId(),
                    content.getSerial_number(),
                    content.getNursing_name(),
                    content.getService_price(),
                    ConsoleTools.statusText(content.getStatus(), "启用", "停用"),
                    content.getExecution_cycle(),
                    content.getExecution_times(),
                    content.getMessage());
        }
    }

    public static List<Nursecontent> enabledContents() throws Exception {
        List<Nursecontent> result = new ArrayList<>();
        for (Nursecontent content : nursecontentService.findAllNursecontent()) {
            if ((content.getIs_deleted() == null || content.getIs_deleted() == 0)
                    && Integer.valueOf(STATUS_ON).equals(content.getStatus())) {
                result.add(content);
            }
        }
        return result;
    }

    public static Date plusMonths(Date date, int months) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(localDate.plusMonths(months).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static void listCustomers() throws Exception {
        String name = ConsoleTools.input("客户姓名（模糊，回车不过滤）：");
        List<Integer> nurseIds = CustomerManagementSupport.nurseCustomerIds();
        List<Customer> result = new ArrayList<>();
        for (Customer c : customerService.findAllCustomers()) {
            if (c.getIs_deleted() != null && c.getIs_deleted() != 0) {
                continue;
            }
            if (!name.isEmpty() && (c.getCustomer_name() == null
                    || !c.getCustomer_name().contains(name))) {
                continue;
            }
            result.add(c);
        }
        if (result.isEmpty()) {
            System.out.println("没有符合条件的客户！");
            return;
        }
        System.out.printf("%-5s%-10s%-12s%-14s%-8s%n", "ID", "姓名", "护理级别ID", "护理级别", "老人类型");
        for (Customer c : result) {
            String levelName = "-";
            if (c.getLevel_id() != null) {
                Nurselevel level = nurselevelService.findNurselevelById(c.getLevel_id());
                levelName = level == null ? "-" : level.getLevel_name();
            }
            System.out.printf("%-5d%-10s%-12s%-14s%-8s%n",
                    c.getId(),
                    c.getCustomer_name(),
                    c.getLevel_id() == null ? "-" : c.getLevel_id(),
                    levelName,
                    nurseIds.contains(c.getId()) ? "护理老人" : "自理老人");
        }
    }
}
