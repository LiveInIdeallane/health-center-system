package dao.RoleMenuS;

import entity.RoleMenuS.RoleMenu;

import java.util.List;

public interface RoleMenuDAO {


    int save(RoleMenu roleMenu);// 新增角色菜单关联
    int deleteByRoleId(Integer roleId); // 根据角色ID删除该角色的所有菜单关联（重新分配菜单时用）
    int deleteByMenuId(Integer menuId); // 根据菜单ID删除该菜单的所有角色关联（删菜单时用）
    int deleteByRoleIdAndMenuId(Integer roleId, Integer menuId); // 精细删除：只删某角色×某菜单的一条（取消单个授权时用）
    List<RoleMenu> findByRoleId(Integer roleId);// 根据角色ID查询该角色拥有的所有菜单ID
    List<RoleMenu> findByMenuId(Integer menuId); // 根据菜单ID查询哪些角色拥有此菜单
    RoleMenu findByRoleIdAndMenuId(Integer roleId, Integer menuId);// 检查某角色是否已绑定某菜单

}
