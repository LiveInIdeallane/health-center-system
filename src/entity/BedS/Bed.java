package entity.BedS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bed {
    private Integer id;
    private Integer room_no;
    private Integer bed_status;
    private String remarks;
    private String bed_no;

    @Override
    public String toString() {
        return id + "\t" + room_no + "\t" + bed_status + "\t" + remarks + "\t" + bed_no;
    }
}