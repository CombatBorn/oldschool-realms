package org.rsmod.content.skills.mining.scripts

import jakarta.inject.Inject
import org.rsmod.api.config.Constants
import org.rsmod.api.config.locParam
import org.rsmod.api.config.locXpParam
import org.rsmod.api.config.objParam
import org.rsmod.api.config.refs.content
import org.rsmod.api.config.refs.params
import org.rsmod.api.config.refs.stats
import org.rsmod.api.player.output.ClientScripts
import org.rsmod.api.player.protect.ProtectedAccess
import org.rsmod.api.player.righthand
import org.rsmod.api.player.stat.miningLvl
import org.rsmod.api.random.GameRandom
import org.rsmod.api.repo.loc.LocRepository
import org.rsmod.api.repo.player.PlayerRepository
import org.rsmod.api.script.onOpLoc1
import org.rsmod.api.script.onOpLoc3
import org.rsmod.api.stats.levelmod.InvisibleLevels
import org.rsmod.content.skills.mining.configs.MiningParams
import org.rsmod.events.UnboundEvent
import org.rsmod.game.entity.Player
import org.rsmod.game.inv.InvObj
import org.rsmod.game.loc.BoundLocInfo
import org.rsmod.game.loc.LocInfo
import org.rsmod.game.type.enums.EnumTypeList
import org.rsmod.game.type.enums.UnpackedEnumType
import org.rsmod.game.type.loc.LocType
import org.rsmod.game.type.loc.UnpackedLocType
import org.rsmod.game.type.obj.ObjType
import org.rsmod.game.type.obj.ObjTypeList
import org.rsmod.game.type.obj.UnpackedObjType
import org.rsmod.game.type.seq.SeqType
import org.rsmod.map.zone.ZoneKey
import org.rsmod.plugin.scripts.PluginScript
import org.rsmod.plugin.scripts.ScriptContext

