package service.MealS;

import entity.MealS.Meal;

import java.util.List;

public interface MealService {

    boolean createMeal(Meal meal);
    boolean updateMeal(Meal meal);
    boolean deleteMeal(Meal meal);
    Meal findMealById(Integer id);
    List<Meal> findAllMeals();
}
