package service.MenuS;

import entity.MenuS.Menu;

import java.util.List;

/**
 * 菜单业务接口。
 */
public interface MenuService {

    /** 新增菜单 */
    boolean save(Menu menu);

    /** 修改菜单 */
    boolean update(Menu menu);

    /** 删除菜单（需先解除该菜单在所有角色上的关联） */
    boolean delete(Integer id);

    /** 按主键查 */
    Menu findOne(Integer id);

    /** 按路径查 */
    Menu findByPath(String path);

    /** 查所有菜单 */
    List<Menu> findAll();

    /** 查一级菜单 */
    List<Menu> findTopMenus();

    /** 按父级查子菜单 */
    List<Menu> findByParentId(Integer parentId);

    /** 查某个角色拥有的菜单树（角色-菜单关联） */
    List<Menu> findMenusByRoleId(Integer roleId);
}