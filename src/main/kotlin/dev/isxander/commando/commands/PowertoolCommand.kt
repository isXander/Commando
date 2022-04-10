package dev.isxander.commando.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import dev.isxander.commando.ext.clearPowertool
import dev.isxander.commando.ext.getPowertoolStack
import dev.isxander.commando.ext.registerPowertool
import dev.isxander.commando.utils.Cmd
import dev.isxander.commando.utils.Ctx
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.text.LiteralText

private val NOT_HOLDING_ITEM = SimpleCommandExceptionType(
    LiteralText("You're not holding any item.")
)

fun Cmd.registerPowertool() =
    addCommand("powertool") {
        requiresOp(2)

        runs {
            if (source.player.getPowertoolStack().isEmpty)
                throw NOT_HOLDING_ITEM.create()

            source.player.clearPowertool(source.player.getPowertoolStack().item)
        }

        argument(StringArgumentType.greedyString(), "command") { command ->
            runs {
                if (source.player.getPowertoolStack().isEmpty)
                    throw NOT_HOLDING_ITEM.create()

                source.player.registerPowertool(source.player.getPowertoolStack().item, command())
            }
        }
    }
