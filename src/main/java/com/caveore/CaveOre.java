package com.caveore;

import com.caveore.config.CommonConfiguration;
import com.cupboard.config.CupboardConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(CaveOre.MODID)
public class CaveOre
{
    public static final String MODID = "caveore";

    private static final Logger                              LOGGER = LogManager.getLogger();
    public static        CupboardConfig<CommonConfiguration> config = new CupboardConfig<>(MODID, new CommonConfiguration());
    public static        Random                              rand   = new Random();

    public CaveOre(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("CaveOre initialized");
    }
}
