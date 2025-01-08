package com.caveore.mixin;

import com.caveore.CaveOre;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.OreVeinifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OreVeinifier.class)
public class OreVeinMixin
{
    @Redirect(method = "m_209660_", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;m_188501_()F"), remap = false)
    private static float adjustDensity(final RandomSource instance)
    {
        return (float) (instance.nextFloat() * 1 / (CaveOre.config.getCommonConfig().oreVeinDensityModifier + 0.00001));
    }
}
