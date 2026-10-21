package entity.NurselevelS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nurselevel {
    private Integer id;
    private String level_name;
    private Integer level_status;
    private Integer is_deleted;
}
