package org.rsmod.content.skills.mining.configs

import org.rsmod.api.config.refs.content
import org.rsmod.api.config.refs.objs
import org.rsmod.api.config.refs.params
import org.rsmod.api.type.editors.loc.LocEditor
import org.rsmod.api.type.refs.loc.LocReferences
import org.rsmod.content.skills.mining.configs.MiningParams.rockSuccessRates
import org.rsmod.game.stat.PlayerStatMap
import org.rsmod.game.type.loc.LocType
import org.rsmod.game.type.obj.ObjType

internal typealias rocks = RockLocs
internal typealias drainedRocks = DrainedLocs

internal object MiningOres : LocEditor() {
    init {

        /**
         *
         * Different rocks from main tin -> runite
         *
         **/

        //Tin rocks
        configureRock(
            rock = rocks.tin_rock_1,
            drained = drainedRocks.drained_1,
            ore = objs.tin_ore,
            respawnTime = 4,
            miningXp = 17.5,
            miningLevel = 1,
            isWall = false,
        )

        configureRock(
            rock = rocks.tin_rock_2,
            drained = drainedRocks.drained_2,
            ore = objs.tin_ore,
            respawnTime = 4,
            miningXp = 17.5,
            miningLevel = 1,
            isWall = false,
        )

        //Copper rocks
        configureRock(
            rock = rocks.copper_rock_1,
            drained = drainedRocks.drained_1,
            ore = objs.copper_ore,
            respawnTime = 4,
            miningXp = 17.5,
            miningLevel = 1,
            isWall = false,
        )

        configureRock(
            rock = rocks.copper_rock_2,
            drained = drainedRocks.drained_2,
            ore = objs.copper_ore,
            respawnTime = 4,
            miningXp = 17.5,
            miningLevel = 1,
            isWall = false,
        )

        //Iron rocks
        configureRock(
            rock = rocks.iron_rock_1,
            drained = drainedRocks.drained_1,
            ore = objs.iron_ore,
            respawnTime = 9,
            miningXp = 35.0,
            miningLevel = 15,
            isWall = false,
        )

        configureRock(
            rock = rocks.iron_rock_2,
            drained = drainedRocks.drained_2,
            ore = objs.iron_ore,
            respawnTime = 9,
            miningXp = 35.0,
            miningLevel = 15,
            isWall = false,
        )

        configureRock(
            rock = rocks.prif_mine_iron_rock1,
            drained = drainedRocks.prif_mine_rocks1_empty,
            ore = objs.iron_ore,
            respawnTime = 9,
            miningXp = 35.0,
            miningLevel = 15,
            isWall = false,
        )

        //Silver rocks
        configureRock(
            rock = rocks.silver_rock_1,
            drained = drainedRocks.drained_1,
            ore = objs.silver_ore,
            respawnTime = 100,
            miningXp = 40.0,
            miningLevel = 20,
            isWall = false,
        )

        configureRock(
            rock = rocks.silver_rock_2,
            drained = drainedRocks.drained_2,
            ore = objs.silver_ore,
            respawnTime = 100,
            miningXp = 40.0,
            miningLevel = 20,
            isWall = false,
        )

        configureRock(
            rock = rocks.prif_mine_silver_rock1,
            drained = drainedRocks.prif_mine_rocks1_empty,
            ore = objs.silver_ore,
            respawnTime = 100,
            miningXp = 40.0,
            miningLevel = 20,
        )

        //Coal rocks
        configureRock(
            rock = rocks.coalrock1,
            drained = drainedRocks.drained_1,
            ore = objs.coal,
            respawnTime = 50,
            miningXp = 50.0,
            miningLevel = 30,
        )

        configureRock(
            rock = rocks.coalrock2,
            drained = drainedRocks.drained_2,
            ore = objs.coal,
            respawnTime = 50,
            miningXp = 50.0,
            miningLevel = 30,
        )

        //Gold rock
        configureRock(
            rock = rocks.goldrock1,
            drained = drainedRocks.drained_1,
            ore = objs.gold_ore,
            respawnTime = 100,
            miningXp = 65.0,
            miningLevel = 40,
        )

        configureRock(
            rock = rocks.goldrock2,
            drained = drainedRocks.drained_2,
            ore = objs.gold_ore,
            respawnTime = 100,
            miningXp = 65.0,
            miningLevel = 40,
        )

        configureRock(
            rock = rocks.prif_mine_goldrock1,
            drained = drainedRocks.prif_mine_rocks1_empty,
            ore = objs.gold_ore,
            respawnTime = 100,
            miningXp = 65.0,
            miningLevel = 40,
        )


        configureRock(
            rock = rocks.prif_mine_coalrock1,
            drained = drainedRocks.prif_mine_rocks1_empty,
            ore = objs.coal,
            respawnTime = 50,
            miningXp = 50.0,
            miningLevel = 30,
        )


        //Mithril rock
        configureRock(
            rock = rocks.mithrilrock1,
            drained = drainedRocks.drained_1,
            ore = objs.mithril_ore,
            respawnTime = 200,
            miningXp = 65.0,
            miningLevel = 55,
        )

        configureRock(
            rock = rocks.mithrilrock2,
            drained = drainedRocks.drained_2,
            ore = objs.mithril_ore,
            respawnTime = 200,
            miningXp = 65.0,
            miningLevel = 55,
        )

        configureRock(
            rock = rocks.prif_mine_mithrilrock1,
            drained = drainedRocks.prif_mine_rocks1_empty,
            ore = objs.mithril_ore,
            respawnTime = 200,
            miningXp = 65.0,
            miningLevel = 55,
        )

        //Adamantite rock
        configureRock(
            rock = rocks.adamantiterock1,
            drained = drainedRocks.drained_1,
            ore = objs.adamantite_ore,
            respawnTime = 400,
            miningXp = 95.0,
            miningLevel = 70,
        )
        configureRock(
            rock = rocks.adamantiterock2,
            drained = drainedRocks.drained_2,
            ore = objs.adamantite_ore,
            respawnTime = 400,
            miningXp = 95.0,
            miningLevel = 70,
        )
        configureRock(
            rock = rocks.prif_mine_adamantiterock1,
            drained = drainedRocks.prif_mine_rocks1_empty,
            ore = objs.adamantite_ore,
            respawnTime = 400,
            miningXp = 95.0,
            miningLevel = 70,
        )

        //Runite rock
        configureRock(
            rock = rocks.runiterock1,
            drained = drainedRocks.drained_1,
            ore = objs.runite_ore,
            respawnTime = 1200,
            miningXp = 125.0,
            miningLevel = 85,
        )
        configureRock(
            rock = rocks.runiterock2,
            drained = drainedRocks.drained_2,
            ore = objs.runite_ore,
            respawnTime = 1200,
            miningXp = 125.0,
            miningLevel = 85,
        )
        configureRock(
            rock = rocks.prif_mine_runiterock1,
            drained = drainedRocks.prif_mine_rocks1_empty,
            ore = objs.runite_ore,
            respawnTime = 1200,
            miningXp = 125.0,
            miningLevel = 85,
        )


        /**
         *
         * Soft clay/Clay rocks
         *
         */

        configureRock(
            rock = rocks.prif_mine_softclayrock1,
            drained = drainedRocks.prif_mine_rocks1_empty,
            ore = objs.softclay,
            respawnTime = 2,
            miningXp = 5.0,
            miningLevel = 70
        )

        /**
         *
         *  Wall rocks configs
         *
         */

        configureRock(
            rock = rocks.amethystrock1,
            drained = drainedRocks.amethystrock_empty,
            ore = objs.amethyst,
            respawnTime = 125,
            miningXp = 240.0,
            miningLevel = 92,
            isWall = true
        )

        configureRock(
            rock = rocks.amethystrock2,
            drained = drainedRocks.amethystrock_empty,
            ore = objs.amethyst,
            respawnTime = 125,
            miningXp = 240.0,
            miningLevel = 92,
            isWall = true
        )

        configureMotherlodeWall(
            rock = rocks.motherlode_ore_single,
            drained = drainedRocks.motherlode_depleted_single,
            ore = objs.paydirt,
            respawnTime = 100,

            miningXp = 60.0,
            miningLevel = 30,
            isWall = true
        )
        configureMotherlodeWall(
            rock = rocks.motherlode_ore_middle,
            drained = drainedRocks.motherlode_depleted_middle,
            ore = objs.paydirt,
            respawnTime = 100,
            miningXp = 60.0,
            miningLevel = 30,
            isWall = true
        )
        configureMotherlodeWall(
            rock = rocks.motherlode_ore_left,
            drained = drainedRocks.motherlode_depleted_left,
            ore = objs.paydirt,
            respawnTime = 100,
            miningXp = 60.0,
            miningLevel = 30,
            isWall = true
        )

        configureMotherlodeWall(
            rock = rocks.motherlode_ore_right,
            drained = drainedRocks.motherlode_depleted_right,
            ore = objs.paydirt,
            respawnTime = 100,
            miningXp = 60.0,
            miningLevel = 30,
            isWall = true
        )




    }



