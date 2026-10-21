package service.BedS;


import dao.BedDetailsS.BedDetailsDAO;
import dao.BedDetailsS.BedDetailsDAOImpl;
import dao.BedS.BedDAO;
import dao.BedS.BedDAOImpl;
import dao.RoomS.RoomDAO;
import dao.RoomS.RoomDAOImpl;
import entity.BedS.Bed;
import entity.BedS.BedVO;
import entity.BedDetailsS.BedDetails;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import dao.CustomerS.CustomerDAO;
import dao.CustomerS.CustomerDAOImpl;
import entity.CustomerS.Customer;


public class BedServiceImpl implements BedService {

    private static BedDAO bedDAO = new BedDAOImpl();
    private static BedDetailsDAO bedDetailsDAO = new BedDetailsDAOImpl();
    private static RoomDAO roomDAO = new RoomDAOImpl();
    private static CustomerDAO customerDAO = new CustomerDAOImpl();

    // ==================== 基础 CRUD ====================

    @Override
    public boolean addBed(Bed bed) {
        if (bed == null) throw new RuntimeException("参数对象不能为空!");
        if (bed.getId() == null) throw new RuntimeException("床位Id不能为空!");
        if (bed.getRoom_no() == null) throw new RuntimeException("房间编号不能为空!");
        if (bed.getBed_status() == null) throw new RuntimeException("房间状态不能为空!");
        if (bed.getBed_status() != 1 && bed.getBed_status() != 2 && bed.getBed_status() != 3)
            throw new RuntimeException("房间状态只能为1、2、3!");
        if (bed.getBed_no() == null) throw new RuntimeException("床位编号不能为空!");
        try {
            return bedDAO.save(bed);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateBed(Bed bed) {
        if (bed == null) throw new RuntimeException("参数对象不能为空!");
        if (bed.getId() == null) throw new RuntimeException("床位Id不能为空!");
        if (bed.getRoom_no() == null) throw new RuntimeException("房间编号不能为空!");
        if (bed.getBed_status() == null) throw new RuntimeException("房间状态不能为空!");
        if (bed.getBed_status() != 1 && bed.getBed_status() != 2 && bed.getBed_status() != 3)
            throw new RuntimeException("房间状态只能为1、2、3!");
        if (bed.getBed_no() == null) throw new RuntimeException("床位编号不能为空!");
        try {
            return bedDAO.update(bed);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteBed(Integer id) {
        if (id == null) throw new RuntimeException("ID不能为空！");
        try {
            return bedDAO.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Bed getBedById(Integer id) {
        if (id == null) throw new RuntimeException("ID不能为空！");
        try {
            return bedDAO.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Bed> getAllBeds() {
        try {
            return bedDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== 床位示意图 ====================

    @Override
    public Map<String, Object> getBedMapByFloor(String roomFloor) {
        if (roomFloor == null || roomFloor.trim().isEmpty())
            throw new RuntimeException("楼层不能为空！");
        try {
            List<BedVO> bedVOList = bedDAO.findVOByFloor(roomFloor);

            Map<Integer, Map<String, Object>> roomMap = new LinkedHashMap<>();
            for (BedVO vo : bedVOList) {
                Integer roomNo = vo.getRoomNo();
                roomMap.putIfAbsent(roomNo, new HashMap<>());
                Map<String, Object> room = roomMap.get(roomNo);
                room.put("roomNo", roomNo);

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> beds = (List<Map<String, Object>>) room.get("beds");
                if (beds == null) {
                    beds = new ArrayList<>();
                    room.put("beds", beds);
                }

                Map<String, Object> bedMap = new HashMap<>();
                bedMap.put("bedId", vo.getId());
                bedMap.put("bedNo", vo.getBedNo());
                bedMap.put("status", vo.getRoomStatus());

                if ("有人".equals(vo.getRoomStatus())) {
                    BedDetails details = bedDetailsDAO.findActiveByBedId(vo.getId());
                    if (details != null) {
                        bedMap.put("customerId", details.getCustomer_id());
                    }
                }
                beds.add(bedMap);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("floor", roomFloor);
            result.put("rooms", new ArrayList<>(roomMap.values()));
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== 统计 ====================

    @Override
    public Map<String, Integer> countBedStatus() {
        try {
            int free = bedDAO.countByStatus(1);
            int occupied = bedDAO.countByStatus(2);
            int out = bedDAO.countByStatus(3);
            int total = free + occupied + out;

            Map<String, Integer> result = new HashMap<>();
            result.put("total", total);
            result.put("free", free);
            result.put("occupied", occupied);
            result.put("out", out);
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== 状态修改 ====================

    @Override
    public boolean changeBedStatus(Integer bedId, Integer bedStatus) {
        if (bedId == null) throw new RuntimeException("床位ID不能为空！");
        if (bedStatus == null) throw new RuntimeException("床位状态不能为空！");
        if (bedStatus != 1 && bedStatus != 2 && bedStatus != 3)
            throw new RuntimeException("床位状态只能为1、2、3！");
        try {
            return bedDAO.updateStatus(bedId, bedStatus);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== 下拉联动 ====================

    @Override
    public List<Bed> getFreeBedsByRoomNo(Integer roomNo) {
        if (roomNo == null) throw new RuntimeException("房间号不能为空！");
        try {
            return bedDAO.findFreeBedsByRoomNo(roomNo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getAllFloors() {
        try {
            return roomDAO.findAllFloors();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== 床位调换（事务保护） ====================

    @Override
    public boolean changeBed(Integer customerId, Integer newBedId) {
        if (customerId == null || newBedId == null)
            throw new RuntimeException("客户ID和新床位ID不能为空！");

        try {
            // 1. 查询客户当前生效的床位详情
            BedDetails oldDetails = bedDetailsDAO.findActiveByCustomerId(customerId);
            if (oldDetails == null)
                throw new RuntimeException("该客户没有正在使用的床位！");

            // 2. 判断新旧床位是否相同
            if (newBedId.equals(oldDetails.getBed_id()))
                throw new RuntimeException("新床位和旧床位相同，无需调换！");

            // 3. 校验新床位
            Bed newBed = bedDAO.findById(newBedId);
            if (newBed == null) throw new RuntimeException("新床位不存在！");
            if (newBed.getBed_status() != 1)
                throw new RuntimeException("新床位不是空闲状态，无法调换！");

            // 4. 查出完整客户对象（updateCustomer 是全字段 UPDATE，必须先查再改）
            Customer customer = customerDAO.findCustomer(customerId);
            if (customer == null) throw new RuntimeException("客户不存在！");

            // ===== 事务开始 =====
            Connection conn = JdbcUtils.getConnection();
            boolean oldAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                Date today = new Date();

                // 5. 旧床位详情：结束日期=今天，逻辑删除
                bedDetailsDAO.updateEndDate(oldDetails.getId(), today);
                bedDetailsDAO.logicDelete(oldDetails.getId());

                // 6. 新增新床位详情
                BedDetails newDetails = new BedDetails();
                newDetails.setCustomer_id(customerId);
                newDetails.setBed_id(newBedId);
                newDetails.setStart_date(today);
                newDetails.setIs_deleted(0);
                bedDetailsDAO.save(newDetails);

                // 7. 修改床位状态
                bedDAO.updateStatus(oldDetails.getBed_id(), 1);   // 旧床位空闲
                bedDAO.updateStatus(newBedId, 2);                 // 新床位有人

                // 8. 【关键新增】同步更新客户信息：房间号、床号、楼号
                customer.setBed_id(newBedId);                                // 床号（bed.id）
                customer.setRoom_no(String.valueOf(newBed.getRoom_no()));    // 房间号（varchar）
                customer.setBuilding_no("606");                              // 楼栋固定 606
                customerDAO.updateCustomer(customer);

                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException("床位调换失败：" + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(oldAutoCommit);
            }
            // ===== 事务结束 =====

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}