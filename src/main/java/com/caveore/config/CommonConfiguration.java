package com.caveore.config;

import com.caveore.CaveOre;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class CommonConfiguration
{
    public List<String> caveblocks   = Lists.newArrayList("minecraft:air", "minecraft:cave_air", "minecraft:water", "minecraft:lava");
    public List<String> excludedOres = new ArrayList<>();
    public int          spawnchance  = 100;
    public double       airChance    = 10.0;
    public boolean      inverted     = false;

    protected CommonConfiguration()
    {
    }

    public JsonObject serialize()
    {
        final JsonObject root = new JsonObject();

        final JsonObject entry = new JsonObject();
        entry.addProperty("desc:",
          "List of blocks to which ores are allowed to spawn next to. This does not override existing spawn restrictions of the ores, as those are restrictions on the block they can spawn instead of.  e.g. format :  [\"minecraft:air\", \"minecraft:cave_air\"]");
        final JsonArray list = new JsonArray();
        for (final String name : caveblocks)
        {
            list.add(name);
        }
        entry.add("caveblocks", list);
        root.add("caveblocks", entry);


        final JsonObject entry2 = new JsonObject();
        entry2.addProperty("desc:", "List of excluded ores beeing affected, these are mod-specific. : e.g. format :  [\"mod:orename\", \"minecraft:iron_ore\"]");
        final JsonArray list2 = new JsonArray();
        for (final String name : excludedOres)
        {
            list2.add(name);
        }
        entry2.add("excludedOres", list2);
        root.add("excludedOres", entry2);

        final JsonObject entry3 = new JsonObject();
        entry3.addProperty("desc:", "Inverts the exluded list to an included only list, of which ores are affected. Default : false");
        entry3.addProperty("inverted", inverted);
        root.add("inverted", entry3);

        final JsonObject entry4 = new JsonObject();
        entry4.addProperty("desc:", "Chance for an ore vein to appear, reduced below 100% to reduce spawn rates.");
        entry4.addProperty("spawnchance", spawnchance);
        root.add("spawnchance", entry4);

        final JsonObject entry5 = new JsonObject();
        entry5.addProperty("desc:", "Alters chance of ores to not get spawned on air, increase to have more ores spawning on air.");
        entry5.addProperty("airChance", airChance);
        root.add("airChance", entry5);

        return root;
    }

    public void deserialize(JsonObject data)
    {
        if (data == null)
        {
            CaveOre.LOGGER.error("Config file was empty!");
            return;
        }

        try
        {
            caveblocks = new ArrayList<>();
            for (final JsonElement element : data.get("caveblocks").getAsJsonObject().get("caveblocks").getAsJsonArray())
            {
                caveblocks.add(element.getAsString());
            }

            excludedOres = new ArrayList<>();
            for (final JsonElement element : data.get("excludedOres").getAsJsonObject().get("excludedOres").getAsJsonArray())
            {
                excludedOres.add(element.getAsString());
            }

            inverted = data.get("inverted").getAsJsonObject().get("inverted").getAsBoolean();
            spawnchance = data.get("spawnchance").getAsJsonObject().get("spawnchance").getAsInt();
            airChance = data.get("airChance").getAsJsonObject().get("airChance").getAsDouble();
        }
        catch (Exception e)
        {
            CaveOre.LOGGER.error("Could not parse config file", e);
        }
    }
}
