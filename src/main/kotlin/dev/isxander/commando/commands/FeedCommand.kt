package dev.isxander.commando.commands

import dev.isxander.commando.ext.Cmd
import dev.isxander.commando.ext.Ctx
import dev.isxander.commando.ext.requiresPerm
import io.ejekta.kambrik.command.addCommand
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText

fun Cmd.registerFeed() =
    addCommand("feed") {
        requiresPerm("commando.feed", 2)

        runs { executeFeed(listOf(source.player)) }

        argument(EntityArgumentType.players(), "targets") { targets ->
            requiresPerm("commando.feed.others", 2)

            runs { executeFeed(targets().getPlayers(source)) }
        }
    }

fun Ctx.executeFeed(targets: List<ServerPlayerEntity>) {
    val successful = mutableListOf<ServerPlayerEntity>()
    for (target in targets) {
        target.hungerManager.foodLevel = 20
        target.hungerManager.saturationLevel = 20f
        successful.add(target)
    }

    if (successful.count() == 1) {
        source.sendFeedback(LiteralText("Successfully fed ").append(successful.single().name), true)
    } else {
        val count = successful.count()
        source.sendFeedback(LiteralText("Successfully fed $count ${if (count == 1) "player" else "players"}"), true)
    }
}
