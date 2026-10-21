package dao.BedDetailsS;


import entity.BedDetailsS.BedDetails;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface BedDetailsDAO {
    boolean save(BedDetails beddetails) throws SQLException;
    boolean update(BedDetails beddetails) throws SQLException;
    boolean delete(Integer id) throws SQLException;
    BedDetails findById(Integer id) throws SQLException;
    List<BedDetails> findAll() throws SQLException;

    /** 根据床位ID查询当前生效的床位详情 */
    BedDetails findActiveByBedId(Integer bedId) throws SQLException;

    /** 根据客户ID查询当前生效的床位详情 */
    BedDetails findActiveByCustomerId(Integer customerId) throws SQLException;

    /** 更新结束日期 */
    boolean updateEndDate(Integer id, Date endDate) throws SQLException;

    /** 逻辑删除 */
    boolean logicDelete(Integer id) throws SQLException;

    /** 多条件组合查询床位详情 */
    List<BedDetails> search(String customerName, Date startDate, Integer status) throws SQLException;
}