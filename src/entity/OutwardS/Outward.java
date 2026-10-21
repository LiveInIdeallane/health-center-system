package entity.OutwardS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Outward {
    private Integer id;
    private String remarks;
    private Integer is_delete;
    private Integer customer_id;
    private String outgoingreason;
    private Date outgoingtime;
    private Date expectedreturntime;
    private Date actualreturntime;
    private String escorted;
    private String relation;
    private String escortedtel;
    private Integer auditstatus;
    private String auditperson;
    private Date audittime;

}
