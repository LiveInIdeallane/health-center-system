package entity.UserS;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id;
    private Date createTime;   // create_time
    private Integer createBy;           // create_by
    private Date updateTime;   // update_time
    private Integer updateBy;           // update_by
    private Integer isDeleted;          // is_deleted 逻辑删除(0显示1隐藏)

    private String nickname;            // 真实姓名
    private String username;            // 系统账号
    private String password;            // 密码
    private Integer sex;                // 0女 1男
    private String email;               // 邮箱
    private String phoneNumber;         // phone_number 电话
    private Integer roleId;             // role_id 角色(1管理员2健康管家)
}


