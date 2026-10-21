package controller.systemAdmin.nursecontroller.nurselevel;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.support.NurseManagementSupport;
import entity.NursecontentS.Nursecontent;
import entity.NurselevelS.Nurselevel;
import entity.NurselevelitemS.Nurselevelitem;
import service.NursecontentS.Nursecontentservice;
import service.NursecontentS.NursecontentserviceImpl;
import service.NurselevelS.Nurselevelservice;
import service.NurselevelS.NurselevelserviceImpl;
import service.NurselevelitemS.Nurselevelitemservice;
import service.NurselevelitemS.NurselevelitemserviceImpl;

import java.util.ArrayList;
import java.util.List;

public class NurseLevelController {

    private static final Nurselevelservice nurselevelService = new NurselevelserviceImpl();
    private static final Nursecontentservice nursecontentService = new NursecontentserviceImpl();
    private static final Nurselevelitemservice nurselevelitemService = new NurselevelitemserviceImpl();

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("护理级别");
            System.out.println("1、查询护理级别列表");
            System.out.println("2、添加护理级别");
            System.out.println("3、修改护理级别状态");
            System.out.println("4、护理级别项目配置");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        listLevels();
                        break;
                    case "2":
                        addLevel();
                        break;
                    case "3":
                        updateLevelStatus();
                        break;
                    case "4":
                        configLevelItems();
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

    private static void listLevels() throws Exception {
        Integer status = NurseManagementSupport.inputStatusFilter("护理级别状态(1启用 2停用，回车默认启用，输入0查询全部)：");
        List<Nurselevel> result = new ArrayList<>();
        for (Nurselevel level : nurselevelService.findAllNurselevel()) {
            if (level.getIs_deleted() != null && level.getIs_deleted() != 0) {
                continue;
            }
            if (status != null && !status.equals(level.getLevel_status())) {
                continue;
            }
            result.add(level);
        }
        if (result.isEmpty()) {
            System.out.println("没有符合条件的护理级别！");
            return;
        }
        System.out.printf("%-5s%-16s%-10s%n", "ID", "护理级别", "状态");
        for (Nurselevel level : result) {
            System.out.printf("%-5d%-16s%-10s%n",
                    level.getId(),
                    level.getLevel_name(),
                    ConsoleTools.statusText(level.getLevel_status(), "启用", "停用"));
        }
    }

    private static void addLevel() {
        String name = ConsoleTools.input("护理级别名称：");
        if (name.isEmpty()) {
            System.out.println("护理级别名称不能为空！");
            return;
        }
        Integer status = NurseManagementSupport.inputStatus("状态(1启用 2停用)：");
        if (status == null) {
            System.out.println("状态输入有误！");
            return;
        }
        Nurselevel level = new Nurselevel();
        level.setLevel_name(name);
        level.setLevel_status(status);
        level.setIs_deleted(0);
        System.out.println(nurselevelService.addNurselevel(level) ? "添加成功！" : "添加失败！");
    }

    private static void updateLevelStatus() throws Exception {
        int id = ConsoleTools.inputInt("请输入要修改的护理级别ID：");
        Nurselevel level = nurselevelService.findNurselevelById(id);
        if (level == null) {
            System.out.println("护理级别不存在！");
            return;
        }
        System.out.println("当前状态：" + ConsoleTools.statusText(level.getLevel_status(), "启用", "停用"));
        Integer status = NurseManagementSupport.inputStatus("新状态(1启用 2停用)：");
        if (status == null) {
            System.out.println("状态输入有误！");
            return;
        }
        level.setLevel_status(status);
        System.out.println(nurselevelService.updateNurselevel(level) ? "修改成功！" : "修改失败！");
    }

    private static void configLevelItems() throws Exception {
        int levelId = ConsoleTools.inputInt("请输入护理级别ID：");
        Nurselevel level = nurselevelService.findNurselevelById(levelId);
        if (level == null) {
            System.out.println("护理级别不存在！");
            return;
        }
        while (true) {
            ConsoleTools.printTitle("护理级别项目配置 - " + level.getLevel_name());
            printLevelItems(levelId);
            System.out.println("1、移除该级别的护理项目");
            System.out.println("2、添加护理项目到该级别");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        removeLevelItem(levelId);
                        break;
                    case "2":
                        addLevelItem(levelId);
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

    private static void printLevelItems(int levelId) throws Exception {
        List<Nurselevelitem> items = nurselevelitemService.findAllNurselevelitem();
        boolean empty = true;
        System.out.printf("%-8s%-8s%-10s%-16s%-10s%n", "配置ID", "级别ID", "项目ID", "项目名称", "价格");
        for (Nurselevelitem item : items) {
            if (!Integer.valueOf(levelId).equals(item.getLevel_id())) {
                continue;
            }
            empty = false;
            Nursecontent content = nursecontentService.findNursecontentByid(item.getItem_id());
            System.out.printf("%-8d%-8d%-10d%-16s%-10s%n",
                    item.getId(),
                    item.getLevel_id(),
                    item.getItem_id(),
                    content == null ? "-" : content.getNursing_name(),
                    content == null ? "-" : content.getService_price());
        }
        if (empty) {
            System.out.println("该级别尚未配置护理项目！");
        }
    }

    private static void removeLevelItem(int levelId) throws Exception {
        int itemId = ConsoleTools.inputInt("请输入要移除的配置ID：");
        Nurselevelitem item = nurselevelitemService.findNurselevelitemById(itemId);
        if (item == null || !Integer.valueOf(levelId).equals(item.getLevel_id())) {
            System.out.println("该配置不存在或不属于当前级别！");
            return;
        }
        if (!ConsoleTools.confirm("确认移除该护理项目？(y/n)：")) {
            System.out.println("已取消！");
            return;
        }
        System.out.println(nurselevelitemService.deleteNurselevelitem(itemId) ? "移除成功！" : "移除失败！");
    }

    private static void addLevelItem(int levelId) throws Exception {
        List<Nursecontent> contents = NurseManagementSupport.enabledContents();
        List<Integer> configured = new ArrayList<>();
        for (Nurselevelitem item : nurselevelitemService.findAllNurselevelitem()) {
            if (Integer.valueOf(levelId).equals(item.getLevel_id())) {
                configured.add(item.getItem_id());
            }
        }
        List<Nursecontent> candidates = new ArrayList<>();
        for (Nursecontent content : contents) {
            if (!configured.contains(content.getId())) {
                candidates.add(content);
            }
        }
        if (candidates.isEmpty()) {
            System.out.println("没有可添加的护理项目！");
            return;
        }
        NurseManagementSupport.printContents(candidates);
        int itemId = ConsoleTools.inputInt("请选择护理项目ID：");
        Nursecontent selected = null;
        for (Nursecontent content : candidates) {
            if (content.getId() != null && content.getId() == itemId) {
                selected = content;
                break;
            }
        }
        if (selected == null) {
            System.out.println("所选项目不可添加！");
            return;
        }
        Nurselevelitem item = new Nurselevelitem();
        item.setLevel_id(levelId);
        item.setItem_id(selected.getId());
        System.out.println(nurselevelitemService.addNurselevelitem(item) ? "添加成功！" : "添加失败！");
    }
}
