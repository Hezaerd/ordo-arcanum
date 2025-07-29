package com.hezaerd.lumos.database;

import com.hezaerd.lumos.ModLib;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

public final class DatabaseConnection {
    private static volatile DatabaseConnection instance;
    private final DataSource dataSource;
    private final AtomicInteger activeConnections = new AtomicInteger(0);

    private DatabaseConnection(Path dbPath) {
        this.dataSource = createDataSource(dbPath);
    }

    public static DatabaseConnection getInstance(Path dbPath) {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection(dbPath);
                }
            }
        }
        return instance;
    }

    private DataSource createDataSource(Path dbPath) {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL(DatabaseConfig.buildDatabaseUrl(dbPath));
        ds.setUser("sa");
        ds.setPassword("");

        // Set connection pool properties
//        ds.setMaxConnections(DatabaseConfig.MAX_CONNECTIONS);
//        ds.setMinConnections(DatabaseConfig.MIN_CONNECTIONS);
//        ds.setConnectionTimeout(DatabaseConfig.CONNECTION_TIMEOUT);
        
        return ds;
    }

    public Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        activeConnections.incrementAndGet();

        // Log connection usage for debugging
        ModLib.LOGGER.debug("Database connection acquired. Active connections: {}", activeConnections.get());

        return conn;
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                activeConnections.decrementAndGet();
                ModLib.LOGGER.debug("Database connection closed. Active connections: {}", activeConnections.get());
            } catch (SQLException e) {
                ModLib.LOGGER.error("Error closing database connection", e);
            }
        }
    }

    public int getActiveConnections() {
        return activeConnections.get();
    }
}
