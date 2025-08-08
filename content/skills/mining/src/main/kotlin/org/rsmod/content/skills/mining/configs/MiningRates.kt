package org.rsmod.content.skills.mining.configs

import org.rsmod.api.config.refs.objs
import org.rsmod.api.config.refs.params
import org.rsmod.api.type.builders.enums.EnumBuilder
import org.rsmod.api.type.builders.param.ParamBuilder
import org.rsmod.api.type.editors.obj.ObjEditor
import org.rsmod.api.type.refs.enums.EnumReferences
import org.rsmod.api.type.refs.param.ParamReferences
import org.rsmod.game.type.enums.EnumType
import org.rsmod.game.type.loc.LocType
import org.rsmod.game.type.obj.ObjType
import org.rsmod.game.type.seq.SeqType

object MiningParams : ParamReferences() {

    //reference to params that reference enums
    val rockSuccessRates = find<EnumType<LocType, Int>>("rocks_success_rates")

}

//Build enums
internal object MiningParamBuilder : ParamBuilder() {
    init {
        build<EnumType<LocType, Int>>("rocks_success_rates")
        build<Int>("mining_pickaxe_tick_rates")
        build<SeqType>("wall_anim")
        build<Boolean>("mineable_wall") { default = false }
    }
}

//reference to enums
internal object MiningEnums : EnumReferences() {
    val rocks = find<LocType, Int>("rocks")
}

internal object Pickaxes : ObjEditor() {

    //Ticks between rolls against getting an ore
    private const val BRONZE_PICKAXE_TICK_ROLL_RATE = 8;
    private const val IRON_PICKAXE_RATE_TICK_ROLL_RATE = 7;
    private const val STEEL_PICKAXE_RATE_TICK_ROLL_RATE = 6;
    private const val BLACK_PICKAXE_RATE_TICK_ROLL_RATE = 5;
    private const val MITHRIL_PICKAXE_RATE_TICK_ROLL_RATE = 5;
    private const val ADAMANT_PICKAXE_RATE_TICK_ROLL_RATE = 4;
    private const val RUNE_PICKAXE_RATE_TICK_ROLL_RATE = 3;
    private const val DRAGON_PICKAXE_RATE_TICK_ROLL_RATE = 3;


    init {
        edit(objs.bronze_pickaxe) {
            configurePickaxe(pickaxe = objs.bronze_pickaxe, tickRate = BRONZE_PICKAXE_TICK_ROLL_RATE)
            configurePickaxe(pickaxe = objs.iron_pickaxe, tickRate = IRON_PICKAXE_RATE_TICK_ROLL_RATE)
            configurePickaxe(pickaxe = objs.steel_pickaxe, tickRate = STEEL_PICKAXE_RATE_TICK_ROLL_RATE)
            configurePickaxe(pickaxe = objs.black_pickaxe, tickRate = BLACK_PICKAXE_RATE_TICK_ROLL_RATE)
            configurePickaxe(pickaxe = objs.mithril_pickaxe, tickRate = MITHRIL_PICKAXE_RATE_TICK_ROLL_RATE)
            configurePickaxe(pickaxe = objs.adamant_pickaxe, tickRate = ADAMANT_PICKAXE_RATE_TICK_ROLL_RATE)
            configurePickaxe(pickaxe = objs.rune_pickaxe, tickRate = RUNE_PICKAXE_RATE_TICK_ROLL_RATE)
            configurePickaxe(pickaxe = objs.dragon_pickaxe, tickRate = DRAGON_PICKAXE_RATE_TICK_ROLL_RATE)
            configurePickaxe(pickaxe = objs.dragon_pickaxe_upgraded, tickRate = DRAGON_PICKAXE_RATE_TICK_ROLL_RATE)
            configurePickaxe(pickaxe = objs.dragon_pickaxe_or_trailblazer, tickRate = DRAGON_PICKAXE_RATE_TICK_ROLL_RATE)
            configurePickaxe(pickaxe = objs.dragon_pickaxe_or_trailblazer_reloaded, tickRate = DRAGON_PICKAXE_RATE_TICK_ROLL_RATE)
            configurePickaxe(pickaxe = objs.dragon_pickaxe_or_zalcano, tickRate = DRAGON_PICKAXE_RATE_TICK_ROLL_RATE)
            configurePickaxe(pickaxe = objs.infernal_pickaxe, tickRate = DRAGON_PICKAXE_RATE_TICK_ROLL_RATE)
            configurePickaxe(pickaxe = objs.third_age_pickaxe, tickRate = DRAGON_PICKAXE_RATE_TICK_ROLL_RATE)
        }
    }


    private fun configurePickaxe(pickaxe: ObjType, tickRate : Int) {
        edit(pickaxe) {
            param[params.pickaxeTickRates] = tickRate
        }
    }
}

internal object MiningEnumBuilder : EnumBuilder() {
    init {

        build<LocType, Int>("rocks") {
            this[rocks.copper_rock_1] = rate(128, 400)
            this[rocks.copper_rock_2] = rate(128, 400)

            this[rocks.tin_rock_1] = rate(128, 400)
            this[rocks.tin_rock_2] = rate(128, 400)

            this[rocks.iron_rock_1] = rate(96, 350)
            this[rocks.iron_rock_2] = rate(96, 350)
            this[rocks.prif_mine_iron_rock1] = rate(96, 350)

            this[rocks.silver_rock_1] = rate(25, 200)
            this[rocks.silver_rock_2] = rate(25, 200)
            this[rocks.prif_mine_silver_rock1] = rate(25, 200)

            this[rocks.coalrock1] = rate(16, 100)
            this[rocks.coalrock2] = rate(16, 100)
            this[rocks.prif_mine_coalrock1] = rate(16, 100)

            this[rocks.goldrock1] = rate(7, 75)
            this[rocks.goldrock2] = rate(7, 75)
            this[rocks.prif_mine_goldrock1] = rate(7, 75)

            this[rocks.mithrilrock1] = rate(4, 50)
            this[rocks.mithrilrock2] = rate(4, 50)
            this[rocks.prif_mine_mithrilrock1] = rate(4, 50)

            this[rocks.adamantiterock1] = rate(2, 25)
            this[rocks.adamantiterock2] = rate(2, 25)
            this[rocks.prif_mine_adamantiterock1] = rate(2, 25)

            this[rocks.runiterock1] = rate(1, 18)
            this[rocks.runiterock2] = rate(1, 18)
            this[rocks.prif_mine_runiterock1] = rate(1, 18)

            this[rocks.prif_mine_softclayrock1] = rate(128, 400)

            this[rocks.amethystrock1] = rate(18, 10)
            this[rocks.amethystrock2] = rate(18, 10)

            this[rocks.motherlode_ore_left] = rate(60, 105)
            this[rocks.motherlode_ore_right] = rate(60, 105)
            this[rocks.motherlode_ore_middle] = rate(60, 105)
            this[rocks.motherlode_ore_single] = rate(60, 105)
        }
    }



    private fun rate(low: Int, high: Int): Int = (low shl 16) or high
}
