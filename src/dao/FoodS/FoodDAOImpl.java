package dao.FoodS;

import entity.FoodS.Food;
import utils.CRUDUtils;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodDAOImpl implements FoodDAO {


    @Override
    public boolean save(Food obj) throws SQLException {
        String sql = "insert into food(food_name,food_type,price,is_halal,food_img)values(?,?,?,?,?)";
        Object[] params = {obj.getFoodName(),obj.getFoodType(),obj.getPrice(),obj.getIsHalal(),obj.getFoodImg()};
        return CRUDUtils.update(sql, params);
    }


    @Override
    public boolean update(Food obj) throws SQLException {
        String sql = "update food set food_name=?, food_type=?, price=?, is_halal=?, food_img=? where id=?";
        Object[] params = {obj.getFoodName(),obj.getFoodType(),obj.getPrice(),obj.getIsHalal(),obj.getFoodImg(),obj.getId()};
        return CRUDUtils.update(sql, params);
    }


    @Override
    public boolean delete(Integer id) throws SQLException {
        String sql = "delete from food where id=?";
        Object[] params = {id};
        return CRUDUtils.update(sql, params);
    }


    @Override
    public Food findOne(Integer id) throws SQLException {
        String sql = "select * from food where id=?";
        Object[] params = {id};
        List<Food> foodlist = CRUDUtils.select(sql,params,Food.class);
        return foodlist.size()>0?foodlist.get(0):null;
    }



    @Override
    public List<Food> findAll() throws SQLException {
        String sql = "select * from food";
        List<Food> foodList = CRUDUtils.select(sql,null,Food.class);
        return foodList;
    }
}
