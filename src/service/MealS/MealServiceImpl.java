package service.MealS;

import dao.MealS.MealDAO;
import dao.MealS.MealDAOImpl;
import entity.MealS.Meal;
import utils.JdbcUtils;

import java.sql.SQLException;
import java.util.List;

public class MealServiceImpl implements MealService {

    private MealDAO mealDAO = new MealDAOImpl();

    @Override
    public boolean createMeal(Meal meal) {
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            boolean b = mealDAO.saveMeal(meal);
            JdbcUtils.getConnection().commit();
            return b;
        } catch (SQLException e) {
            try {
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close();
        }
    }

    @Override
    public boolean updateMeal(Meal meal) {
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            boolean b = mealDAO.updateMeal(meal);
            JdbcUtils.getConnection().commit();
            return b;
        } catch (SQLException e) {
            try {
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close();
        }
    }

    @Override
    public boolean deleteMeal(Meal meal) {
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            boolean b = mealDAO.deleteMeal(meal);
            JdbcUtils.getConnection().commit();
            return b;
        } catch (SQLException e) {
            try {
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close();
        }
    }

    @Override
    public Meal findMealById(Integer id) {
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            Meal meal = mealDAO.findMealById(id);
            JdbcUtils.getConnection().commit();
            return meal;
        } catch (SQLException e) {
            try {
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close();
        }
    }

    @Override
    public List<Meal> findAllMeals() {
        try {
            JdbcUtils.getConnection().setAutoCommit(false);
            List<Meal> list = mealDAO.findAllMeals();
            JdbcUtils.getConnection().commit();
            return list;
        } catch (SQLException e) {
            try {
                JdbcUtils.getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close();
        }
    }
}
