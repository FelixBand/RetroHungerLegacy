package mod.nero.nhpl.core.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.annotation.Nullable;

@Mixin(ItemFood.class)
public abstract class MixinItemFood {
    @Redirect(
            method = "onItemUseFinish",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;" +
                            "playSound(Lnet/minecraft/entity/player/EntityPlayer;" +
                            "DDDLnet/minecraft/util/SoundEvent;" +
                            "Lnet/minecraft/util/SoundCategory;FF)V"
            )
    )
    private void onItemUseFinish_redirectPlaySound(World worldInstance,
                                                   @Nullable EntityPlayer player,
                                                   double x, double y, double z,
                                                   SoundEvent soundIn,
                                                   SoundCategory category,
                                                   float volume,
                                                   float pitch) {

    }
}