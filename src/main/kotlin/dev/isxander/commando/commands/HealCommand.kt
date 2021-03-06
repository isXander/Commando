package dev.isxander.commando.commands

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import dev.isxander.commando.ext.Cmd
import dev.isxander.commando.ext.Ctx
import dev.isxander.commando.ext.requiresPerm
import io.ejekta.kambrik.command.addCommand
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText

private val FAILED_ENTITY_EXCEPTION = DynamicCommandExceptionType { entityName ->
    TranslatableText(
        "commands.enchant.failed.entity",
        entityName
    )
}

fun Cmd.registerHeal() =
    addCommand("heal") {
        requiresPerm("commando.heal", 2)

        runs { executeHeal(listOf(source.entityOrThrow)) }

        argument(EntityArgumentType.entities(), "targets") { targets ->
            requiresPerm("commando.heal.others", 2)

            runs { executeHeal(targets().getEntities(source)) }
        }
    }

fun Ctx.executeHeal(targets: List<Entity>) {
    var count = 0
    for (target in targets) {
        if (target is LivingEntity) {
            target.heal(Float.MAX_VALUE)
            count++
        } else {
            throw FAILED_ENTITY_EXCEPTION.create(target.name.string)
        }
    }

    source.sendFeedback(LiteralText("Successfully healed $count ${if (count == 1) "entity" else "entities"}"), true)
}
