package com.caveore.config;

import com.caveore.CaveOre;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Holds values of the config
 */
public class ConfigValues
{
    public static Set<Identifier> allowedBlocks  = new HashSet<>();
    public static Set<Identifier> excludedBlocks = new HashSet<>();
    public static int             oreChance      = 100;
    public static boolean         inverted       = false;
    public static float           airChance      = 1.0f;

    public static void init()
    {
        allowedBlocks = new HashSet<>();
        for (final String data : CaveOre.config.getCommonConfig().caveblocks)
        {
            allowedBlocks.add(getResourceLocation(data));
        }

        excludedBlocks = new HashSet<>();
        for (final String data : CaveOre.config.getCommonConfig().excludedOres)
        {
            excludedBlocks.add(getResourceLocation(data));
        }

        oreChance = CaveOre.config.getCommonConfig().spawnchance;
        inverted = CaveOre.config.getCommonConfig().inverted;
        airChance = (float) (1f / CaveOre.config.getCommonConfig().airChance);
    }

    public static Identifier getResourceLocation(String string) throws InvalidIdentifierException
    {
        if (string != null && !string.equals(EMPTY))
        {
            String[] split = string.split(":");
            if (split.length == 2)
            {
                return new Identifier(split[0], split[1]);
            }
        }

        throw new InvalidIdentifierException("Cannot parse:" + string + " to a valid resource location");
    }
}
