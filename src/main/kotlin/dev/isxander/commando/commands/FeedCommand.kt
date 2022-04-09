package dev.isxander.commando.commands

import dev.isxander.commando.utils.Cmd
import dev.isxander.commando.utils.Ctx
import io.ejekta.kambrik.command.addCommand
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.LiteralText

fun Cmd.registerFeed() =
    addCommand("feed") {
        runs { executeFeed(listOf(source.player)) }

        argument(EntityArgumentType.players(), "targets") { targets ->
            runs { executeFeed(targets().getPlayers(source)) }
        }
    }

fun Ctx.executeFeed(targets: List<PlayerEntity>) {
    var count = 0
    for (target in targets) {
        target.hungerManager.foodLevel = 20
        target.hungerManager.saturationLevel = 20f
        count++
    }

    source.sendFeedback(LiteralText("Successfully fed $count ${if (count == 1) "player" else "players"}"), true)
}
