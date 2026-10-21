package dao.MealS;

import entity.MealS.Meal;

import java.sql.SQLException;
import java.util.List;

public interface MealDAO {

    boolean saveMeal(Meal meal) throws SQLException;
    boolean deleteMeal(Meal meal) throws SQLException;
    boolean updateMeal(Meal meal) throws SQLException;
    Meal findMealById(int id) throws SQLException;
    List<Meal> findAllMeals() throws SQLException;

}