    private fun configureRock(rock: LocType, drained: LocType, ore: ObjType, respawnTime: Int, miningXp : Double, miningLevel: Int, isWall : Boolean = false) {
        edit(rock) {
            contentGroup = content.rocks
            param[params.respawn_time] = respawnTime
            param[params.levelrequire] = miningLevel // Set the required mining level
            param[params.skill_xp] = PlayerStatMap.toFineXP(miningXp).toInt()
            param[params.skill_productitem] = ore
            param[params.next_loc_stage] = drained
            param[params.mineable_wall] = isWall
            param[rockSuccessRates] = MiningEnums.rocks
        }
    }

    private fun configureMotherlodeWall(rock: LocType, drained: LocType, ore: ObjType, respawnTime: Int, miningXp : Double, miningLevel: Int, isWall : Boolean = false) {
        edit(rock) {
            contentGroup = content.rocks
            param[params.respawn_time] = respawnTime
            param[params.despawn_time] = rate(39, 45)
            param[params.levelrequire] = miningLevel // Set the required mining level
            param[params.skill_xp] = PlayerStatMap.toFineXP(miningXp).toInt()
            param[params.skill_productitem] = ore
            param[params.next_loc_stage] = drained
            param[params.mineable_wall] = isWall
            param[rockSuccessRates] = MiningEnums.rocks
        }
    }

}

