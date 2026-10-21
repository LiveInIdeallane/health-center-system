package dao.FoodS;

import entity.FoodS.Food;

import java.sql.SQLException;
import java.util.List;
/**
 * 接口定义规则
 */
public interface FoodDAO {
    boolean save(Food obj) throws SQLException;
    boolean update(Food obj) throws SQLException;
    boolean delete(Integer id) throws SQLException;
    Food findOne(Integer id) throws SQLException;
    List<Food> findAll() throws SQLException;
}