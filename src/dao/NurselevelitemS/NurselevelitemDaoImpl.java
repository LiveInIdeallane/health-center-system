package dao.NurselevelitemS;
import entity.NurselevelitemS.Nurselevelitem;
import utils.CRUDUtils;

import java.sql.SQLException;
import java.util.List;

public class NurselevelitemDaoImpl implements NurselevelitemDao {
    @Override
    public boolean save(Nurselevelitem obj) throws SQLException {
        String sql="insert into Nurselevelitem values(?,?,?)";
        Object[] objects={obj.getId(),obj.getLevel_id(),obj.getItem_id()};
        return CRUDUtils.update(sql,objects);
    }

    @Override
    public boolean update(Nurselevelitem obj) throws SQLException {
        String sql="update Nurselevelitem set level_id=?,item_id=? where id=?";
        Object[] objects={obj.getLevel_id(),obj.getItem_id(),obj.getId()};
        return CRUDUtils.update(sql,objects);
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql="delete from Nurselevelitem where id=?";
        Object[] objects={id};
        return CRUDUtils.update(sql,objects);
    }

    @Override
    public Nurselevelitem findOne(Integer id) throws SQLException {
        String sql="select * from Nurselevelitem where id=?";
        Object[] objects={id};
        List list=CRUDUtils.select(sql, objects, Nurselevelitem.class);
        return list.size()>0?(Nurselevelitem) list.get(0):null ;
    }

    @Override
    public List <Nurselevelitem> findAll() throws SQLException {
        String sql="select * from Nurselevelitem";
        Object[] objects={};
        return CRUDUtils.select(sql, objects, Nurselevelitem.class);
    }
}
