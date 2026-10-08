package com.caveore.mixin;

import com.caveore.CaveOre;
import com.caveore.config.ConfigValues;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractOreFeature;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(AbstractOreFeature.class)
public class OreMixin
{
    @Inject(method = "canPlaceOre", at = @At("HEAD"), cancellable = true)
    private void onCanPlaceOre(
        final BlockState state,
        final Function<BlockPos, BlockState> blockGetter,
        final RandomSource random,
        final BlockReplacement targetState,
        final BlockPos.MutableBlockPos orePos,
        final CallbackInfoReturnable<Boolean> cir)
    {
        final BlockState oreState = targetState.state();
        final boolean listed = ConfigValues.excludedBlocks.contains(BuiltInRegistries.BLOCK.getKey(oreState.getBlock()));
        if (!oreState.is(CaveOre.oreTag) || ConfigValues.inverted != listed)
        {
            return;
        }

        if (!targetState.target().test(state, orePos, random))
        {
            cir.setReturnValue(false);
            return;
        }

        // also checks neighbors across chunk section boundaries
        final BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
        for (final Direction direction : Direction.values())
        {
            neighborPos.setWithOffset(orePos, direction);
            final BlockState neighborState = blockGetter.apply(neighborPos);
            if (neighborState.is(CaveOre.oreTag))
            {
                cir.setReturnValue(true);
                return;
            }

            if (ConfigValues.allowedBlocks.contains(BuiltInRegistries.BLOCK.getKey(neighborState.getBlock())))
            {
                cir.setReturnValue(random.nextInt(100) < ConfigValues.oreChance);
                return;
            }
        }

        // Affected ores use exposure rules instead of vanilla air discarding
        cir.setReturnValue(random.nextInt(100) < CaveOre.config.getCommonConfig().hiddenOreChance);
    }
}
