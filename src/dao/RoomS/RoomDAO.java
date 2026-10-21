package dao.RoomS;

import entity.RoomS.Room;

import java.sql.SQLException;
import java.util.List;

public interface RoomDAO {
    boolean save(Room room) throws SQLException;
    boolean update(Room room) throws SQLException;
    boolean delete(Integer id) throws SQLException;
    Room findById(Integer id) throws SQLException;
    List<Room> findAll() throws SQLException;

    /** 根据楼层查询房间列表 */
    List<Room> findByFloor(String roomFloor) throws SQLException;

    /** 查询所有楼层（去重） */
    List<String> findAllFloors() throws SQLException;
}