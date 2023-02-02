package com.caveore;

import com.caveore.config.Configuration;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
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
        return block.isIn(BlockTags.COAL_ORES)
                 || block.isIn(BlockTags.COPPER_ORES)
                 || block.isIn(BlockTags.DIAMOND_ORES)
                 || block.isIn(BlockTags.EMERALD_ORES)
                 || block.isIn(BlockTags.GOLD_ORES)
                 || block.isIn(BlockTags.IRON_ORES)
                 || block.isIn(BlockTags.LAPIS_ORES)
                 || block.isIn(BlockTags.REDSTONE_ORES);
    }
}
