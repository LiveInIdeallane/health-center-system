package dao.PreferenceS;

import entity.PreferenceS.Preference;

import java.util.List;

public interface PreferenceDAO {

    int save(Preference preference);// 新增
    int update(Preference preference);//修改
    int delete(Integer id);//根据主键删除
    Preference findOne(Integer id);//根据主键查询单条
    Preference findByCustomerId(Integer customerId);// 根据顾客ID查询（一个顾客一条偏好记录）
    int deleteByCustomerId(Integer customerId);// 根据顾客ID删除偏好（重置偏好时"先全删再加"）
    int batchSave(List<Preference> preferences);// 批量保存多条偏好
}
