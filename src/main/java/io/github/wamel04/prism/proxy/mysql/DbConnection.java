package io.github.wamel04.prism.proxy.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.wamel04.prism.proxy.ProxyInitializer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DbConnection {

    private static ProxyInitializer plugin = ProxyInitializer.getInstance();
    private static HikariDataSource dataSource;

    public DbConnection() {
        testConnection((HikariDataSource) dataSource(MysqlConfig.username, MysqlConfig.password));
    }

    public DataSource dataSource(String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://" + "127.0.0.1" + ":" + MysqlConfig.port + "/" + MysqlConfig.database + "?useUnicode=yes&characterEncoding=utf-8&useSSL=false");
        config.setUsername(username);
        config.setPassword(password);
        config.setIdleTimeout(30000000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
        dataSource.setMaximumPoolSize(MysqlConfig.poolsize);
        return dataSource;
    }

    private void testConnection(HikariDataSource ds) {
        try (Connection ignored = ds.getConnection()) {
            ProxyInitializer.getInstance().getProxy().getConsole().sendMessage("§a[PRISM] 데이터베이스와 연결되었습니다. poolsize: " + ds.getMaximumPoolSize());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closeConnection() {
        if (dataSource.isRunning() ||
                !dataSource.isClosed())
            try {
                dataSource.close();
                ProxyInitializer.getInstance().getProxy().getConsole().sendMessage("§c[PRISM] 데이터베이스와의 연결을 종료합니다.");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

}
