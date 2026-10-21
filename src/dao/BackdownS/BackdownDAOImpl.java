package dao.BackdownS;

import entity.BackdownS.Backdown;
import utils.*;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class BackdownDAOImpl implements BackdownDAO {

    @Override
    public boolean saveBackdown(Backdown backdown) throws SQLException {
        String sql = "insert into backdown(id,remarks,is_deleted,customer_id,retreattime,retreattype,retreatreason,auditstatus,auditperson,audittime) values(?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {backdown.getId(),backdown.getRemarks(),backdown.getIs_delete(),backdown.getCustomer_id(),backdown.getRetreattime(),backdown.getRetreattype(),backdown.getRetreatreason(),backdown.getAuditstatus(),backdown.getAuditperson(),backdown.getAudittime()};
        return CRUDUtils.update(sql,params);
    }

    @Override
    public boolean updateBackdown(Backdown backdown) throws SQLException {
        String sql = "UPDATE backdown SET remarks = ?, is_deleted = ?, customer_id = ?, retreattime = ?, retreattype = ?, retreatreason = ?, auditstatus = ?, auditperson = ?, audittime = ? WHERE id = ?";
        Object[] params = {backdown.getRemarks(),backdown.getIs_delete(),backdown.getCustomer_id(),
                toTimestamp(backdown.getRetreattime()),backdown.getRetreattype(),backdown.getRetreatreason(),
                backdown.getAuditstatus(),backdown.getAuditperson(),toTimestamp(backdown.getAudittime()),
                backdown.getId()};
        return CRUDUtils.update(sql,params);
    }

    private Timestamp toTimestamp(Date date) {
        return date == null ? null : new Timestamp(date.getTime());
    }

    @Override
    public boolean deleteBackdown(Integer id) throws SQLException {
        String sql = "delete from backdown where id=?";
        Object[] params = {id};
        return CRUDUtils.update(sql,params);
    }

    @Override
    public Backdown findBackdownById(Integer id) throws SQLException, InstantiationException, IllegalAccessException {
        String sql = "select * from backdown where id=?";
        Object[] params = {id};
        List<Backdown> backdowns = CRUDUtils.select(sql,params,Backdown.class);
        return  backdowns.size()>0?backdowns.get(0):null;
    }

    @Override
    public List<Backdown> getAllBackdowns() throws SQLException, InstantiationException, IllegalAccessException {
        String sql = "select * from backdown";
        Object[] params = {};
        List<Backdown> backdowns = CRUDUtils.select(sql,params,Backdown.class);
        return backdowns;
    }
}
