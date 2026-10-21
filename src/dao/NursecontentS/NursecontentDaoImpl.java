package dao.NursecontentS;


import entity.NursecontentS.Nursecontent;
import utils.CRUDUtils;

import java.sql.SQLException;
import java.util.List;

public class NursecontentDaoImpl implements NursecontentDao {
        @Override
        public boolean save(Nursecontent obj) throws SQLException {
            String sql = "insert into Nursecontent(id,serial_number,nursing_name,service_price,message,status,execution_cycle,execution_times,is_deleted) values(?,?,?,?,?,?,?,?,?)";

            Object[] objects = {obj.getId(),obj.getSerial_number(),obj.getNursing_name(),obj.getService_price(),obj.getMessage(),obj.getStatus(),obj.getExecution_cycle(),obj.getExecution_times(),obj.getIs_deleted()};
            return CRUDUtils.update(sql,objects);
        }
        @Override
        public boolean update(Nursecontent obj) throws SQLException {
            String sql = "update Nursecontent set serial_number=?,nursing_name=?,service_price=?,message=?,status=?,execution_cycle=?,execution_times=?,is_deleted=? where id=?";
            Object[] objects =
                    {obj.getSerial_number(),obj.getNursing_name(),obj.getService_price(),obj.getMessage(),obj.getStatus(),obj.getExecution_cycle(),obj.getExecution_times(),obj.getIs_deleted(),obj.getId()};
            return CRUDUtils.update(sql,objects);
        }
        @Override
        public boolean delete(Integer id) throws SQLException {
            String sql = "delete from Nursecontent where id=?";
            Object[] objects = {id};
            return CRUDUtils.update(sql,objects);
        }
        @Override
        public Nursecontent findOne(Integer id) throws SQLException {
            String sql = "select * from Nursecontent where id=?";
            Object[] objects = {id};
            List list = CRUDUtils.select(sql, objects, Nursecontent.class);
            return list.size()>0? (Nursecontent) list.get(0) :null;
        }
        @Override
        public List<Nursecontent> findAll() throws SQLException {
            String sql = "select * from Nursecontent";
            Object[] objects = {};
            return CRUDUtils.select(sql, objects, Nursecontent.class);
        }
    }

