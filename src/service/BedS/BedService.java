package service.BedS;


import entity.BedS.Bed;

import java.util.List;
import java.util.Map;

public interface BedService {

    /** 新增床位 */
    boolean addBed(Bed bed);

    /** 修改床位 */
    boolean updateBed(Bed bed);

    /** 删除床位 */
    boolean deleteBed(Integer id);

    /** 根据 id 查询床位 */
    Bed getBedById(Integer id);

    /** 查询所有床位 */
    List<Bed> getAllBeds();

    /** 按楼层查询房间及床位列表（床位示意图） */
    Map<String, Object> getBedMapByFloor(String roomFloor);

    /** 统计总床位、空闲、有人、外出数量 */
    Map<String, Integer> countBedStatus();

    /** 修改床位状态 */
    boolean changeBedStatus(Integer bedId, Integer bedStatus);

    List<Bed> getFreeBedsByRoomNo(Integer roomNo);

    List<String> getAllFloors();

    boolean changeBed(Integer customerId, Integer newBedId);
}