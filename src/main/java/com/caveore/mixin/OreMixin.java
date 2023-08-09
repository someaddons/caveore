package com.caveore.mixin;

import com.caveore.CaveOre;
import com.caveore.config.ConfigValues;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OreFeature.class)
public class OreMixin
{
    private boolean isOreBlock = false;

    @Inject(method = "doPlace", at = @At("HEAD"))
    private void ongenerate(
      final WorldGenLevel p_225172_,
      final RandomSource p_225173_,
      final OreConfiguration config,
      final double p_225175_,
      final double p_225176_,
      final double p_225177_,
      final double p_225178_,
      final double p_225179_,
      final double p_225180_,
      final int p_225181_,
      final int p_225182_,
      final int p_225183_,
      final int p_225184_,
      final int p_225185_, final CallbackInfoReturnable<Boolean> cir)
    {
        isOreBlock = config.targetStates.stream().anyMatch(state -> CaveOre.isOre(state.state) &&
                                                                      (ConfigValues.inverted
                                                                         && ConfigValues.excludedBlocks.contains(BuiltInRegistries.BLOCK.getKey(state.state.getBlock()))
                                                                         || !ConfigValues.inverted
                                                                              && !ConfigValues.excludedBlocks.contains(BuiltInRegistries.BLOCK.getKey(state.state.getBlock()))));
    }

    @Redirect(method = "doPlace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;getBlockState(III)Lnet/minecraft/world/level/block/state/BlockState;"))
    private BlockState ongetBlockState(final LevelChunkSection iWorld, final int x, final int y, final int z)
    {
        if (isOreBlock)
        {
            final BlockPos posI = new BlockPos(x, y, z);
            for (final Direction dir : Direction.values())
            {
                // Check surroundings
                final BlockPos offsetPos = posI.relative(dir);

                if (offsetPos.getX() > 15 || offsetPos.getX() < 0
                      || offsetPos.getY() > 15 || offsetPos.getY() < 0
                      || offsetPos.getZ() > 15 || offsetPos.getZ() < 0)
                {
                    continue;
                }

                final BlockState state = iWorld.getBlockState(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
                if (CaveOre.isOre(state))
                {
                    return iWorld.getBlockState(x, y, z);
                }
                else if (ConfigValues.allowedBlocks.contains(BuiltInRegistries.BLOCK.getKey(state.getBlock())))
                {
                    if (CaveOre.rand.nextInt(100) <= ConfigValues.oreChance)
                    {
                        return iWorld.getBlockState(x, y, z);
                    }
                    else
                    {
                        return Blocks.AIR.defaultBlockState();
                    }
                }
                else if (CaveOre.rand.nextInt(100) <= CaveOre.config.getCommonConfig().hiddenOreChance)
                {
                    return iWorld.getBlockState(x, y, z);
                }
            }
        }
        else
        {
            return iWorld.getBlockState(x, y, z);
        }

        return Blocks.AIR.defaultBlockState();
    }

    @Inject(method = "shouldSkipAirCheck", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F"), cancellable = true)
    private static void on(
      final RandomSource rand,
      final float chance,
      final CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(true);
    }
}
