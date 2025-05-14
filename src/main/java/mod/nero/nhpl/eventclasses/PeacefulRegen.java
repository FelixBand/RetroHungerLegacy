package mod.nero.nhpl.eventclasses;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PeacefulRegen {

    private static final int REGEN_INTERVAL = 20;
    private static final float REGEN_AMOUNT = 1.0F;

    @SubscribeEvent
    public void peacefulRegen(TickEvent.PlayerTickEvent event) {
        var player = event.player;
        var world = player.world;
        if (event.phase == TickEvent.Phase.END && !world.isRemote) {
            if (world.getDifficulty().getId() == 0 && player.ticksExisted % REGEN_INTERVAL == 0) {
                if (player.getHealth() < player.getMaxHealth()) {
                    player.heal(REGEN_AMOUNT);
                }
            }
        }
    }

}
