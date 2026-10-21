package dao.BackdownS;

import entity.BackdownS.Backdown;

import java.sql.SQLException;
import java.util.List;

public interface BackdownDAO {

    boolean saveBackdown(Backdown backdown) throws SQLException;
    boolean updateBackdown(Backdown backdown) throws SQLException;
    boolean deleteBackdown(Integer id) throws SQLException;
    Backdown findBackdownById(Integer id) throws SQLException, InstantiationException, IllegalAccessException;
    List<Backdown> getAllBackdowns() throws SQLException, InstantiationException, IllegalAccessException;

}
