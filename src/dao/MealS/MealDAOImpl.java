package dao.MealS;

import entity.MealS.Meal;
import utils.CRUDUtils;

import java.sql.SQLException;
import java.util.List;

public class MealDAOImpl implements MealDAO {
    @Override
    public boolean saveMeal(Meal meal) throws SQLException {
        String sql = "INSERT INTO meal(week_day,food_id,meal_type,taste,is_deleted) VALUES (?,?,?,?,?)";
        Object[] params = {meal.getWeek_day(),meal.getFood_id(),meal.getMeal_type(),meal.getTaste(),meal.getIs_deleted()};

        return CRUDUtils.update(sql,params);
    }

    @Override
    public boolean deleteMeal(Meal meal) throws SQLException {
        String sql = "DELETE FROM meal WHERE id = ?";
        Object[] params = {meal.getId()};
        return CRUDUtils.update(sql,params);
    }

    @Override
    public boolean updateMeal(Meal meal) throws SQLException {
        String sql = "update meal set week_day = ?,food_id = ?, meal_type = ?,taste = ?, is_deleted = ? where id = ?;";
        Object[] params = {meal.getWeek_day(),meal.getFood_id(),meal.getMeal_type(),meal.getTaste(),meal.getIs_deleted(),meal.getId()};

        return CRUDUtils.update(sql,params);
    }

    @Override
    public Meal findMealById(int id) throws SQLException {
        String sql = "select * from meal where id = ?";
        Object[] params = {id};
        List<Meal> list = CRUDUtils.select(sql,params,Meal.class);
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<Meal> findAllMeals() throws SQLException {
        String sql = "select * from meal";
        Object[] params = {};
        List<Meal> list = CRUDUtils.select(sql,params, Meal.class);
        return list;
    }
}
