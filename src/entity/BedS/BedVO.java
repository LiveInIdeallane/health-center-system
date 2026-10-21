package entity.BedS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BedVO {
    private Integer id;
    private Integer roomNo;
    private String roomStatus;
    private String remarks;
    private String bedNo;
    private String roomFloor;
}
