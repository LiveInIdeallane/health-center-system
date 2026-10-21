package service.FoodS;
import entity.FoodS.Food;

import java.util.List;

public interface FoodService {

    boolean addFood(Food food);
    boolean deleteFood(Food food);
    boolean updateFood(Food food);
    Food findFoodById(Integer Id);
    List<Food> findAllFood();

}
