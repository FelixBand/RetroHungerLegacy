package mod.nero.nhpl.eventclasses;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class HungerDisabler {
    @SubscribeEvent
    public void disableHunger(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            event.player.getFoodStats().setFoodLevel(6);
        }
    }
}
