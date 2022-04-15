package dev.isxander.commando.commands

import dev.isxander.commando.ext.Cmd
import dev.isxander.commando.ext.Ctx
import dev.isxander.commando.ext.requiresPerm
import dev.isxander.commando.utils.SavingPlayerDataGui
import dev.isxander.commando.utils.logger
import eu.pb4.sgui.api.gui.SimpleGui
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtIo
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Util
import net.minecraft.util.WorldSavePath
import java.io.File

fun Cmd.registerInvSee() =
    addCommand("invsee") {
        requiresPerm("commando.invsee", 2)

        argument(EntityArgumentType.player(), "target") { target ->
            runs { executeInvSee(source.player, target().getPlayer(source)) }
        }
    }

private fun Ctx.executeInvSee(player: ServerPlayerEntity, target: ServerPlayerEntity) {
    val gui = SavingPlayerDataGui(ScreenHandlerType.GENERIC_9X5, player, target)
    gui.title = target.name

    for (i in 0 until player.inventory.size()) {
        gui.setSlotRedirect(i, Slot(target.inventory, i, 0, 0))
    }

    gui.open()
}

