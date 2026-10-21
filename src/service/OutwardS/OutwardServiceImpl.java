package service.OutwardS;

import dao.OutwardS.OutwardDAO;

import dao.OutwardS.OutwardDAO;
import dao.OutwardS.OutwardDAOImpl;
import entity.OutwardS.Outward;
import utils.JdbcUtils;

import java.sql.SQLException;
import java.util.List;

public class OutwardServiceImpl implements OutwardService {

    OutwardDAO outwardDAO = new OutwardDAOImpl();

    @Override
    public boolean CreateOutward(Outward outward){
        //进行数据校验



        //进行事务处理
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            boolean b = outwardDAO.saveOutward(outward);
            JdbcUtils.getConnection().commit();
            return b;
        } catch (SQLException e) {
            try {
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close();
        }
    }

    @Override
    public boolean UpdateOutward(Outward outward){

        //进行数据检测


        //进行事务处理
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            boolean b = outwardDAO.updateOutward(outward);
            JdbcUtils.getConnection().commit();
            return b;
        } catch (SQLException e) {
            try {
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close();
        }
    }


    @Override
    public boolean DeleteOutward(Outward  outward){
        //进行数据检测


        //进行事务处理
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            boolean b = outwardDAO.deleteOutward(outward.getId());
            JdbcUtils.getConnection().commit();
            return b;
        } catch (SQLException e) {
            try {
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close();
        }
    }

    @Override
    public Outward GetOutwardById(Integer id){

        //进行数据检测

        //进行事务处理
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            Outward outward = outwardDAO.findOutwardById(id);
            JdbcUtils.getConnection().commit();
            return outward;
        } catch (SQLException e) {
            try {
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close();
        }

    }

    @Override
    public List<Outward> GetOutwards() {

        //直接进行事务处理
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            List<Outward> outwards = outwardDAO.findAllOutward();
            JdbcUtils.getConnection().commit();
            return outwards;
        } catch (SQLException e) {
            try {
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close();
        }
    }
}
