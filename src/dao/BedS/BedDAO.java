package dao.BedS;

import entity.BedS.Bed;
import entity.BedS.BedVO;

import java.sql.SQLException;
import java.util.List;

public interface BedDAO {
    boolean save(Bed bed) throws SQLException;
    boolean update(Bed bed) throws SQLException;
    boolean delete(Integer id) throws SQLException;
    Bed findById(Integer id) throws SQLException;
    List<Bed> findAll() throws SQLException;
    List<BedVO> findAllVO() throws SQLException;

    /** 根据楼层查询床位VO列表（床位示意图用） */
    List<BedVO> findVOByFloor(String roomFloor) throws SQLException;

    /** 统计各状态床位数量 */
    int countByStatus(Integer bedStatus) throws SQLException;

    /** 修改床位状态 */
    boolean updateStatus(Integer bedId, Integer bedStatus) throws SQLException;

    /** 根据房间号查询空闲床位 */
    List<Bed> findFreeBedsByRoomNo(Integer roomNo) throws SQLException;
}