package dev.isxander.commando.commands

import dev.isxander.commando.utils.Cmd
import dev.isxander.commando.utils.Ctx
import dev.isxander.commando.utils.logger
import eu.pb4.sgui.api.gui.SimpleGui
import io.ejekta.kambrik.command.addCommand
import io.ejekta.kambrik.command.requiresOp
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtIo
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Util
import net.minecraft.util.WorldSavePath
import java.io.File

fun Cmd.registerInvSee() =
    addCommand("invsee") {
        requiresOp(2)

        argument(EntityArgumentType.player(), "target") { target ->
            runs { executeInvSee(source.player, target().getPlayer(source)) }
        }
    }

private fun Ctx.executeInvSee(player: ServerPlayerEntity, target: ServerPlayerEntity) {
    val gui = InvSee.SavingPlayerDataGui(ScreenHandlerType.GENERIC_9X5, player, target)
    gui.title = target.name

    for (i in 0 until player.inventory.size()) {
        gui.setSlotRedirect(i, Slot(target.inventory, i, 0, 0))
    }

    gui.open()
}

private object InvSee {
    class SavingPlayerDataGui(type: ScreenHandlerType<*>, player: ServerPlayerEntity, private val savedPlayer: ServerPlayerEntity) : SimpleGui(type, player, false) {
        override fun onClose() {
            savePlayerData(savedPlayer)
        }
    }

    private fun savePlayerData(player: ServerPlayerEntity) {
        val playerDataDir = player.server.getSavePath(WorldSavePath.PLAYERDATA).toFile()
        try {
            val compoundTag = player.writeNbt(NbtCompound())
            val file: File = File.createTempFile(player.uuidAsString + "-", ".dat", playerDataDir)
            NbtIo.writeCompressed(compoundTag, file)
            val file2 = File(playerDataDir, player.uuidAsString + ".dat")
            val file3 = File(playerDataDir, player.uuidAsString + ".dat_old")
            Util.backupAndReplace(file2, file, file3)
        } catch (var6: Exception) {
            logger.warn("Failed to save player data for {}", player.name.string)
        }
    }
}

