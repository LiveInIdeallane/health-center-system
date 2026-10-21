package entity.MenuS;

import lombok.Data;

@Data
public class Menu {
    private Integer id;
    private String menusIndex;          // menus_index 一级菜单索引
    private String title;               // 菜单标题
    private String icon;                // 菜单图标
    private String path;                // 菜单路径
    private Integer parentId;           // parent_id 父级Id
}