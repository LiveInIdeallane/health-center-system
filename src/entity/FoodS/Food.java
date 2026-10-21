package entity.FoodS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food {
    /*
    int 默认值为0
    Integer 默认值为null
    可以确定用户是否填入该数据
    */
    private Integer id;
    private String foodName;
    private Double price;
    private String foodType;
    private Integer isHalal;
    private String foodImg;

}