package dev.isxander.commando.utils

import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import org.slf4j.LoggerFactory

fun min(value: Int) = value..Int.MAX_VALUE
fun min(value: Float) = value..Float.MAX_VALUE

fun max(value: Int) = Int.MIN_VALUE..value
fun max(value: Float) = Float.MIN_VALUE..value

val logger = LoggerFactory.getLogger("Commando")

fun ServerPlayerEntity.openHandledScreen(title: Text, type: ScreenHandlerType<*>) {
    openHandledScreen(SimpleNamedScreenHandlerFactory({ syncId, inventory, _ -> type.create(syncId, inventory) }, title))
}
