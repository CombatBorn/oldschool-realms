package org.rsmod.content.skills.fishing.config

import org.rsmod.api.config.refs.objs
import org.rsmod.api.script.onOpLoc1
import org.rsmod.api.type.refs.loc.LocReferences
import org.rsmod.plugin.scripts.PluginScript
import org.rsmod.plugin.scripts.ScriptContext

class FishingEquipmentLocs : PluginScript() {
    typealias locs = FishingNet

    object FishingNet : LocReferences() {
        val fishing_net = find("net")
    }

    override fun ScriptContext.startup() {
        onOpLoc1(FishingNet.fishing_net) {
            when {
                inv.contains(objs.small_fishing_net) -> mes("You already have a fishing net.")
                inv.isFull() -> mes("Your inventory is too full to pick up a net.")
                else -> {
                    invAdd(inv, objs.small_fishing_net, 1)
                    mes("You take a fishing net.")
                }
            }
        }
    }
}
