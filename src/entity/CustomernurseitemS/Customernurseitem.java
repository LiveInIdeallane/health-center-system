package entity.CustomernurseitemS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customernurseitem {
    private Integer id;
    private Integer item_id;
    private Integer customer_id;
    private Integer level_id;
    private Integer nurse_number;
    private Integer is_deleted;
    private Date buy_time;
    private Date maturity_time;


}
