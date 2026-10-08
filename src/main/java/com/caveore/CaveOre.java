package com.caveore;

import com.caveore.config.CommonConfiguration;
import com.cupboard.config.CupboardConfig;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class CaveOre implements ModInitializer
{
    public static final String MODID = "caveore";

    public static final Logger                              LOGGER = LogManager.getLogger();
    public static       CupboardConfig<CommonConfiguration> config = new CupboardConfig<>(MODID, new CommonConfiguration());
    public static       Random                              rand   = new Random();
    public static TagKey<Block> oreTag = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores"));

    public CaveOre()
    {
    }

    @Override
    public void onInitialize()
    {
        LOGGER.info("CaveOre initialized");
    }
}
