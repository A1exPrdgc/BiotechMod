package net.A1exPrdgc.biotechmod.block.custom;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.block.LeverBlock.POWERED;

public class Extractor extends DirectionalBlock {
	public Extractor(Properties builder){

		super(builder);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context){
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
	{


		Direction dir = state.get(FACING);
		BlockPos blocPos = pos.offset(dir.getOpposite());
		Block bloc = worldIn.getBlockState(blocPos).getBlock();
		if(bloc == Blocks.OAK_LOG && worldIn.isBlockPowered(pos))
		{
			bloc.harvestBlock(worldIn, null ,blocPos, worldIn.getBlockState(blocPos), null,  stack);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving){
		Direction dir = state.get(FACING);
		BlockPos blocPos = pos.offset(dir.getOpposite());
		Block bloc = worldIn.getBlockState(blocPos).getBlock();
		if(bloc == Blocks.OAK_LOG && worldIn.isBlockPowered(pos))
		{
			System.out.println("dfghjklm");
		}
	}
}
