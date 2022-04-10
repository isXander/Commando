package dev.isxander.commando.commands

import dev.isxander.commando.utils.Cmd
import dev.isxander.commando.utils.Ctx
import eu.pb4.sgui.api.gui.SimpleGui
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.stat.Stats
import net.minecraft.text.TranslatableText

fun Cmd.registerCraft() =
    addCommand("craft") {
        requiresOp(2)

        runs { executeCraft() }
    }

private fun Ctx.executeCraft() {
    SimpleGui(ScreenHandlerType.CRAFTING, source.player, true).apply {
        title = TranslatableText("block.minecraft.crafting_table")
    }.open()
    source.player.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE)
}
