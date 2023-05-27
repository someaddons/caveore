package com.caveore;

import com.caveore.config.Configuration;
import net.fabricmc.api.ModInitializer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class CaveOre implements ModInitializer
{
    public static final String MODID = "caveore";

    public static final Logger        LOGGER = LogManager.getLogger();
    public static       Configuration config;
    public static       Random        rand   = new Random();

    public CaveOre()
    {
        config = new Configuration();
    }

    @Override
    public void onInitialize()
    {
        LOGGER.info("CaveOre initialized");
        config.load();
    }

    public static boolean isOre(final BlockState block)
    {
        return block.is(BlockTags.COAL_ORES)
                 || block.is(BlockTags.COPPER_ORES)
                 || block.is(BlockTags.DIAMOND_ORES)
                 || block.is(BlockTags.EMERALD_ORES)
                 || block.is(BlockTags.GOLD_ORES)
                 || block.is(BlockTags.IRON_ORES)
                 || block.is(BlockTags.LAPIS_ORES)
                 || block.is(BlockTags.REDSTONE_ORES);
    }
}
