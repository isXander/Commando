package dev.isxander.commando.commands

import dev.isxander.commando.utils.Cmd
import dev.isxander.commando.utils.Ctx
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.command.argument.StatusEffectArgumentType
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.text.LiteralText

fun Cmd.registerRemoveEffect() =
    addCommand("removeeffect") {
        requiresOp(2)

        argument(EntityArgumentType.entities(), "targets") { targets ->
            argument(StatusEffectArgumentType.statusEffect(), "effect") { effect ->
                runs { executeRemoveEffect(targets().getEntities(source), effect()) }
            }
        }
    }

private fun Ctx.executeRemoveEffect(targets: List<Entity>, effect: StatusEffect) {
    var successCount = 0
    for (target in targets) {
        if (target is LivingEntity && target.hasStatusEffect(effect)) {
            target.removeStatusEffect(effect)
            successCount++
        }
    }

    source.sendFeedback(LiteralText("Removed ").append(effect.name).append(" from $successCount ${if (successCount == 1) "entity" else "entities"}"), true)
}
