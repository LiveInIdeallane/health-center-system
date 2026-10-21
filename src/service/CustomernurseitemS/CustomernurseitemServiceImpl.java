package service.CustomernurseitemS;

import dao.CustomernurseitemS.CustomernurseitemDao;
import dao.CustomernurseitemS.CustomernurseitemDaoImpl;
import entity.CustomernurseitemS.Customernurseitem;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CustomernurseitemServiceImpl implements CustomernurseitemService {

    private CustomernurseitemDao cusDAO = new CustomernurseitemDaoImpl();

    @Override
    public boolean addCustomernurseitem(Customernurseitem customernurseitem) {
        //1. 判断对象不能为空
        if (customernurseitem == null) {
            throw new RuntimeException("参数对象不能为空!");
        }

        //2. item_id 护理项目编号（int，不能trim）非空校验
        if (customernurseitem.getItem_id() == null) {
            throw new RuntimeException("护理项目编号item_id不能为空!");
        }

        //3. customer_id客户编号非空
        if (customernurseitem.getCustomer_id() == null) {
            throw new RuntimeException("客户编号customer_id不能为空!");
        }

        //4. nurse_number购买数量非空 + 必须大于0
        if (customernurseitem.getNurse_number() == null) {
            throw new RuntimeException("购买数量不能为空!");
        }
        if (customernurseitem.getNurse_number() <= 0) {
            throw new RuntimeException("购买数量必须大于0!");
        }
        Integer del =customernurseitem.getIs_deleted();
        if (del != null) {
            if(del!=1&&del!=0){
                throw new RuntimeException("删除标记只能为1和0");
            }
        }
        //5. buy_time购买日期非空
        Date buyTime = customernurseitem.getBuy_time();
        if (buyTime == null) {
            throw new RuntimeException("服务购买日期不能为空!");
        }

        //6. maturity_time到期日期非空
        Date maturityTime = customernurseitem.getMaturity_time();
        if (maturityTime == null) {
            throw new RuntimeException("服务到期日期不能为空!");
        }

        //7. 业务校验：到期时间不能早于购买时间
        if(maturityTime.before(buyTime)){
            throw new RuntimeException("到期日期不能早于购买日期!");
        }

        // ==========事务处理==========
        Connection conn = null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false); //关闭自动提交，开启事务
            boolean res = cusDAO.save(customernurseitem);
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
    public boolean updateCustomernurseitem(Customernurseitem customernurseitem) {
        //1. 判断对象不能为空
        if (customernurseitem == null) {
            throw new RuntimeException("参数对象不能为空!");
        }

        //2. item_id 护理项目编号（int，不能trim）非空校验
        if (customernurseitem.getItem_id() == null) {
            throw new RuntimeException("护理项目编号item_id不能为空!");
        }

        //3. customer_id客户编号非空
        if (customernurseitem.getCustomer_id() == null) {
            throw new RuntimeException("客户编号customer_id不能为空!");
        }

        //4. nurse_number购买数量非空 + 必须大于0
        if (customernurseitem.getNurse_number() == null) {
            throw new RuntimeException("购买数量不能为空!");
        }
        if (customernurseitem.getNurse_number() <= 0) {
            throw new RuntimeException("购买数量必须大于0!");
        }
        Integer del =customernurseitem.getIs_deleted();
        if (del != null) {
            if(del!=1&&del!=0){
                throw new RuntimeException("删除标记只能为1和0");
            }
        }
        //5. buy_time购买日期非空
        Date buyTime = customernurseitem.getBuy_time();
        if (buyTime == null) {
            throw new RuntimeException("服务购买日期不能为空!");
        }

        //6. maturity_time到期日期非空
        Date maturityTime = customernurseitem.getMaturity_time();
        if (maturityTime == null) {
            throw new RuntimeException("服务到期日期不能为空!");
        }

        //7. 业务校验：到期时间不能早于购买时间
        if(maturityTime.before(buyTime)){
            throw new RuntimeException("到期日期不能早于购买日期!");
        }

        // ==========事务处理==========
        Connection conn = null;
        try {
            conn = JdbcUtils.getConnection();
            conn.setAutoCommit(false); //关闭自动提交，开启事务
            boolean res = cusDAO.update(customernurseitem);
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
    public boolean deleteCustomernurseitem(Integer id){
        if (id==null) {
            throw new RuntimeException("id不能为空");

        }


    Connection conn = null;
        try {
        conn = JdbcUtils.getConnection();
        conn.setAutoCommit(false); //关闭自动提交，开启事务
        boolean res = cusDAO.delete(id);
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
    public Customernurseitem findCustomernurseitemById(Integer id) throws SQLException {
        if (id == null) {
            throw new RuntimeException("id不能为空");
        }
       return cusDAO.findOne(id);
    }

    @Override
    public List<Customernurseitem> findAllCustomernurseitem() throws SQLException {
        return cusDAO.findAll();
    }

    @Override
    public List<Integer> findCustomerIdsWithNurseItems() {
        Set<Integer> ids = new LinkedHashSet<>();
        try {
            for (Customernurseitem item : cusDAO.findAll()) {
                if (item.getCustomer_id() != null
                        && (item.getIs_deleted() == null || item.getIs_deleted() == 0)) {
                    ids.add(item.getCustomer_id());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(ids);
    }


}
