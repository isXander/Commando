package dev.isxander.commando.commands

import dev.isxander.commando.ext.Cmd
import dev.isxander.commando.ext.Ctx
import dev.isxander.commando.ext.requiresPerm
import dev.isxander.commando.utils.openHandledScreen
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.stat.Stats
import net.minecraft.text.TranslatableText

fun Cmd.registerLoom() =
    addCommand("loom") {
        requiresPerm("commando.loom", 2)

        runs { executeLoom() }
    }

private fun Ctx.executeLoom() {
    source.player.openHandledScreen(TranslatableText("block.minecraft.loom"), ScreenHandlerType.LOOM)
    source.player.incrementStat(Stats.INTERACT_WITH_LOOM)
}
