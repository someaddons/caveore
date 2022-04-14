package com.caveore.mixin;

import com.caveore.CaveOre;
import com.caveore.config.ConfigValues;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
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

    @Inject(method = "generate", at = @At("HEAD"))
    private void ongenerate(
      final FeatureContext<OreFeatureConfig> context, final CallbackInfoReturnable<Boolean> cir)
    {
        isOreBlock = context.getConfig().targets.stream().anyMatch(targetState -> CaveOre.isOre(targetState.state)
                                                                                    &&
                                                                                    (ConfigValues.inverted && ConfigValues.excludedBlocks.contains(targetState.state.getBlock()
                                                                                      .getRegistryEntry()
                                                                                      .registryKey()
                                                                                      .getValue())
                                                                                       || !ConfigValues.inverted
                                                                                            && !ConfigValues.excludedBlocks.contains(targetState.state.getBlock()
                                                                                      .getRegistryEntry().registryKey().getValue())));
    }

    @Redirect(method = "generateVeinPart", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkSection;getBlockState(III)Lnet/minecraft/block/BlockState;"))
    private BlockState ongetBlockState(final ChunkSection iWorld, final int x, final int y, final int z)
    {
        if (isOreBlock)
        {
            final BlockPos posI = new BlockPos(x, y, z);
            for (final Direction dir : Direction.values())
            {
                // Check surroundings
                final BlockPos offsetPos = posI.offset(dir);

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
                else if (ConfigValues.allowedBlocks.contains(state.getBlock().getRegistryEntry().registryKey().getValue()))
                {
                    if (CaveOre.rand.nextInt(100) <= ConfigValues.oreChance)
                    {
                        return iWorld.getBlockState(x, y, z);
                    }
                    else
                    {
                        return Blocks.AIR.getDefaultState();
                    }
                }
            }
        }
        else
        {
            return iWorld.getBlockState(x, y, z);
        }

        return Blocks.AIR.getDefaultState();
    }

    @Inject(method = "shouldNotDiscard", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F"), cancellable = true)
    private static void on(final Random rand, final float chance, final CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(rand.nextFloat() >= chance * ConfigValues.airChance);
    }
}
