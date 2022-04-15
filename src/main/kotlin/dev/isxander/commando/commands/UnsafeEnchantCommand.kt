package dev.isxander.commando.commands

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import dev.isxander.commando.ext.Cmd
import dev.isxander.commando.ext.Ctx
import dev.isxander.commando.ext.requiresPerm
import io.ejekta.kambrik.command.addCommand
import net.minecraft.command.argument.EnchantmentArgumentType
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.text.TranslatableText

private val FAILED_ENTITY_EXCEPTION = DynamicCommandExceptionType { entityName ->
    TranslatableText(
        "commands.enchant.failed.entity",
        entityName
    )
}

private val FAILED_ITEMLESS_EXCEPTION = DynamicCommandExceptionType { entityName ->
    TranslatableText(
        "commands.enchant.failed.itemless",
        entityName
    )
}

private val FAILED_EXCEPTION = SimpleCommandExceptionType(TranslatableText("commands.enchant.failed"))

fun Cmd.registerUnsafeEnchant() =
    addCommand("unsafeenchant") {
        requiresPerm("commando.unsafeenchant", 2)

        argument(EntityArgumentType.entities(), "targets") { targets ->
            argument(EnchantmentArgumentType.enchantment(), "enchantment") { enchantment ->
                argInt("level", 1..127) { level ->
                    runs { executeUnsafeEnchant(targets().getEntities(source), enchantment(), level()) }
                }
            }
        }
    }

private fun Ctx.executeUnsafeEnchant(targets: List<Entity>, enchantment: Enchantment, level: Int) {
    var count = 0

    for (entity in targets) {
        if (entity is LivingEntity) {
            val stack = entity.mainHandStack

            if (stack.isEmpty)
                throw FAILED_ITEMLESS_EXCEPTION.create(entity.name.string)

            stack.addEnchantment(enchantment, level)
            count++
        } else {
            throw FAILED_ENTITY_EXCEPTION.create(entity.name.string)
        }
    }

    if (count == 0)
        throw FAILED_EXCEPTION.create()

    if (targets.size == 1)
        source.sendFeedback(
            TranslatableText(
                "commands.enchant.success.single",
                enchantment.getName(level),
                targets.single().displayName
            ), true
        )
    else
        source.sendFeedback(
            TranslatableText(
                "commands.enchant.success.multiple",
                enchantment.getName(level),
                targets.size
            ), true
        )
}
