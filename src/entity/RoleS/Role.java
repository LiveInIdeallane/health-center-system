package entity.RoleS;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Role {
    private Integer id;
    private LocalDateTime createTime;   // create_time
    private LocalDateTime updateTime;   // update_time
    private Integer updateBy;           // update_by 更新人
    private Integer isDeleted;          // is_deleted 逻辑删除

    private String name;                // 系统角色名

}
