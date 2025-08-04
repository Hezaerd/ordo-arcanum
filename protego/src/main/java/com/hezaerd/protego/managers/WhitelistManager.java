package com.hezaerd.protego.managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.hezaerd.lumos.database.SharedDatabase;
import com.hezaerd.protego.ModLib;

public final class WhitelistManager {
	private static volatile WhitelistManager instance;
	private final Map<String, WhitelistInfo> whitelistCache = new ConcurrentHashMap<>();
	private final Map<String, Boolean> playerWhitelistCache = new ConcurrentHashMap<>();
	private volatile boolean initialized = false;

	private WhitelistManager() {}

	public static WhitelistManager getInstance() {
		if (instance == null) {
			synchronized (WhitelistManager.class) {
				if (instance == null) {
					instance = new WhitelistManager();
				}
			}
		}
		return instance;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void initialize() {
		if (initialized) return;

		synchronized (this) {
			if (initialized) return;

			// Load all whitelists into cache
			loadAllWhitelists().thenRun(() -> {
				initialized = true;
				ModLib.LOGGER.info("WhitelistManager initialized successfully");
			});
		}
	}


	// Whitelist Management Methods
	public CompletableFuture<Boolean> createWhitelist(String name, String displayName, String description, UUID createdBy) {
		return SharedDatabase.executeWithResult(conn -> {
			String sql = """
				INSERT INTO protego_whitelists (name, display_name, description, created_by)
				VALUES (?, ?, ?, ?)
				""";

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, name.toLowerCase());
				stmt.setString(2, displayName);
				stmt.setString(3, description);
				stmt.setString(4, createdBy.toString());
				stmt.executeUpdate();

				// Update cache
				WhitelistInfo info = new WhitelistInfo(name.toLowerCase(), displayName, description, false, createdBy);
				whitelistCache.put(name.toLowerCase(), info);

				return true;
			}
		});
	}

	public CompletableFuture<Boolean> deleteWhitelist(String name) {
		return SharedDatabase.executeWithResult(conn -> {
			String sql = "DELETE FROM protego_whitelists WHERE name = ?";

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, name.toLowerCase());
				int affected = stmt.executeUpdate();

				if (affected > 0) {
					whitelistCache.remove(name.toLowerCase());
					return true;
				}
				return false;
			}
		});
	}

	public CompletableFuture<Boolean> setWhitelistActive(String name, boolean active) {
		return SharedDatabase.executeWithResult(conn -> {
			String sql = "UPDATE protego_whitelists SET is_active = ? WHERE name = ?";

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setBoolean(1, active);
				stmt.setString(2, name.toLowerCase());
				int affected = stmt.executeUpdate();

				if (affected > 0) {
					WhitelistInfo info = whitelistCache.get(name.toLowerCase());
					if (info != null) {
						info.setActive(active);
					}
					return true;
				}
				return false;
			}
		});
	}

	// Player Management Methods

	public CompletableFuture<Boolean> addPlayerToWhitelist(String whitelistName, UUID playerUuid, String playerName, UUID addedBy) {
		return SharedDatabase.executeWithResult(conn -> {
			String sql = """
				INSERT INTO protego_whitelist_players (whitelist_id, player_uuid, player_name, added_by)
				SELECT id, ?, ?, ? FROM protego_whitelists WHERE name = ?
				""";

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, playerUuid.toString());
				stmt.setString(2, playerName);
				stmt.setString(3, addedBy.toString());
				stmt.setString(4, whitelistName.toLowerCase());
				stmt.executeUpdate();

				// Clear player cache
				playerWhitelistCache.remove(playerUuid.toString());

				return true;
			}
		});
	}

	public CompletableFuture<Boolean> removePlayerFromWhitelist(String whitelistName, UUID playerUuid) {
		return SharedDatabase.executeWithResult(conn -> {
			String sql = """
				DELETE wp FROM protego_whitelist_players wp
				JOIN protego_whitelists w ON wp.whitelist_id = w.id
				WHERE w.name = ? AND wp.player_uuid = ?
				""";

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, whitelistName.toLowerCase());
				stmt.setString(2, playerUuid.toString());
				int affected = stmt.executeUpdate();

				if (affected > 0) {
					playerWhitelistCache.remove(playerUuid.toString());
					return true;
				}
				return false;
			}
		});
	}

	public CompletableFuture<Boolean> addPlayerToMultipleWhitelists(List<String> whitelistNames, UUID playerUuid, String playerName, UUID addedBy) {
		return SharedDatabase.executeWithResult(conn -> {
			conn.setAutoCommit(false);
			try {
				String sql = """
					INSERT INTO protego_whitelist_players (whitelist_id, player_uuid, player_name, added_by)
					SELECT id, ?, ?, ? FROM protego_whitelists WHERE name = ?
					""";

				try (PreparedStatement stmt = conn.prepareStatement(sql)) {
					for (String whitelistName : whitelistNames) {
						stmt.setString(1, playerUuid.toString());
						stmt.setString(2, playerName);
						stmt.setString(3, addedBy.toString());
						stmt.setString(4, whitelistName.toLowerCase());
						stmt.executeUpdate();
					}
				}

				conn.commit();
				playerWhitelistCache.remove(playerUuid.toString());
				return true;
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				conn.setAutoCommit(true);
			}
		});
	}

	public CompletableFuture<Boolean> removePlayerFromMultipleWhitelists(List<String> whitelistNames, UUID playerUuid) {
		return SharedDatabase.executeWithResult(conn -> {
			conn.setAutoCommit(false);
			try {
				String sql = """
					DELETE wp FROM protego_whitelist_players wp
					JOIN protego_whitelists w ON wp.whitelist_id = w.id
					WHERE w.name = ? AND wp.player_uuid = ?
					""";

				try (PreparedStatement stmt = conn.prepareStatement(sql)) {
					for (String whitelistName : whitelistNames) {
						stmt.setString(1, whitelistName.toLowerCase());
						stmt.setString(2, playerUuid.toString());
						stmt.executeUpdate();
					}
				}

				conn.commit();
				playerWhitelistCache.remove(playerUuid.toString());
				return true;
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			} finally {
				conn.setAutoCommit(true);
			}
		});
	}

	// Query Methods

	public CompletableFuture<Boolean> isPlayerWhitelisted(UUID playerUuid) {
		String cacheKey = playerUuid.toString();
		Boolean cached = playerWhitelistCache.get(cacheKey);
		if (cached != null) {
			return CompletableFuture.completedFuture(cached);
		}

		return SharedDatabase.query(conn -> {
			String sql = """
				SELECT COUNT(*) FROM protego_whitelist_players wp
				JOIN protego_whitelists w ON wp.whitelist_id = w.id
				WHERE wp.player_uuid = ? AND w.is_active = TRUE
				AND (wp.expires_at IS NULL OR wp.expires_at > CURRENT_TIMESTAMP)
				""";

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, playerUuid.toString());

				try (ResultSet rs = stmt.executeQuery()) {
					boolean whitelisted = rs.next() && rs.getInt(1) > 0;
					playerWhitelistCache.put(cacheKey, whitelisted);
					return whitelisted;
				}
			}
		});
	}

	public CompletableFuture<List<WhitelistInfo>> getAllWhitelists() {
		return SharedDatabase.query(conn -> {
			String sql = "SELECT name, display_name, description, is_active, created_by FROM protego_whitelists ORDER BY name";

			List<WhitelistInfo> whitelists = new ArrayList<>();

			try (PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

				while (rs.next()) {
					WhitelistInfo info = new WhitelistInfo(
						rs.getString("name"),
						rs.getString("display_name"),
						rs.getString("description"),
						rs.getBoolean("is_active"),
						UUID.fromString(rs.getString("created_by"))
					);
					whitelists.add(info);
				}
			}

			return whitelists;
		});
	}

	public CompletableFuture<List<PlayerWhitelistInfo>> getPlayerWhitelists(UUID playerUuid) {
		return SharedDatabase.query(conn -> {
			String sql = """
				SELECT w.name, w.display_name, wp.added_at, wp.expires_at, wp.added_by
				FROM protego_whitelist_players wp
				JOIN protego_whitelists w ON wp.whitelist_id = w.id
				WHERE wp.player_uuid = ?
				ORDER BY wp.added_at DESC
				""";

			List<PlayerWhitelistInfo> playerWhitelists = new ArrayList<>();

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, playerUuid.toString());

				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						PlayerWhitelistInfo info = new PlayerWhitelistInfo(
							rs.getString("name"),
							rs.getString("display_name"),
							rs.getTimestamp("added_at").toLocalDateTime(),
							rs.getTimestamp("expires_at") != null ? rs.getTimestamp("expires_at").toLocalDateTime() : null,
							UUID.fromString(rs.getString("added_by"))
						);
						playerWhitelists.add(info);
					}
				}
			}

			return playerWhitelists;
		});
	}

	public CompletableFuture<List<String>> getWhitelistedPlayers(String whitelistName) {
		return SharedDatabase.query(conn -> {
			// First check if the whitelist exists
			String checkSql = "SELECT id FROM protego_whitelists WHERE name = ?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
				checkStmt.setString(1, whitelistName.toLowerCase());
				try (ResultSet checkRs = checkStmt.executeQuery()) {
					if (!checkRs.next()) {
						// Whitelist doesn't exist, return null to indicate this
						return null;
					}
				}
			}

			// Whitelist exists, get the players
			String sql = """
				SELECT wp.player_name FROM protego_whitelist_players wp
				JOIN protego_whitelists w ON wp.whitelist_id = w.id
				WHERE w.name = ?
				ORDER BY wp.player_name
				""";

			List<String> players = new ArrayList<>();

			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, whitelistName.toLowerCase());

				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						players.add(rs.getString("player_name"));
					}
				}
			}

			return players;
		});
	}

	// Cache Management

	private CompletableFuture<Void> loadAllWhitelists() {
		return getAllWhitelists().thenAccept(whitelists -> {
			whitelistCache.clear();
			for (WhitelistInfo info : whitelists) {
				whitelistCache.put(info.getName(), info);
			}
		});
	}

	public void clearCache() {
		whitelistCache.clear();
		playerWhitelistCache.clear();
	}

	// Synchronous method for suggestions
	public List<String> getCachedWhitelistNames() {
		return whitelistCache.keySet().stream()
			.sorted()
			.collect(Collectors.toList());
	}

	// Data Classes

	public static class WhitelistInfo {
		private final String name;
		private final String displayName;
		private final String description;
		private boolean active;
		private final UUID createdBy;

		public WhitelistInfo(String name, String displayName, String description, boolean active, UUID createdBy) {
			this.name = name;
			this.displayName = displayName;
			this.description = description;
			this.active = active;
			this.createdBy = createdBy;
		}

		// Getters
		public String getName() { return name; }
		public String getDisplayName() { return displayName; }
		public String getDescription() { return description; }
		public boolean isActive() { return active; }
		public UUID getCreatedBy() { return createdBy; }

		// Setters
		public void setActive(boolean active) { this.active = active; }
	}

	public static class PlayerWhitelistInfo {
		private final String whitelistName;
		private final String displayName;
		private final LocalDateTime addedAt;
		private final LocalDateTime expiresAt;
		private final UUID addedBy;

		public PlayerWhitelistInfo(String whitelistName, String displayName, LocalDateTime addedAt, LocalDateTime expiresAt, UUID addedBy) {
			this.whitelistName = whitelistName;
			this.displayName = displayName;
			this.addedAt = addedAt;
			this.expiresAt = expiresAt;
			this.addedBy = addedBy;
		}

		// Getters
		public String getWhitelistName() { return whitelistName; }
		public String getDisplayName() { return displayName; }
		public LocalDateTime getAddedAt() { return addedAt; }
		public LocalDateTime getExpiresAt() { return expiresAt; }
		public UUID getAddedBy() { return addedBy; }

		public boolean isExpired() {
			return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
		}
	}
}
