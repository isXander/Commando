package dev.isxander.commando.saving

fun registerSaveLoad(save: CommandoPlayerDataEvents.Save, load: CommandoPlayerDataEvents.Load) {
    CommandoPlayerDataEvents.SAVE.register(save)
    CommandoPlayerDataEvents.LOAD.register(load)
}
