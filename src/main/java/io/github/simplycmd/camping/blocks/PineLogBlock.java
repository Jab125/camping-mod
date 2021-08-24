package io.github.simplycmd.camping.blocks;

import io.github.simplycmd.camping.Main;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class PineLogBlock extends PillarBlock {
    public static final BooleanProperty SAPPY = BooleanProperty.of("sappy");

    public PineLogBlock(FabricBlockSettings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(SAPPY, false));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        super.appendProperties(stateManager);
        stateManager.add(SAPPY);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // Logs that pass this `if` statement will hopefully be uncommon enough to not impact performance when scans are done
        if (random.nextFloat() > 0.992) {
            final int scanRadius = 20;
            boolean sapNearby = false;

            // Only become sappy if nearby logs aren't, to prevent unnecessary buildup of sap
            for (int x = -(scanRadius / 2); x < scanRadius; x++)
                for (int y = -(scanRadius / 2); y < scanRadius; y++)
                    for (int z = -(scanRadius / 2); z < scanRadius; z++)
                        if (world.getBlockState(new BlockPos(x, y, z)).getBlock().equals(Main.PINE_LOG) && world.getBlockState(new BlockPos(x, y, z)).get(SAPPY))
                            sapNearby = true;

            if (!sapNearby) world.setBlockState(pos, state.with(SAPPY, true));
        }
    }
}