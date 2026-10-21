package controller;


import service.BedDetailsS.BedDetailsService;
import service.BedDetailsS.BedDetailsServiceImpl;
import service.BedS.BedService;
import service.BedS.BedServiceImpl;
import service.RoomS.RoomService;
import service.RoomS.RoomServiceImpl;
import entity.RoomS.Room;
import entity.BedDetailsS.BedDetails;
import entity.BedS.Bed;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BedView {

    private static Scanner scanner = new Scanner(System.in);
    private static BedService bedService = new BedServiceImpl();
    private static BedDetailsService bedDetailsService = new BedDetailsServiceImpl();
    private static RoomService roomService = new RoomServiceImpl();

    private static String getStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 1: return "空闲";
            case 2: return "有人";
            case 3: return "外出";
            default: return "未知";
        }
    }

    private static void printTitle(String title) {
        System.out.println();
        System.out.println("===== " + title + " =====");
    }

    // ==================== 主菜单 ====================

    public static void menu() {
        while (true) {
            printTitle("床位管理模块");
            System.out.println("1、床位示意图");
            System.out.println("2、床位管理");
            System.out.println("3、房间管理");
            System.out.println("4、床位调换");
            System.out.println("5、床位使用详情");
            System.out.println("0、返回上层菜单");
            System.out.print("请输入序号：");

            int select = scanner.nextInt();
            switch (select) {
                case 1: showBedMap(); break;
                case 2: bedManageMenu(); break;
                case 3: roomManageMenu(); break;
                case 4: changeBed(); break;
                case 5: bedDetailsMenu(); break;
                case 0: return;
                default: System.out.println("输入有误，请重新输入！");
            }
        }
    }

    // ==================== 床位示意图 ====================

    private static void showBedMap() {
        printTitle("床位示意图");
        List<String> floors = bedService.getAllFloors();
        if (floors.isEmpty()) {
            System.out.println("暂无楼层信息！");
            return;
        }
        System.out.println("可选楼层：" + floors);
        scanner.nextLine();
        System.out.print("请输入楼层名称（直接回车默认「" + floors.get(0) + "」）：");
        String floor = scanner.nextLine().trim();
        if (floor.isEmpty()) {
            floor = floors.get(0);
        }

        Map<String, Object> map = bedService.getBedMapByFloor(floor);
        System.out.println("楼层：" + map.get("floor"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rooms = (List<Map<String, Object>>) map.get("rooms");
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("该楼层暂无房间信息！");
            return;
        }

        for (Map<String, Object> room : rooms) {
            System.out.println("-- 房间号：" + room.get("roomNo"));
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> beds = (List<Map<String, Object>>) room.get("beds");
            if (beds != null) {
                for (Map<String, Object> bed : beds) {
                    System.out.println("   床位ID:" + bed.get("bedId")
                            + "  床位号:" + bed.get("bedNo")
                            + "  状态:" + bed.get("status"));
                }
            }
        }

        Map<String, Integer> count = bedService.countBedStatus();
        System.out.println("统计：总床位 " + count.get("total")
                + "，空闲 " + count.get("free")
                + "，有人 " + count.get("occupied")
                + "，外出 " + count.get("out"));
    }

    // ==================== 床位管理 ====================

    private static void bedManageMenu() {
        while (true) {
            printTitle("床位管理");
            System.out.println("1、查询所有床位");
            System.out.println("2、根据ID查询床位");
            System.out.println("3、添加床位");
            System.out.println("4、修改床位");
            System.out.println("5、删除床位");
            System.out.println("6、修改床位状态");
            System.out.println("7、查询指定房间空闲床位");
            System.out.println("0、返回上层菜单");
            System.out.print("请输入序号：");

            int select = scanner.nextInt();
            switch (select) {
                case 1: listAllBeds(); break;
                case 2: findBedById(); break;
                case 3: addBed(); break;
                case 4: updateBed(); break;
                case 5: deleteBed(); break;
                case 6: changeBedStatus(); break;
                case 7: findFreeBeds(); break;
                case 0: return;
                default: System.out.println("输入有误，请重新输入！");
            }
        }
    }

    private static void listAllBeds() {
        List<Bed> beds = bedService.getAllBeds();
        if (beds.isEmpty()) {
            System.out.println("暂无床位信息！");
            return;
        }
        System.out.printf("%-8s%-8s%-10s%-15s%-12s%n", "床位ID", "房间号", "状态", "备注", "床位编号");
        for (Bed bed : beds) {
            System.out.printf("%-8d%-8d%-10s%-15s%-12s%n",
                    bed.getId(),
                    bed.getRoom_no(),
                    getStatusText(bed.getBed_status()),
                    bed.getRemarks() == null ? "" : bed.getRemarks(),
                    bed.getBed_no());
        }
    }

    private static void findBedById() {
        System.out.print("请输入床位ID：");
        int id = scanner.nextInt();
        Bed bed = bedService.getBedById(id);
        if (bed == null) {
            System.out.println("未找到该床位！");
            return;
        }
        System.out.println("床位ID：" + bed.getId());
        System.out.println("房间号：" + bed.getRoom_no());
        System.out.println("状态：" + getStatusText(bed.getBed_status()));
        System.out.println("床位编号：" + bed.getBed_no());
        System.out.println("备注：" + bed.getRemarks());
    }

    private static void addBed() {
        System.out.print("请输入床位ID：");
        int id = scanner.nextInt();
        System.out.print("请输入房间号：");
        int roomNo = scanner.nextInt();
        System.out.print("请输入床位状态（1空闲 2有人 3外出）：");
        int status = scanner.nextInt();
        System.out.print("请输入床位编号：");
        String bedNo = scanner.next();
        System.out.print("请输入备注：");
        String remarks = scanner.next();

        Bed bed = new Bed(id, roomNo, status, remarks, bedNo);
        boolean b = bedService.addBed(bed);
        System.out.println(b ? "添加成功！" : "添加失败！");
    }

    private static void updateBed() {
        System.out.print("请输入要修改的床位ID：");
        int id = scanner.nextInt();
        Bed bed = bedService.getBedById(id);
        if (bed == null) {
            System.out.println("未找到该床位！");
            return;
        }
        System.out.print("新的房间号（原：" + bed.getRoom_no() + "）：");
        int roomNo = scanner.nextInt();
        System.out.print("新的床位状态（1空闲 2有人 3外出，原：" + getStatusText(bed.getBed_status()) + "）：");
        int status = scanner.nextInt();
        System.out.print("新的床位编号（原：" + bed.getBed_no() + "）：");
        String bedNo = scanner.next();
        System.out.print("新的备注（原：" + bed.getRemarks() + "）：");
        String remarks = scanner.next();

        bed.setRoom_no(roomNo);
        bed.setBed_status(status);
        bed.setBed_no(bedNo);
        bed.setRemarks(remarks);

        boolean b = bedService.updateBed(bed);
        System.out.println(b ? "修改成功！" : "修改失败！");
    }

    private static void deleteBed() {
        System.out.print("请输入要删除的床位ID：");
        int id = scanner.nextInt();
        boolean b = bedService.deleteBed(id);
        System.out.println(b ? "删除成功！" : "删除失败！");
    }

    private static void changeBedStatus() {
        System.out.print("请输入床位ID：");
        int bedId = scanner.nextInt();
        System.out.print("请输入新的床位状态（1空闲 2有人 3外出）：");
        int status = scanner.nextInt();
        boolean b = bedService.changeBedStatus(bedId, status);
        System.out.println(b ? "修改成功！" : "修改失败！");
    }

    private static void findFreeBeds() {
        System.out.print("请输入房间号：");
        int roomNo = scanner.nextInt();
        List<Bed> beds = bedService.getFreeBedsByRoomNo(roomNo);
        if (beds.isEmpty()) {
            System.out.println("该房间暂无空闲床位！");
            return;
        }
        System.out.println("房间 " + roomNo + " 的空闲床位：");
        for (Bed bed : beds) {
            System.out.println("  床位ID:" + bed.getId() + "  床位编号:" + bed.getBed_no());
        }
    }

    // ==================== 房间管理 ====================

    private static void roomManageMenu() {
        while (true) {
            printTitle("房间管理");
            System.out.println("1、查询所有房间");
            System.out.println("2、根据楼层查询房间");
            System.out.println("3、查询所有楼层");
            System.out.println("4、添加房间");
            System.out.println("5、修改房间");
            System.out.println("6、删除房间");
            System.out.println("0、返回上层菜单");
            System.out.print("请输入序号：");

            int select = scanner.nextInt();
            switch (select) {
                case 1: listAllRooms(); break;
                case 2: findRoomsByFloor(); break;
                case 3: listAllFloors(); break;
                case 4: addRoom(); break;
                case 5: updateRoom(); break;
                case 6: deleteRoom(); break;
                case 0: return;
                default: System.out.println("输入有误，请重新输入！");
            }
        }
    }

    private static void listAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("暂无房间信息！");
            return;
        }
        System.out.printf("%-10s%-12s%-10s%n", "房间ID", "楼层", "房间号");
        for (Room room : rooms) {
            System.out.printf("%-10d%-12s%-10d%n",
                    room.getId(), room.getRoom_floor(), room.getRoom_no());
        }
    }

    private static void findRoomsByFloor() {
        System.out.print("请输入楼层：");
        String floor = scanner.next();
        List<Room> rooms = roomService.getRoomsByFloor(floor);
        if (rooms.isEmpty()) {
            System.out.println("该楼层暂无房间！");
            return;
        }
        System.out.printf("%-10s%-12s%-10s%n", "房间ID", "楼层", "房间号");
        for (Room room : rooms) {
            System.out.printf("%-10d%-12s%-10d%n",
                    room.getId(), room.getRoom_floor(), room.getRoom_no());
        }
    }

    private static void listAllFloors() {
        List<String> floors = roomService.getAllFloors();
        System.out.println("所有楼层：" + floors);
    }

    private static void addRoom() {
        System.out.print("请输入房间ID：");
        int id = scanner.nextInt();
        System.out.print("请输入楼层：");
        String floor = scanner.next();
        System.out.print("请输入房间号：");
        int roomNo = scanner.nextInt();

        Room room = new Room(id, floor, roomNo);
        boolean b = roomService.addRoom(room);
        System.out.println(b ? "添加成功！" : "添加失败！");
    }

    private static void updateRoom() {
        System.out.print("请输入要修改的房间ID：");
        int id = scanner.nextInt();
        Room room = roomService.getRoomById(id);
        if (room == null) {
            System.out.println("未找到该房间！");
            return;
        }
        System.out.print("新的楼层（原：" + room.getRoom_floor() + "）：");
        String floor = scanner.next();
        System.out.print("新的房间号（原：" + room.getRoom_no() + "）：");
        int roomNo = scanner.nextInt();

        room.setRoom_floor(floor);
        room.setRoom_no(roomNo);

        boolean b = roomService.updateRoom(room);
        System.out.println(b ? "修改成功！" : "修改失败！");
    }

    private static void deleteRoom() {
        System.out.print("请输入要删除的房间ID：");
        int id = scanner.nextInt();
        boolean b = roomService.deleteRoom(id);
        System.out.println(b ? "删除成功！" : "删除失败！");
    }

    // ==================== 床位调换 ====================

    private static void changeBed() {
        printTitle("床位调换");
        System.out.print("请输入客户ID：");
        int customerId = scanner.nextInt();
        System.out.print("请输入新床位ID：");
        int newBedId = scanner.nextInt();

        boolean b = bedService.changeBed(customerId, newBedId);
        System.out.println(b ? "床位调换成功！" : "床位调换失败！");
    }

    // ==================== 床位使用详情 ====================

    private static void bedDetailsMenu() {
        while (true) {
            printTitle("床位使用详情");
            System.out.println("1、查询正在使用的床位详情");
            System.out.println("2、查询使用历史");
            System.out.println("3、根据客户ID查询当前生效");
            System.out.println("4、修改结束日期");
            System.out.println("5、逻辑删除");
            System.out.println("0、返回上层菜单");
            System.out.print("请输入序号：");

            int select = scanner.nextInt();
            switch (select) {
                case 1: listActiveDetails(); break;
                case 2: listHistoryDetails(); break;
                case 3: findActiveByCustomerId(); break;
                case 4: updateEndDate(); break;
                case 5: logicDeleteDetail(); break;
                case 0: return;
                default: System.out.println("输入有误，请重新输入！");
            }
        }
    }

    private static void listActiveDetails() {
        System.out.println("正在使用的床位详情：");
        List<BedDetails> list = bedDetailsService.getAllBedDetails();
        boolean found = false;
        java.util.Date now = new java.util.Date();
        for (BedDetails d : list) {
            if (d.getIs_deleted() != null && d.getIs_deleted() == 0
                    && (d.getEnd_date() == null || !d.getEnd_date().before(now))) {
                printDetail(d);
                found = true;
            }
        }
        if (!found) System.out.println("暂无正在使用的床位详情");
    }

    private static void listHistoryDetails() {
        System.out.println("使用历史：");
        List<BedDetails> list = bedDetailsService.getAllBedDetails();
        boolean found = false;
        java.util.Date now = new java.util.Date();
        for (BedDetails d : list) {
            boolean isHistory = (d.getIs_deleted() != null && d.getIs_deleted() == 1)
                    || (d.getEnd_date() != null && d.getEnd_date().before(now));
            if (isHistory) {
                printDetail(d);
                found = true;
            }
        }
        if (!found) System.out.println("暂无使用历史");
    }

    private static void findActiveByCustomerId() {
        System.out.print("请输入客户ID：");
        int customerId = scanner.nextInt();
        BedDetails d = bedDetailsService.getActiveByCustomerId(customerId);
        if (d == null) {
            System.out.println("该客户暂无正在使用的床位");
            return;
        }
        printDetail(d);
    }

    private static void updateEndDate() {
        System.out.print("请输入床位详情ID：");
        int id = scanner.nextInt();
        System.out.print("请输入结束日期（yyyy-MM-dd）：");
        String dateStr = scanner.next();
        try {
            java.util.Date endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            boolean b = bedDetailsService.updateEndDate(id, endDate);
            System.out.println(b ? "修改成功！" : "修改失败！");
        } catch (Exception e) {
            System.out.println("日期格式错误！");
        }
    }

    private static void logicDeleteDetail() {
        System.out.print("请输入床位详情ID：");
        int id = scanner.nextInt();
        boolean b = bedDetailsService.logicDelete(id);
        System.out.println(b ? "删除成功！" : "删除失败！");
    }

    private static void printDetail(BedDetails d) {
        System.out.println("  详情ID：" + d.getId()
                + "  客户ID：" + d.getCustomer_id()
                + "  床位ID：" + d.getBed_id()
                + "  起始：" + d.getStart_date()
                + "  结束：" + (d.getEnd_date() == null ? "未结束" : d.getEnd_date()));
    }
}