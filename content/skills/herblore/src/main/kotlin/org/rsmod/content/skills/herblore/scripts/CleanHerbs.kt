package org.rsmod.content.skills.herblore.scripts

import jakarta.inject.Inject
import org.rsmod.api.config.refs.content
import org.rsmod.api.config.refs.objs
import org.rsmod.api.config.refs.stats
import org.rsmod.api.script.onOpHeld1
import org.rsmod.game.type.obj.ObjType
import org.rsmod.plugin.scripts.PluginScript
import org.rsmod.plugin.scripts.ScriptContext

class CleanHerbs
@Inject
constructor() : PluginScript() {

    internal companion object {
        val herbConvertMap: MutableMap<Int, HerbData> = HashMap()
        fun defineMap() {
            herbConvertMap[objs.grimy_guam.id] = HerbData(objs.clean_guam, 1, 2.5)
            herbConvertMap[objs.grimy_marentill.id] = HerbData(objs.clean_marentill, 5, 3.8)
            herbConvertMap[objs.grimy_tarromin.id] = HerbData(objs.clean_tarromin, 11, 5.0)
            herbConvertMap[objs.grimy_harralander.id] = HerbData(objs.clean_harralander, 20, 6.3)
            herbConvertMap[objs.grimy_ranarr.id] = HerbData(objs.clean_ranarr, 25, 7.5)
            herbConvertMap[objs.grimy_irit.id] = HerbData(objs.clean_irit, 40, 8.8)
            herbConvertMap[objs.grimy_avantoe.id] = HerbData(objs.clean_avantoe, 48, 10.0)
            herbConvertMap[objs.grimy_kwuarm.id] = HerbData(objs.clean_kwuarm, 54, 11.3)
            herbConvertMap[objs.grimy_cadantine.id] = HerbData(objs.clean_cadantine, 65, 12.5)
            herbConvertMap[objs.grimy_dwarf_weed.id] = HerbData(objs.clean_dwarf_weed, 70, 13.8)
            herbConvertMap[objs.grimy_torstol.id] = HerbData(objs.clean_torstol, 75, 15.0)
        }

        init {
            defineMap()
        }
    }

    override fun ScriptContext.startup() {
        onOpHeld1(content.grimy_herbs) { it ->

            if (!herbConvertMap.contains(it.type.id))
                return@onOpHeld1
            val cleanedHerb = herbConvertMap[it.type.id]!!

            if (stat(stats.herblore) < cleanedHerb.levelRequirement) {
                objbox(it.type, "Herblore level of ${cleanedHerb.levelRequirement} is " +
                    "required to clean this herb.")
                return@onOpHeld1
            }

            statAdvance(stats.herblore,cleanedHerb.baseExp)
            invReplace(inv, it.type, 1, cleanedHerb.cleanedHerb)
        }
    }
}

class HerbData(
    final val cleanedHerb: ObjType,
    final val levelRequirement: Int,
    final val baseExp: Double
)
