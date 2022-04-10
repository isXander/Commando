package dev.isxander.commando.ext

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

fun ServerPlayerEntity.getPowertool(item: Item): String? =
    (this as IPowertoolHandler).getPowertool(item)

fun ServerPlayerEntity.registerPowertool(item: Item, command: String) =
    (this as IPowertoolHandler).registerPowertool(item, command)

fun ServerPlayerEntity.clearPowertool(item: Item) =
    (this as IPowertoolHandler).clearPowertool(item)

fun ServerPlayerEntity.getPowertoolStack(): ItemStack {
    if (mainHandStack.isEmpty)
        if (!offHandStack.isEmpty)
            return offHandStack

    return mainHandStack
}

interface IPowertoolHandler {
    fun registerPowertool(item: Item, command: String)
    fun getPowertool(item: Item): String?
    fun clearPowertool(item: Item)
}
