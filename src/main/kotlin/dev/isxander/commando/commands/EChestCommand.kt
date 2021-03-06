package dev.isxander.commando.commands

import dev.isxander.commando.ext.Cmd
import dev.isxander.commando.ext.Ctx
import dev.isxander.commando.ext.requiresPerm
import dev.isxander.commando.utils.SavingPlayerDataGui
import eu.pb4.sgui.api.gui.SimpleGui
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.stat.Stats

fun Cmd.registerEChest() =
    addCommand("echest") {
        requiresPerm("commando.echest", 2)

        runs { executeEChest(source.player) }

        argument(EntityArgumentType.player(), "target") { target ->
            requiresPerm("commando.echest.others", 2)

            runs { executeEChest(target().getPlayer(source)) }
        }
    }

private fun Ctx.executeEChest(target: ServerPlayerEntity) {
    SavingPlayerDataGui(ScreenHandlerType.GENERIC_9X3, source.player, target).apply {
        title = target.name.copy().append("'s Ender Chest")

        for (i in 0 until target.enderChestInventory.size()) {
            setSlotRedirect(i, Slot(target.enderChestInventory, i, 0, 0))
        }
    }.open()
    source.player.incrementStat(Stats.OPEN_ENDERCHEST)
}
