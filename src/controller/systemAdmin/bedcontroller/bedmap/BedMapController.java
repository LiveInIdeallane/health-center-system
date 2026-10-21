package controller.systemAdmin.bedcontroller.bedmap;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.support.CustomerManagementSupport;
import service.BedS.BedService;
import service.BedS.BedServiceImpl;

import java.util.List;
import java.util.Map;

public class BedMapController {

    private static final BedService bedService = new BedServiceImpl();

    public static void show() {
        Map<String, Integer> count = bedService.countBedStatus();
        System.out.println("总床位数：" + count.get("total")
                + "，空闲：" + count.get("free")
                + "，有人：" + count.get("occupied")
                + "，外出：" + count.get("out"));

        List<String> floors = bedService.getAllFloors();
        if (floors.isEmpty()) {
            System.out.println("暂无楼层数据！");
            return;
        }
        System.out.println("可选楼层：" + floors);
        String floor = ConsoleTools.input("楼层（回车默认" + floors.get(0) + "）：");
        if (floor.isEmpty()) {
            floor = floors.get(0);
        }

        Map<String, Object> bedMap = bedService.getBedMapByFloor(floor);
        Object rooms = bedMap.get("rooms");
        if (!(rooms instanceof List) || ((List<?>) rooms).isEmpty()) {
            System.out.println("该楼层暂无房间床位信息！");
            return;
        }
        for (Object roomObj : (List<?>) rooms) {
            Map<?, ?> room = (Map<?, ?>) roomObj;
            System.out.println("房间：" + room.get("roomNo"));
            Object beds = room.get("beds");
            if (!(beds instanceof List)) {
                continue;
            }
            for (Object bedObj : (List<?>) beds) {
                Map<?, ?> bed = (Map<?, ?>) bedObj;
                String line = "    床位ID=" + bed.get("bedId")
                        + "  床号=" + bed.get("bedNo")
                        + "  状态=" + bed.get("status");
                if (bed.get("customerId") != null) {
                    line += "  客户=" + CustomerManagementSupport.customerName((Integer) bed.get("customerId"))
                            + "(ID=" + bed.get("customerId") + ")";
                }
                System.out.println(line);
            }
        }
    }
}
