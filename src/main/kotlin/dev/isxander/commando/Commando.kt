package dev.isxander.commando

import dev.isxander.commando.commands.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback

object Commando : ModInitializer {
    override fun onInitialize() {
        CommandRegistrationCallback.EVENT.register { dispatcher, dedicated ->
            dispatcher.registerFly()
            dispatcher.registerFlySpeed()
            dispatcher.registerSpawnMob()
            dispatcher.registerRemoveEffect()
            dispatcher.registerUnsafeEnchant()
            dispatcher.registerHeal()
            dispatcher.registerFeed()
            dispatcher.registerInvSee()
            dispatcher.registerAnvil()
            dispatcher.registerCraft()
            dispatcher.registerEChest()
            dispatcher.registerGamemodes()
        }
    }
}
