package dao.BedS;

import entity.BedS.Bed;
import entity.BedS.BedVO;
import utils.CRUDUtils;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BedDAOImpl implements BedDAO {

    @Override
    public boolean save(Bed bed) throws SQLException {
        String sql = "insert into bed(id,room_no,bed_status,remarks,bed_no) values(?,?,?,?,?)";
        Object[] objs = {bed.getId(), bed.getRoom_no(), bed.getBed_status(), bed.getRemarks(), bed.getBed_no()};
        return CRUDUtils.update(sql, objs);
    }

    @Override
    public boolean update(Bed bed) throws SQLException {
        String sql = "update bed set room_no=?,bed_status=?,remarks=?,bed_no=? where id=?";
        Object[] objs = {bed.getRoom_no(), bed.getBed_status(), bed.getRemarks(), bed.getBed_no(), bed.getId()};
        return CRUDUtils.update(sql, objs);
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "delete from bed where id=?";
        Object[] objs = {id};
        return CRUDUtils.update(sql, objs);
    }

    @Override
    public Bed findById(Integer id) throws SQLException {
        String sql = "select * from bed where id=?";
        Object[] objects = {id};
        List list = CRUDUtils.select(sql, objects, Bed.class);
        return list.size() > 0 ? (Bed) list.get(0) : null;
    }

    @Override
    public List<Bed> findAll() throws SQLException {
        String sql = "select * from bed";
        Object[] objects = {};
        return CRUDUtils.select(sql, objects, Bed.class);
    }

    @Override
    public List<BedVO> findAllVO() throws SQLException {
        String sql = "select bed.id,bed.room_no, case " +
                "when bed.bed_status=1 then '可用' " +
                "when bed.bed_status=2 then '有人' " +
                "when bed.bed_status=3 then '外出' " +
                "end as 'room_status',bed.remarks,bed.bed_no,room.room_floor " +
                "from bed left join room " +
                "on bed.room_no=room.room_no";
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        ResultSet rs = stat.executeQuery();
        List<BedVO> bedList = new ArrayList<>();
        while (rs.next()) {
            BedVO bedVO = new BedVO();
            bedVO.setId(rs.getInt(1));
            bedVO.setRoomNo(rs.getInt(2));
            bedVO.setRoomStatus(rs.getString(3));
            bedVO.setRemarks(rs.getString(4));
            bedVO.setBedNo(rs.getString(5));
            bedVO.setRoomFloor(rs.getString(6));
            bedList.add(bedVO);
        }
        return bedList;
    }

    @Override
    public List<BedVO> findVOByFloor(String roomFloor) throws SQLException {
        String sql = "select bed.id, bed.room_no, " +
                "case " +
                "when bed.bed_status=1 then '可用' " +
                "when bed.bed_status=2 then '有人' " +
                "when bed.bed_status=3 then '外出' " +
                "end as room_status, bed.remarks, bed.bed_no, room.room_floor " +
                "from bed left join room on bed.room_no = room.room_no " +
                "where room.room_floor = ? " +
                "order by bed.room_no, bed.bed_no";
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1, roomFloor);
        ResultSet rs = stat.executeQuery();
        List<BedVO> bedList = new ArrayList<>();
        while (rs.next()) {
            BedVO bedVO = new BedVO();
            bedVO.setId(rs.getInt(1));
            bedVO.setRoomNo(rs.getInt(2));
            bedVO.setRoomStatus(rs.getString(3));
            bedVO.setRemarks(rs.getString(4));
            bedVO.setBedNo(rs.getString(5));
            bedVO.setRoomFloor(rs.getString(6));
            bedList.add(bedVO);
        }
        return bedList;
    }

    @Override
    public int countByStatus(Integer bedStatus) throws SQLException {
        String sql = "select count(*) from bed where bed_status = ?";
        Connection conn = JdbcUtils.getConnection();
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setInt(1, bedStatus);
        ResultSet rs = stat.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    @Override
    public boolean updateStatus(Integer bedId, Integer bedStatus) throws SQLException {
        String sql = "update bed set bed_status = ? where id = ?";
        Object[] objs = {bedStatus, bedId};
        return CRUDUtils.update(sql, objs);
    }

    @Override
    public List<Bed> findFreeBedsByRoomNo(Integer roomNo) throws SQLException {
        String sql = "select * from bed where room_no = ? and bed_status = 1";
        Object[] objs = {roomNo};
        return CRUDUtils.select(sql, objs, Bed.class);
    }
}