package controller.systemAdmin.customercontroller.checkin;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.support.CustomerManagementSupport;
import entity.BedS.Bed;
import entity.BedDetailsS.BedDetails;
import entity.CustomerS.Customer;
import service.BedDetailsS.BedDetailsService;
import service.BedDetailsS.BedDetailsServiceImpl;
import service.BedS.BedService;
import service.BedS.BedServiceImpl;
import service.CustomerS.CustomerService;
import service.CustomerS.CustomerServiceImpl;
import service.RoomS.RoomService;
import service.RoomS.RoomServiceImpl;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CheckinController {

    private static final CustomerService customerService = new CustomerServiceImpl();
    private static final RoomService roomService = new RoomServiceImpl();
    private static final BedService bedService = new BedServiceImpl();
    private static final BedDetailsService bedDetailsService = new BedDetailsServiceImpl();

    private static final String BUILDING_NO = "606";

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("入住登记");
            System.out.println("1、查询客户信息列表");
            System.out.println("2、登记客户入住信息");
            System.out.println("3、修改客户信息");
            System.out.println("4、删除客户信息");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        listCustomers();
                        break;
                    case "2":
                        registerCheckin();
                        break;
                    case "3":
                        updateCustomer();
                        break;
                    case "4":
                        deleteCustomer();
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
        String type = ConsoleTools.input("老人类型（0自理 1护理，回车默认自理）：");
        boolean onlyNurse = "1".equals(type);

        List<Integer> nurseIds = CustomerManagementSupport.nurseCustomerIds();
        List<Customer> all = customerService.findAllCustomers();
        List<Customer> result = new ArrayList<>();
        for (Customer c : all) {
            if (c.getIs_deleted() != null && c.getIs_deleted() != 0) {
                continue;
            }
            if (!name.isEmpty() && (c.getCustomer_name() == null
                    || !c.getCustomer_name().contains(name))) {
                continue;
            }
            if (nurseIds.contains(c.getId()) != onlyNurse) {
                continue;
            }
            result.add(c);
        }

        if (result.isEmpty()) {
            System.out.println("没有符合条件的客户！");
            return;
        }
        System.out.printf("%-5s%-10s%-6s%-6s%-22s%-8s%-8s%-12s%-12s%-8s%n",
                "ID", "姓名", "年龄", "性别", "身份证号", "楼栋", "房间", "床位", "合同到期", "类型");
        for (Customer c : result) {
            System.out.printf("%-5d%-10s%-6s%-6s%-22s%-8s%-8s%-12s%-12s%-8s%n",
                    c.getId(),
                    c.getCustomer_name(),
                    c.getCustomer_age(),
                    ConsoleTools.sexText(c.getCustomer_sex()),
                    c.getIdcard(),
                    c.getBuilding_no(),
                    c.getRoom_no(),
                    c.getBed_id(),
                    ConsoleTools.fmt(c.getExpiration_date()),
                    nurseIds.contains(c.getId()) ? "护理老人" : "自理老人");
        }
    }

    private static void registerCheckin() {
        String name = ConsoleTools.input("客户姓名：");
        if (name.isEmpty()) {
            System.out.println("客户姓名不能为空！");
            return;
        }

        Date birthday = ConsoleTools.parseDate(ConsoleTools.input("出生日期(yyyy-MM-dd)："));
        if (birthday == null) {
            System.out.println("出生日期格式错误！");
            return;
        }
        int age = calcAge(birthday);

        String sex = ConsoleTools.input("性别(0男 1女)：");
        if (!"0".equals(sex) && !"1".equals(sex)) {
            System.out.println("性别输入有误！");
            return;
        }

        String idcard = ConsoleTools.input("身份证号：");
        if (idcard.isEmpty()) {
            System.out.println("身份证号不能为空！");
            return;
        }
        if (!customerService.search("", "", idcard, "").isEmpty()) {
            System.out.println("该身份证号已登记过！");
            return;
        }

        String bloodType = ConsoleTools.input("血型：");
        String family = ConsoleTools.input("家属：");
        String tel = ConsoleTools.input("联系电话：");
        if (tel.isEmpty()) {
            System.out.println("联系电话不能为空！");
            return;
        }

        Map<String, List<String>> rooms = roomService.getRoomsGroupByFloor();
        if (rooms.isEmpty()) {
            System.out.println("暂无房间数据，无法登记入住！");
            return;
        }
        System.out.println("可选房间（按楼层分组）：");
        for (Map.Entry<String, List<String>> entry : rooms.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }
        String roomNo = ConsoleTools.input("请选择房间号：");
        if (!containsRoom(rooms, roomNo)) {
            System.out.println("所选房间号不存在！");
            return;
        }

        List<Bed> freeBeds = bedService.getFreeBedsByRoomNo(Integer.parseInt(roomNo));
        if (freeBeds.isEmpty()) {
            System.out.println("该房间没有空闲床位！");
            return;
        }
        System.out.println("该房间空闲床位：" + freeBeds);
        int bedId = ConsoleTools.inputInt("请选择床位ID：");
        boolean freeSelected = false;
        for (Bed b : freeBeds) {
            if (b.getId() != null && b.getId() == bedId) {
                freeSelected = true;
                break;
            }
        }
        if (!freeSelected) {
            System.out.println("所选床位不是该房间的空闲床位！");
            return;
        }

        Date checkin = new Date();
        String s = ConsoleTools.input("入住时间(yyyy-MM-dd，回车默认今天)：");
        if (!s.isEmpty()) {
            checkin = ConsoleTools.parseDate(s);
            if (checkin == null) {
                System.out.println("入住时间格式错误！");
                return;
            }
        }

        Date expiration = ConsoleTools.parseDate(ConsoleTools.input("合同到期时间(yyyy-MM-dd)："));
        if (expiration == null) {
            System.out.println("合同到期时间格式错误！");
            return;
        }
        if (expiration.before(checkin)) {
            System.out.println("合同到期时间不能小于入住时间！");
            return;
        }

        String state = ConsoleTools.input("身心状况(可空)：");
        String attention = ConsoleTools.input("注意事项(可空)：");
        Integer levelId = ConsoleTools.inputIntOrNull("护理等级ID(可空)：");
        Integer userId = ConsoleTools.inputIntOrNull("健康管家ID(可空)：");

        Customer c = new Customer();
        c.setIs_deleted(0);
        c.setCustomer_name(name);
        c.setCustomer_age(age);
        c.setCustomer_sex(Integer.valueOf(sex));
        c.setIdcard(idcard);
        c.setBuilding_no(floorOf(rooms, roomNo));
        c.setRoom_no(roomNo);
        c.setBed_id(bedId);
        c.setCheckin_date(checkin);
        c.setExpiration_date(expiration);
        c.setContact_tel(tel);
        c.setBlood_type(bloodType);
        c.setFilepath("");
        c.setFamily_member(family);
        c.setBirthday(birthday);
        c.setPsychosomatic_state(state);
        c.setAttention(attention);
        c.setLevel_id(levelId);
        c.setUser_id(userId);

        if (!customerService.createCustomer(c)) {
            System.out.println("入住登记失败！");
            return;
        }
        Customer saved = customerService.findByIdCard(idcard);
        if (saved != null) {
            BedDetails details = new BedDetails();
            details.setCustomer_id(saved.getId());
            details.setBed_id(bedId);
            details.setStart_date(checkin);
            details.setIs_deleted(0);
            bedDetailsService.addBedDetails(details);
            bedService.changeBedStatus(bedId, 2);
        }
        System.out.println("入住登记成功！");
    }

    private static void updateCustomer() {
        int id = ConsoleTools.inputInt("请输入要修改的客户ID：");
        Customer old = customerService.findCustomerById(id);
        if (old == null) {
            System.out.println("客户不存在！");
            return;
        }

        try {
            Customer c = new Customer();
            c.setId(old.getId());
            c.setIs_deleted(old.getIs_deleted());
            c.setCustomer_name(ConsoleTools.keep("客户姓名(" + old.getCustomer_name() + ")：", old.getCustomer_name()));
            String age = ConsoleTools.input("年龄(" + old.getCustomer_age() + ")：");
            c.setCustomer_age(age.isEmpty() ? old.getCustomer_age() : Integer.valueOf(age));
            String sex = ConsoleTools.input("性别(0男 1女)(" + old.getCustomer_sex() + ")：");
            c.setCustomer_sex(sex.isEmpty() ? old.getCustomer_sex() : Integer.valueOf(sex));
            c.setIdcard(ConsoleTools.keep("身份证号(" + old.getIdcard() + ")：", old.getIdcard()));
            c.setRoom_no(ConsoleTools.keep("房间号(" + old.getRoom_no() + ")：", old.getRoom_no()));
            c.setContact_tel(ConsoleTools.keep("联系电话(" + old.getContact_tel() + ")：", old.getContact_tel()));
            c.setFamily_member(ConsoleTools.keep("家属(" + old.getFamily_member() + ")：", old.getFamily_member()));
            c.setBlood_type(ConsoleTools.keep("血型(" + old.getBlood_type() + ")：", old.getBlood_type()));
            c.setPsychosomatic_state(ConsoleTools.keep("身心状况：", old.getPsychosomatic_state()));
            c.setAttention(ConsoleTools.keep("注意事项：", old.getAttention()));

            Date expiration = ConsoleTools.keepDate("合同到期时间(" + ConsoleTools.fmt(old.getExpiration_date()) + ")：",
                    old.getExpiration_date());
            boolean expirationChanged = expiration != null
                    && !expiration.equals(old.getExpiration_date());
            if (expirationChanged && expiration.before(old.getCheckin_date())) {
                System.out.println("合同到期时间不能小于入住时间！");
                return;
            }
            c.setCheckin_date(old.getCheckin_date());
            c.setExpiration_date(expiration);
            c.setBuilding_no(old.getBuilding_no());
            c.setBed_id(old.getBed_id());
            c.setBirthday(old.getBirthday());
            c.setHeight(old.getHeight());
            c.setWeight(old.getWeight());
            c.setFilepath(old.getFilepath());
            c.setUser_id(old.getUser_id());
            c.setLevel_id(old.getLevel_id());

            if (!customerService.updateCustomer(c)) {
                System.out.println("修改失败！");
                return;
            }
            if (expirationChanged) {
                bedDetailsService.updateEndDate(id, expiration);
            }
            System.out.println("修改成功！");
        } catch (NumberFormatException | ParseException e) {
            System.out.println("输入格式错误：" + e.getMessage());
        }
    }

    private static void deleteCustomer() {
        int id = ConsoleTools.inputInt("请输入要删除的客户ID：");
        Customer c = customerService.findCustomerById(id);
        if (c == null) {
            System.out.println("客户不存在！");
            return;
        }
        if (!ConsoleTools.confirm("确认删除客户【" + c.getCustomer_name() + "】？(y/n)：")) {
            System.out.println("已取消删除！");
            return;
        }
        if (!customerService.deleteCustomer(c)) {
            System.out.println("删除失败！");
            return;
        }
        if (c.getBed_id() != null) {
            bedService.changeBedStatus(c.getBed_id(), 1);
        }
        bedDetailsService.logicDeleteActiveByCustomer(id);
        System.out.println("删除成功！");
    }

    private static String floorOf(Map<String, List<String>> rooms, String roomNo) {
        for (Map.Entry<String, List<String>> entry : rooms.entrySet()) {
            if (entry.getValue().contains(roomNo)) {
                return entry.getKey();
            }
        }
        return BUILDING_NO;
    }

    private static boolean containsRoom(Map<String, List<String>> rooms, String roomNo) {
        for (List<String> nos : rooms.values()) {
            if (nos.contains(roomNo)) {
                return true;
            }
        }
        return false;
    }

    private static int calcAge(Date birthday) {
        LocalDate b = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(b, LocalDate.now()).getYears();
    }

}
