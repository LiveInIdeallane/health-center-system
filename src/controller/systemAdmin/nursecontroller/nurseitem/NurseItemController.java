package controller.systemAdmin.nursecontroller.nurseitem;

import controller.systemAdmin.ConsoleTools;
import controller.systemAdmin.support.NurseManagementSupport;
import entity.NursecontentS.Nursecontent;
import entity.NurselevelitemS.Nurselevelitem;
import service.NursecontentS.Nursecontentservice;
import service.NursecontentS.NursecontentserviceImpl;
import service.NurselevelitemS.Nurselevelitemservice;
import service.NurselevelitemS.NurselevelitemserviceImpl;

import java.util.ArrayList;
import java.util.List;

public class NurseItemController {

    private static final Nursecontentservice nursecontentService = new NursecontentserviceImpl();
    private static final Nurselevelitemservice nurselevelitemService = new NurselevelitemserviceImpl();

    public static void menu() {
        while (true) {
            ConsoleTools.printTitle("护理项目");
            System.out.println("1、查询护理项目列表");
            System.out.println("2、添加护理项目");
            System.out.println("3、修改护理项目");
            System.out.println("4、删除护理项目");
            System.out.println("0、返回上层菜单");
            String select = ConsoleTools.input("请输入序号：");
            try {
                switch (select) {
                    case "1":
                        listContents();
                        break;
                    case "2":
                        addContent();
                        break;
                    case "3":
                        updateContent();
                        break;
                    case "4":
                        deleteContent();
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

    private static void listContents() throws Exception {
        String name = ConsoleTools.input("护理项目名称（模糊，回车不过滤）：");
        Integer status = NurseManagementSupport.inputStatusFilter("护理项目状态(1启用 2停用，回车默认启用，输入0查询全部)：");
        List<Nursecontent> result = new ArrayList<>();
        for (Nursecontent content : nursecontentService.findAllNursecontent()) {
            if (content.getIs_deleted() != null && content.getIs_deleted() != 0) {
                continue;
            }
            if (!name.isEmpty() && (content.getNursing_name() == null
                    || !content.getNursing_name().contains(name))) {
                continue;
            }
            if (status != null && !status.equals(content.getStatus())) {
                continue;
            }
            result.add(content);
        }
        if (result.isEmpty()) {
            System.out.println("没有符合条件的护理项目！");
            return;
        }
        NurseManagementSupport.printContents(result);
    }

    private static void addContent() throws Exception {
        Nursecontent content = new Nursecontent();
        content.setSerial_number(ConsoleTools.input("编号："));
        content.setNursing_name(ConsoleTools.input("名称："));
        content.setService_price(ConsoleTools.input("价格："));
        content.setStatus(NurseManagementSupport.inputStatus("状态(1启用 2停用)："));
        content.setExecution_cycle(ConsoleTools.input("执行周期："));
        content.setExecution_times(ConsoleTools.input("执行次数："));
        content.setMessage(ConsoleTools.input("描述(可空)："));
        content.setIs_deleted(0);
        if (content.getSerial_number().isEmpty() || content.getNursing_name().isEmpty()
                || content.getService_price().isEmpty()) {
            System.out.println("编号、名称、价格不能为空！");
            return;
        }
        System.out.println(nursecontentService.addNursecontent(content) ? "添加成功！" : "添加失败！");
    }

    private static void updateContent() throws Exception {
        int id = ConsoleTools.inputInt("请输入要修改的护理项目ID：");
        Nursecontent old = nursecontentService.findNursecontentByid(id);
        if (old == null) {
            System.out.println("护理项目不存在！");
            return;
        }
        Nursecontent content = new Nursecontent();
        content.setId(old.getId());
        content.setIs_deleted(old.getIs_deleted());
        content.setSerial_number(ConsoleTools.keep("编号(" + old.getSerial_number() + ")：", old.getSerial_number()));
        content.setNursing_name(ConsoleTools.keep("名称(" + old.getNursing_name() + ")：", old.getNursing_name()));
        content.setService_price(ConsoleTools.keep("价格(" + old.getService_price() + ")：", old.getService_price()));
        String status = ConsoleTools.input("状态(1启用 2停用)(" + old.getStatus() + ")：");
        content.setStatus(status.isEmpty() ? old.getStatus() : Integer.valueOf(status));
        content.setExecution_cycle(ConsoleTools.keep("执行周期(" + old.getExecution_cycle() + ")：", old.getExecution_cycle()));
        content.setExecution_times(ConsoleTools.keep("执行次数(" + old.getExecution_times() + ")：", old.getExecution_times()));
        content.setMessage(ConsoleTools.keep("描述(" + old.getMessage() + ")：", old.getMessage()));

        if (!nursecontentService.updateNursecontent(content)) {
            System.out.println("修改失败！");
            return;
        }
        if (content.getStatus() != null && content.getStatus() == NurseManagementSupport.STATUS_OFF) {
            removeFromLevelItems(id);
        }
        System.out.println("修改成功！");
    }

    private static void deleteContent() throws Exception {
        int id = ConsoleTools.inputInt("请输入要删除的护理项目ID：");
        Nursecontent content = nursecontentService.findNursecontentByid(id);
        if (content == null) {
            System.out.println("护理项目不存在！");
            return;
        }
        if (!ConsoleTools.confirm("确认删除护理项目【" + content.getNursing_name() + "】？(y/n)：")) {
            System.out.println("已取消删除！");
            return;
        }
        content.setIs_deleted(1);
        if (!nursecontentService.updateNursecontent(content)) {
            System.out.println("删除失败！");
            return;
        }
        removeFromLevelItems(id);
        System.out.println("删除成功！");
    }

    private static void removeFromLevelItems(int itemId) throws Exception {
        for (Nurselevelitem item : nurselevelitemService.findAllNurselevelitem()) {
            if (Integer.valueOf(itemId).equals(item.getItem_id())) {
                nurselevelitemService.deleteNurselevelitem(item.getId());
            }
        }
    }
}
