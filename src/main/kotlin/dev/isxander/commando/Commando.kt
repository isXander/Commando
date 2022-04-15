package dev.isxander.commando

import dev.isxander.commando.commands.*
import dev.isxander.commando.utils.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback

object Commando : ModInitializer {
    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, dedicated ->
            dispatcher.registerFly()
            dispatcher.registerFlySpeed()
            dispatcher.registerSpawnMob()
            dispatcher.registerUnsafeEnchant()
            dispatcher.registerHeal()
            dispatcher.registerFeed()
            dispatcher.registerInvSee()
            dispatcher.registerAnvil()
            dispatcher.registerCraft()
            dispatcher.registerEChest()
            dispatcher.registerGamemodes()
            dispatcher.registerPowertool()
            dispatcher.registerKittyCannon()
            dispatcher.registerLoom()
            dispatcher.registerWalkSpeed()
        }
    }
}
