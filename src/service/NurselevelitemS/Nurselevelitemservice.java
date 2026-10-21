package service.NurselevelitemS;

import entity.NurselevelitemS.Nurselevelitem;

import java.sql.SQLException;
import java.util.List;


public interface Nurselevelitemservice {
    boolean addNurselevelitem(Nurselevelitem nurselevelitem);
    boolean updateNurselevelitem(Nurselevelitem nurselevelitem);
    boolean deleteNurselevelitem(Integer id);
    Nurselevelitem findNurselevelitemById(Integer id) throws SQLException;
    List<Nurselevelitem> findAllNurselevelitem() throws SQLException;

}
