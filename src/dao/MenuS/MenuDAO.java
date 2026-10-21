package dao.MenuS;

import entity.MenuS.Menu;

import java.util.List;

public interface MenuDAO {
    /** 新增 */
    int save(Menu menu);

    /** 修改 */
    int update(Menu menu);

    /** 根据主键删除 */
    int delete(Integer id);

    /** 根据主键查询单条 */
    Menu findOne(Integer id);

    /** 查询所有菜单 */
    List<Menu> findAll();

    /** 查询顶级菜单（一级菜单，parent_id 为 0 或 null） */
    List<Menu> findTopMenus();

    /** 根据父级ID查询子菜单 */
    List<Menu> findByParentId(Integer parentId);

    /** 根据菜单路径查询（路由权限判断用） */
    Menu findByPath(String path);

}
