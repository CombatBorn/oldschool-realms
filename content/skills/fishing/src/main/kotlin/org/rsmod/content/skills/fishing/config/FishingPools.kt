package org.rsmod.content.skills.fishing.config

import com.google.inject.Inject
import kotlin.ranges.random
import org.rsmod.api.config.refs.npcs
import org.rsmod.api.hunt.NpcSearch
import org.rsmod.api.repo.npc.NpcRepository
import org.rsmod.api.repo.player.PlayerRepository
import org.rsmod.api.repo.world.WorldRepository
import org.rsmod.api.script.onAiTimer
import org.rsmod.game.MapClock
import org.rsmod.game.entity.Npc
import org.rsmod.game.entity.Player
import org.rsmod.game.type.npc.NpcTypeList
import org.rsmod.map.CoordGrid
import org.rsmod.plugin.scripts.PluginScript
import org.rsmod.plugin.scripts.ScriptContext
import org.rsmod.routefinder.collision.CollisionFlagMap

class FishingPools
@Inject
constructor(
    private val npcRepository: NpcRepository,
    private val playerRepository: PlayerRepository,
    private val mapClock: MapClock,
    private val worldRepo: WorldRepository,
    private val search: NpcSearch,
    private val collision: CollisionFlagMap,
    private val npcTypes: NpcTypeList,
) : PluginScript() {


    private val foundNpcs = mutableListOf<Npc>()
    private val npcTimers = mutableMapOf<Npc, Int>()
    private val teleportQueue = mutableListOf<Npc>()
    private val playersAtSpots = mutableMapOf<Npc, MutableSet<Player>>()
    private var initialized = false
    private var initializationAttempts = 0
    private var nextInitializationAttempt = 0

    companion object {
        private val ALL_SPOT_LOCATIONS =
            listOf(
                CoordGrid(0,50,49,42,7),
                CoordGrid(0,50,49,40,10),
                CoordGrid(0,50,49,40,11),
                CoordGrid(0,50,49,41,12),
                CoordGrid(0,50,49,42,12),
                CoordGrid(0,50,49,44,14),
                CoordGrid(0,50,49,45,16),
                CoordGrid(0,50,49,46,19),
            )

        // Timer delays for the 5 NPCs
        private val STAGGERED_TIMERS = listOf(250/10, 320/10, 390/10, 460/10, 530/10)
        private const val RETRY_INITIALIZATION_DELAY = 10
    }

    override fun ScriptContext.startup() {
        println("[FishingPools] Plugin starting up with new movement system")
        onAiTimer(npcs.fishing_spot_1530) { processSpotMovements() }
        initializeFishingSpots()
    }

    private fun initializeFishingSpots() {
        initializationAttempts++
        println("\n[FishingPools] Initialization attempt #$initializationAttempts")
        println("[FishingPools] Initializing fishing spots with new movement system")

        // Find all existing fishing spots using mapNotNull approach
        val allSpots = ALL_SPOT_LOCATIONS.mapNotNull { location ->
            val npc = findNpcAtLocation(location)
            if (npc == null) {
                println("[DEBUG] No fishing spot found at $location")
                null
            } else {
                println("[DEBUG] Found fishing spot ${npc.id} (${npc.type.internalName}) at $location")
                npc
            }
        }

        foundNpcs.clear()
        foundNpcs.addAll(allSpots)

        if (foundNpcs.isEmpty()) {
            println("[FishingPools] No fishing spots found, will retry in $RETRY_INITIALIZATION_DELAY ticks")
            nextInitializationAttempt = mapClock.cycle + RETRY_INITIALIZATION_DELAY
            return
        }

        println("[DEBUG] Total spots found: ${foundNpcs.size}/${ALL_SPOT_LOCATIONS.size}")
        foundNpcs.forEach { npc -> println("[DEBUG] Located spot ${npc.id} at ${npc.coords}") }

        // Teleport each NPC to a random spot ensuring no overlap
        val availableSpots = ALL_SPOT_LOCATIONS.toMutableList()
        foundNpcs.forEach { npc ->
            if (availableSpots.isNotEmpty()) {
                val randomSpot = availableSpots.removeAt(availableSpots.indices.random())
                npc.teleport(collision, randomSpot)
                println("[DEBUG] Teleported NPC ${npc.id} to initial position $randomSpot")
            }
        }

        // Set staggered timers for each NPC (250, 320, 390, 460, 530)
        foundNpcs.forEachIndexed { index, npc ->
            if (index < STAGGERED_TIMERS.size) {
                val timerDelay = STAGGERED_TIMERS[index]
                val scheduledTime = mapClock.cycle + timerDelay
                npcTimers[npc] = scheduledTime
                println("[DEBUG] Set timer for NPC ${npc.id} (index $index) to ${timerDelay} cycles (scheduled at cycle $scheduledTime)")
            }
        }

        initialized = true
        println("[DEBUG] Initialization complete - Managing ${foundNpcs.size} NPCs")
        println("[DEBUG] Current map cycle: ${mapClock.cycle}")
        println("[DEBUG] Timer schedule: ${npcTimers.map { "NPC ${it.key.id} -> cycle ${it.value}" }}")
    }

    fun processSpotMovements() {
        // Check if we need to retry initialization
        if (!initialized && mapClock.cycle >= nextInitializationAttempt && nextInitializationAttempt > 0) {
            initializeFishingSpots()
            return
        }

        if (!initialized) return

        val currentCycle = mapClock.cycle
        val npcsReadyToMove = mutableListOf<Npc>()

        // Check which NPCs are ready to move
        npcTimers.forEach { (npc, scheduledTime) ->
            if (currentCycle >= scheduledTime) {
                npcsReadyToMove.add(npc)
                println("[DEBUG] NPC ${npc.id} ready to move at cycle $currentCycle (was scheduled for $scheduledTime)")
            }
        }

        // If multiple NPCs are ready, add them to queue and process one at a time
        if (npcsReadyToMove.size > 1) {
            teleportQueue.addAll(npcsReadyToMove)
            npcsReadyToMove.forEach { npc ->
                npcTimers.remove(npc)
            }
            println("[DEBUG] Added ${npcsReadyToMove.size} NPCs to teleport queue")
        } else if (npcsReadyToMove.isNotEmpty()) {
            val npc = npcsReadyToMove.first()
            teleportNpcToAvailableSpot(npc)
            npcTimers.remove(npc)
        }

        // Process queue (one NPC per tick to prevent overlap)
        if (teleportQueue.isNotEmpty()) {
            val npc = teleportQueue.removeAt(0)
            teleportNpcToAvailableSpot(npc)
            println("[DEBUG] Processed NPC ${npc.id} from teleport queue")
        }
    }

    private fun teleportNpcToAvailableSpot(npc: Npc) {
        // Find spots that don't have any NPCs
        val occupiedSpots = foundNpcs.map { it.coords }.toSet()
        val availableSpots = ALL_SPOT_LOCATIONS.filter { it !in occupiedSpots }

        if (availableSpots.isNotEmpty()) {
            val newSpot = availableSpots.random()
            npc.teleport(collision, newSpot)

            // Set new timer randomly selecting from staggered timer values
            val newTimerDelay = STAGGERED_TIMERS.random()
            val newScheduledTime = mapClock.cycle + newTimerDelay
            npcTimers[npc] = newScheduledTime
            npc.aiTimer(newTimerDelay)

            println("[DEBUG] Teleported NPC ${npc.id} to $newSpot, next move in $newTimerDelay cycles")
        } else {
            // No available spots, reschedule for later
            val retryDelay = 50
            val retryTime = mapClock.cycle + retryDelay
            npcTimers[npc] = retryTime
            println("[DEBUG] No available spots for NPC ${npc.id}, retrying in $retryDelay cycles")
        }
    }

    private fun findNpcAtLocation(location: CoordGrid): Npc? {
        val npcsAtLocation = npcRepository.findAll(location).toList()
        println("[DEBUG] Checking location $location:")
        println("[DEBUG] Found ${npcsAtLocation.size} NPCs at location")
        npcsAtLocation.forEach { npc ->
            println("[DEBUG] - NPC ${npc.id} with internal name: '${npc.type.internalName}'")
        }
        val fishingSpot = npcsAtLocation.firstOrNull { it.type.internalName == "0_50_49_saltfish" }
        if (fishingSpot != null) {
            println("[DEBUG] Found matching fishing spot ${fishingSpot.id} at $location")
        } else {
            println("[DEBUG] No fishing spot with internal name '0_50_49_saltfish' found at $location")
        }
        return fishingSpot
    }
//    override fun ScriptContext.startup() {
//        println("[FishingPools] Plugin starting up")
//
//        // Set up fish spot behaviors to use AI timer for movement
//        onAiTimer(npcs.fishing_spot_1530) { processSpotMovements() }
//
//        initializeWithExistingNpcs()
//    }
//
//    fun initializeWithExistingNpcs() {
//        initializationAttempts++
//        println("\n[FishingPools] Initialization attempt #$initializationAttempts")
//
//        // Find all existing fishing spots
//        val allSpots =
//            ALL_SPOT_LOCATIONS.mapNotNull { location ->
//                val npc = findNpcAtLocation(location)
//                if (npc == null) {
//                    println("[DEBUG] No fishing spot found at $location")
//                    null
//                } else {
//                    println(
//                        "[DEBUG] Found fishing spot ${npc.id} (${npc.type.internalName}) at $location"
//                    )
//                    location to npc
//                }
//            }
//                .toMap()
//
//        if (allSpots.isEmpty()) {
//            println(
//                "[FishingPools] No fishing spots found, will retry in $RETRY_INITIALIZATION_DELAY ticks"
//            )
//            nextInitializationAttempt = mapClock.cycle + RETRY_INITIALIZATION_DELAY
//            return
//        }
//
//        println("[DEBUG] Total spots found: ${allSpots.size}/${ALL_SPOT_LOCATIONS.size}")
//        allSpots.forEach { (loc, npc) -> println("[DEBUG] Located spot ${npc.id} at $loc") }
//
//        // Randomly select spots to hide
//        val locationsToHide = allSpots.keys.shuffled().take(INITIAL_HIDDEN_SPOTS)
//        println("[DEBUG] Locations to hide: ${locationsToHide.joinToString()}")
//
//        allSpots.forEach { (location, npc) ->
//            if (location in locationsToHide) {
//                println("[DEBUG] Hiding spot ${npc.id} at $location")
//                npcRepository.hide(npc, Int.MAX_VALUE)
//                hiddenSpots[location] = npc
//            } else {
//                activeSpots[location] = npc
//                val delay =
//                    if (activeSpots.size == 1) {
//                        println("[DEBUG] First active spot gets 250 tick timer at $location")
//                        250
//                    } else {
//                        val randomDelay = (MIN_MOVE_DELAY..MAX_MOVE_DELAY).random()
//                        println("[DEBUG] Setting $randomDelay tick timer at $location")
//                        randomDelay
//                    }
//                npcMoveTimers[npc] = mapClock.cycle + delay
//                // Set initial AI timer for active spots
//                npc.aiTimer(delay)
//            }
//        }
//
//        println("[DEBUG] Initialization complete")
//        println("[DEBUG] Active spots (${activeSpots.size}): ${activeSpots.keys.joinToString()}")
//        println("[DEBUG] Hidden spots (${hiddenSpots.size}): ${hiddenSpots.keys.joinToString()}")
//    }
}
