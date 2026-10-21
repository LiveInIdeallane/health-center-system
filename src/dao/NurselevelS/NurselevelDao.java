package dao.NurselevelS;

import entity.NurselevelS.Nurselevel;

import java.sql.SQLException;
import java.util.List;

public interface NurselevelDao {
    boolean save(Nurselevel obj) throws SQLException;
    boolean update(Nurselevel obj)  throws SQLException;
    boolean delete(Integer id)  throws SQLException;
    Nurselevel findOne(Integer id)  throws SQLException;
    List<Nurselevel> findAll()  throws SQLException;
}
