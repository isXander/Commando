package dev.isxander.commando.commands

import dev.isxander.commando.ext.Cmd
import dev.isxander.commando.ext.Ctx
import dev.isxander.commando.ext.requiresPerm
import dev.isxander.commando.utils.min
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText

fun Cmd.registerWalkSpeed() =
    addCommand("walkspeed") {
        requiresPerm("commando.walkspeed", 2)

        argInt("speed", min(0)) { speed ->
            runs { executeWalkSpeed(source.player, speed()) }

            argument(EntityArgumentType.player(), "target") { player ->
                requiresPerm("commando.flyspeed.other", 2)

                runs { executeWalkSpeed(player().getPlayer(source), speed()) }
            }
        }
    }

private fun Ctx.executeWalkSpeed(player: ServerPlayerEntity, speed: Int) {
    player.abilities.walkSpeed = 0.1f * speed
    player.sendAbilitiesUpdate()
    player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)!!.baseValue = player.abilities.walkSpeed.toDouble()

    source.sendFeedback(LiteralText("Updated walking speed for ").append(player.displayName).append(LiteralText(" to ${speed}x")), true)
}
