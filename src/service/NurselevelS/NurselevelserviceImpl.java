package service.NurselevelS;

import dao.NurselevelS.NurselevelDao;
import dao.NurselevelS.NurselevelDaoImpl;
import entity.NurselevelS.Nurselevel;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class NurselevelserviceImpl implements Nurselevelservice {
    private NurselevelDao nurseDAO = new NurselevelDaoImpl();

    public boolean addNurselevel(Nurselevel nurselevel) {
        if (nurselevel == null) {
            throw new RuntimeException("参数对象不可为空");
        }
        if (nurselevel.getLevel_name() == null || nurselevel.getLevel_name().equals("")) {
            throw new RuntimeException("护理级别不能为空");
        }
        Integer status = nurselevel.getLevel_status();
        if (status != null) {
            if (status != 1 && status != 2) {
                throw new RuntimeException("级别状态只能是1和2");
            }
        }
        Integer del = nurselevel.getIs_deleted();
        if (del != null) {
            if (del != 1 && del != 0) {
                throw new RuntimeException("删除标记只能是0和1");
            }
        }
        Connection conn = null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false); //关闭自动提交，开启事务
            boolean res = nurseDAO.save(nurselevel);
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

    public boolean updateNurselevel(Nurselevel nurselevel) {
        if (nurselevel == null) {
            throw new RuntimeException("参数对象不可为空");
        }
        if (nurselevel.getLevel_name() == null || nurselevel.getLevel_name().equals("")) {
            throw new RuntimeException("护理级别不能为空");
        }
        Integer status = nurselevel.getLevel_status();
        if (status != null) {
            if (status != 1 && status != 2) {
                throw new RuntimeException("级别状态只能是1和2");
            }
        }
        Integer del = nurselevel.getIs_deleted();
        if (del != null) {
            if (del != 1 && del != 0) {
                throw new RuntimeException("删除标记只能是0和1");
            }
        }
        Connection conn = null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false); //关闭自动提交，开启事务
            boolean res = nurseDAO.update(nurselevel);
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
    public boolean deleteNurselevel(Integer id) {
        if (id == null) {
            throw new RuntimeException("id不能为空");
        }
        Connection conn = null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false); //关闭自动提交，开启事务
            boolean res = nurseDAO.delete(id);
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
    public Nurselevel findNurselevelById(Integer id) throws SQLException {
        if (id == null) {
            throw new RuntimeException("id不能为空");
        }
        return nurseDAO.findOne(id);
    }

    @Override
    public List<Nurselevel> findAllNurselevel() throws SQLException {
        return nurseDAO.findAll();
    }
}
