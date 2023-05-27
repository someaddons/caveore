package com.caveore.config;

import com.caveore.CaveOre;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Configuration
{
    /**
     * Loaded everywhere, not synced
     */
    private final CommonConfiguration commonConfig = new CommonConfiguration();

    /**
     * Loaded clientside, not synced
     */
    // private final ClientConfiguration clientConfig;

    /**
     * Builds configuration tree.
     */
    public Configuration()
    {
    }

    final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public void load()
    {
        final Path configPath = FabricLoader.getInstance().getConfigDir().normalize().resolve(CaveOre.MODID + ".json");
        final File config = configPath.toFile();

        if (!config.exists())
        {
            CaveOre.LOGGER.warn("Config for cave ore not found, recreating default");
            try
            {
                final BufferedWriter writer = Files.newBufferedWriter(configPath);
                gson.toJson(commonConfig.serialize(), JsonObject.class, writer);
                writer.close();
            }
            catch (IOException e)
            {
                CaveOre.LOGGER.error("Could not write config to:" + configPath, e);
            }
        }
        else
        {
            try
            {
                commonConfig.deserialize(gson.fromJson(Files.newBufferedReader(configPath), JsonObject.class));
            }
            catch (IOException e)
            {
                CaveOre.LOGGER.error("Could not read config from:" + configPath, e);
            }
        }

        ConfigValues.init();
    }

    public void save()
    {
        final Path configPath = FabricLoader.getInstance().getConfigDir().normalize().resolve(CaveOre.MODID + ".json");
        try
        {
            final BufferedWriter writer = Files.newBufferedWriter(configPath);
            gson.toJson(commonConfig.serialize(), JsonObject.class, writer);
            writer.close();
        }
        catch (IOException e)
        {
            CaveOre.LOGGER.error("Could not write config to:" + configPath, e);
        }
    }

    public CommonConfiguration getCommonConfig()
    {
        return commonConfig;
    }
}
