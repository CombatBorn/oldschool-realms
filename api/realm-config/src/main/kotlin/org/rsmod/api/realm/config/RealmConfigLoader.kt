package org.rsmod.api.realm.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.inject.Inject
import org.rsmod.api.db.Database
import org.rsmod.api.db.DatabaseConnection
import org.rsmod.api.db.util.getStringOrNull
import org.rsmod.api.parsers.json.Json
import org.rsmod.api.realm.RealmConfig
import org.rsmod.map.CoordGrid

public class RealmConfigLoader
@Inject
constructor(private val database: Database, @Json private val objectMapper: ObjectMapper) {
    public suspend fun load(realm: String): RealmConfig? {
        return database.withTransaction { connection -> loadRealm(connection, realm) }
    }

    private fun loadRealm(connection: DatabaseConnection, name: String): RealmConfig? {
        val select =
            connection.prepareStatement(
                """
                    SELECT id, login_message, login_broadcast, spawn_coord, respawn_coord,
                        dev_mode, require_registration, ignore_passwords, auto_assign_display_names,
                        player_xp_rate_in_hundreds, global_xp_rate_in_hundreds
                        FROM realms WHERE name = ?
                """
                    .trimIndent()
            )

        select.use {
            it.setString(1, name)
            it.executeQuery().use { resultSet ->
                if (resultSet.next()) {
                    val id = resultSet.getInt("id")
                    val loginMessage = resultSet.getStringOrNull("login_message")
                    val loginBroadcast = resultSet.getStringOrNull("login_broadcast")
                    val baseXpRateInHundreds = resultSet.getInt("player_xp_rate_in_hundreds")
                    val globalXpRateInHundreds = resultSet.getInt("global_xp_rate_in_hundreds")
                    val spawnCoord = resultSet.getString("spawn_coord")
                    val respawnCoord = resultSet.getString("respawn_coord")
                    val devMode = resultSet.getBoolean("dev_mode")
                    val requireRegistration = resultSet.getBoolean("require_registration")
                    val ignorePasswords = resultSet.getBoolean("ignore_passwords")
                    val autoAssignDisplayNames = resultSet.getBoolean("auto_assign_display_names")
                    return RealmConfig(
                        id = id,
                        loginMessage = loginMessage,
                        loginBroadcast = loginBroadcast?.takeUnless(String::isBlank),
                        baseXpRate = baseXpRateInHundreds / 100.0,
                        globalXpRate = globalXpRateInHundreds / 100.0,
                        spawnCoord = spawnCoord.toCoordGrid(),
                        respawnCoord = respawnCoord.toCoordGrid(),
                        devMode = devMode,
                        requireRegistration = requireRegistration,
                        ignorePasswords = ignorePasswords,
                        autoAssignDisplayNames = autoAssignDisplayNames,
                    )
                }
            }
        }

        return null
    }

    private fun String.toCoordGrid(): CoordGrid {
        val withQuotes = "\"$this\"" // Object mapper expects quotes.
        return objectMapper.readValue(withQuotes, CoordGrid::class.java)
    }
}
