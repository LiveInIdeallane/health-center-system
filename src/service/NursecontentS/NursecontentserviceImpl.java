package service.NursecontentS;

import dao.NursecontentS.NursecontentDao;
import dao.NursecontentS.NursecontentDaoImpl;
import entity.NursecontentS.Nursecontent;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class NursecontentserviceImpl implements Nursecontentservice {
    private NursecontentDao nursecontentdao=new NursecontentDaoImpl();
    public boolean addNursecontent(Nursecontent nursecontent) {
        if(nursecontent==null){
            throw new RuntimeException("参数不能为空");
        }
        if(nursecontent.getSerial_number()==null||nursecontent.getSerial_number().trim().isEmpty()){
          throw  new RuntimeException("编号不能为空");
        }
        if(nursecontent.getNursing_name()==null||nursecontent.getNursing_name().trim().isEmpty()){
            throw  new RuntimeException("名称不能为空");

        }
        if(nursecontent.getService_price()==null||nursecontent.getService_price().trim().isEmpty()){
            throw  new RuntimeException("服务价格不能为空");
        }
        Integer status=nursecontent.getStatus();
        if(status==null){
            throw  new RuntimeException("状态不能为空");
        }
        if(status!=1&&status!=2){
            throw  new RuntimeException("状态只能为1和2");
        }
        Connection conn = null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false); //关闭自动提交，开启事务
            boolean res = nursecontentdao.save(nursecontent);
            conn.commit(); //正常提交
            return res;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); //出错回滚
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close();
        }
    }
    public boolean updateNursecontent(Nursecontent nursecontent) {
        if(nursecontent==null){
            throw new RuntimeException("参数不能为空");
        }
        if(nursecontent.getSerial_number()==null||nursecontent.getSerial_number().trim().isEmpty()){
            throw  new RuntimeException("编号不能为空");
        }
        if(nursecontent.getNursing_name()==null||nursecontent.getNursing_name().trim().isEmpty()){
            throw  new RuntimeException("名称不能为空");

        }
        if(nursecontent.getService_price()==null||nursecontent.getService_price().trim().isEmpty()){
            throw  new RuntimeException("服务价格不能为空");
        }
        Integer status=nursecontent.getStatus();
        if(status==null){
            throw  new RuntimeException("状态不能为空");
        }
        if(status!=1&&status!=2){
            throw  new RuntimeException("状态只能为1和2");
        }
        Connection conn = null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false); //关闭自动提交，开启事务
            boolean res = nursecontentdao.update(nursecontent);
            conn.commit(); //正常提交
            return res;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); //出错回滚
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close();
}}

    @Override
    public boolean deleteNursecontent(Integer id) {
        if(id==null){
            throw new RuntimeException("id不能为空");
        }
        Connection conn = null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false); //关闭自动提交，开启事务
            boolean res = nursecontentdao.delete(id);
            conn.commit(); //正常提交
            return res;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); //出错回滚
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close();

    }

}

    @Override
    public Nursecontent findNursecontentByid(Integer id) throws SQLException {
        if (id == null) {
            throw new RuntimeException("id不能为空");
        }
        return nursecontentdao.findOne(id);
    }

    @Override
    public List<Nursecontent> findAllNursecontent() throws SQLException {
        return nursecontentdao.findAll();
    }
}
