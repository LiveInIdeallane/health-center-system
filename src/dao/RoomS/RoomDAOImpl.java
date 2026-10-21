package dao.RoomS;

import entity.RoomS.Room;
import utils.CRUDUtils;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    @Override
    public boolean save(Room room) throws SQLException {
        String sql = "insert into room(id,room_floor,room_no) values(?,?,?)";
        Object[] objs = {room.getId(), room.getRoom_floor(), room.getRoom_no()};
        return CRUDUtils.update(sql, objs);
    }

    @Override
    public boolean update(Room room) throws SQLException {
        String sql = "update room set room_floor=?,room_no=? where id=?";
        Object[] objs = {room.getRoom_floor(), room.getRoom_no(), room.getId()};
        return CRUDUtils.update(sql, objs);
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "delete from room where id=?";
        Object[] objs = {id};
        return CRUDUtils.update(sql, objs);
    }

    @Override
    public Room findById(Integer id) throws SQLException {
        String sql = "select * from room where id=?";
        Object[] objects = {id};
        List list = CRUDUtils.select(sql, objects, Room.class);
        return list.size() > 0 ? (Room) list.get(0) : null;
    }

    @Override
    public List<Room> findAll() throws SQLException {
        String sql = "select * from room";
        Object[] objects = {};
        return CRUDUtils.select(sql, objects, Room.class);
    }

    @Override
    public List<Room> findByFloor(String roomFloor) throws SQLException {
        String sql = "select * from room where room_floor = ? order by room_no";
        Object[] objs = {roomFloor};
        return CRUDUtils.select(sql, objs, Room.class);
    }

    @Override
    public List<String> findAllFloors() throws SQLException {
        String sql = "select distinct room_floor from room order by room_floor";
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        ResultSet rs = stat.executeQuery();
        List<String> floors = new ArrayList<>();
        while (rs.next()) {
            floors.add(rs.getString(1));
        }
        return floors;
    }
}