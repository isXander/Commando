package dev.isxander.commando.commands

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import dev.isxander.commando.ext.Cmd
import dev.isxander.commando.ext.Ctx
import dev.isxander.commando.ext.requiresPerm
import dev.isxander.commando.utils.min
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.command.argument.EntitySummonArgumentType
import net.minecraft.command.argument.NbtCompoundArgumentType
import net.minecraft.command.argument.Vec3ArgumentType
import net.minecraft.command.suggestion.SuggestionProviders
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.mob.MobEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

private val ERROR_FAILED = SimpleCommandExceptionType(
    TranslatableText("commands.summon.failed")
)

private val INVALID_POSITION = SimpleCommandExceptionType(
    TranslatableText("commands.summon.invalidPosition")
)

private val FAILED_UUID_EXCEPTION = SimpleCommandExceptionType(
    TranslatableText("commands.summon.failed.uuid")
)

fun Cmd.registerSpawnMob() =
    addCommand("spawnmob") {
        requiresPerm("commando.spawnmob", 2)

        argument(EntitySummonArgumentType.entitySummon(), "type", SuggestionProviders.SUMMONABLE_ENTITIES) { entity ->
            argInt("amount", min(1)) { amount ->
                runs { executeSpawnMob(entity(), amount()) }

                argument(Vec3ArgumentType.vec3(), "pos") { pos ->
                    runs { executeSpawnMob(entity(), amount(), pos().toAbsolutePos(source)) }

                    argument(NbtCompoundArgumentType.nbtCompound(), "nbt") { nbt ->
                        runs { executeSpawnMob(entity(), amount(), pos().toAbsolutePos(source), nbt(), false) }
                    }
                }
            }
        }
    }

private fun Ctx.executeSpawnMob(type: Identifier, amount: Int, location: Vec3d = source.position, nbt: NbtCompound = NbtCompound(), init: Boolean = true) {
    val blockPos = BlockPos(location)

    if (!World.isValid(blockPos))
        throw INVALID_POSITION.create()

    val nbtData = nbt.copy()
    nbtData.putString("id", type.toString())
    val world = source.world

    repeat(amount) {
        val entity = EntityType.loadEntityWithPassengers(nbtData, world) {
            it.refreshPositionAndAngles(location.x, location.y, location.z, it.yaw, it.pitch)
            it
        } ?: throw ERROR_FAILED.create()

        if (init && entity is MobEntity) {
            entity.initialize(world, world.getLocalDifficulty(blockPos), SpawnReason.COMMAND, null, null)
        }

        if (!world.spawnNewEntityAndPassengers(entity))
            throw FAILED_UUID_EXCEPTION.create()
    }

    source.sendFeedback(LiteralText("Summoned $amount ").append(Registry.ENTITY_TYPE.get(type).name), true)
}
