package dev.isxander.commando.utils

import eu.pb4.sgui.api.gui.SimpleGui
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtIo
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Util
import net.minecraft.util.WorldSavePath
import java.io.File

class SavingPlayerDataGui(type: ScreenHandlerType<*>, player: ServerPlayerEntity, private val savedPlayer: ServerPlayerEntity) : SimpleGui(type, player, false) {
    override fun onClose() {
        val playerDataDir = savedPlayer.server.getSavePath(WorldSavePath.PLAYERDATA).toFile()
        try {
            val compoundTag = savedPlayer.writeNbt(NbtCompound())
            val file: File = File.createTempFile(savedPlayer.uuidAsString + "-", ".dat", playerDataDir)
            NbtIo.writeCompressed(compoundTag, file)
            val file2 = File(playerDataDir, savedPlayer.uuidAsString + ".dat")
            val file3 = File(playerDataDir, savedPlayer.uuidAsString + ".dat_old")
            Util.backupAndReplace(file2, file, file3)
        } catch (var6: Exception) {
            logger.warn("Failed to save player data for {}", savedPlayer.name.string)
        }
    }
}
