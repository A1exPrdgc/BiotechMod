package net.A1exPrdgc.biotechmod.block.custom;


import net.A1exPrdgc.biotechmod.container.SqueezerContainer;
import net.A1exPrdgc.biotechmod.tileentity.ModTileEntities;
import net.A1exPrdgc.biotechmod.tileentity.SqueezerTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class Squeezer extends DirectionalBlock
{

	private SqueezerTile tileEntity;
	public Squeezer(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context){
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		//vérifie que le serveur répond
		if(!worldIn.isRemote())
		{
			this.tileEntity = (SqueezerTile)worldIn.getTileEntity(pos);

			//vérifie que le joueur n'est pas en sneak
			if(!player.isCrouching())
			{
				//agit si le joueur est en sneak
				if(tileEntity instanceof SqueezerTile)
				{
					INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);

					NetworkHooks.openGui(((ServerPlayerEntity) player), containerProvider, tileEntity.getPos());

					((SqueezerTile)tileEntity).liquid_root_creation();

					System.out.println("azerty : " + ((SqueezerTile)tileEntity).getFluidInTank(1).getAmount());
				}
				else
				{
					throw new IllegalStateException("container called is missing");
				}
			}
		}
		return ActionResultType.SUCCESS;
	}

	private INamedContainerProvider createContainerProvider(World worldIn, BlockPos pos)
	{
		return new INamedContainerProvider() {
			@Override
			public ITextComponent getDisplayName() {
				return new TranslationTextComponent("Squeezer");
			}
			@Nullable
			@Override
			public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
				return new SqueezerContainer(i, playerInventory, playerEntity, tileEntity);
			}
		};
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		return ModTileEntities.SQUEEZER.get().create();
	}

	@Override
	public boolean hasTileEntity(BlockState state){
		return true;
	}




}
