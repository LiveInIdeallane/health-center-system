package service.BedDetailsS;

import dao.BedDetailsS.BedDetailsDAO;
import dao.BedDetailsS.BedDetailsDAOImpl;
import entity.BedDetailsS.BedDetails;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class BedDetailsServiceImpl implements BedDetailsService {

    private static BedDetailsDAO bedDetailsDAO = new BedDetailsDAOImpl();

    @Override
    public boolean addBedDetails(BedDetails details) {
        if (details == null) {
            throw new RuntimeException("参数对象不能为空!");
        }
        if (details.getCustomer_id() == null) {
            throw new RuntimeException("客户ID不能为空!");
        }
        if (details.getBed_id() == null) {
            throw new RuntimeException("床位ID不能为空!");
        }
        try {
            return bedDetailsDAO.save(details);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateBedDetails(BedDetails details) {
        if (details == null) {
            throw new RuntimeException("参数对象不能为空!");
        }
        if (details.getId() == null) {
            throw new RuntimeException("ID不能为空!");
        }
        try {
            return bedDetailsDAO.update(details);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteBedDetails(Integer id) {
        if (id == null) {
            throw new RuntimeException("ID不能为空！");
        }
        try {
            return bedDetailsDAO.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BedDetails getBedDetailsById(Integer id) {
        if (id == null) {
            throw new RuntimeException("ID不能为空！");
        }
        try {
            return bedDetailsDAO.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<BedDetails> getAllBedDetails() {
        try {
            return bedDetailsDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BedDetails getActiveByCustomerId(Integer customerId) {
        if (customerId == null) {
            throw new RuntimeException("客户ID不能为空！");
        }
        try {
            return bedDetailsDAO.findActiveByCustomerId(customerId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BedDetails getActiveByBedId(Integer bedId) {
        if (bedId == null) {
            throw new RuntimeException("床位ID不能为空！");
        }
        try {
            return bedDetailsDAO.findActiveByBedId(bedId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateEndDate(Integer id, Date endDate) {
        if (id == null) {
            throw new RuntimeException("ID不能为空！");
        }
        if (endDate == null) {
            throw new RuntimeException("结束日期不能为空！");
        }
        try {
            return bedDetailsDAO.updateEndDate(id, endDate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean logicDelete(Integer id) {
        if (id == null) {
            throw new RuntimeException("ID不能为空！");
        }
        try {
            return bedDetailsDAO.logicDelete(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean logicDeleteActiveByCustomer(int id) {
        try {
            BedDetails details = bedDetailsDAO.findActiveByCustomerId(id);
            return details != null && bedDetailsDAO.logicDelete(details.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}