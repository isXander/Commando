package dev.isxander.commando.commands

import dev.isxander.commando.utils.Cmd
import dev.isxander.commando.utils.Ctx
import dev.isxander.commando.utils.coroutineTask
import dev.isxander.commando.utils.min
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.entity.EntityType
import net.minecraft.entity.passive.CatEntity
import net.minecraft.world.explosion.Explosion
import kotlin.time.Duration.Companion.seconds

fun Cmd.registerKittyCannon() =
    addCommand("kittycannon") {
        requiresOp(2)

        runs { executeKittyCannon(false, 0f) }

        argFloat("power", min(0f)) { power ->
            runs { executeKittyCannon(false, power()) }

            argBool("destroy") { destroy ->
                runs { executeKittyCannon(destroy(), power()) }
            }
        }
    }

private fun Ctx.executeKittyCannon(destroy: Boolean, power: Float) {
    val kitty = EntityType.CAT.create(source.world) ?: return
    kitty.catType = CatEntity.TEXTURES.keys.random()
    kitty.isTamed = true
    kitty.isBaby = true
    kitty.setPosition(source.player.eyePos)
    kitty.velocity = source.player.rotationVector.multiply(2.0, 2.0, 2.0)
    source.world.spawnEntity(kitty)

    coroutineTask(delay = 1.seconds) {
        val world = kitty.world
        val location = kitty.pos
        kitty.discard()
        world.createExplosion(null, location.x, location.y, location.z, power, if (destroy) Explosion.DestructionType.DESTROY else Explosion.DestructionType.NONE)
    }
}
