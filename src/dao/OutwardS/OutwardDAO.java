package dao.OutwardS;

import entity.OutwardS.Outward;

import java.sql.SQLException;
import java.util.List;

public interface OutwardDAO {

    public Boolean saveOutward(Outward Outward) throws SQLException;
    public Boolean deleteOutward(Integer id) throws SQLException;
    public Boolean updateOutward(Outward outward) throws SQLException;
    public Outward findOutwardById(Integer id) throws SQLException;
    public List<Outward> findAllOutward() throws SQLException;

}
