package dev.isxander.commando.mixins;

import com.mojang.authlib.GameProfile;
import dev.isxander.commando.ext.IPowertoolHandler;
import dev.isxander.commando.ext.PowertoolsKt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin implements IPowertoolHandler {
    @Shadow @Final public MinecraftServer server;

    @Unique
    private final Map<Item, String> commando$powertools = new HashMap<>();

    protected ServerPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void registerPowertool(@NotNull Item item, @NotNull String command) {
        commando$powertools.put(item, command);
    }

    @Nullable
    @Override
    public String getPowertool(@NotNull Item item) {
        return commando$powertools.get(item);
    }

    @Override
    public void clearPowertool(@NotNull Item item) {
        commando$powertools.remove(item);
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;attack(Lnet/minecraft/entity/Entity;)V"), cancellable = true)
    private void onAttack(Entity target, CallbackInfo ci) {
        ItemStack stack = PowertoolsKt.getPowertoolStack((ServerPlayerEntity) (Object) this);
        if (!stack.isEmpty() && getPowertool(stack.getItem()) != null)
            ci.cancel();
    }

    @Inject(method = "swingHand", at = @At("HEAD"), cancellable = true)
    private void onSwingHand(Hand hand, CallbackInfo ci) {
        ItemStack stack = PowertoolsKt.getPowertoolStack((ServerPlayerEntity) (Object) this);
        String powertool = !stack.isEmpty() ? getPowertool(stack.getItem()) : null;

        if (powertool != null) {
            server.getCommandManager().execute(getCommandSource(), powertool);
            ci.cancel();
        }
    }

    @Override
    protected void onBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = PowertoolsKt.getPowertoolStack((ServerPlayerEntity) (Object) this);
        if (!stack.isEmpty() && getPowertool(stack.getItem()) != null)
            cir.setReturnValue(true);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writePowertoolNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtCompound powertoolNbt = new NbtCompound();
        commando$powertools.forEach((item, command) -> powertoolNbt.putString(Registry.ITEM.getId(item).toString(), command));
        nbt.put("CommandoPowertools", powertoolNbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readPowertoolNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("CommandoPowertools")) {
            NbtCompound powertoolNbt = nbt.getCompound("CommandoPowertools");
            ((NbtCompoundAccessor) powertoolNbt).getEntries().forEach((itemIdStr, command) -> {
                Item item = Registry.ITEM.get(Identifier.tryParse(itemIdStr));
                registerPowertool(item, command.asString());
            });
        }
    }
}