internal object RockLocs : LocReferences() {
    val copper_rock_1 = find("copperrock1", 4812402897244152685)
    val copper_rock_2 = find("copperrock2", 4812402897244152903)
    val tin_rock_1 = find("tinrock1", 4296591596338609998)
    val tin_rock_2 = find("tinrock2", 4296591596338609999)


    val iron_rock_1 = find("ironrock1")
    val iron_rock_2 = find("ironrock2")
    val prif_mine_iron_rock1 = find("prif_mine_ironrock1")

    val silver_rock_2 = find("silverrock2")
    val silver_rock_1 = find("silverrock1")
    val prif_mine_silver_rock1 = find("prif_mine_silverrock1")


    val coalrock2 = find("coalrock2")
    val coalrock1 = find("coalrock1")
    val prif_mine_coalrock1 = find("prif_mine_coalrock1")

    val goldrock1 = find("goldrock1")
    val goldrock2 = find("goldrock2")
    val prif_mine_goldrock1 = find("prif_mine_goldrock1")

    val enakh_granite_rocks = find("enakh_granite_rocks")
    val mithrilrock2 = find("mithrilrock2")
    val mithrilrock1 = find("mithrilrock1")
    val prif_mine_mithrilrock1 = find("prif_mine_mithrilrock1")

    val adamantiterock2 = find("adamantiterock2")
    val adamantiterock1 = find("adamantiterock1")
    val prif_mine_adamantiterock1 = find("prif_mine_adamantiterock1")

    val runiterock2 = find("runiterock2")
    val runiterock1 = find("runiterock1")
    val prif_mine_runiterock1 = find("prif_mine_runiterock1")

    val amethystrock1 = find("amethystrock1")
    val amethystrock2 = find("amethystrock2")

    val clayrock2 = find("clayrock2")
    val clayrock1 = find("clayrock1")
    val blankrunestone = find("blankrunestone")
    val limestone_rock1 = find("limestone_rock1")
    val limestone_rock2 = find("limestone_rock2")
    val limestone_rock3 = find("limestone_rock3")
    val limestone_rock_noore = find("limestone_rock_noore")
    val camdozaalrock1 = find("camdozaalrock1")
    val camdozaalrock2 = find("camdozaalrock2")
    val camdozaalrock1_empty = find("camdozaalrock1_empty")
    val camdozaalrock2_empty = find("camdozaalrock2_empty")
    val area_sanguine_mine_minerocks_01 = find("area_sanguine_mine_minerocks_01")
    val area_sanguine_mine_minerocks_02 = find("area_sanguine_mine_minerocks_02")
    val area_sanguine_mine_minerocks_03 = find("area_sanguine_mine_minerocks_03")
    val area_sanguine_mine_minerocks_04 = find("area_sanguine_mine_minerocks_04")
    val fossil_ashpile = find("fossil_ashpile")
    val fossil_ashpile_empty = find("fossil_ashpile_empty")