class Mining
@Inject
constructor(
    private val objTypes: ObjTypeList,
    private val enumTypes: EnumTypeList,
    private val invisibleLvls: InvisibleLevels,
    private val playerRepo: PlayerRepository,
    private val locRepo: LocRepository
) : PluginScript() {
    override fun ScriptContext.startup() {
        onOpLoc1(content.rocks) { attempt(it.loc, it.type) }
        onOpLoc3(content.rocks) { mine(it.loc, it.type) }

    }

    private val locInteractionTickMap: MutableMap<BoundLocInfo, Int> = HashMap()

    private fun ProtectedAccess.attempt(rock: BoundLocInfo, type: UnpackedLocType) {
        if (player.miningLvl < type.miningLevelReq) {
            mes("You need a Mining level of ${type.miningLevelReq} to mine this rock.")
            return
        }

        if (inv.isFull()) {
            val product = objTypes[type.rockOre]
            mes("Your inventory is too full to hold any more ${product.name.lowercase()}.")
            return
        }

        val pickaxe = findPickaxe(player, objTypes)

        if (pickaxe == null) {
            mes("You need a pickaxe to mine this rock.")
            mes("You do not have a pickaxe which you have the mining level to use.")
            return
        }

        if (actionDelay < mapClock) {
            actionDelay = mapClock + objTypes[pickaxe].pickaxeTickRate - 1
            skillAnimDelay = mapClock + 7
            opLoc1(rock)
        } else {
            anim(resolveAnim(type, pickaxe))
            spam("You swing your pickaxe at the rock.")
            mine(rock, type)
        }
    }

    private fun ProtectedAccess.mine(rock: BoundLocInfo, type: UnpackedLocType) {
        val pickaxe = findPickaxe(player, objTypes)
        if (pickaxe == null) {

            mes("You need a pickaxe to mine this rock.")
            mes("You do not have a pickaxe which you have the mining level to use.")
            return
        }

        if (player.miningLvl < type.miningLevelReq) {
            mes("You need a Mining level of ${type.miningLevelReq} to mine this rock.")
            return
        }

        if (inv.isFull()) {
            val product = objTypes[type.rockOre]
            mes("Your inventory is too full to hold any more ${product.name.lowercase()}.")
            return
        }

        if (skillAnimDelay < mapClock) {
            skillAnimDelay = mapClock + 8
            anim(resolveAnim(type, pickaxe))
        }

        var mineOre = false

        if (actionDelay < mapClock) {
            actionDelay = mapClock + objTypes[pickaxe].pickaxeTickRate - 1
        } else if (actionDelay == mapClock) {
            val (low, high) = miningSuccessRates(type, enumTypes)
            mineOre = statRandom(stats.mining, low, high, invisibleLvls)
        }

        if (mineOre) {
            val product = objTypes[type.rockOre]
            val xp = type.miningXp
            spam("You get some ${product.name.lowercase()}.")
            statAdvance(stats.mining, xp)
            invAdd(inv, product)
            publish(MinedOre(player, rock, product))

            if (type.motherlode) {
                // rock is being obtained right now

                if (locInteractionTickMap[rock] == null) {
                    locInteractionTickMap[rock] = mapClock
                }
                val lastInteraction = locInteractionTickMap[rock]!!
                val tickNumber = mapClock - lastInteraction

                val resetRock = mapClock - lastInteraction > 145
                if (resetRock) {
                    locInteractionTickMap[rock] = mapClock
                } else if (tickNumber > 45) {
                    val respawnTime = lastInteraction + 145 + (Math.random() * 8).toInt() - mapClock
                    val loc = LocInfo(rock.layer, rock.coords, rock.entity)
                    locRepo.deleteBypassChecks(loc, respawnTime)
                    resetAnim()
                }
            }

            else {
                //change rock and then respawn
                val respawnTime = type.resolveRespawnTime(random)
                locRepo.change(rock, type.drainedRock, respawnTime)
                resetAnim()
            }
        }

        opLoc3(rock)
    }

    private fun findPickaxe(player: Player, objTypes: ObjTypeList): InvObj? {
        val worn = player.wornPickaxe(objTypes)
        val carried = player.carriedPickaxe(objTypes)

        if (worn != null && carried != null) {
            if (objTypes[worn].pickaxeMiningReq >= objTypes[carried].pickaxeMiningReq) {
                return worn
            }
            return carried
        }
        return worn ?: carried
    }

    private fun resolveAnim(type: UnpackedLocType, pickaxe: InvObj) : SeqType{
        if (type.isWall) {
           return objTypes[pickaxe].pickaxeWallMiningAnim
        }
        return objTypes[pickaxe].pickaxeMiningAnim
    }
    private fun sendLocalOverlayLoc(rock: BoundLocInfo, type: UnpackedLocType, respawnTime: Int) {
        val players = playerRepo.findAll(ZoneKey.from(rock.coords), zoneRadius = 3)
        for (player in players) {
            ClientScripts.addOverlayTimerLoc(
                player = player,
                coords = rock.coords,
                loc = type,
                shape = rock.shape,
                timer = Constants.overlay_timer_woodcutting,
                ticks = respawnTime,
                colour = 16765184,
            )
        }
    }

    private fun UnpackedLocType.resolveRespawnTime(random: GameRandom): Int {
        val fixed = rockRespawnTime
        if (fixed > 0) {
            return fixed
        }
        return random.of(rockRespawnTimeLow, rockRespawnTimeHigh)
    }


    private fun Player.wornPickaxe(objTypes: ObjTypeList): InvObj? {
        val righthand = righthand ?: return null
        return righthand.takeIf { objTypes[it].isUsablePickaxe(miningLvl) }
    }

    private fun Player.carriedPickaxe(objTypes: ObjTypeList): InvObj? {
        return inv.filterNotNull { objTypes[it].isUsablePickaxe(miningLvl) }
            .maxByOrNull { objTypes[it].pickaxeMiningReq }
    }

    private fun UnpackedObjType.isUsablePickaxe(miningLevel: Int): Boolean =
        isContentType(content.mining_pickaxe) && miningLevel >= pickaxeMiningReq

    data class MinedOre(val player: Player, val rock: BoundLocInfo, val product: ObjType) : UnboundEvent

    companion object {
        val UnpackedObjType.pickaxeMiningReq: Int by objParam(params.levelrequire)
        val UnpackedObjType.pickaxeMiningAnim: SeqType by objParam(params.skill_anim)
        val UnpackedObjType.pickaxeWallMiningAnim: SeqType by objParam(params.wall_anim)
        val UnpackedObjType.pickaxeTickRate: Int by objParam(params.pickaxeTickRates)

        val UnpackedLocType.isWall: Boolean by locParam(params.mineable_wall)
        val UnpackedLocType.miningLevelReq: Int by locParam(params.levelrequire)
        val UnpackedLocType.rockOre: ObjType by locParam(params.skill_productitem)
        val UnpackedLocType.miningXp: Double by locXpParam(params.skill_xp)
        val UnpackedLocType.motherlode: Boolean by locParam(params.motherlode)
        val UnpackedLocType.rockRespawnTime: Int by locParam(params.respawn_time)
        val UnpackedLocType.rockRespawnTimeLow: Int by locParam(params.respawn_time_low)
        val UnpackedLocType.rockRespawnTimeHigh: Int by locParam(params.respawn_time_high)
        val UnpackedLocType.drainedRock: LocType by locParam(params.next_loc_stage)
    }

    fun miningSuccessRates(rock: UnpackedLocType, enumTypes: EnumTypeList): Pair<Int, Int> {
        val enum = rock.param(MiningParams.rockSuccessRates)
        val rates: Int = enumTypes[enum].find(rock) ?: error("No rate for $rock.")
        val low: Int = rates ushr 16
        val high: Int = rates and 0xFFFF
        return low to high
    }

    fun <V : Any> UnpackedEnumType<LocType, V>.find(loc: UnpackedLocType): V? =
        findOrNull(loc) ?: throw NoSuchElementException("Key $loc is missing in the map.")

    fun <V : Any> UnpackedEnumType<LocType, V>.findOrNull(loc: UnpackedLocType): V? {
        for ((key, value) in this) {
            if (key.id == loc.id) {
                return value
            }
        }
        return null
    }


}
