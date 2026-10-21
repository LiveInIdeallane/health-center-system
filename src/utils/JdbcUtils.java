package utils;

import java.sql.*;

public class JdbcUtils {


    //创建线程变量
    private static ThreadLocal<Connection> tl = new ThreadLocal<>();


    //加载驱动
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //创立连接
    public static Connection getConnection() throws SQLException {
        Connection conn = tl.get();
        if(conn==null){

            //大家从github从仓库下载项目结构后，注意在这里修改自己数据库的信息！
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_service","root","123456");
            tl.set(conn);
        }
        return conn;
    }


    //关闭连接
    public static void close(){
        Connection conn = tl.get();
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }finally{
                tl.remove();
            }
        }
    }



}
