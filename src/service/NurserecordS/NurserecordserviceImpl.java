package service.NurserecordS;

import dao.NurserecordS.NurserecordDao;
import dao.NurserecordS.NurserecordDaoImpl;
import entity.NurserecordS.Nurserecord;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class NurserecordserviceImpl implements Nurserecordservice {
    private NurserecordDao nurserecorddao=new NurserecordDaoImpl();

    @Override
    public boolean addNurserecord(Nurserecord nurserecord) {
        if(nurserecord == null){
            throw new RuntimeException("护理记录对象不能为空！");
        }

        // 逻辑删除标记校验
        Integer del = nurserecord.getIs_deleted();
        if(del == null || (del != 0 && del !=1)){
            throw new RuntimeException("删除标记只能是0(显示)或1(隐藏)");
        }

        //客户id
        if(nurserecord.getCustomer_id() == null){
            throw new RuntimeException("客户ID不能为空");
        }
        //护理项目id
        if(nurserecord.getItem_id() == null){
            throw new RuntimeException("护理项目ID不能为空");
        }
        //护理时间
        if(nurserecord.getNursing_time() == null){
            throw new RuntimeException("护理时间不能为空");
        }
        //护理数量
        Integer count = nurserecord.getNursing_count();
        if(count == null || count <= 0){
            throw new RuntimeException("护理数量必须大于0");
        }
        //护理人员id
        if(nurserecord.getUser_id() == null){
            throw new RuntimeException("护理人员ID不能为空");
        }

        Connection conn = null;
        try{
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false);
            boolean res = nurserecorddao.save(nurserecord);
            conn.commit();
            return res;
        }catch (SQLException e){
            if(conn != null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close();
        }

    }
    public boolean updateNurserecord(Nurserecord nurserecord) {
        if(nurserecord == null){
            throw new RuntimeException("护理记录对象不能为空！");
        }

        // 逻辑删除标记校验
        Integer del = nurserecord.getIs_deleted();
        if(del == null || (del != 0 && del !=1)){
            throw new RuntimeException("删除标记只能是0(显示)或1(隐藏)");
        }

        //客户id
        if(nurserecord.getCustomer_id() == null){
            throw new RuntimeException("客户ID不能为空");
        }
        //护理项目id
        if(nurserecord.getItem_id() == null){
            throw new RuntimeException("护理项目ID不能为空");
        }
        //护理时间
        if(nurserecord.getNursing_time() == null){
            throw new RuntimeException("护理时间不能为空");
        }
        //护理数量
        Integer count = nurserecord.getNursing_count();
        if(count == null || count <= 0){
            throw new RuntimeException("护理数量必须大于0");
        }
        //护理人员id
        if(nurserecord.getUser_id() == null){
            throw new RuntimeException("护理人员ID不能为空");
        }

        Connection conn = null;
        try{
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false);
            boolean res = nurserecorddao.update(nurserecord);
            conn.commit();
            return res;
        }catch (SQLException e){
            if(conn != null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close();
        }
}

    @Override
    public boolean deleteNurserecord(Integer id) {
        if(id == null){
            throw new RuntimeException("id不能为空");
        }
        Connection conn = null;
        try{
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false);
            boolean res = nurserecorddao.delete(id);
            conn.commit();
            return res;
        }catch (SQLException e){
            if(conn != null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close();
        }

    }

    @Override
    public Nurserecord findnurserecordById(Integer id) throws SQLException {
        if(id == null){
            throw new RuntimeException("id不能为空");
        }
        return nurserecorddao.findOne(id);
    }

    @Override
    public List<Nurserecord> findAllNurserecord() throws SQLException {
        return nurserecorddao.findAll();
    }
}
