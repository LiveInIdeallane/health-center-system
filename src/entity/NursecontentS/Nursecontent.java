package entity.NursecontentS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nursecontent {
    private Integer id;
    private String serial_number;
    private String nursing_name;
    private String service_price;
    private String message;
    private Integer status;
    private String execution_cycle;
    private String execution_times;
    private Integer is_deleted;
}
