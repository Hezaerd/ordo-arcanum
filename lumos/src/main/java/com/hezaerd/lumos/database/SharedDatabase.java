package com.hezaerd.lumos.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

/**
 * Shared database interface for Accio and Protego mods
 * This class provides a clean API for database operations
 */
public final class SharedDatabase {
	private static final DatabaseManager dbManager = DatabaseManager.getInstance();

	private SharedDatabase() {}

	/**
	 * Initialize the shared database
	 * This should be called by Lumos during mod initialization
	 */
	public static void initialize() {
		dbManager.initialize();
	}

	/**
	 * Execute a database operation with automatic connection management
	 * @param operation The database operation to execute
	 * @return CompletableFuture that completes when the operation is done
	 */
	public static CompletableFuture<Void> execute(DatabaseOperation operation) {
		return dbManager.executeAsync(() -> {
			try (Connection conn = dbManager.getConnection()) {
				operation.execute(conn);
			} catch (SQLException e) {
				throw new RuntimeException("Database operation failed", e);
			}
		});
	}

	/**
	 * Execute a database operation with automatic connection management and return a result
	 * @param operation The database operation to execute
	 * @return CompletableFuture that completes with the operation result
	 */
	public static <T> CompletableFuture<T> executeWithResult(DatabaseOperationWithResult<T> operation) {
		return dbManager.executeAsync(() -> {
			try (Connection conn = dbManager.getConnection()) {
				return operation.execute(conn);
			} catch (SQLException e) {
				throw new RuntimeException("Database operation failed", e);
			}
		});
	}

	/**
	 * Execute a database query with automatic connection management
	 * @param query The database query to execute
	 * @return CompletableFuture that completes with the query result
	 */
	public static <T> CompletableFuture<T> query(DatabaseQuery<T> query) {
		return dbManager.executeAsync(() -> {
			try (Connection conn = dbManager.getConnection()) {
				return query.execute(conn);
			} catch (SQLException e) {
				throw new RuntimeException("Database query failed", e);
			}
		});
	}

	/**
	 * Functional interface for database operations
	 */
	@FunctionalInterface
	public interface DatabaseOperation {
		void execute(Connection connection) throws SQLException;
	}

	/**
	 * Functional interface for database operations that return a result
	 */
	@FunctionalInterface
	public interface DatabaseOperationWithResult<T> {
		T execute(Connection connection) throws SQLException;
	}

	/**
	 * Functional interface for database queries
	 */
	@FunctionalInterface
	public interface DatabaseQuery<T> {
		T execute(Connection connection) throws SQLException;
	}

	/**
	 * Utility method to create a prepared statement
	 */
	public static PreparedStatement prepareStatement(Connection conn, String sql) throws SQLException {
		return conn.prepareStatement(sql);
	}

	/**
	 * Utility method to safely close resources
	 */
	public static void closeResources(AutoCloseable... resources) {
		for (AutoCloseable resource : resources) {
			if (resource != null) {
				try {
					resource.close();
				} catch (Exception e) {
					// Log but don't throw - this is cleanup code
				}
			}
		}
	}

	/**
	 * Check if the database is initialized
	 */
	public static boolean isInitialized() {
		return dbManager.isInitialized();
	}

	/**
	 * Get the number of active database connections
	 */
	public static int getActiveConnections() {
		return dbManager.getActiveConnections();
	}
}
