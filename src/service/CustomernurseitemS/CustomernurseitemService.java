package service.CustomernurseitemS;

import entity.CustomernurseitemS.Customernurseitem;

import java.sql.SQLException;
import java.util.List;

public interface CustomernurseitemService {
    boolean addCustomernurseitem(Customernurseitem customernurseitem);
    boolean updateCustomernurseitem(Customernurseitem customernurseitem);
    boolean deleteCustomernurseitem(Integer id);
    Customernurseitem findCustomernurseitemById(Integer id) throws SQLException;
    List<Customernurseitem> findAllCustomernurseitem() throws SQLException;


    /** 配置了护理项目的客户ID集合（即"护理老人"） */
    List<Integer> findCustomerIdsWithNurseItems();
}