    val prif_mine_softclayrock1 = find("prif_mine_softclayrock1")
    val dorgesh_cavewall_slope_edge2 = find("dorgesh_cavewall_slope_edge2")
    val dorgesh_cavewall_slope_edge4 = find("dorgesh_cavewall_slope_edge4")
    val dorgesh_cavewall_slope_mid2 = find("dorgesh_cavewall_slope_mid2")
    val ancient_essence_rock_active = find("ancient_essence_rock_active")
    val ancient_essence_rock_empty = find("ancient_essence_rock_empty")
    val crimson_lovakite_large_active = find("crimson_lovakite_large_active")
    val crimson_lovakite_large_inactive = find("crimson_lovakite_large_inactive")
    val crimson_lovakite1 = find("crimson_lovakite1")
    val crimson_lovakite2 = find("crimson_lovakite2")

    val lunar_mine_stalagmite_twin = find("lunar_mine_stalagmite_twin")
    val lunar_mine_stalagmite_small = find("lunar_mine_stalagmite_small")
    val daeyalt_stone_top_active = find("daeyalt_stone_top_active")
    val daeyalt_stone_top = find("daeyalt_stone_top")
    val poh_trap_1 = find("poh_trap_1")
    val varlamore_mining_rock = find("varlamore_mining_rock")
    val varlamore_mining_rock02 = find("varlamore_mining_rock02")
    val varlamore_mining_rock03 = find("varlamore_mining_rock03")
    val varlamore_mining_rock04 = find("varlamore_mining_rock04")
    val sulphur_rock_01 = find("sulphur_rock_01")
    val sulphur_rock_02 = find("sulphur_rock_02")
    val sulphur_rock_03 = find("sulphur_rock_03")

    val motherlode_ore_single = find("motherlode_ore_single")
    val motherlode_ore_left = find("motherlode_ore_left")
    val motherlode_ore_middle = find("motherlode_ore_middle")
    val motherlode_ore_right = find("motherlode_ore_right")
    val dwarf_keldagrim_cavewall_tunnel_cart = find("dwarf_keldagrim_cavewall_tunnel_cart")
    val dwarf_keldagrim_palace_statue = find("dwarf_keldagrim_palace_statue")
    val dwarf_keldagrim_door_interior_factory = find("dwarf_keldagrim_door_interior_factory")
    val dwarf_keldagrim_factory_door = find("dwarf_keldagrim_factory_door")
    val arceuus_runestone_base_mine = find("arceuus_runestone_base_mine")
    val arceuus_runestone_middle_mine = find("arceuus_runestone_middle_mine")
    val arceuus_runestone_top_mine = find("arceuus_runestone_top_mine")
    val arceuus_runestone_base_depleted = find("arceuus_runestone_base_depleted")
    val arceuus_runestone_middle_depleted = find("arceuus_runestone_middle_depleted")
    val arceuus_runestone_top_depleted = find("arceuus_runestone_top_depleted")
    val gemrock = find("gemrock")
    val gemrock1 = find("gemrock1")
    val village_gem_rock1 = find("village_gem_rock1")
    val village_gem_rock2 = find("village_gem_rock2")
    val village_gem_rock3 = find("village_gem_rock3")

}


internal object DrainedLocs : LocReferences() {
    val drained_3 = find("rocks3")
    val drained_2 = find("rocks2")
    val drained_1 = find("rocks1")
    val amethystrock_empty = find("amethystrock_empty")
    val prif_mine_rocks1_empty = find("prif_mine_rocks1_empty")

    val motherlode_depleted_single = find("motherlode_depleted_single")
    val motherlode_depleted_left = find("motherlode_depleted_left")
    val motherlode_depleted_middle = find("motherlode_depleted_middle")
    val motherlode_depleted_right = find("motherlode_depleted_right")
}

private fun rate(low: Int, high: Int): Int = (low shl 16) or high
