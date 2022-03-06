package com.caveore.mixin;

import com.caveore.CaveOre;
import com.caveore.config.ConfigValues;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.common.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(OreFeature.class)
public class OreMixin
{
    private boolean isOreBlock = false;

    @Inject(method = "doPlace", at = @At("HEAD"))
    private void ongenerate(
      final WorldGenLevel p_66533_,
      final Random p_66534_,
      final OreConfiguration config,
      final double p_66536_,
      final double p_66537_,
      final double p_66538_,
      final double p_66539_,
      final double p_66540_,
      final double p_66541_,
      final int p_66542_,
      final int p_66543_,
      final int p_66544_,
      final int p_66545_,
      final int p_66546_, final CallbackInfoReturnable<Boolean> cir)
    {
        isOreBlock = config.targetStates.stream().anyMatch(state -> state.state.is(Tags.Blocks.ORES) &&
                                                                      (ConfigValues.inverted && ConfigValues.excludedBlocks.contains(state.state.getBlock().getRegistryName())
                                                                         || !ConfigValues.inverted && !ConfigValues.excludedBlocks.contains(state.state.getBlock()
                                                                        .getRegistryName())));
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
                if (state.is(Tags.Blocks.ORES))
                {
                    return iWorld.getBlockState(x, y, z);
                }
                else if (ConfigValues.allowedBlocks.contains(state.getBlock().getRegistryName()))
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
            }
        }
        else
        {
            return iWorld.getBlockState(x, y, z);
        }

        return Blocks.AIR.defaultBlockState();
    }

    @Inject(method = "shouldSkipAirCheck", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F"), cancellable = true)
    private static void on(final Random rand, final float chance, final CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(rand.nextFloat() >= chance * ConfigValues.airChance);
    }
}
