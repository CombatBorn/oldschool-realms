package org.rsmod.content.skills.mining.motherlode.scripts

import com.github.michaelbull.logging.InlineLogger
import jakarta.inject.Inject
import org.rsmod.api.config.refs.content
import org.rsmod.api.player.protect.ProtectedAccess
import org.rsmod.api.repo.loc.LocRepository
import org.rsmod.api.script.onOpLoc1
import org.rsmod.api.script.onOpLoc3
import org.rsmod.content.skills.mining.configs.drainedRocks
import org.rsmod.content.skills.mining.configs.rocks
import org.rsmod.game.type.loc.LocTypeList
import org.rsmod.map.zone.ZoneKey
import org.rsmod.plugin.scripts.PluginScript
import org.rsmod.plugin.scripts.ScriptContext

class Motherlode
@Inject constructor(
    val locRepo : LocRepository,
    val locTypes : LocTypeList
) : PluginScript() {
    private val logger = InlineLogger()

    override fun ScriptContext.startup() {
        //Replace the depleted rocks with ore veins
        configureMotherlodeWalls()

        onOpLoc1(content.motherlode_walls){}
        onOpLoc3(content.motherlode_walls){}
    }

    fun ProtectedAccess.attempt() {

    }

    private fun configureMotherlodeWalls() {

        val wallReplacements = mapOf(
            drainedRocks.motherlode_depleted_single.id to locTypes[rocks.motherlode_ore_single],
            drainedRocks.motherlode_depleted_left.id to locTypes[rocks.motherlode_ore_left],
            drainedRocks.motherlode_depleted_middle.id to locTypes[rocks.motherlode_ore_middle],
            drainedRocks.motherlode_depleted_right.id to locTypes[rocks.motherlode_ore_right]
        )

        var totalReplacements = 0

        // Define zone coordinate bounds - expanded to cover larger motherlode mine area
        val bottomLeftZoneX = 460   // Expanded search area
        val bottomLeftZoneZ = 700   // Expanded search area
        val topRightZoneX = 480     // Expanded search area
        val topRightZoneZ = 720     // Expanded search area

        // Loop through zone coordinates incrementing by 1
        for (zoneX in bottomLeftZoneX..topRightZoneX) {
            for (zoneZ in bottomLeftZoneZ..topRightZoneZ) {
                for (level in 0..3) {
                    val zone = ZoneKey(zoneX, zoneZ, level)

                    val wallPositions = locRepo.findAll(zone)
                        .filter { wall -> wallReplacements.containsKey(wall.entity.id) }
                        .toList()

                    wallPositions.forEach { wall ->
                        val targetType = wallReplacements[wall.entity.id]
                        if (targetType != null) {
                            locRepo.change(wall, targetType, Int.MAX_VALUE)
                        }
                    }
                    totalReplacements += wallPositions.size
                }
            }
        }

        logger.info { "Total replaced $totalReplacements motherlode walls across region" }
    }

}
