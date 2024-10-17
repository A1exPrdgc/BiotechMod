package net.A1exPrdgc.biotechmod.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;

import javax.annotation.Nullable;

public class Extractor extends HorizontalBlock{
	public Extractor(Properties builder){
		super(builder);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(HORIZONTAL_FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context){
		return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
	}


}
