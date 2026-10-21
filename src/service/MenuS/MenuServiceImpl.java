package service.MenuS;

import dao.MenuS.MenuDAO;
import dao.MenuS.MenuDAOImpl;
import dao.RoleMenuS.RoleMenuDAO;
import dao.RoleMenuS.RoleMenuDAOImpl;
import dao.RoleS.RoleDAO;
import dao.RoleS.RoleDAOImpl;
import entity.MenuS.Menu;
import entity.RoleMenuS.RoleMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单业务实现类。
 * 关联功能：
 * - 删除菜单前需解除角色-菜单关联
 * - 查某个角色拥有的菜单树
 */
public class MenuServiceImpl implements MenuService {

    private MenuDAO menuDAO = new MenuDAOImpl();
    private RoleMenuDAO roleMenuDAO = new RoleMenuDAOImpl();
    private RoleDAO roleDAO = new RoleDAOImpl();

    // ===================== 增 =====================

    @Override
    public boolean save(Menu menu) {
        if (menu.getTitle() == null || menu.getTitle().isEmpty()) {
            throw new RuntimeException("菜单标题不能为空");
        }
        if (menu.getPath() != null && !menu.getPath().isEmpty()
                && menuDAO.findByPath(menu.getPath()) != null) {
            throw new RuntimeException("菜单路径已存在：" + menu.getPath());
        }
        return menuDAO.save(menu) > 0;
    }

    // ===================== 改 =====================

    @Override
    public boolean update(Menu menu) {
        if (menuDAO.findOne(menu.getId()) == null) {
            throw new RuntimeException("菜单不存在，id=" + menu.getId());
        }
        if (menu.getPath() != null && !menu.getPath().isEmpty()) {
            Menu byPath = menuDAO.findByPath(menu.getPath());
            if (byPath != null && !byPath.getId().equals(menu.getId())) {
                throw new RuntimeException("菜单路径已被占用：" + menu.getPath());
            }
        }
        return menuDAO.update(menu) > 0;
    }

    // ===================== 删 =====================

    @Override
    public boolean delete(Integer id) {
        if (menuDAO.findOne(id) == null) {
            throw new RuntimeException("菜单不存在，id=" + id);
        }
        // 删除菜单前，先解除该菜单在所有角色上的关联
        roleMenuDAO.deleteByMenuId(id);
        return menuDAO.delete(id) > 0;
    }

    // ===================== 查 =====================

    @Override
    public Menu findOne(Integer id) {
        return menuDAO.findOne(id);
    }

    @Override
    public Menu findByPath(String path) {
        return menuDAO.findByPath(path);
    }

    @Override
    public List<Menu> findAll() {
        return menuDAO.findAll();
    }

    @Override
    public List<Menu> findTopMenus() {
        return menuDAO.findTopMenus();
    }

    @Override
    public List<Menu> findByParentId(Integer parentId) {
        return menuDAO.findByParentId(parentId);
    }

    @Override
    public List<Menu> findMenusByRoleId(Integer roleId) {
        if (roleDAO.findOne(roleId) == null) {
            throw new RuntimeException("角色不存在，roleId=" + roleId);
        }
        List<RoleMenu> roleMenus = roleMenuDAO.findByRoleId(roleId);
        List<Menu> menus = new ArrayList<>();
        for (RoleMenu rm : roleMenus) {
            Menu menu = menuDAO.findOne(rm.getMenuId());
            if (menu != null) {
                menus.add(menu);
            }
        }
        return menus;
    }
}