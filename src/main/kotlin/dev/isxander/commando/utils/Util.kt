package dev.isxander.commando.utils

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource

typealias Cmd = CommandDispatcher<ServerCommandSource>
typealias Ctx = CommandContext<ServerCommandSource>

fun min(value: Int) = value..Int.MAX_VALUE
fun max(value: Int) = Int.MIN_VALUE..value
