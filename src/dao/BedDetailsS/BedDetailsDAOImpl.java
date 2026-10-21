package dao.BedDetailsS;

import entity.BedDetailsS.BedDetails;
import utils.CRUDUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BedDetailsDAOImpl implements BedDetailsDAO {

    @Override
    public boolean save(BedDetails beddetails) throws SQLException {
        String sql = "insert into beddetails(start_date,end_date,bed_details,customer_id,bed_id,is_deleted) values(?,?,?,?,?,?)";
        Object[] objs = {
                beddetails.getStart_date(),
                beddetails.getEnd_date(),
                beddetails.getBed_details(),
                beddetails.getCustomer_id(),
                beddetails.getBed_id(),
                beddetails.getIs_deleted()
        };
        return CRUDUtils.update(sql, objs);
    }

    @Override
    public boolean update(BedDetails beddetails) throws SQLException {
        String sql = "update beddetails set start_date=?,end_date=?,bed_details=?,customer_id=?,bed_id=?,is_deleted=? where id=?";
        Object[] objs = {
                beddetails.getStart_date(),
                beddetails.getEnd_date(),
                beddetails.getBed_details(),
                beddetails.getCustomer_id(),
                beddetails.getBed_id(),
                beddetails.getIs_deleted(),
                beddetails.getId()
        };
        return CRUDUtils.update(sql, objs);
    }

    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "delete from beddetails where id=?";
        Object[] objs = {id};
        return CRUDUtils.update(sql, objs);
    }

    @Override
    public BedDetails findById(Integer id) throws SQLException {
        String sql = "select * from beddetails where id=?";
        Object[] objects = {id};
        List list = CRUDUtils.select(sql, objects, BedDetails.class);
        return list.size() > 0 ? (BedDetails) list.get(0) : null;
    }

    @Override
    public List<BedDetails> findAll() throws SQLException {
        String sql = "select * from beddetails";
        Object[] objects = {};
        return CRUDUtils.select(sql, objects, BedDetails.class);
    }

    @Override
    public BedDetails findActiveByBedId(Integer bedId) throws SQLException {
        String sql = "select * from beddetails where bed_id = ? and is_deleted = 0 and (end_date is null or end_date >= curdate()) limit 1";
        Object[] objs = {bedId};
        List list = CRUDUtils.select(sql, objs, BedDetails.class);
        return list.size() > 0 ? (BedDetails) list.get(0) : null;
    }

    @Override
    public BedDetails findActiveByCustomerId(Integer customerId) throws SQLException {
        String sql = "select * from beddetails where customer_id = ? and is_deleted = 0 and (end_date is null or end_date >= curdate()) limit 1";
        Object[] objs = {customerId};
        List list = CRUDUtils.select(sql, objs, BedDetails.class);
        return list.size() > 0 ? (BedDetails) list.get(0) : null;
    }

    @Override
    public boolean updateEndDate(Integer id, Date endDate) throws SQLException {
        String sql = "update beddetails set end_date = ? where id = ?";
        Object[] objs = {new java.sql.Date(endDate.getTime()), id};
        return CRUDUtils.update(sql, objs);
    }

    @Override
    public boolean logicDelete(Integer id) throws SQLException {
        String sql = "update beddetails set is_deleted = 1 where id = ?";
        Object[] objs = {id};
        return CRUDUtils.update(sql, objs);
    }

    @Override
    public List<BedDetails> search(String customerName, Date startDate, Integer status) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "select bd.* from beddetails bd " +
                        "left join customer c on bd.customer_id = c.id " +
                        "where 1=1 "
        );
        List<Object> params = new ArrayList<>();

        if (customerName != null && !customerName.trim().isEmpty()) {
            sql.append("and c.customer_name like ? ");
            params.add("%" + customerName.trim() + "%");
        }
        if (startDate != null) {
            sql.append("and bd.start_date = ? ");
            params.add(new java.sql.Date(startDate.getTime()));
        }
        if (status != null) {
            if (status == 1) {
                sql.append("and bd.is_deleted = 0 and (bd.end_date is null or bd.end_date >= curdate()) ");
            } else if (status == 2) {
                sql.append("and (bd.is_deleted = 1 or bd.end_date < curdate()) ");
            }
        }
        sql.append("order by bd.start_date desc");
        return CRUDUtils.select(sql.toString(), params.toArray(), BedDetails.class);
    }
}