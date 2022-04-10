package dev.isxander.commando.utils

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.ejekta.kambrik.command.KambrikArgBuilder
import net.minecraft.server.command.ServerCommandSource
import org.slf4j.LoggerFactory

typealias Cmd = CommandDispatcher<ServerCommandSource>
typealias Ctx = CommandContext<ServerCommandSource>
typealias Builder = KambrikArgBuilder<ServerCommandSource, LiteralArgumentBuilder<ServerCommandSource>>

fun min(value: Int) = value..Int.MAX_VALUE
fun max(value: Int) = Int.MIN_VALUE..value

val logger = LoggerFactory.getLogger("Commando")
