package service.BackdownS;

import dao.BackdownS.BackdownDAO;
import dao.BackdownS.BackdownDAOImpl;
import entity.BackdownS.Backdown;
import utils.*;

import java.sql.SQLException;
import java.util.List;

public class BackdownServiceImpl implements BackdownService {

    private BackdownDAO backdownDAO = new BackdownDAOImpl();

    @Override
    public boolean createBackdown(Backdown backdown) {
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            boolean b = backdownDAO.saveBackdown(backdown);
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
    public boolean updateBackdown(Backdown backdown) {

        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            boolean b = backdownDAO.updateBackdown(backdown);
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
    public boolean deleteBackdown(Backdown backdown) {
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            boolean b = backdownDAO.deleteBackdown(backdown.getId());
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
    public Backdown findBackdownById(Integer id) {
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            Backdown backdown = backdownDAO.findBackdownById(id);
            JdbcUtils.getConnection().commit();
            return backdown;
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
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
    public List<Backdown> findAllBackdowns() {
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            List<Backdown> backdowns= backdownDAO.getAllBackdowns();
            JdbcUtils.getConnection().commit();
            return backdowns;
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
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
