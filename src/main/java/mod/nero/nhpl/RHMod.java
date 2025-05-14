package mod.nero.nhpl;

import mod.nero.nhpl.eventclasses.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class RHMod {
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        EVENT_BUS.register(new HungerDisabler());
        EVENT_BUS.register(new PeacefulRegen());
        EVENT_BUS.register(new FoodEventHandler());
    }

}
