package mod.nero.nhpl.core.mixin;

import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.TreeMap;

@Mixin(GameRules.class)
public abstract class GameRuleMixin {

    @Shadow
    @Final
    private TreeMap<String, Object> rules;

    @Unique
    private static final String NATURAL_REGENERATION_RULE = "naturalRegeneration";

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        this.rules.remove(NATURAL_REGENERATION_RULE);
    }

    @Inject(method = "getBoolean", at = @At("HEAD"), cancellable = true)
    private void onGetBoolean(String name, CallbackInfoReturnable<Boolean> cir) {
        if (NATURAL_REGENERATION_RULE.equals(name)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getString", at = @At("HEAD"), cancellable = true)
    private void onGetString(String name, CallbackInfoReturnable<String> cir) {
        if (NATURAL_REGENERATION_RULE.equals(name)) {
            cir.setReturnValue("false");
        }
    }

    @Inject(method = "setOrCreateGameRule", at = @At("HEAD"), cancellable = true)
    private void onSetOrCreateGameRule(String key, String ruleValue, CallbackInfo ci) {
        if (NATURAL_REGENERATION_RULE.equals(key)) {
            ci.cancel();
        }
    }

    @Inject(method = "hasRule", at = @At("HEAD"), cancellable = true)
    private void onHasRule(String name, CallbackInfoReturnable<Boolean> cir) {
        if (NATURAL_REGENERATION_RULE.equals(name)) {
            cir.setReturnValue(false);
        }
    }
}