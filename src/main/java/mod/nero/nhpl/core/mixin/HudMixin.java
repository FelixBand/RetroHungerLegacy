package mod.nero.nhpl.core.mixin;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.*;
import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.AIR;

@Mixin(GuiIngameForge.class)
public class HudMixin extends GuiIngame {

    @Shadow
    public static int left_height;

    @Shadow
    public static int right_height;

    @Shadow
    private RenderGameOverlayEvent eventParent;

    @Shadow
    private boolean pre(RenderGameOverlayEvent.ElementType type)
    {
        return MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Pre(eventParent, type));
    }

    @Shadow
    private void post(RenderGameOverlayEvent.ElementType type)
    {
        MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(eventParent, type));
    }

    @Shadow
    private void bind(ResourceLocation res)
    {
        mc.getTextureManager().bindTexture(res);
    }

    public HudMixin(Minecraft mcIn) {
        super(mcIn);
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true, remap = false)
    private void onRenderFood(int width, int height, CallbackInfo ci) {

        ci.cancel();
    }

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true, remap = false)
    private void onRenderArmor(int width, int height, CallbackInfo ci) {
        if (pre(ARMOR)) return;
        mc.profiler.startSection("armor");

        GlStateManager.enableBlend();
        var left = width / 2 + 82;
        var top = height - right_height;
        right_height += 10;

        var level = ForgeHooks.getTotalArmorValue(mc.player);
        for (var i = 1; level > 0 && i < 20; i += 2)
        {
            if (i < level)
            {
                drawTexturedModalRect(left, top, 34, 9, 9, 9);
            }
            else if (i == level)
            {
                drawTexturedModalRect(left, top, 25, 9, 9, 9);
            }
            else {
                drawTexturedModalRect(left, top, 16, 9, 9, 9);
            }
            left -= 8;
        }
        left_height += 10;

        GlStateManager.disableBlend();
        mc.profiler.endSection();
        post(ARMOR);

        ci.cancel();
    }

    @Inject(method = "renderHealthMount", at = @At("HEAD"), cancellable = true, remap = false)
    private void onRenderHealthMount(int width, int height, CallbackInfo ci) {
        var player = (EntityPlayer)mc.getRenderViewEntity();
        assert player != null;
        var tmp = player.getRidingEntity();
        var level = ForgeHooks.getTotalArmorValue(mc.player);
        if (!(tmp instanceof EntityLivingBase mount)) return;

        bind(ICONS);

        if (pre(HEALTHMOUNT)) return;

        var left_align = width / 2 + 91;

        mc.profiler.endStartSection("mountHealth");
        GlStateManager.enableBlend();
        var health = (int)Math.ceil(mount.getHealth());
        var healthMax = mount.getMaxHealth();
        var hearts = (int)(healthMax + 0.5F) / 2;

        if (hearts > 30) hearts = 30;

        final var MARGIN = 52;
        final var HALF = MARGIN + 45;
        final var FULL = MARGIN + 36;

        for (var heart = 0; hearts > 0; heart += 20)
        {
            var top = (level == 0 ? (height - right_height) + 10 : height - right_height);

            var rowCount = Math.min(hearts, 10);
            hearts -= rowCount;

            for (var i = 0; i < rowCount; ++i)
            {
                var x = left_align - i * 8 - 9;
                drawTexturedModalRect(x, top, MARGIN, 9, 9, 9);

                if (i * 2 + 1 + heart < health)
                    drawTexturedModalRect(x, top, FULL, 9, 9, 9);
                else if (i * 2 + 1 + heart == health)
                    drawTexturedModalRect(x, top, HALF, 9, 9, 9);
            }

            right_height += 10;
        }
        GlStateManager.disableBlend();
        post(HEALTHMOUNT);
        ci.cancel();
    }

    @Inject(method = "renderAir", at = @At("HEAD"), cancellable = true, remap = false)
    private void onRenderAir(int width, int height, CallbackInfo ci)
    {
        if (pre(AIR)) return;
        mc.profiler.startSection("air");
        EntityPlayer player = (EntityPlayer)this.mc.getRenderViewEntity();
        GlStateManager.enableBlend();
        var left = width / 2 + 91;
        var level = ForgeHooks.getTotalArmorValue(mc.player);
        var top = (level == 0 ? (height - right_height) + 10 : height - right_height);

        assert player != null;
        if (player.isInsideOfMaterial(Material.WATER))
        {
            var air = player.getAir();
            var full = MathHelper.ceil((double)(air - 2) * 10.0D / 300.0D);
            var partial = MathHelper.ceil((double)air * 10.0D / 300.0D) - full;

            for (var i = 0; i < full + partial; ++i)
            {
                drawTexturedModalRect(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
            }
            right_height += 10;
        }

        GlStateManager.disableBlend();
        mc.profiler.endSection();
        post(AIR);
        ci.cancel();
    }
}