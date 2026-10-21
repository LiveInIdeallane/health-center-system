package service.RoleS;

import entity.MenuS.Menu;
import entity.RoleS.Role;

import java.util.List;

/**
 * 角色业务接口。
 * 关联功能：
 * - 角色-菜单：分配菜单、查角色菜单、取消授权
 * - 角色-用户：查角色下的用户
 */
public interface RoleService {

    /** 新增角色（校验角色名不重复） */
    boolean save(Role role);

    /** 修改角色（校验角色名不重复） */
    boolean update(Role role);

    /** 逻辑删除角色（需先解除该角色下的用户和菜单关联） */
    boolean delete(Integer id);

    /** 按主键查 */
    Role findOne(Integer id);

    /** 按角色名查 */
    Role findByName(String name);

    /** 查所有角色 */
    List<Role> findAll();

    /** 分页查 */
    List<Role> findPage(int pageNum, int pageSize);

    /** 统计总数 */
    int count();

    // ========== 角色-菜单关联 ==========

    /** 给角色分配菜单（先删旧关联，再批量插入新关联） */
    boolean assignMenus(Integer roleId, List<Integer> menuIds);

    /** 查角色拥有的所有菜单 */
    List<Menu> findMenusByRoleId(Integer roleId);

    /** 查角色拥有的菜单 ID 列表 */
    List<Integer> findMenuIdsByRoleId(Integer roleId);

    /** 取消角色对某个菜单的授权 */
    boolean removeMenu(Integer roleId, Integer menuId);
}