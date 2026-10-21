package entity.MealS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meal {

    private Integer id;
    private String week_day;
    private Integer food_id;
    private Integer meal_type;
    private String  taste;
    private Integer is_deleted;

}
