package controller.healthManager;

import entity.CustomerS.Customer;
import entity.NurserecordS.Nurserecord;
import entity.CustomernurseitemS.Customernurseitem;
import entity.OutwardS.Outward;
import entity.BackdownS.Backdown;

import service.CustomerS.CustomerService;
import service.CustomerS.CustomerServiceImpl;
import service.CustomernurseitemS.CustomernurseitemService;
import service.CustomernurseitemS.CustomernurseitemServiceImpl;
import service.NurserecordS.Nurserecordservice;
import service.NurserecordS.NurserecordserviceImpl;
import service.OutwardS.OutwardService;
import service.OutwardS.OutwardServiceImpl;
import service.BackdownS.BackdownService;
import service.BackdownS.BackdownServiceImpl;

import utils.Param;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * 健康管家视图。
 * 按用例图实现：
 * 1. 登录
 * 2. 日常护理 → 生成护理记录 + 查询自己服务的客户信息列表
 * 3. 服务对象护理记录 → 查询自己服务的客户信息列表 + 浏览护理记录
 * 4. 客户管理
 *    - 外出申请：查询客户列表 + 提交申请 + 浏览申请列表
 *    - 退住申请：查询客户列表 + 提交申请 + 浏览申请列表
 */
public class NurseView {

    private static Scanner sc = new Scanner(System.in);

    private static CustomerService customerService = new CustomerServiceImpl();
    private static Nurserecordservice nurserecordService = new NurserecordserviceImpl();
    private static CustomernurseitemService customernurseitemService = new CustomernurseitemServiceImpl();
    private static OutwardService outwardService = new OutwardServiceImpl();
    private static BackdownService backdownService = new BackdownServiceImpl();

    // ===================== 主菜单 =====================

