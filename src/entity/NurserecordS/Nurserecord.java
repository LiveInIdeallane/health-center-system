package entity.NurserecordS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nurserecord {
    private Integer id;
    private Integer is_deleted;
    private Integer customer_id;
    private Integer item_id;
    private Date nursing_time;
    private String nursing_content;
    private Integer nursing_count;
    private Integer user_id;
}