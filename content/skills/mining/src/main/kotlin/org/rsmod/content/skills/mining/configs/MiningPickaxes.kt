package org.rsmod.content.skills.mining.configs

import org.rsmod.api.config.refs.content
import org.rsmod.api.config.refs.objs
import org.rsmod.api.config.refs.params
import org.rsmod.api.config.refs.seqs
import org.rsmod.api.config.refs.stats
import org.rsmod.api.type.editors.obj.ObjEditor


internal object MiningPickaxes : ObjEditor() {
    // Attack level requirements for pickaxes
    private const val BRONZE_PICKAXE_ATTACK_REQ = 1
    private const val IRON_PICKAXE_ATTACK_REQ = 1
    private const val STEEL_PICKAXE_ATTACK_REQ = 5
    private const val BLACK_PICKAXE_ATTACK_REQ = 10
    private const val MITHRIL_PICKAXE_ATTACK_REQ = 20
    private const val ADAMANT_PICKAXE_ATTACK_REQ = 30
    private const val RUNE_PICKAXE_ATTACK_REQ = 40
    private const val DRAGON_PICKAXE_ATTACK_REQ = 60
    private const val THIRD_AGE_PICKAXE_ATTACK_REQ = 65
    private const val INFERNAL_PICKAXE_ATTACK_REQ = 60


    init {
        edit(objs.bronze_pickaxe) {
            contentGroup = content.mining_pickaxe
            param[params.skill_anim] = seqs.human_mining_bronze_pickaxe_noreachforward
            param[params.wall_anim] = seqs.human_mining_bronze_pickaxe_wall
            param[params.statreq1_skill] = stats.attack
            param[params.statreq1_level] = BRONZE_PICKAXE_ATTACK_REQ
        }

        edit(objs.iron_pickaxe) {
            contentGroup = content.mining_pickaxe
            param[params.skill_anim] = seqs.human_mining_iron_pickaxe_noreachforward
            param[params.wall_anim] = seqs.human_mining_iron_pickaxe_wall
            param[params.statreq1_skill] = stats.attack
            param[params.statreq1_level] = IRON_PICKAXE_ATTACK_REQ
        }

        edit(objs.steel_pickaxe) {
            contentGroup = content.mining_pickaxe
            param[params.skill_anim] = seqs.human_mining_steel_pickaxe_noreachforward
            param[params.wall_anim] = seqs.human_mining_steel_pickaxe_wall
            param[params.statreq1_skill] = stats.attack
            param[params.statreq1_level] = STEEL_PICKAXE_ATTACK_REQ
        }

        edit(objs.black_pickaxe) {
            contentGroup = content.mining_pickaxe
            param[params.skill_anim] = seqs.human_mining_black_pickaxe_noreachforward
            param[params.wall_anim] = seqs.human_mining_black_pickaxe_wall
            param[params.statreq1_skill] = stats.attack
            param[params.statreq1_level] = BLACK_PICKAXE_ATTACK_REQ
        }

        edit(objs.mithril_pickaxe) {
            contentGroup = content.mining_pickaxe
            param[params.skill_anim] = seqs.human_mining_mithril_pickaxe_noreachforward
            param[params.wall_anim] = seqs.human_mining_mithril_pickaxe_wall
            param[params.statreq1_skill] = stats.attack
            param[params.statreq1_level] = MITHRIL_PICKAXE_ATTACK_REQ
        }

        edit(objs.adamant_pickaxe) {
            contentGroup = content.mining_pickaxe
            param[params.skill_anim] = seqs.human_mining_adamant_pickaxe_noreachforward
            param[params.wall_anim] = seqs.human_mining_adamant_pickaxe_wall
            param[params.statreq1_skill] = stats.attack
            param[params.statreq1_level] = ADAMANT_PICKAXE_ATTACK_REQ
        }

        edit(objs.rune_pickaxe) {
            contentGroup = content.mining_pickaxe
            param[params.skill_anim] = seqs.human_mining_rune_pickaxe_noreachforward
            param[params.wall_anim] = seqs.human_mining_rune_pickaxe_wall
            param[params.statreq1_skill] = stats.attack
            param[params.statreq1_level] = RUNE_PICKAXE_ATTACK_REQ
        }

        /**
         *
         * Dragon pickaxe variations
         *
         */
        edit(objs.dragon_pickaxe) {
            contentGroup = content.mining_pickaxe
            param[params.skill_anim] = seqs.human_mining_dragon_pickaxe_noreachforward
            param[params.wall_anim] = seqs.human_mining_dragon_pickaxe_wall
            param[params.statreq1_skill] = stats.attack
            param[params.statreq1_level] = DRAGON_PICKAXE_ATTACK_REQ
        }

        edit(objs.dragon_pickaxe_upgraded) {
            contentGroup = content.mining_pickaxe
            param[params.skill_anim] = seqs.human_mining_dragon_pickaxe_pretty_noreachforward
            param[params.wall_anim] = seqs.human_mining_dragon_pickaxe_pretty_wall
            param[params.statreq1_skill] = stats.attack
            param[params.statreq1_level] = DRAGON_PICKAXE_ATTACK_REQ
        }

        edit(objs.dragon_pickaxe_or_zalcano) {
            contentGroup = content.mining_pickaxe
            param[params.skill_anim] = seqs.human_mining_zalcano_pickaxe_noreachforward
            param[params.wall_anim] = seqs.human_mining_zalcano_pickaxe_wall
            param[params.statreq1_skill] = stats.attack
            param[params.statreq1_level] = DRAGON_PICKAXE_ATTACK_REQ
        }

        edit(objs.dragon_pickaxe_or_trailblazer) {
            contentGroup = content.mining_pickaxe
            param[params.skill_anim] = seqs.human_mining_trailblazer_pickaxe_no_infernal_noreachforward
            param[params.wall_anim] = seqs.human_mining_trailblazer_pickaxe_no_infernal_wall
            param[params.statreq1_skill] = stats.attack
            param[params.statreq1_level] = DRAGON_PICKAXE_ATTACK_REQ
        }

        edit(objs.dragon_pickaxe_or_trailblazer_reloaded) {
            contentGroup = content.mining_pickaxe
            param[params.skill_anim] = seqs.human_mining_trailblazer_reloaded_pickaxe_no_infernal_noreachforward
            param[params.wall_anim] = seqs.human_mining_trailblazer_reloaded_pickaxe_no_infernal_wall
            param[params.statreq1_skill] = stats.attack
            param[params.statreq1_level] = DRAGON_PICKAXE_ATTACK_REQ
            //Temp fix, until engine reconfigure
            iop1 = null
            iop2 = "Wield"
        }

        /**
         *
         * End of dragon pickaxe variations
         *
         */

        edit(objs.third_age_pickaxe) {
            contentGroup = content.mining_pickaxe
            param[params.skill_anim] = seqs.human_mining_3a_pickaxe_noreachforward
            param[params.wall_anim] = seqs.human_mining_3a_pickaxe_wall
            param[params.statreq1_skill] = stats.attack
            param[params.statreq1_level] = THIRD_AGE_PICKAXE_ATTACK_REQ
        }

        edit(objs.infernal_pickaxe) {
            contentGroup = content.mining_pickaxe
            param[params.skill_anim] = seqs.human_mining_infernal_pickaxe_noreachforward
            param[params.wall_anim] = seqs.human_mining_infernal_pickaxe_wall
            param[params.statreq1_skill] = stats.attack
            param[params.statreq1_level] = INFERNAL_PICKAXE_ATTACK_REQ
        }
    }
}
