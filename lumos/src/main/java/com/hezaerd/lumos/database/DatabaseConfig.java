package com.hezaerd.lumos.database;

import java.nio.file.Path;

public final class DatabaseConfig {
	private DatabaseConfig() {}

	// Database file location relative to Minecraft's run directory
	public static final String DATABASE_NAME = "ordo_arcanum";
	public static final String DATABASE_FILE = DATABASE_NAME + ".db";

	// Connection settings
	public static final String DB_URL_PREFIX = "jdbc:h2:";
	public static final String DB_URL_SUFFIX = ";DB_CLOSE_DELAY=-1";

	// Connection pool settings
	public static final int MAX_CONNECTIONS = 10;
	public static final int MIN_CONNECTIONS = 2;
	public static final long CONNECTION_TIMEOUT = 30000; // 30 seconds

	/**
	 * Builds the database URL for the given path
	 * @param dbPath The path to the database file
	 * @return The complete JDBC URL
	 */
	public static String buildDatabaseUrl(Path dbPath) {
		return DB_URL_PREFIX + dbPath.resolve(DATABASE_FILE) + DB_URL_SUFFIX;
	}

	/**
	 * Builds the database URL for in-memory database (for testing)
	 * @return The complete JDBC URL for in-memory database
	 */
	public static String buildInMemoryDatabaseUrl() {
		return DB_URL_PREFIX + "mem:" + DATABASE_NAME + DB_URL_SUFFIX;
	}
}
