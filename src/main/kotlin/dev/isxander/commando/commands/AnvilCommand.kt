package dev.isxander.commando.commands

import dev.isxander.commando.utils.Cmd
import dev.isxander.commando.utils.Ctx
import eu.pb4.sgui.api.gui.SimpleGui
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.stat.Stats
import net.minecraft.text.TranslatableText

fun Cmd.registerAnvil() =
    addCommand("anvil") {
        requiresOp(2)

        runs { executeAnvil() }
    }

private fun Ctx.executeAnvil() {
    SimpleGui(ScreenHandlerType.ANVIL, source.player, true).apply {
        title = TranslatableText("block.minecraft.anvil")
    }.open()
    source.player.incrementStat(Stats.INTERACT_WITH_ANVIL)
}
