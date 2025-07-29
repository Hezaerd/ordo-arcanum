package com.hezaerd.lumos.database;

import com.hezaerd.lumos.ModLib;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class DatabaseManager {
    private static volatile DatabaseManager instance;
    private final DatabaseConnection connection;
    private final ExecutorService executorService;
    private volatile boolean initialized = false;

    private DatabaseManager() {
        Path dbPath = FabricLoader.getInstance().getGameDir().resolve("ordo-arcanum");
        this.connection = DatabaseConnection.getInstance(dbPath);
        this.executorService = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r, "OrdoArcanum-DB-" + System.currentTimeMillis());
            t.setDaemon(true);
            return t;
        });
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    public void initialize() {
        if (initialized) {
            return;
        }

        synchronized (this) {
            if (initialized) {
                return;
            }

            try {
                createTables();
                initialized = true;
                ModLib.LOGGER.info("Database initialized successfully");
            } catch (SQLException e) {
                ModLib.LOGGER.error("Failed to initialize database", e);
                throw new RuntimeException("Database initialization failed", e);
            }
        }
    }

    private void createTables() throws SQLException {
        try (Connection conn = connection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS protego_permissions (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    player_uuid VARCHAR(36) NOT NULL,
                    permission VARCHAR(255) NOT NULL,
                    granted_by VARCHAR(36),
                    granted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    expires_at TIMESTAMP NULL,
                    UNIQUE(player_uuid, permission)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS protego_broadcasts (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    message TEXT NOT NULL,
                    broadcast_by VARCHAR(36) NOT NULL,
                    broadcast_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    expires_at TIMESTAMP NULL
                )
            """);

            // NEW: Whitelist system tables
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS protego_whitelists (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL UNIQUE,
                    display_name VARCHAR(255) NOT NULL,
                    description TEXT,
                    is_active BOOLEAN DEFAULT FALSE,
                    created_by VARCHAR(36) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS protego_whitelist_players (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    whitelist_id BIGINT NOT NULL,
                    player_uuid VARCHAR(36) NOT NULL,
                    player_name VARCHAR(255) NOT NULL,
                    added_by VARCHAR(36) NOT NULL,
                    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    expires_at TIMESTAMP NULL,
                    FOREIGN KEY (whitelist_id) REFERENCES protego_whitelists(id) ON DELETE CASCADE,
                    UNIQUE(whitelist_id, player_uuid)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS protego_whitelist_settings (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    setting_key VARCHAR(100) NOT NULL UNIQUE,
                    setting_value TEXT NOT NULL,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                )
            """);

            // Create indexes for better performance
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_protego_permissions_player ON protego_permissions(player_uuid)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_protego_permissions_expires ON protego_permissions(expires_at)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_protego_whitelist_players_whitelist ON protego_whitelist_players(whitelist_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_protego_whitelist_players_player ON protego_whitelist_players(player_uuid)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_protego_whitelist_players_expires ON protego_whitelist_players(expires_at)");

            // Insert default whitelist if none exists
            stmt.execute("""
                MERGE INTO protego_whitelists (name, display_name, description, is_active, created_by)
                KEY(name)
                VALUES ('default', 'Default Whitelist', 'Default whitelist for the server', FALSE, '00000000-0000-0000-0000-000000000000')
            """);

            // Insert default settings
            stmt.execute("""
                MERGE INTO protego_whitelist_settings (setting_key, setting_value)
                KEY(setting_key)
                VALUES
                ('enforce_whitelist', 'false'),
                ('kick_message', 'You are not whitelisted on this server.'),
                ('max_whitelists_per_player', '10')
            """);

        }
    }

    public Connection getConnection() throws SQLException {
        if (!initialized) {
            throw new IllegalStateException("Database not initialized. Call initialize() first.");
        }
        return connection.getConnection();
    }

    public void closeConnection(Connection connection) {
        this.connection.closeConnection(connection);
    }

    public CompletableFuture<Void> executeAsync(Runnable task) {
        return CompletableFuture.runAsync(task, executorService);
    }

    public <T> CompletableFuture<T> executeAsync(java.util.function.Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, executorService);
    }

    public void shutdown() {
        executorService.shutdown();
        ModLib.LOGGER.info("Database manager shutdown complete");
    }

    public boolean isInitialized() {
        return initialized;
    }

    public int getActiveConnections() {
        return connection.getActiveConnections();
    }
}
