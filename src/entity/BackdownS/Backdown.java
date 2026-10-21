package entity.BackdownS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Backdown {
    private Integer id;
    private String remarks;
    private Integer is_delete;
    private Integer customer_id;
    private Date retreattime;
    private Integer retreattype;
    private String retreatreason;
    private Integer auditstatus;
    private String auditperson;
    private Date audittime;
}

