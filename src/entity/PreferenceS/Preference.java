package entity.PreferenceS;

import lombok.Data;

@Data
public class Preference {
    private Integer id;
    private Integer customerId;         // customer_id 顾客ID
    private String preferences;         // 饮食喜好
    private String attention;           // 注意事项
    private String remark;              // 备注
    private Integer isDeleted;          // is_deleted 逻辑删除
}
