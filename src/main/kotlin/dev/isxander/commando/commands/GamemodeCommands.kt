package dev.isxander.commando.commands

import dev.isxander.commando.ext.Builder
import dev.isxander.commando.ext.Cmd
import dev.isxander.commando.ext.Ctx
import dev.isxander.commando.ext.requiresPerm
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Util
import net.minecraft.world.GameMode
import net.minecraft.world.GameRules

fun Cmd.registerGamemodes() {
    addCommand("gm") {
        gameModeNode("c", GameMode.CREATIVE)
        gameModeNode("s", GameMode.SURVIVAL)
        gameModeNode("a", GameMode.ADVENTURE)
        gameModeNode("sp", GameMode.SPECTATOR)
    }

    addCommand("gmc") { gameModeNode(null, GameMode.CREATIVE) }
    addCommand("gms") { gameModeNode(null, GameMode.SURVIVAL) }
    addCommand("gma") { gameModeNode(null, GameMode.ADVENTURE) }
    addCommand("gmsp") { gameModeNode(null, GameMode.SPECTATOR) }
}

private fun Builder.gameModeNode(literal: String? = null, gameMode: GameMode) {
    if (literal == null) {
        innerGameModeNode(gameMode)
    } else literal { innerGameModeNode(gameMode) }
}

private fun Builder.innerGameModeNode(gameMode: GameMode) {
    requiresPerm("commando.gamemode.${gameMode.getName()}", 2)

    runs { changeGamemode(source.player, gameMode) }

    argument(EntityArgumentType.player(), "target") { target ->
        requiresPerm("commando.gamemode.${gameMode.getName()}.others", 2)

        runs { changeGamemode(target().getPlayer(source), gameMode) }
    }
}

private fun Ctx.changeGamemode(target: ServerPlayerEntity, gameMode: GameMode) {
    target.changeGameMode(gameMode)

    val text: Text = TranslatableText("gameMode." + gameMode.getName())
    if (source.entity == target) {
        source.sendFeedback(TranslatableText("commands.gamemode.success.self", text), true)
    } else {
        if (source.world.gameRules.getBoolean(GameRules.SEND_COMMAND_FEEDBACK)) {
            target.sendSystemMessage(TranslatableText("gameMode.changed", text), Util.NIL_UUID)
        }
        source.sendFeedback(TranslatableText("commands.gamemode.success.other", target.displayName, text), true)
    }
}
