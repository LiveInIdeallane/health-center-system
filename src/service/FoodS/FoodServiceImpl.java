package service.FoodS;

import dao.FoodS.*;

import entity.FoodS.Food;
import utils.JdbcUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class FoodServiceImpl implements FoodService {
    //生成DAO对象
    private FoodDAO foodDAO = new FoodDAOImpl();
    @Override
    public boolean addFood(Food food) {
        //先判断参数对象是否为空
        if(food == null){
            throw new RuntimeException("参数对象不能为空!");
        }
        //判断食品名称
        //判断非空 null
        //判断非空字符串 ""
        if(food.getFoodName() == null || "".equals(food.getFoodName().trim())){
            throw new RuntimeException("食品名称不能为空!");
        }
        //判断价格非空
        if(food.getPrice() == null){
            throw new RuntimeException("价格不能为空!");
        }
        //判断价格是否大于0
        //BigDecimal 提供的方法 compareTo 比较两个BigDecimal对象的大小 0 相等 1 大于 -1 小于
        if(new BigDecimal(0).compareTo(BigDecimal.valueOf(food.getPrice()))>0){
            throw new RuntimeException("价格不能小于0!");
        }
        //判断是否清真非空
        if(food.getIsHalal() == null){
            throw new RuntimeException("是否清真不能为空!");
        }
        //判断是否是0或1
        if(food.getIsHalal()<0||food.getIsHalal()>1){
            throw new RuntimeException("是否清真只能是0或1!");
        }
        //获得连接对象 一个事务中获取的连接对象只能是同一个
        try {
            //将自动提交事务取消，需要手动提交
            JdbcUtils.getConnection().setAutoCommit(false);
            boolean b = foodDAO.save(food);
            //手动提交 将缓存中的数据提交到数据库
            JdbcUtils.getConnection().commit();
            return b;
        } catch (SQLException e) {
            try {
                //回滚事务
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            //释放连接
            JdbcUtils.close();
        }
    }

    @Override
    public boolean deleteFood(Food food) {

        //数据校验


        //事务处理
        try {
            //将自动提交事务取消，需要手动提交
            JdbcUtils.getConnection().setAutoCommit(false);
            boolean b = foodDAO.delete(food.getId());
            //手动提交 将缓存中的数据提交到数据库
            JdbcUtils.getConnection().commit();
            return b;
        } catch (SQLException e) {
            try {
                //回滚事务
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            //释放连接
            JdbcUtils.close();
        }
    }

    @Override
    public boolean updateFood(Food food) {

        //数据校验


        //事务处理
        try {
            //将自动提交事务取消，需要手动提交
            JdbcUtils.getConnection().setAutoCommit(false);
            boolean b = foodDAO.update(food);
            //手动提交 将缓存中的数据提交到数据库
            JdbcUtils.getConnection().commit();
            return b;
        } catch (SQLException e) {
            try {
                //回滚事务
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            //释放连接
            JdbcUtils.close();
        }
    }

    @Override
    public Food findFoodById(Integer Id) {
        //数据校验


        //事务处理
        try {
            //将自动提交事务取消，需要手动提交
            JdbcUtils.getConnection().setAutoCommit(false);
            Food food = foodDAO.findOne(Id);
            //手动提交 将缓存中的数据提交到数据库
            JdbcUtils.getConnection().commit();
            return food;
        } catch (SQLException e) {
            try {
                //回滚事务
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            //释放连接
            JdbcUtils.close();
        }
    }

    @Override
    public List<Food> findAllFood() {
        //数据校验


        //事务处理
        try {
            //将自动提交事务取消，需要手动提交
            JdbcUtils.getConnection().setAutoCommit(false);
            List<Food> list = foodDAO.findAll();
            //手动提交 将缓存中的数据提交到数据库
            JdbcUtils.getConnection().commit();
            return list;
        } catch (SQLException e) {
            try {
                //回滚事务
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            //释放连接
            JdbcUtils.close();
        }
    }
}