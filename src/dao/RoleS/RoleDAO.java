package dao.RoleS;

import entity.RoleS.Role;

import java.util.List;

public interface RoleDAO {

    int save(Role role); // 新增角色
    int update(Role role);//修改角色
    int delete(Integer id);// 根据主键删除
    Role findOne(Integer id);// 根据主键查询单条
    Role findByName(String name);// 根据角色名查询（新增角色时校验重名用）
    List<Role> findAll();//查询所角色
    List<Role> findPage(int offset, int limit);//分页查询
    int count(); //统计总数
}
