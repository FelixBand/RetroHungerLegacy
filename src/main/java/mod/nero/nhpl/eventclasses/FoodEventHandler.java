package mod.nero.nhpl.eventclasses;

import net.minecraft.item.ItemFood;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FoodEventHandler {
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickItem event) {
        var player = event.getEntityPlayer();
        var itemStack = event.getItemStack();

        if (itemStack.getItem() instanceof ItemFood foodItem) {
            var world = player.world;

            var healingAmount = foodItem.getHealAmount(itemStack);
            player.heal(healingAmount);

            player.getFoodStats().addStats(foodItem.getHealAmount(itemStack), foodItem.getSaturationModifier(itemStack));
            foodItem.onItemUseFinish(itemStack, world, player);

            event.setCanceled(true);
        }
    }
}