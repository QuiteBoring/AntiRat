package dev.antirat.mixin;

import dev.antirat.AntiRat;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { YggdrasilMinecraftSessionService.class }, priority = Integer.MAX_VALUE, remap = false)
public class MixinYggdrasil {

    @ModifyVariable(method = { "joinServer" }, at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private String onJoinServer(final String value) {
        return AntiRat.changedToken ? AntiRat.changed : value;
    }

}