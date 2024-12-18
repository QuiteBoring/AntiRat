package dev.antirat.mixin;

import dev.antirat.AntiRat;
import dev.antirat.util.FileUtil;
import dev.antirat.util.RandomString;
import dev.antirat.util.RatException;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.Session;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Mixin(value = Session.class, priority = Integer.MAX_VALUE)
public class MixinSession {

    @Final
    @Mutable
    @Shadow
    private String token;

    @Final
    @Inject(method = "getSessionID", at = @At("HEAD"))
    public void getSessionID(final CallbackInfoReturnable<String> cir) {
        if (!AntiRat.changedToken) {
            this.antiRat$changeToken();
        }

        throw new RatException();
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(final String usernameIn, final String playerIDIn, final String tokenIn, final String sessionTypeIn, final CallbackInfo ci) {
        this.antiRat$changeToken(usernameIn + ":" + playerIDIn);
    }

    @Final
    @Inject(method = "getToken", at = @At("HEAD"), cancellable = true)
    public void getToken(final CallbackInfoReturnable<String> cir) {
        if (!AntiRat.changedToken) {
            this.antiRat$changeToken();
        }

        final String executor = FileUtil.getExecutor(3);
        if (executor.contains("gg.essential.handlers.") && FileUtil.getFileLocation(executor).endsWith("/essential/Essential%20(forge_1.8.9).jar")) {
            cir.setReturnValue(AntiRat.changed);
        } else {
            cir.setReturnValue(this.token);
        }
    }

    @Unique
    private void antiRat$changeToken(final String id) {
        AntiRat.changedToken = true;
        AntiRat.changed = this.token;
        this.token = this.antiRat$randomToken(id);
        this.antiRat$changeLaunchArgs();
    }

    @Unique
    private void antiRat$changeToken() {
        AntiRat.changedToken = true;
        AntiRat.changed = this.token;
        this.token = this.antiRat$randomToken(AntiRat.MC.getSession().getUsername() + ":" + AntiRat.MC.getSession().getPlayerID());
        this.antiRat$changeLaunchArgs();
    }

    @Unique
    private int antiRat$getPlayerHash() {
        return new SimpleDateFormat("dd/MM/yyyy HH").format(new Date()).hashCode();
    }

    @Unique
    private void antiRat$changeLaunchArgs() {
        List<String> stringList = (List<String>) Launch.blackboard.get("ArgumentList");

        for (int i = 0; i < stringList.size(); ++i) {
            final String s = stringList.get(i);
            if (s.equals("--accessToken")) {
                stringList.set(i + 1, this.token);
                Launch.blackboard.replace("ArgumentsList", stringList);
                break;
            }
        }
    }

    @Unique
    private String antiRat$randomToken(final String id) {
        final int length = 290 + Math.abs(this.antiRat$getPlayerHash() + id.hashCode()) % 75;
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder("eyJhbGciOiJIUzI1NiJ9.eyJ");

        while (sb.length() != length) {
            if (sb.length() == length - 44) {
                sb.append('.');
            } else {
                sb.append(RandomString.alphabet[random.nextInt(RandomString.alphabetLength)]);
            }
        }

        return sb.toString();
    }

}