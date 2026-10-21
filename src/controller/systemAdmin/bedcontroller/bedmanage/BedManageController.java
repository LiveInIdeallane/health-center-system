package controller.systemAdmin.bedcontroller.bedmanage;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.support.CustomerManagementSupport;
import entity.BedDetailsS.BedDetails;
import entity.BedS.Bed;
import entity.CustomerS.Customer;
import service.BedDetailsS.BedDetailsService;
import service.BedDetailsS.BedDetailsServiceImpl;
import service.BedS.BedService;
import service.BedS.BedServiceImpl;
import service.CustomerS.CustomerService;
import service.CustomerS.CustomerServiceImpl;
import service.RoomS.RoomService;
import service.RoomS.RoomServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class BedManageController {

    private static final BedService bedService = new BedServiceImpl();
    private static final BedDetailsService bedDetailsService = new BedDetailsServiceImpl();
    private static final CustomerService customerService = new CustomerServiceImpl();
    private static final RoomService roomService = new RoomServiceImpl();

    private static final String BUILDING_NO = "606";
    private static final int DETAIL_IN_USE = 1;
    private static final int DETAIL_HISTORY = 2;

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("床位管理");
            System.out.println("1、查询床位使用详情");
            System.out.println("2、修改床位详情结束时间");
            System.out.println("3、床位调换");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        searchBedDetails();
                        break;
                    case "2":
                        updateEndDate();
                        break;
                    case "3":
                        changeBed();
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

    private static void searchBedDetails() {
        String name = ConsoleTools.input("客户姓名（模糊，回车不过滤）：");
        String startStr = ConsoleTools.input("入住日期(yyyy-MM-dd，回车不过滤)：");
        Date startDate = ConsoleTools.parseDate(startStr);
        if (!startStr.isEmpty() && startDate == null) {
            System.out.println("入住日期格式错误！");
            return;
        }
        String statusStr = ConsoleTools.input("使用状态(1正在使用 2使用历史，回车默认1)：");
        int status = statusStr.isEmpty() ? DETAIL_IN_USE : Integer.parseInt(statusStr);

        List<BedDetails> all = bedDetailsService.getAllBedDetails();
        Date today = new Date();
        int shown = 0;
        System.out.printf("%-5s%-10s%-12s%-12s%-10s%-12s%-10s%n",
                "ID", "客户ID", "客户姓名", "床位ID", "入住日期", "退住日期", "状态");
        for (BedDetails d : all) {
            if (!name.isEmpty()) {
                String cName = CustomerManagementSupport.customerName(d.getCustomer_id());
                if (!cName.contains(name)) {
                    continue;
                }
            }
            if (startDate != null && !sameDay(d.getStart_date(), startDate)) {
                continue;
            }
            if (!matchStatus(d, status, today)) {
                continue;
            }
            shown++;
            System.out.printf("%-5d%-10d%-12s%-12s%-10s%-12s%-10s%n",
                    d.getId(),
                    d.getCustomer_id(),
                    CustomerManagementSupport.customerName(d.getCustomer_id()),
                    d.getBed_id(),
                    ConsoleTools.fmt(d.getStart_date()),
                    ConsoleTools.fmt(d.getEnd_date()),
                    matchStatus(d, DETAIL_IN_USE, today) ? "正在使用" : "使用历史");
        }
        if (shown == 0) {
            System.out.println("没有符合条件的床位使用详情！");
        }
    }

    private static void updateEndDate() {
        int id = ConsoleTools.inputInt("请输入床位详情ID：");
        BedDetails d = bedDetailsService.getBedDetailsById(id);
        if (d == null) {
            System.out.println("床位详情不存在！");
            return;
        }
        System.out.println("当前详情：" + d);
        Date end = ConsoleTools.parseDate(ConsoleTools.input("结束时间(yyyy-MM-dd)："));
        if (end == null) {
            System.out.println("结束时间格式错误！");
            return;
        }
        if (d.getStart_date() != null && end.before(d.getStart_date())) {
            System.out.println("结束时间不能早于入住时间！");
            return;
        }
        System.out.println(bedDetailsService.updateEndDate(id, end) ? "修改成功！" : "修改失败！");
    }

    private static String floorOf(Map<String, List<String>> rooms, String roomNo) {
        for (Map.Entry<String, List<String>> entry : rooms.entrySet()) {
            if (entry.getValue().contains(roomNo)) {
                return entry.getKey();
            }
        }
        return BUILDING_NO;
    }

    private static void changeBed() {
        int customerId = ConsoleTools.inputInt("请输入要调换床位的客户ID：");
        Customer c = customerService.findCustomerById(customerId);
        if (c == null) {
            System.out.println("客户不存在！");
            return;
        }
        BedDetails active = bedDetailsService.getActiveByCustomerId(customerId);
        if (active == null) {
            System.out.println("该客户没有正在使用的床位！");
            return;
        }
        System.out.println("当前床位：" + c.getBuilding_no() + "栋 " + c.getRoom_no()
                + "房 床位ID=" + active.getBed_id());

        Map<String, List<String>> rooms = roomService.getRoomsGroupByFloor();
        if (rooms.isEmpty()) {
            System.out.println("暂无房间数据，无法调换床位！");
            return;
        }
        System.out.println("可选房间（按楼层分组）：");
        for (Map.Entry<String, List<String>> entry : rooms.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }
        String roomNo = ConsoleTools.input("请选择新房间号：");
        boolean roomExists = false;
        for (List<String> nos : rooms.values()) {
            if (nos.contains(roomNo)) {
                roomExists = true;
                break;
            }
        }
        if (!roomExists) {
            System.out.println("所选房间号不存在！");
            return;
        }

        List<Bed> freeBeds = bedService.getFreeBedsByRoomNo(Integer.parseInt(roomNo));
        if (freeBeds.isEmpty()) {
            System.out.println("该房间没有空闲床位！");
            return;
        }
        System.out.println("该房间空闲床位：" + freeBeds);
        int newBedId = ConsoleTools.inputInt("请选择新床位ID：");
        boolean freeSelected = false;
        for (Bed b : freeBeds) {
            if (b.getId() != null && b.getId() == newBedId) {
                freeSelected = true;
                break;
            }
        }
        if (!freeSelected) {
            System.out.println("所选床位不是该房间的空闲床位！");
            return;
        }

        if (!bedService.changeBed(customerId, newBedId)) {
            System.out.println("床位调换失败！");
            return;
        }
        c.setBuilding_no(floorOf(rooms, roomNo));
        c.setRoom_no(roomNo);
        c.setBed_id(newBedId);
        if (!customerService.updateCustomer(c)) {
            System.out.println("床位已调换，但客户房间/床位信息更新失败！");
            return;
        }
        System.out.println("床位调换成功！");
    }

    private static boolean matchStatus(BedDetails d, int status, Date today) {
        boolean inUse = (d.getIs_deleted() == null || d.getIs_deleted() == 0)
                && (d.getEnd_date() == null || !d.getEnd_date().before(today));
        return status == DETAIL_IN_USE ? inUse : !inUse;
    }

    private static boolean sameDay(Date a, Date b) {
        if (a == null || b == null) {
            return a == b;
        }
        return ConsoleTools.fmt(a).equals(ConsoleTools.fmt(b));
    }
}
