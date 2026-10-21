package entity.RoleMenuS;

import lombok.Data;

@Data
public class RoleMenu {
    private Integer id;
    private Integer roleId;             // role_id 角色编号
    private Integer menuId;             // 菜单编号（你表里字段名是 menu，建议改成 menu_id）
}
