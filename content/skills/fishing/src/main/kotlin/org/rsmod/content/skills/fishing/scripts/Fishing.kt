package org.rsmod.content.skills.fishing.scripts

import jakarta.inject.Inject
import org.rsmod.api.config.refs.objs
import org.rsmod.api.config.refs.seqs
import org.rsmod.api.config.refs.stats
import org.rsmod.api.player.clearInteractionRoute
import org.rsmod.api.player.protect.ProtectedAccess
import org.rsmod.api.player.protect.clearPendingAction
import org.rsmod.api.player.stat.stat
import org.rsmod.api.repo.npc.NpcRepository
import org.rsmod.api.script.onOpNpc1
import org.rsmod.api.script.onOpNpc3
import org.rsmod.api.stats.levelmod.InvisibleLevels
import org.rsmod.api.type.refs.npc.NpcReferences
import org.rsmod.content.skills.fishing.config.FishingPools
import org.rsmod.game.entity.Npc
import org.rsmod.game.type.obj.ObjTypeList
import org.rsmod.map.CoordGrid
import org.rsmod.plugin.scripts.PluginScript
import org.rsmod.plugin.scripts.ScriptContext

class Fishing
@Inject
constructor(
    private val objTypes: ObjTypeList,
    private val invisibleLvls: InvisibleLevels,
    private val npcRepository: NpcRepository,
    private val fishingPools: FishingPools,
) : PluginScript() {

    object FishingNpcs : NpcReferences() {
        val saltfish = find("0_50_49_saltfish")
    }

    companion object {
        const val FISHING_ANIMATION = 621
        const val SHRIMP_FISHING_XP = 10.0
        const val BASE_SUCCESS_RATE = 19
        const val MAX_SUCCESS_RATE = 50
        const val MIN_TICKS = 3
        const val MAX_TICKS = 5
    }

    // Track original fishing spot coordinates for each player
    private val fishingSessions = mutableMapOf<String, CoordGrid>()

    override fun ScriptContext.startup() {
        onOpNpc1(FishingNpcs.saltfish) { startFishing(it.npc) }
        onOpNpc3(FishingNpcs.saltfish) {
            println("[DEBUG] onOpNpc3 triggered for NPC ${it.npc.id} at ${it.npc.coords}")
            fish(it.npc)
        }
    }

    private suspend fun ProtectedAccess.startFishing(npc: Npc) {
        println("[startFishing] Started fishing at spot ${npc.id}")

        when {
            !inv.contains(objs.small_fishing_net) -> {
                objbox(objs.small_fishing_net, "You need a Small Fishing Net to catch fish here.")
                return
            }
            inv.isFull() -> {
                spam("Your inventory is full.")
                return
            }
            actionDelay > mapClock -> {
                return
            }
//            fishingPools.isSpotScheduledToTeleport(npc) -> {
//                // Spot will teleport soon, just play animation but don't start fishing loop
//                println("[DEBUG] startFishing: Spot ${npc.id} scheduled to teleport, not starting fishing loop")
//                player.anim(seqs.human_smallnet, 0, 0)
//                spam("The fishing spot looks like it's about to move...")
//                return
//            }
            else -> {
                // Store the original fishing spot coordinates and register player
                fishingSessions[player.username] = npc.coords
//                fishingPools.addPlayerToSpot(npc, player)
                player.anim(seqs.human_smallnet, 0, 0)
                spam("You cast out your net...")
                actionDelay = mapClock + (MIN_TICKS..MAX_TICKS).random()
                fish(npc)
            }
        }
    }

    private suspend fun ProtectedAccess.fish(npc: Npc) {
        // Check stop conditions
        if (!inv.contains(objs.small_fishing_net)) {
            objbox(objs.small_fishing_net, "You need a Small Fishing Net to catch fish here.")
            fishingSessions.remove(player.username)
            return
        }
        if (inv.isFull()) {
            spam("Your inventory is full.")
            fishingSessions.remove(player.username)
            return
        }
        // Check if fishing spot is about to teleport (1 tick before)
//        if (fishingPools.isSpotScheduledToTeleport(npc)) {
//            println("[DEBUG] fish: Spot ${npc.id} scheduled to teleport, stopping fishing")
//            spam("The fishing spot is about to move...")
//            fishingSessions.remove(player.username)
//            fishingPools.removePlayerFromSpot(npc, player)
//            stopAction()
//            player.clearInteraction()
//            player.clearWalkTrigger()
//            player.clearInteractionRoute()
//            return
//        }

        // Check if fishing spot has moved from original position
        val originalCoords = fishingSessions[player.username]
        if (originalCoords != null && npc.coords != originalCoords) {
            spam("The fishing spot has moved away.")
            fishingSessions.remove(player.username)
//            fishingPools.removePlayerFromSpot(npc, player)
            stopAction()
            player.clearInteraction()
            player.clearWalkTrigger()
            player.clearInteractionRoute()
            return
        }

        // Handle animation
        if (skillAnimDelay < mapClock) {
            player.anim(seqs.human_smallnet, 0, 0)
        }

        // Handle fishing attempt
        if (actionDelay <= mapClock) {
            // Calculate success
            val fishingLevel = player.stat(stats.fishing)
            val successRate =
                BASE_SUCCESS_RATE +
                    ((MAX_SUCCESS_RATE - BASE_SUCCESS_RATE) * (fishingLevel - 1) / 98)
            val caughtFish = (1..100).random() < successRate

            // Handle catch results
            if (caughtFish) {
                spam("You catch some shrimp.")
                statAdvance(stats.fishing, SHRIMP_FISHING_XP)
                invAdd(inv, objs.raw_shrimp)
                println("[DEBUG] Caught fish at spot ${npc.id}")
            } else {
                spam("You fail to catch anything.")
            }

            actionDelay = mapClock + (MIN_TICKS..MAX_TICKS).random()
        }

        // Schedule next attempt
        val delayCheck = actionDelay > mapClock
        val inventoryCheck = !inv.isFull()
        val netCheck = inv.contains(objs.small_fishing_net)
//        val teleportCheck = !fishingPools.isSpotScheduledToTeleport(npc)
//
//        println("[DEBUG] Schedule next attempt for NPC ${npc.id}: delayCheck=$delayCheck, inventoryCheck=$inventoryCheck, netCheck=$netCheck, teleportCheck=$teleportCheck")
//        println("[DEBUG] NPC timers map: ${fishingPools.getNpcTimersDebug()}")

//        if (delayCheck && inventoryCheck && netCheck && teleportCheck) {
        if (delayCheck && inventoryCheck && netCheck) {
            println("[DEBUG] Calling opNpc3 for NPC ${npc.id}")
            opNpc3(npc)
        } else {
            println("[DEBUG] NOT calling opNpc3 for NPC ${npc.id} - one or more conditions failed")
        }
    }
}

// ::varbit 11556 1

// class Fishing : PluginScript() {
//    object FishingNpcs : NpcReferences() {
//        val saltfish = find("woman")
//        val man = find("man")  // Add local reference
//    }
//    override fun ScriptContext.startup() {
//        onOpNpc1(FishingNpcs.saltfish) {
//            startDialogue(it.npc) {
//                when {
//                    inv.contains(objs.small_fishing_net) -> {
//                        chatNpcSpecific(
//                            "Woman",
//                            FishingNpcs.man,
//                            neutral,
//                            "You already have a net, get fishing!",
//                        )
//                    }
//                    inv.isFull() -> {
//                        chatNpcSpecific("Woman", FishingNpcs.man, angry, "Make some room first!")
//                    }
//                    else -> {
//                        chatNpcSpecific(
//                            "Woman",
//                            FishingNpcs.man,
//                            happy,
//                            "Here you go, a fishing net!",
//                        )
//                        invAdd(inv, objs.small_fishing_net, 1)
//                        chatPlayer(happy, "Thanks!")
//                    }
//                }
//            }
//        }
//    }
// }
