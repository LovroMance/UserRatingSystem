package cn.fzw.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DButils {
    // Druid 数据源（线程安全，全局唯一）
    private static final DruidDataSource dataSource;

    static {
        // 初始化连接池配置
        Properties properties = new Properties();
        properties.setProperty("url", "jdbc:mysql://localhost:3306/user_rating_system");
        properties.setProperty("username", "root");
        properties.setProperty("password", "1234");
        properties.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");

        // 连接池性能优化配置（根据需求调整）
        properties.setProperty("initialSize", "5");          // 初始连接数
        properties.setProperty("maxActive", "20");          // 最大连接数
        properties.setProperty("minIdle", "5");             // 最小空闲连接数
        properties.setProperty("maxWait", "30000");         // 获取连接超时时间（ms）
        properties.setProperty("timeBetweenEvictionRunsMillis", "60000"); // 检查空闲连接的间隔时间（ms）
        properties.setProperty("minEvictableIdleTimeMillis", "300000");   // 连接最小空闲时间（ms）

        try {
            // 初始化数据源
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException("初始化 Druid 连接池失败", e);
        }
    }

    /**
     * 从连接池获取数据库连接
     * @return 数据库连接对象
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * 关闭数据库连接（实际是将连接归还到连接池）
     * @param connection 数据库连接对象
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close(); // Druid 会管理连接的归还
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行 SQL 更新（示例方法，可根据需要调整）
     * @param sql SQL 语句
     * @return 受影响的行数
     * @throws SQLException SQL异常
     */
    public static int executeUpdate(String sql) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            return pstmt.executeUpdate();
        }
    }
}