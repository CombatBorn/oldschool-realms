package org.rsmod.content.skills.fishing.scripts

import jakarta.inject.Inject
import org.rsmod.api.config.refs.objs
import org.rsmod.api.config.refs.seqs
import org.rsmod.api.config.refs.stats
import org.rsmod.api.player.protect.ProtectedAccess
import org.rsmod.api.player.stat.stat
import org.rsmod.api.repo.npc.NpcRepository
import org.rsmod.api.script.onOpNpc1
import org.rsmod.api.script.onOpNpc3
import org.rsmod.api.stats.levelmod.InvisibleLevels
import org.rsmod.api.type.refs.npc.NpcReferences
import org.rsmod.game.entity.Npc
import org.rsmod.game.type.obj.ObjTypeList
import org.rsmod.plugin.scripts.PluginScript
import org.rsmod.plugin.scripts.ScriptContext
class Fishing
@Inject
constructor(
    private val objTypes: ObjTypeList,
    private val invisibleLvls: InvisibleLevels,
    private val npcRepository: NpcRepository,
): PluginScript() {

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
        const val MIN_DEPLETION = 5
        const val MAX_DEPLETION = 25
    }

    private val depletionMap = mutableMapOf<Npc, Int>()
    private val maxDepletionMap = mutableMapOf<Npc, Int>()

    override fun ScriptContext.startup() {
        onOpNpc1(FishingNpcs.saltfish) { startFishing(it.npc) }
        onOpNpc3(FishingNpcs.saltfish) { fish(it.npc) }
    }

    private suspend fun ProtectedAccess.startFishing(npc: Npc) {
        println("[startFishing] Initializing fishing")
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
            else -> {
                if (!depletionMap.containsKey(npc)) {
                    val maxFish = (MIN_DEPLETION..MAX_DEPLETION).random()
                    depletionMap[npc] = maxFish
                    maxDepletionMap[npc] = maxFish
                    println("[DEBUG] New fishing spot created. Max fish: $maxFish")
                }

                player.anim(seqs.human_smallnet, 0, 0)
                spam("You cast out your net...")
                actionDelay = mapClock + (MIN_TICKS..MAX_TICKS).random()
                fish(npc)
            }
        }
    }

    private suspend fun ProtectedAccess.fish(npc: Npc) {
        // Check stop conditions before each attempt
        if (!inv.contains(objs.small_fishing_net)) {
            objbox(objs.small_fishing_net, "You need a Small Fishing Net to catch fish here.")
            depletionMap.remove(npc)
            maxDepletionMap.remove(npc)
            println("[DEBUG] Fishing stopped - no net. Spot removed from tracking.")
            return
        }
        if (inv.isFull()) {
            spam("Your inventory is full.")
            depletionMap.remove(npc)
            maxDepletionMap.remove(npc)
            println("[DEBUG] Fishing stopped - full inventory. Spot removed from tracking.")
            return
        }

        // Handle animation
        if (skillAnimDelay < mapClock) {
            player.anim(seqs.human_smallnet, 0, 0)
        }

        // Handle fishing attempt
        if (actionDelay <= mapClock) {
            val currentDepletion = depletionMap[npc] ?: 0
            val maxDepletion = maxDepletionMap[npc] ?: MAX_DEPLETION
            val shouldDecrement = currentDepletion >= maxDepletion
            println("[DEBUG] Fishing attempt - Current: $currentDepletion, Max: $maxDepletion, Should decrement: $shouldDecrement")

            val fishingLevel = player.stat(stats.fishing)
            val successRate = BASE_SUCCESS_RATE + ((MAX_SUCCESS_RATE - BASE_SUCCESS_RATE) * (fishingLevel - 1) / 98)
            val caughtFish = (1..100).random() < successRate

            if (caughtFish && currentDepletion > 0) {
                spam("You catch some shrimp.")
                statAdvance(stats.fishing, SHRIMP_FISHING_XP)
                invAdd(inv, objs.raw_shrimp)

                if (!shouldDecrement) {
                    depletionMap[npc] = currentDepletion + 1
                    println("[DEBUG] Fish caught! Incrementing depletion to ${currentDepletion + 1}")
                } else {
                    println("[DEBUG] Fish caught but in decrement phase (no change to counter)")
                }
            } else {
                spam("You fail to catch anything.")
                println("[DEBUG] Failed to catch fish")
            }

            if (shouldDecrement) {
                depletionMap[npc] = currentDepletion - 1
                println("[DEBUG] Decrementing depletion to ${currentDepletion - 1}")
            }

            // Depletion check (updated)
            if ((shouldDecrement && currentDepletion <= 1) || currentDepletion <= 0) {
                spam("The fishing spot has been depleted.")
                println("[DEBUG] Fishing spot depleted! Removing from tracking.")
                depletionMap.remove(npc)
                maxDepletionMap.remove(npc)
                npcRepository.despawn(npc, duration = 60)
                return
            }

            actionDelay = mapClock + (MIN_TICKS..MAX_TICKS).random()
        }

        // Schedule next attempt if conditions are met
        if (actionDelay > mapClock && !inv.isFull() && inv.contains(objs.small_fishing_net) && depletionMap.containsKey(npc)) {
            println("[DEBUG] Scheduling next fishing attempt")
            opNpc3(npc)
        }
    }
}

//class Fishing : PluginScript() {
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
//}
