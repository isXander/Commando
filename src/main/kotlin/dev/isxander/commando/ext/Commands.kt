package dev.isxander.commando.ext

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.ejekta.kambrik.command.KambrikArgBuilder
import me.lucko.fabric.api.permissions.v0.Permissions
import net.minecraft.server.command.ServerCommandSource

typealias Cmd = CommandDispatcher<ServerCommandSource>
typealias Ctx = CommandContext<ServerCommandSource>
typealias Builder = KambrikArgBuilder<ServerCommandSource, LiteralArgumentBuilder<ServerCommandSource>>

fun Builder.requiresPerm(permission: String, fallback: Boolean = false) =
    requires(Permissions.require(permission, fallback))

fun Builder.requiresPerm(permission: String, fallback: Int) =
    requires(Permissions.require(permission, fallback))
