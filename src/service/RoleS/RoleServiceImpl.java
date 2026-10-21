package service.RoleS;

import dao.RoleS.RoleDAO;
import dao.UserS.UserDAOImpl;
import entity.MenuS.Menu;
import entity.RoleS.Role;
import entity.RoleMenuS.RoleMenu;
import entity.UserS.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色业务实现类。
 * 关联功能：
 * - 角色-菜单：分配菜单、查角色菜单、取消授权
 * - 角色-用户：删除角色前需解除用户关联
 */
public class RoleServiceImpl implements RoleService {

    private RoleDAO roleDAO = new dao.RoleS.RoleDAOImpl();
    private dao.RoleMenuS.RoleMenuDAO roleMenuDAO = new dao.RoleMenuS.RoleMenuDAOImpl();
    private dao.MenuS.MenuDAOImpl menuDAO = new dao.MenuS.MenuDAOImpl();
    private dao.UserS.UserDAOImpl userDAO = new UserDAOImpl();

    // ===================== 增 =====================

    @Override
    public boolean save(Role role) {
        if (role.getName() == null || role.getName().isEmpty()) {
            throw new RuntimeException("角色名不能为空");
        }
        if (roleDAO.findByName(role.getName()) != null) {
            throw new RuntimeException("角色名已存在：" + role.getName());
        }
        return roleDAO.save(role) > 0;
    }

    // ===================== 改 =====================

    @Override
    public boolean update(Role role) {
        Role exist = roleDAO.findOne(role.getId());
        if (exist == null) {
            throw new RuntimeException("角色不存在，id=" + role.getId());
        }
        Role byName = roleDAO.findByName(role.getName());
        if (byName != null && !byName.getId().equals(role.getId())) {
            throw new RuntimeException("角色名已被占用：" + role.getName());
        }
        return roleDAO.update(role) > 0;
    }

    // ===================== 删 =====================

    @Override
    public boolean delete(Integer id) {
        Role role = roleDAO.findOne(id);
        if (role == null) {
            throw new RuntimeException("角色不存在，id=" + id);
        }
        // 校验该角色下是否还有用户
        List<User> users = userDAO.findByRoleId(id);
        if (users != null && !users.isEmpty()) {
            throw new RuntimeException("该角色下还有 " + users.size() + " 个用户，不能删除");
        }
        // 解除角色-菜单关联
        roleMenuDAO.deleteByRoleId(id);
        // 逻辑删除角色
        return roleDAO.delete(id) > 0;
    }

    // ===================== 查 =====================

    @Override
    public Role findOne(Integer id) {
        return roleDAO.findOne(id);
    }

    @Override
    public Role findByName(String name) {
        return roleDAO.findByName(name);
    }

    @Override
    public List<Role> findAll() {
        return roleDAO.findAll();
    }

    @Override
    public List<Role> findPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return roleDAO.findPage(offset, pageSize);
    }

    @Override
    public int count() {
        return roleDAO.count();
    }

    // ===================== 角色-菜单关联 =====================

    @Override
    public boolean assignMenus(Integer roleId, List<Integer> menuIds) {
        // 1. 校验角色存在
        if (roleDAO.findOne(roleId) == null) {
            throw new RuntimeException("角色不存在，roleId=" + roleId);
        }
        // 2. 先删旧关联（物理删除）
        roleMenuDAO.deleteByRoleId(roleId);
        // 3. 批量插入新关联
        if (menuIds == null || menuIds.isEmpty()) {
            return true;
        }
        for (Integer menuId : menuIds) {
            if (menuDAO.findOne(menuId) == null) {
                throw new RuntimeException("菜单不存在，menuId=" + menuId);
            }
            RoleMenu rm = new RoleMenu();
            rm.setRoleId(roleId);
            rm.setMenuId(menuId);
            roleMenuDAO.save(rm);
        }
        return true;
    }

    @Override
    public List<Menu> findMenusByRoleId(Integer roleId) {
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

    @Override
    public List<Integer> findMenuIdsByRoleId(Integer roleId) {
        List<RoleMenu> roleMenus = roleMenuDAO.findByRoleId(roleId);
        List<Integer> ids = new ArrayList<>();
        for (RoleMenu rm : roleMenus) {
            ids.add(rm.getMenuId());
        }
        return ids;
    }

    @Override
    public boolean removeMenu(Integer roleId, Integer menuId) {
        if (roleDAO.findOne(roleId) == null) {
            throw new RuntimeException("角色不存在，roleId=" + roleId);
        }
        if (menuDAO.findOne(menuId) == null) {
            throw new RuntimeException("菜单不存在，menuId=" + menuId);
        }
        return roleMenuDAO.deleteByRoleIdAndMenuId(roleId, menuId) > 0;
    }
}