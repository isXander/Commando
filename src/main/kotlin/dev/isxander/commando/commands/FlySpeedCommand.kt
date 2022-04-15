package dev.isxander.commando.commands

import dev.isxander.commando.ext.Cmd
import dev.isxander.commando.ext.Ctx
import dev.isxander.commando.ext.requiresPerm
import dev.isxander.commando.utils.min
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText

fun Cmd.registerFlySpeed() =
    addCommand("flyspeed") {
        requiresPerm("commando.flyspeed", 2)

        argInt("speed", min(0)) { speed ->
            runs { executeFlySpeed(source.player, speed()) }

            argument(EntityArgumentType.player(), "target") { player ->
                requiresPerm("commando.flyspeed.other", 2)

                runs { executeFlySpeed(player().getPlayer(source), speed()) }
            }
        }
    }

private fun Ctx.executeFlySpeed(player: ServerPlayerEntity, speed: Int) {
    player.abilities.flySpeed = 0.05f * speed
    player.sendAbilitiesUpdate()

    source.sendFeedback(LiteralText("Updated flying speed for ").append(player.displayName).append(LiteralText(" to ${speed}x")), true)
}
