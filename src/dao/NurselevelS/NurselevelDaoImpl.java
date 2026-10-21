package dao.NurselevelS;

import entity.NurselevelS.Nurselevel;
import utils.CRUDUtils;

import java.sql.SQLException;
import java.util.List;

public class NurselevelDaoImpl implements NurselevelDao {
    @Override
    public boolean save(Nurselevel obj) throws SQLException {
        String sql = "insert into Nurselevel values(?,?,?,?)";
        Object[] objects={obj.getId(),obj.getLevel_name(),obj.getLevel_status(),obj.getIs_deleted()};
        return CRUDUtils.update(sql,objects);
    }

    @Override
    public boolean update(Nurselevel obj) throws SQLException {
        String sql ="update Nurselevel set level_name=?,level_status=?, is_deleted=? where id=?";
        Object[] objects={obj.getLevel_name(),obj.getLevel_status(),obj.getIs_deleted(),obj.getId()};
        return CRUDUtils.update(sql,objects);
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "delete from Nurselevel where id=?";
        Object[] objects={id};
        return CRUDUtils.update(sql,objects);
    }

    @Override
    public Nurselevel findOne(Integer id) throws SQLException {
        String sql = "select * from Nurselevel where id=?";
        Object[] objects={id};
        List list = CRUDUtils.select(sql, objects, Nurselevel.class);
        return list.size()>0?(Nurselevel) list.get(0):null ;
    }
    public  List<Nurselevel> findAll() throws SQLException {
        String sql = "select * from Nurselevel";
        Object[] objects = {};
        return CRUDUtils.select(sql, objects, Nurselevel.class);
    }
}

