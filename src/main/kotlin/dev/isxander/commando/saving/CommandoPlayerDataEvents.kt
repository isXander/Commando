package dev.isxander.commando.saving

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity

object CommandoPlayerDataEvents {
    @JvmStatic
    val SAVE = EventFactory.createArrayBacked(
        Save::class.java
    ) { callbacks: Array<Save> ->
        object : Save {
            override fun save(nbt: NbtCompound, player: ServerPlayerEntity) {
                for (callback in callbacks) callback.save(nbt, player)
            }
        }
    }

    @JvmStatic
    val LOAD = EventFactory.createArrayBacked(
        Load::class.java
    ) { callbacks: Array<Load> ->
        object : Load {
            override fun load(nbt: NbtCompound, player: ServerPlayerEntity) {
                for (callback in callbacks) callback.load(nbt, player)
            }
        }
    }

    interface Save {
        fun save(nbt: NbtCompound, player: ServerPlayerEntity)
    }

    interface Load {
        fun load(nbt: NbtCompound, player: ServerPlayerEntity)
    }
}
