package com.caveore.mixin;

import com.caveore.CaveOre;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.material.rule.OreVeinRule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OreVeinRule.class)
public class OreVeinMixin
{
    @Redirect(method = "lambda$compile$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F"), require = 3, allow = 3)
    private float adjustDensity(final RandomSource instance)
    {
        final float sample = instance.nextFloat();
        final double modifier = CaveOre.config.getCommonConfig().oreVeinDensityModifier;
        return modifier <= 0.0 ? Float.POSITIVE_INFINITY : (float) (sample / modifier);
    }
}
