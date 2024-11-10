package net.A1exPrdgc.biotechmod.block.custom;


import net.A1exPrdgc.biotechmod.item.ModItems;
import net.A1exPrdgc.biotechmod.tileentity.ExtractorTile;
import net.A1exPrdgc.biotechmod.tileentity.SqueezerTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class Extractor extends DirectionalBlock {

	private ExtractorTile tileEntity;

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
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit){
		if(!worldIn.isRemote())
		{
			this.tileEntity = ((ExtractorTile)worldIn.getTileEntity(pos));

			if(tileEntity instanceof ExtractorTile)
			{
				System.out.println(tileEntity);
				NetworkHooks.openGui(((ServerPlayerEntity) player), tileEntity, (PacketBuffer packetBuffer) ->
				{packetBuffer.writeBlockPos(tileEntity.getPos());});

			}
			else
			{
				throw new IllegalStateException("container called is missing");
			}

		}
		return ActionResultType.SUCCESS;

	}

	public Block getNeighborFacing(World worldIn, BlockPos pos)
	{
		Direction dir = worldIn.getBlockState(pos).get(FACING);
		BlockPos blocPos = pos.offset(dir.getOpposite());
		Block bloc = worldIn.getBlockState(blocPos).getBlock();
		return bloc;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return new ExtractorTile();
	}

	@Override
	public boolean hasTileEntity(BlockState state){
		return true;
	}
}
