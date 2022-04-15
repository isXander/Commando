package dev.isxander.commando.commands

import dev.isxander.commando.ext.Cmd
import dev.isxander.commando.ext.Ctx
import dev.isxander.commando.ext.requiresPerm
import dev.isxander.commando.utils.openHandledScreen
import eu.pb4.sgui.api.gui.SimpleGui
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.stat.Stats
import net.minecraft.text.TranslatableText

fun Cmd.registerAnvil() =
    addCommand("anvil") {
        requiresPerm("commando.anvil", 2)

        runs { executeAnvil() }
    }

private fun Ctx.executeAnvil() {
    source.player.openHandledScreen(TranslatableText("block.minecraft.anvil"), ScreenHandlerType.ANVIL)
    source.player.incrementStat(Stats.INTERACT_WITH_ANVIL)
}
