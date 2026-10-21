package entity.BedDetailsS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BedDetails {
    private Integer id;
    private Date start_date;
    private Date end_date;
    private String bed_details;
    private Integer customer_id;
    private Integer bed_id;
    private Integer is_deleted;

    @Override
    public String toString() {
        return id + "\t" + start_date + "\t" + end_date + "\t" + bed_details + "\t" + customer_id + "\t" + bed_id + "\t" + is_deleted;
    }
}