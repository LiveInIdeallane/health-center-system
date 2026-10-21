package dao.NurserecordS;

import entity.NurserecordS.Nurserecord;
import utils.CRUDUtils;

import java.sql.SQLException;
import java.util.List;

public class NurserecordDaoImpl implements NurserecordDao{
    @Override
    public boolean save(Nurserecord obj) throws SQLException {
        String sql="insert into Nurserecord values(?,?,?,?,?,?,?,?)";
        Object[] objects={obj.getId(),obj.getIs_deleted(),obj.getCustomer_id(),obj.getItem_id(),obj.getNursing_time(),obj.getNursing_content(),obj.getNursing_count(),obj.getUser_id()};
        return CRUDUtils.update(sql,objects);
    }
    @Override
    public boolean update(Nurserecord obj) throws SQLException {
        String sql="update Nurserecord set is_deleted=?,customer_id=?,item_id=?,nursing_time=?,nursing_content=?,nursing_count=? where id=?";
        Object[] objects={obj.getIs_deleted(),obj.getCustomer_id(),obj.getItem_id(),obj.getNursing_time(),obj.getNursing_content(),obj.getNursing_count(),obj.getId()};
        return CRUDUtils.update(sql,objects);
    }

    @Override
    public boolean delete(Integer id) throws  SQLException {
        String sql="delete from Nurserecord where id=?";
        Object[] objects={id};
        return CRUDUtils.update(sql,objects);
    }

    @Override
    public Nurserecord findOne(Integer id) throws SQLException {
        String sql="select * from Nurserecord where id=?";
        Object[] objects={id};
        List list =CRUDUtils.select(sql,objects,Nurserecord.class);
        return list.size()>0?(Nurserecord)list.get(0):null;
    }

    @Override
    public List<Nurserecord> findAll() throws SQLException {
        String sql="select * from Nurserecord";
        Object[] objects={};
        return CRUDUtils.select(sql,objects,Nurserecord.class);
    }
}
