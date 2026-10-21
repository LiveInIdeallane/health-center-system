package service.NursecontentS;

import entity.NursecontentS.Nursecontent;

import java.sql.SQLException;
import java.util.List;


public interface Nursecontentservice {
    boolean addNursecontent(Nursecontent nursecontent);
    boolean updateNursecontent(Nursecontent nursecontent);
    boolean deleteNursecontent(Integer id);
    Nursecontent findNursecontentByid(Integer id) throws SQLException;
    List<Nursecontent> findAllNursecontent() throws SQLException;

}
