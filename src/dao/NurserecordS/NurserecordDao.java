package dao.NurserecordS;

import entity.NurserecordS.Nurserecord;

import java.sql.SQLException;
import java.util.List;

public interface NurserecordDao {
        boolean save(Nurserecord obj) throws SQLException;
        boolean update(Nurserecord obj)  throws SQLException;
        boolean delete(Integer id)  throws SQLException;
        Nurserecord findOne(Integer id)  throws SQLException;
        List<Nurserecord> findAll()  throws SQLException;
}
