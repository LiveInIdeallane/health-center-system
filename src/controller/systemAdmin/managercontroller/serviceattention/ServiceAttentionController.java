package controller.systemAdmin.managercontroller.serviceattention;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.support.HealthManagerSupport;
import entity.CustomernurseitemS.Customernurseitem;
import entity.CustomerS.Customer;
import entity.NursecontentS.Nursecontent;
import service.CustomernurseitemS.CustomernurseitemService;
import service.CustomernurseitemS.CustomernurseitemServiceImpl;
import service.CustomerS.CustomerService;
import service.CustomerS.CustomerServiceImpl;
import service.NursecontentS.Nursecontentservice;
import service.NursecontentS.NursecontentserviceImpl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceAttentionController {

    private static final CustomerService customerService = new CustomerServiceImpl();
    private static final CustomernurseitemService nurseItemService = new CustomernurseitemServiceImpl();
    private static final Nursecontentservice nursecontentService = new NursecontentserviceImpl();

    private static final int EXPIRE_SOON_DAYS = 30;

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("服务关注");
            System.out.println("1、查询客户信息列表");
            System.out.println("2、查看客户已购护理服务");
            System.out.println("3、续费护理项目");
            System.out.println("4、移除护理项目");
            System.out.println("5、购买护理项目");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        listCustomers();
                        break;
                    case "2":
                        listPurchased();
                        break;
                    case "3":
                        renewItem();
                        break;
                    case "4":
                        removeItem();
                        break;
                    case "5":
                        buyItems();
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

    private static void listCustomers() {
        String name = ConsoleTools.input("客户姓名（模糊，回车不过滤）：");
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
        HealthManagerSupport.printCustomers(result);
    }

    private static void listPurchased() throws Exception {
        int customerId = ConsoleTools.inputInt("请输入客户ID：");
        Customer customer = customerService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("客户不存在！");
            return;
        }
        List<Customernurseitem> items = purchasedItems(customerId);
        if (items.isEmpty()) {
            System.out.println("该客户暂未购买护理服务！");
            return;
        }
        printPurchased(items);
    }

    private static void renewItem() throws Exception {
        int id = ConsoleTools.inputInt("请输入要续费的护理项目记录ID：");
        Customernurseitem item = nurseItemService.findCustomernurseitemById(id);
        if (item == null) {
            System.out.println("该护理项目记录不存在！");
            return;
        }
        printPurchased(singletonList(item));
        int add = ConsoleTools.inputInt("新增数量：");
        if (add <= 0) {
            System.out.println("新增数量必须大于0！");
            return;
        }
        Date maturity = ConsoleTools.parseDate(ConsoleTools.input("服务到期时间(yyyy-MM-dd)："));
        if (maturity == null) {
            System.out.println("服务到期时间格式错误！");
            return;
        }
        item.setNurse_number((item.getNurse_number() == null ? 0 : item.getNurse_number()) + add);
        item.setMaturity_time(maturity);
        System.out.println(nurseItemService.updateCustomernurseitem(item) ? "续费成功！" : "续费失败！");
    }

    private static void removeItem() throws Exception {
        int id = ConsoleTools.inputInt("请输入要移除的护理项目记录ID：");
        Customernurseitem item = nurseItemService.findCustomernurseitemById(id);
        if (item == null) {
            System.out.println("该护理项目记录不存在！");
            return;
        }
        printPurchased(singletonList(item));
        if (!ConsoleTools.confirm("移除后客户将不再享有该服务，确认移除？(y/n)：")) {
            System.out.println("已取消移除！");
            return;
        }
        item.setIs_deleted(1);
        System.out.println(nurseItemService.updateCustomernurseitem(item) ? "移除成功！" : "移除失败！");
    }

    private static void buyItems() throws Exception {
        int customerId = ConsoleTools.inputInt("请输入客户ID：");
        Customer customer = customerService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("客户不存在！");
            return;
        }
        List<Integer> owned = new ArrayList<>();
        for (Customernurseitem item : purchasedItems(customerId)) {
            owned.add(item.getItem_id());
        }
        String name = ConsoleTools.input("护理项目名称（模糊，回车不过滤）：");
        List<Nursecontent> candidates = new ArrayList<>();
        for (Nursecontent content : nursecontentService.findAllNursecontent()) {
            if (content.getIs_deleted() != null && content.getIs_deleted() != 0) {
                continue;
            }
            if (content.getStatus() == null || content.getStatus() != 1) {
                continue;
            }
            if (owned.contains(content.getId())) {
                continue;
            }
            if (!name.isEmpty() && (content.getNursing_name() == null
                    || !content.getNursing_name().contains(name))) {
                continue;
            }
            candidates.add(content);
        }
        if (candidates.isEmpty()) {
            System.out.println("没有可购买的护理项目！");
            return;
        }
        System.out.printf("%-5s%-10s%-16s%-10s%n", "ID", "编号", "名称", "价格");
        for (Nursecontent content : candidates) {
            System.out.printf("%-5d%-10s%-16s%-10s%n",
                    content.getId(),
                    content.getSerial_number(),
                    content.getNursing_name(),
                    content.getService_price());
        }
        String ids = ConsoleTools.input("请选择护理项目ID（多个用逗号分隔）：");
        if (ids.isEmpty()) {
            System.out.println("未选择护理项目！");
            return;
        }

        Date today = new Date();
        int added = 0;
        for (String s : ids.split("[,，]")) {
            String trim = s.trim();
            if (trim.isEmpty()) {
                continue;
            }
            Nursecontent selected = null;
            for (Nursecontent content : candidates) {
                if (content.getId() != null && content.getId() == Integer.parseInt(trim)) {
                    selected = content;
                    break;
                }
            }
            if (selected == null) {
                System.out.println("护理项目ID " + trim + " 不可购买，已跳过！");
                continue;
            }
            int count = inputCount("【" + selected.getNursing_name() + "】购买数量(回车默认1)：");
            Date maturity = ConsoleTools.parseDate(ConsoleTools.input(
                    "【" + selected.getNursing_name() + "】服务到期时间(yyyy-MM-dd，回车默认今天)："));
            if (maturity == null) {
                maturity = today;
            }
            Customernurseitem item = new Customernurseitem();
            item.setItem_id(selected.getId());
            item.setCustomer_id(customerId);
            item.setLevel_id(customer.getLevel_id());
            item.setNurse_number(count <= 0 ? 1 : count);
            item.setIs_deleted(0);
            item.setBuy_time(today);
            item.setMaturity_time(maturity);
            if (nurseItemService.addCustomernurseitem(item)) {
                added++;
            }
        }
        System.out.println("购买完成，本次新增 " + added + " 个护理服务！");
    }

    private static List<Customernurseitem> purchasedItems(Integer customerId) throws Exception {
        List<Customernurseitem> result = new ArrayList<>();
        for (Customernurseitem item : nurseItemService.findAllCustomernurseitem()) {
            if (Integer.valueOf(customerId).equals(item.getCustomer_id())
                    && (item.getIs_deleted() == null || item.getIs_deleted() == 0)) {
                result.add(item);
            }
        }
        return result;
    }

    private static void printPurchased(List<Customernurseitem> items) throws Exception {
        System.out.printf("%-5s%-16s%-8s%-12s%-12s%-10s%n",
                "记录ID", "护理项目", "数量", "购买日期", "到期日期", "状态");
        for (Customernurseitem item : items) {
            Nursecontent content = nursecontentService.findNursecontentByid(item.getItem_id());
            System.out.printf("%-5d%-16s%-8s%-12s%-12s%-10s%n",
                    item.getId(),
                    content == null ? "-" : content.getNursing_name(),
                    item.getNurse_number() == null ? "-" : item.getNurse_number(),
                    ConsoleTools.fmt(item.getBuy_time()),
                    ConsoleTools.fmt(item.getMaturity_time()),
                    focusStatus(item));
        }
    }

    private static String focusStatus(Customernurseitem item) {
        Date maturity = item.getMaturity_time();
        if (maturity != null && maturity.before(new Date())) {
            return "到期";
        }
        if (item.getNurse_number() == null || item.getNurse_number() <= 0) {
            return "欠费";
        }
        if (maturity != null && daysBetween(new Date(), maturity) <= EXPIRE_SOON_DAYS) {
            return "未到期";
        }
        return "数量正常";
    }

    private static long daysBetween(Date from, Date to) {
        LocalDate a = java.time.Instant.ofEpochMilli(from.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate b = java.time.Instant.ofEpochMilli(to.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(a, b);
    }

    private static int inputCount(String tip) {
        String s = ConsoleTools.input(tip);
        if (s.isEmpty()) {
            return 1;
        }
        try {
            int n = Integer.parseInt(s);
            return n <= 0 ? 1 : n;
        } catch (NumberFormatException e) {
            System.out.println("数量输入有误，按1计算！");
            return 1;
        }
    }

    private static List<Customernurseitem> singletonList(Customernurseitem item) {
        List<Customernurseitem> list = new ArrayList<>();
        list.add(item);
        return list;
    }
}
