package dev.isxander.commando.commands

import dev.isxander.commando.utils.Cmd
import dev.isxander.commando.utils.Ctx
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText

fun Cmd.registerFly() =
    addCommand("fly") {
        requiresOp(2)

        runs { executeFly(source.player) }

        argument(EntityArgumentType.player(), "player") { player ->
            runs { executeFly(player().getPlayer(source)) }
        }
    }

private fun Ctx.executeFly(player: ServerPlayerEntity) {
    val newState = !player.abilities.allowFlying
    player.abilities.allowFlying = newState
    if (!newState)
        player.abilities.flying = false

    player.sendAbilitiesUpdate()

    val stateText = if (newState) "Enabled" else "Disabled"
    source.sendFeedback(LiteralText("$stateText flying for ").append(player.displayName), true)
}
