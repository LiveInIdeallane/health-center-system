package service.BedDetailsS;


import entity.BedDetailsS.BedDetails;

import java.util.Date;
import java.util.List;

public interface BedDetailsService {

    /** 新增床位详情 */
    boolean addBedDetails(BedDetails details);

    /** 修改床位详情 */
    boolean updateBedDetails(BedDetails details);

    /** 删除床位详情 */
    boolean deleteBedDetails(Integer id);

    /** 根据ID查询 */
    BedDetails getBedDetailsById(Integer id);

    /** 查询所有 */
    List<BedDetails> getAllBedDetails();

    /** 根据客户ID查询当前生效的床位详情 */
    BedDetails getActiveByCustomerId(Integer customerId);

    /** 根据床位ID查询当前生效的床位详情 */
    BedDetails getActiveByBedId(Integer bedId);

    /** 更新结束日期 */
    boolean updateEndDate(Integer id, Date endDate);

    /** 逻辑删除 */
    boolean logicDelete(Integer id);

    Boolean logicDeleteActiveByCustomer(int id);
}