package controller.systemAdmin.nursecontroller.customernurseset;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.support.NurseManagementSupport;
import entity.CustomernurseitemS.Customernurseitem;
import entity.CustomerS.Customer;
import entity.NursecontentS.Nursecontent;
import entity.NurselevelS.Nurselevel;
import entity.NurselevelitemS.Nurselevelitem;
import service.CustomernurseitemS.CustomernurseitemService;
import service.CustomernurseitemS.CustomernurseitemServiceImpl;
import service.CustomerS.CustomerService;
import service.CustomerS.CustomerServiceImpl;
import service.NursecontentS.Nursecontentservice;
import service.NursecontentS.NursecontentserviceImpl;
import service.NurselevelS.Nurselevelservice;
import service.NurselevelS.NurselevelserviceImpl;
import service.NurselevelitemS.Nurselevelitemservice;
import service.NurselevelitemS.NurselevelitemserviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerNurseSetController {

    private static final Nurselevelservice nurselevelService = new NurselevelserviceImpl();
    private static final Nursecontentservice nursecontentService = new NursecontentserviceImpl();
    private static final Nurselevelitemservice nurselevelitemService = new NurselevelitemserviceImpl();
    private static final CustomernurseitemService customernurseitemService = new CustomernurseitemServiceImpl();
    private static final CustomerService customerService = new CustomerServiceImpl();

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("客户护理设置");
            System.out.println("1、查询客户信息列表");
            System.out.println("2、设置护理级别");
            System.out.println("3、移除护理级别");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        NurseManagementSupport.listCustomers();
                        break;
                    case "2":
                        setCustomerLevel();
                        break;
                    case "3":
                        removeCustomerLevel();
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

    private static void setCustomerLevel() throws Exception {
        int customerId = ConsoleTools.inputInt("请输入客户ID：");
        Customer customer = customerService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("客户不存在！");
            return;
        }
        if (customer.getLevel_id() != null) {
            System.out.println("该客户已有护理级别，请先移除护理级别！");
            return;
        }
        List<Nurselevel> levels = new ArrayList<>();
        for (Nurselevel level : nurselevelService.findAllNurselevel()) {
            if ((level.getIs_deleted() == null || level.getIs_deleted() == 0)
                    && Integer.valueOf(NurseManagementSupport.STATUS_ON).equals(level.getLevel_status())) {
                levels.add(level);
            }
        }
        if (levels.isEmpty()) {
            System.out.println("暂无启用的护理级别！");
            return;
        }
        System.out.printf("%-5s%-16s%-10s%n", "ID", "护理级别", "状态");
        for (Nurselevel level : levels) {
            System.out.printf("%-5d%-16s%-10s%n",
                    level.getId(),
                    level.getLevel_name(),
                    ConsoleTools.statusText(level.getLevel_status(), "启用", "停用"));
        }
        int levelId = ConsoleTools.inputInt("请选择护理级别ID：");
        Nurselevel selected = null;
        for (Nurselevel level : levels) {
            if (level.getId() != null && level.getId() == levelId) {
                selected = level;
                break;
            }
        }
        if (selected == null) {
            System.out.println("所选护理级别不可用！");
            return;
        }
        List<Nursecontent> items = levelContents(levelId);
        if (items.isEmpty()) {
            System.out.println("该护理级别尚未配置护理项目！");
            return;
        }
        System.out.println("该级别包含的护理项目：");
        NurseManagementSupport.printContents(items);
        if (!ConsoleTools.confirm("确认给客户【" + customer.getCustomer_name() + "】设置该护理级别？(y/n)：")) {
            System.out.println("已取消设置！");
            return;
        }

        Date buyTime = new Date();
        Date maturity = NurseManagementSupport.plusMonths(buyTime, NurseManagementSupport.BUY_MONTHS);
        int added = 0;
        for (Nursecontent content : items) {
            Customernurseitem item = new Customernurseitem();
            item.setItem_id(content.getId());
            item.setCustomer_id(customerId);
            item.setLevel_id(levelId);
            item.setNurse_number(1);
            item.setIs_deleted(0);
            item.setBuy_time(buyTime);
            item.setMaturity_time(maturity);
            if (customernurseitemService.addCustomernurseitem(item)) {
                added++;
            }
        }
        customer.setLevel_id(levelId);
        if (!customerService.updateCustomer(customer)) {
            System.out.println("护理项目已添加，但客户护理级别更新失败！");
            return;
        }
        System.out.println("设置成功，共添加 " + added + " 个护理项目！");
    }

    private static void removeCustomerLevel() throws Exception {
        int customerId = ConsoleTools.inputInt("请输入客户ID：");
        Customer customer = customerService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("客户不存在！");
            return;
        }
        if (customer.getLevel_id() == null) {
            System.out.println("该客户没有护理级别，无法移除！");
            return;
        }
        int levelId = customer.getLevel_id();
        if (!ConsoleTools.confirm("移除将同时删除该客户当前级别的护理项目，确认移除？(y/n)：")) {
            System.out.println("已取消移除！");
            return;
        }
        int removed = 0;
        for (Customernurseitem item : customernurseitemService.findAllCustomernurseitem()) {
            if (Integer.valueOf(customerId).equals(item.getCustomer_id())
                    && Integer.valueOf(levelId).equals(item.getLevel_id())
                    && (item.getIs_deleted() == null || item.getIs_deleted() == 0)) {
                if (customernurseitemService.deleteCustomernurseitem(item.getId())) {
                    removed++;
                }
            }
        }
        customer.setLevel_id(null);
        if (!customerService.updateCustomer(customer)) {
            System.out.println("护理项目已删除，但客户护理级别更新失败！");
            return;
        }
        System.out.println("移除成功，共删除 " + removed + " 个护理项目！");
    }

    private static List<Nursecontent> levelContents(int levelId) throws Exception {
        List<Integer> itemIds = new ArrayList<>();
        for (Nurselevelitem item : nurselevelitemService.findAllNurselevelitem()) {
            if (Integer.valueOf(levelId).equals(item.getLevel_id())) {
                itemIds.add(item.getItem_id());
            }
        }
        List<Nursecontent> result = new ArrayList<>();
        for (Nursecontent content : nursecontentService.findAllNursecontent()) {
            if ((content.getIs_deleted() == null || content.getIs_deleted() == 0)
                    && itemIds.contains(content.getId())) {
                result.add(content);
            }
        }
        return result;
    }
}