    public static void nurseMenu() {
        while (true) {
            System.out.println("\n===== 健康管家系统 =====");
            System.out.println("当前用户：" + Param.loggingUser.getNickname());
            System.out.println("--- 日常护理 ---");
            System.out.println("1. 生成护理记录");
            System.out.println("2. 查询护理记录");
            System.out.println("--- 客户管理 ---");
            System.out.println("3. 客户外出申请");
            System.out.println("4. 客户退住申请");
            System.out.println("0. 退出登录");
            System.out.print("请选择：");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": dailyCareMenu(); break;
                    case "2": nurseRecordMenu(); break;
                    case "3": outwardMenu(); break;
                    case "4": backdownMenu(); break;
                    case "0":
                        System.out.println("已退出登录。");
                        System.exit(0);
                    default:
                        System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    // ===================== 公共：查询自己服务的客户 =====================

    /** 查询自己服务的客户信息列表 */
    private static List<Customer> myCustomers() {
        return customerService.findByUserId(Param.loggingUser.getId());
    }

    /** 按姓名模糊 + 只查自己服务的 */
    private static List<Customer> myCustomersByName(String name) {
        List<Customer> all = myCustomers();
        if (name == null || name.isEmpty()) return all;
        List<Customer> result = new ArrayList<>();
        for (Customer c : all) {
            if (c.getCustomer_name() != null && c.getCustomer_name().contains(name)) {
                result.add(c);
            }
        }
        return result;
    }

    private static void printCustomers(List<Customer> list) {
        if (list.isEmpty()) { System.out.println("暂无客户"); return; }
        for (Customer c : list) {
            System.out.println("  id=" + c.getId()
                    + ", 姓名=" + c.getCustomer_name()
                    + ", 年龄=" + c.getCustomer_age()
                    + ", 房间号=" + c.getRoom_no()
                    + ", 床号=" + c.getBed_id());
        }
    }

    // ===================== 用例 2：日常护理 =====================

    private static void dailyCareMenu() {
        // 子用例：查询自己服务的客户信息列表
        System.out.print("客户姓名（模糊，直接回车查全部）：");
        String name = sc.nextLine().trim();
        List<Customer> list = myCustomersByName(name);
        printCustomers(list);
        if (list.isEmpty()) return;

        System.out.print("请选择客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());

        // 查该客户的护理项目（用 findAll + 筛选）
        List<Customernurseitem> items = new ArrayList<>();
        try {
            for (Customernurseitem it : customernurseitemService.findAllCustomernurseitem()) {
                if (customerId.equals(it.getCustomer_id())
                        && (it.getIs_deleted() == null || it.getIs_deleted() == 0)) {
                    items.add(it);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (items.isEmpty()) {
            System.out.println("该客户暂无护理项目");
            return;
        }
        System.out.println("护理项目：");
        for (Customernurseitem it : items) {
            System.out.println("  项目id=" + it.getItem_id()
                    + ", 数量=" + it.getNurse_number()
                    + ", 购买时间=" + it.getBuy_time()
                    + ", 到期时间=" + it.getMaturity_time());
        }

        System.out.print("请选择护理项目 id：");
        Integer itemId = Integer.parseInt(sc.nextLine());

        System.out.print("护理数量：");
        Integer count = Integer.parseInt(sc.nextLine());

        // 子用例：生成护理记录
        Nurserecord record = new Nurserecord();
        record.setIs_deleted(0);
        record.setCustomer_id(customerId);
        record.setItem_id(itemId);
        record.setNursing_time(new Date());
        record.setNursing_content("日常护理");
        record.setNursing_count(count);
        record.setUser_id(Param.loggingUser.getId());

        boolean ok = nurserecordService.addNurserecord(record);
        if (ok) {
            // 找到刚才选中的护理项目，把数量扣减
            for (Customernurseitem it : items) {
                if (it.getItem_id().equals(itemId)) {
                    int newCount = it.getNurse_number() - count;
                    it.setNurse_number(newCount);
                    customernurseitemService.updateCustomernurseitem(it); // 调组员修改方法
                    break;
                }
            }
            System.out.println("护理记录生成成功，已扣减库存！");
        } else {
            System.out.println("生成失败");
        }
    }

    // ===================== 用例 3：服务对象护理记录 =====================

    private static void nurseRecordMenu() {
        // 子用例：查询自己服务的客户信息列表
        System.out.print("客户姓名（模糊，直接回车查全部）：");
        String name = sc.nextLine().trim();
        List<Customer> list = myCustomersByName(name);
        printCustomers(list);
        if (list.isEmpty()) return;

        System.out.print("请选择客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());

        // 子用例：浏览护理记录
        List<Nurserecord> records = new ArrayList<>();
        try {
            for (Nurserecord r : nurserecordService.findAllNurserecord()) {
                if (customerId.equals(r.getCustomer_id())
                        && (r.getIs_deleted() == null || r.getIs_deleted() == 0)) {
                    records.add(r);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (records.isEmpty()) {
            System.out.println("该客户暂无护理记录");
            return;
        }
        System.out.println("护理记录：");
        for (Nurserecord r : records) {
            System.out.println("  记录id=" + r.getId()
                    + ", 项目id=" + r.getItem_id()
                    + ", 时间=" + r.getNursing_time()
                    + ", 数量=" + r.getNursing_count()
                    + ", 内容=" + r.getNursing_content());
        }
    }

    // ===================== 用例 4：客户管理 =====================

    private static void customerMenu() {
        while (true) {
            System.out.println("\n--- 客户管理 ---");
            System.out.println("1. 外出申请");
            System.out.println("2. 退住申请");
            System.out.println("0. 返回上级");
            System.out.print("请选择：");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": outwardMenu(); break;
                    case "2": backdownMenu(); break;
                    case "0": return;
                    default: System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    // ===================== 用例 4.1：外出申请 =====================

    private static void outwardMenu() {
        while (true) {
            System.out.println("\n--- 外出申请 ---");
            System.out.println("1. 提交外出申请");
            System.out.println("2. 浏览外出申请列表");
            System.out.println("3. 登记回院时间");        // ← 新增
            System.out.println("0. 返回上级");
            System.out.print("请选择：");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": addOutward(); break;
                    case "2": listOutward(); break;
                    case "3": updateReturnTime(); break;   // ← 新方法
                    case "0": return;
                    default: System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    /** 登记回院时间 */
    private static void updateReturnTime() {
        System.out.print("客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());

        List<Outward> outs = new ArrayList<>();
        for (Outward o : outwardService.GetOutwards()) {
            if (customerId.equals(o.getCustomer_id())
                    && (o.getIs_delete() == null || o.getIs_delete() == 0)) {
                outs.add(o);
            }
        }
        if (outs.isEmpty()) { System.out.println("该客户暂无外出申请"); return; }

        for (Outward o : outs) {
            System.out.println("  申请id=" + o.getId()
                    + ", 外出=" + o.getOutgoingtime()
                    + ", 实际回院=" + o.getActualreturntime());
        }

        System.out.print("要登记回院时间的申请 id：");
        Integer oid = Integer.parseInt(sc.nextLine());

        Outward target = null;
        for (Outward o : outs) {
            if (oid.equals(o.getId())) { target = o; break; }
        }
        if (target == null) { System.out.println("未找到该申请"); return; }

        System.out.print("实际回院时间(yyyy-MM-dd)：");
        Date t = java.sql.Date.valueOf(sc.nextLine());

        target.setActualreturntime(t);
        boolean ok = outwardService.UpdateOutward(target);
        System.out.println(ok ? "已登记" : "登记失败");
    }

    private static void addOutward() {
        // 子用例：查询自己服务的客户信息列表
        System.out.print("客户姓名（模糊，直接回车查全部）：");
        String name = sc.nextLine().trim();
        List<Customer> list = myCustomersByName(name);
        printCustomers(list);
        if (list.isEmpty()) return;

        System.out.print("请选择客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());

        // 子用例：提交外出申请
        Outward o = new Outward();
        o.setIs_delete(0);
        o.setCustomer_id(customerId);
        System.out.print("外出事由："); o.setOutgoingreason(sc.nextLine());
        System.out.print("外出时间(yyyy-MM-dd)：");
        o.setOutgoingtime(java.sql.Date.valueOf(sc.nextLine()));
        System.out.print("预计回院时间(yyyy-MM-dd)：");
        o.setExpectedreturntime(java.sql.Date.valueOf(sc.nextLine()));
        System.out.print("陪同人："); o.setEscorted(sc.nextLine());
        System.out.print("与老人关系："); o.setRelation(sc.nextLine());
        System.out.print("陪同人电话："); o.setEscortedtel(sc.nextLine());
        o.setAuditstatus(0);   // 已提交
        o.setAuditperson(null);
        o.setAudittime(null);

        boolean ok = outwardService.CreateOutward(o);
        System.out.println(ok ? "外出申请已提交" : "提交失败");
    }

    private static void listOutward() {
        // 子用例：查询自己服务的客户信息列表
        System.out.print("客户姓名（模糊，直接回车查全部）：");
        String name = sc.nextLine().trim();
        List<Customer> list = myCustomersByName(name);
        printCustomers(list);
        if (list.isEmpty()) return;

        System.out.print("请选择客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());

        // 子用例：浏览外出申请列表
        List<Outward> outs = new ArrayList<>();
        for (Outward o : outwardService.GetOutwards()) {
            if (customerId.equals(o.getCustomer_id())
                    && (o.getIs_delete() == null || o.getIs_delete() == 0)) {
                outs.add(o);
            }
        }

        if (outs.isEmpty()) {
            System.out.println("暂无外出申请");
            return;
        }
        for (Outward o : outs) {
            System.out.println("  申请id=" + o.getId()
                    + ", 事由=" + o.getOutgoingreason()
                    + ", 外出=" + o.getOutgoingtime()
                    + ", 预计回院=" + o.getExpectedreturntime()
                    + ", 实际回院=" + o.getActualreturntime()
                    + ", 审批状态=" + o.getAuditstatus());
        }
    }

    // ===================== 用例 4.2：退住申请 =====================

    private static void backdownMenu() {
        while (true) {
            System.out.println("\n--- 退住申请 ---");
            System.out.println("1. 提交退住申请");
            System.out.println("2. 浏览退住申请列表");
            System.out.println("0. 返回上级");
            System.out.print("请选择：");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": addBackdown(); break;
                    case "2": listBackdown(); break;
                    case "0": return;
                    default: System.out.println("无效序号。");
                }
            } catch (Exception e) {
                System.out.println("操作失败：" + e.getMessage());
            }
        }
    }

    private static void addBackdown() {
        // 子用例：查询自己服务的客户信息列表
        System.out.print("客户姓名（模糊，直接回车查全部）：");
        String name = sc.nextLine().trim();
        List<Customer> list = myCustomersByName(name);
        printCustomers(list);
        if (list.isEmpty()) return;

        System.out.print("请选择客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());

        // 子用例：提交退住申请
        Backdown b = new Backdown();
        b.setIs_delete(0);
        b.setCustomer_id(customerId);
        System.out.print("退住类型(0正常退住 1死亡退住 2保留床位)：");
        b.setRetreattype(Integer.parseInt(sc.nextLine()));
        System.out.print("退住原因："); b.setRetreatreason(sc.nextLine());
        System.out.print("退住时间(yyyy-MM-dd)：");
        b.setRetreattime(java.sql.Date.valueOf(sc.nextLine()));
        b.setAuditstatus(0);   // 已提交
        b.setAuditperson(null);
        b.setAudittime(null);

        boolean ok = backdownService.createBackdown(b);
        System.out.println(ok ? "退住申请已提交" : "提交失败");
    }

    private static void listBackdown() {
        // 子用例：查询自己服务的客户信息列表
        System.out.print("客户姓名（模糊，直接回车查全部）：");
        String name = sc.nextLine().trim();
        List<Customer> list = myCustomersByName(name);
        printCustomers(list);
        if (list.isEmpty()) return;

        System.out.print("请选择客户 id：");
        Integer customerId = Integer.parseInt(sc.nextLine());

        // 子用例：浏览退住申请列表
        List<Backdown> backdowns = new ArrayList<>();
        for (Backdown b : backdownService.findAllBackdowns()) {
            if (customerId.equals(b.getCustomer_id())
                    && (b.getIs_delete() == null || b.getIs_delete() == 0)) {
                backdowns.add(b);
            }
        }

        if (backdowns.isEmpty()) {
            System.out.println("暂无退住申请");
            return;
        }
        for (Backdown b : backdowns) {
            System.out.println("  申请id=" + b.getId()
                    + ", 退住类型=" + b.getRetreattype()
                    + ", 退住原因=" + b.getRetreatreason()
                    + ", 退住时间=" + b.getRetreattime()
                    + ", 审批状态=" + b.getAuditstatus());
        }
    }
}