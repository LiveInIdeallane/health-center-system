package service.RoomS;


import dao.RoomS.RoomDAO;
import dao.RoomS.RoomDAOImpl;
import entity.RoomS.Room;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoomServiceImpl implements RoomService {

    private static RoomDAO roomDAO = new RoomDAOImpl();

    @Override
    public boolean addRoom(Room room) {
        if (room == null) {
            throw new RuntimeException("参数对象不能为空!");
        }
        if (room.getId() == null) {
            throw new RuntimeException("房间ID不能为空!");
        }
        if (room.getRoom_floor() == null) {
            throw new RuntimeException("楼层不能为空!");
        }
        if (room.getRoom_no() == null) {
            throw new RuntimeException("房间号不能为空!");
        }
        try {
            return roomDAO.save(room);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateRoom(Room room) {
        if (room == null) {
            throw new RuntimeException("参数对象不能为空!");
        }
        if (room.getId() == null) {
            throw new RuntimeException("房间ID不能为空!");
        }
        try {
            return roomDAO.update(room);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteRoom(Integer id) {
        if (id == null) {
            throw new RuntimeException("ID不能为空！");
        }
        try {
            return roomDAO.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Room getRoomById(Integer id) {
        if (id == null) {
            throw new RuntimeException("ID不能为空！");
        }
        try {
            return roomDAO.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Room> getAllRooms() {
        try {
            return roomDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Room> getRoomsByFloor(String floor) {
        if (floor == null || floor.trim().isEmpty()) {
            throw new RuntimeException("楼层不能为空！");
        }
        try {
            return roomDAO.findByFloor(floor);
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

    @Override
    public Map<String, List<String>> getRoomsGroupByFloor() {
        Map<String, List<String>> result = new LinkedHashMap<>();
        try {
            for (Room room : roomDAO.findAll()) {
                String floor = room.getRoom_floor();
                if (floor == null) {
                    floor = "-";
                }
                List<String> rooms = result.get(floor);
                if (rooms == null) {
                    rooms = new ArrayList<>();
                    result.put(floor, rooms);
                }
                rooms.add(room.getRoom_no() == null ? "" : String.valueOf(room.getRoom_no()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}