package service.NurselevelS;

import entity.NurselevelS.Nurselevel;

import java.sql.SQLException;
import java.util.List;


public interface Nurselevelservice {
    boolean addNurselevel(Nurselevel nurselevel);
    boolean updateNurselevel(Nurselevel nurselevel);
    boolean deleteNurselevel(Integer id);
    Nurselevel findNurselevelById(Integer id) throws SQLException;
    List<Nurselevel> findAllNurselevel() throws SQLException;
}
