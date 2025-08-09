package org.rsmod.content.skills.herblore.scripts

import jakarta.inject.Inject
import org.rsmod.api.config.refs.content
import org.rsmod.api.config.refs.objs
import org.rsmod.api.config.refs.seqs
import org.rsmod.api.config.refs.stats
import org.rsmod.api.config.refs.synths
import org.rsmod.api.script.onOpHeld1
import org.rsmod.api.script.onOpHeldU
import org.rsmod.game.type.obj.ObjType
import org.rsmod.plugin.scripts.PluginScript
import org.rsmod.plugin.scripts.ScriptContext

class CleanHerbs @Inject constructor() : PluginScript() {

    internal companion object {
        val herbConvertMap = mapOf(
            objs.grimy_guam.id to HerbData(objs.clean_guam, 1, 2.5),
            objs.grimy_marentill.id to HerbData(objs.clean_marentill, 5, 3.8),
            objs.grimy_tarromin.id to HerbData(objs.clean_tarromin, 11, 5.0),
            objs.grimy_harralander.id to HerbData(objs.clean_harralander, 20, 6.3),
            objs.grimy_ranarr.id to HerbData(objs.clean_ranarr, 25, 7.5),
            objs.grimy_toadflax.id to HerbData(objs.clean_toadflax, 30, 8.0),
            objs.grimy_irit.id to HerbData(objs.clean_irit, 40, 8.8),
            objs.grimy_avantoe.id to HerbData(objs.clean_avantoe, 48, 10.0),
            objs.grimy_kwuarm.id to HerbData(objs.clean_kwuarm, 54, 11.3),
            objs.grimy_snapdragon.id to HerbData(objs.clean_snapdragon, 59, 11.8),
            objs.grimy_cadantine.id to HerbData(objs.clean_cadantine, 65, 12.5),
            objs.grimy_lantadyme.id to HerbData(objs.clean_lantadyme, 67, 13.1),
            objs.grimy_dwarf_weed.id to HerbData(objs.clean_dwarf_weed, 70, 13.8),
            objs.grimy_torstol.id to HerbData(objs.clean_torstol, 75, 15.0)
        ).toMutableMap()
        val unfPotConvertMap = mapOf(
            objs.clean_guam.id to HerbData(objs.unfinished_guam, 1),
            objs.clean_marentill.id to HerbData(objs.unfinished_marentill, 5),
            objs.clean_tarromin.id to HerbData(objs.unfinished_tarromin, 12),
            objs.clean_harralander.id to HerbData(objs.unfinished_harralander, 22),
            objs.clean_ranarr.id to HerbData(objs.unfinished_ranarr, 30),
            objs.clean_toadflax.id to HerbData(objs.unfinished_toadflax, 34),
            objs.clean_irit.id to HerbData(objs.unfinished_irit, 45),
            objs.clean_avantoe.id to HerbData(objs.unfinished_avantoe, 50),
            objs.clean_kwuarm.id to HerbData(objs.unfinished_kwuarm, 55),
            objs.clean_snapdragon.id to HerbData(objs.unfinished_snapdragon, 63),
            objs.clean_cadantine.id to HerbData(objs.unfinished_cadantine, 66),
            objs.clean_lantadyme.id to HerbData(objs.unfinished_lantadyme, 69),
            objs.clean_dwarf_weed.id to HerbData(objs.unfinished_dwarf_weed, 72),
            objs.clean_torstol.id to HerbData(objs.unfinished_torstol, 78)
        ).toMutableMap()
    }

    override fun ScriptContext.startup() {
        onOpHeld1(content.grimy_herbs) { item ->

            for (herb in herbConvertMap) {
                invAdd(inv,herb.value.product,1)
                invAdd(inv,objs.vial_water,1)
            }

            herbConvertMap[item.type.id]?.let { cleanedHerb ->
                if (stat(stats.herblore) < cleanedHerb.levelRequirement) {
                    objbox(
                        item.type,
                        "Herblore level of ${cleanedHerb.levelRequirement} is required to clean this herb."
                    )
                    return@onOpHeld1
                }
                statAdvance(stats.herblore, cleanedHerb.baseExp)
                invReplace(inv, item.type, 1, cleanedHerb.product)
            }
        }

        onOpHeldU(content.clean_herbs, objs.vial_water) { item ->
            unfPotConvertMap[item.first.id]?.let { unfPot ->

                if (stat(stats.herblore) < unfPot.levelRequirement) {
                    objbox(
                        item.second,
                        "Herblore level of ${unfPot.levelRequirement} is required to create the " +
                            "unfinished potion."
                    )
                    return@onOpHeldU
                }
                invDel(inv, item.first, 1)
                invDel(inv, item.second, 1)
                invAdd(inv, unfPot.product, 1)
                anim(seqs.human_herbing_vial)
                soundSynth(synths.fill_vial)
            }
        }
    }
}

data class HerbData(
    val product: ObjType,
    val levelRequirement: Int,
    val baseExp: Double = 0.0
)
