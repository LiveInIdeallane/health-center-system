package dao.CustomernurseitemS;



import entity.CustomernurseitemS.Customernurseitem;

import java.sql.SQLException;
import java.util.List;

public interface CustomernurseitemDao {
    boolean save(Customernurseitem obj) throws SQLException;
    boolean update(Customernurseitem obj)  throws SQLException;
    boolean delete(Integer id)  throws SQLException;
    Customernurseitem findOne(Integer id)  throws SQLException;
    List<Customernurseitem> findAll()  throws SQLException;
}
