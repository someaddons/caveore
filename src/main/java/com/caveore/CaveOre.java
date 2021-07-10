package com.caveore;

import com.caveore.config.ConfigValues;
import com.caveore.config.Configuration;
import com.caveore.event.ModEventHandler;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(CaveOre.MODID)
public class CaveOre
{
    public static final String MODID = "caveore";

    private static final Logger        LOGGER = LogManager.getLogger();
    public static        Configuration config;
    public static        Random        rand   = new Random();

    public CaveOre()
    {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        config = new Configuration();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        Mod.EventBusSubscriber.Bus.MOD.bus().get().register(ModEventHandler.class);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        ConfigValues.init();
        LOGGER.info("CaveOre initialized");
    }
}
