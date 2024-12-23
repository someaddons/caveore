package com.caveore.config;

import com.cupboard.config.ICommonConfig;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class CommonConfiguration implements ICommonConfig
{
    public List<String> caveblocks             = Lists.newArrayList("minecraft:air", "minecraft:cave_air", "minecraft:water", "minecraft:lava");
    public List<String> excludedOres           = new ArrayList<>();
    public int          spawnchance            = 100;
    public int          hiddenOreChance        = 10;
    public double       oreVeinDensityModifier = 1.0;
    public boolean      inverted               = false;

    public CommonConfiguration()
    {
    }

    public JsonObject serialize()
    {
        final JsonObject root = new JsonObject();

        final JsonObject entry4 = new JsonObject();
        entry4.addProperty("desc:",
            "Global chance of an ore block being placed(except veins), in percent. 50 = half as many ores as normal. More than 100 has no effect. default: 100");
        entry4.addProperty("spawnchance", spawnchance);
        root.add("spawnchance", entry4);

        final JsonObject entry = new JsonObject();
        entry.addProperty("desc:",
            " List of block IDs where non-hidden ore blocks can be placed next to.  e.g. format :  [\"minecraft:air\", \"minecraft:cave_air\"]");
        final JsonArray list = new JsonArray();
        for (final String name : caveblocks)
        {
            list.add(name);
        }
        entry.add("caveblocks", list);
        root.add("caveblocks", entry);

        final JsonObject entry5 = new JsonObject();
        entry5.addProperty("desc:",
            "Chance for ore block placement to ignore the caveblock list restriction. This controls the amount of ores outside of caves/accessable surfaces. default: 10%, vanilla = 100%");
        entry5.addProperty("hiddenOreChance", hiddenOreChance);
        root.add("hiddenOreChance", entry5);

        final JsonObject entry6 = new JsonObject();
        entry6.addProperty("desc:",
            "Modifier for the ore density in ore veins(iron/copper veins). 0 = disable veins, 0.25 = 75% less ore, 1.5 = 50% more ore. default: 1.0 (no change)");
        entry6.addProperty("oreVeinDensityModifier", oreVeinDensityModifier);
        root.add("oreVeinDensityModifier", entry6);

        final JsonObject entry2 = new JsonObject();
        entry2.addProperty("desc:",
            "List of excluded ores which are ignored for cave restriction checks, these are mod-specific. : e.g. format :  [\"mod:orename\", \"minecraft:iron_ore\"]");
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

        return root;
    }

    public void deserialize(JsonObject data)
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
        hiddenOreChance = data.get("hiddenOreChance").getAsJsonObject().get("hiddenOreChance").getAsInt();
        oreVeinDensityModifier = data.get("oreVeinDensityModifier").getAsJsonObject().get("oreVeinDensityModifier").getAsDouble();

        ConfigValues.parse();
    }
}
