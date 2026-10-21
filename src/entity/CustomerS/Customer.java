package entity.CustomerS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private Integer id;
    private Integer is_deleted;
    private String customer_name;
    private Integer customer_age;
    private Integer customer_sex;
    private String idcard;
    private String room_no;
    private String building_no;
    private Date checkin_date;
    private Date expiration_date;
    private String contact_tel;
    private Integer bed_id;
    private String psychosomatic_state;
    private String attention;
    private Date birthday;
    private String height;
    private String weight;
    private String blood_type;
    private String filepath;
    private Integer user_id;
    private Integer level_id;
    private String family_member;

}