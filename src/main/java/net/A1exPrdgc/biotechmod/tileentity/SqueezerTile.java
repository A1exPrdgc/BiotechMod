package net.A1exPrdgc.biotechmod.tileentity;

import net.A1exPrdgc.biotechmod.fluid.ModFluids;
import net.A1exPrdgc.biotechmod.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class SqueezerTile extends TileEntity implements ITickableTileEntity
{
	//-----------Constants--------------
	private static final int COOKING_TIME = 5;
	private static final int CAPACITY = 20_000;

	//-----------Obligatoire------------
	private final ItemStackHandler itemHandler = createHandler();
	private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
	private final FluidTank tank = createTank();

	private boolean isActive = true;
	private int timer = 0;

	/*-----------------NBT---------------*/
	private static final String NBTFLUID= "liq";
	private static final  String NBTINV = "inv";


	/*-------------Constructors-----------*/
	public SqueezerTile(TileEntityType<?> tileEntityTypeIn){
		super(tileEntityTypeIn);
	}

	public SqueezerTile()
	{
		this(ModTileEntities.SQUEEZER.get());
	}


	/*----------save and load---------*/
	@Override
	public void read(BlockState state, CompoundNBT nbt)
	{
		super.read(state, nbt);
		itemHandler.deserializeNBT(nbt.getCompound(NBTINV));
		tank.readFromNBT(nbt.getCompound("FluidTank"));
	}
	@Override
	public CompoundNBT write(CompoundNBT compound){
		compound.put(NBTINV, itemHandler.serializeNBT());
		compound.put("FluidTank", this.tank.writeToNBT(new CompoundNBT()));
		return super.write(compound);
	}
	/*-----------------------------------*/





	/*-----------Handler's creation------------*/
	private  ItemStackHandler createHandler()
	{
		return new ItemStackHandler(7) {

			@Override
			protected void onContentsChanged(int slot){
				markDirty();
			}

			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack){
				switch(slot)
				{
					case 2  : return stack.getItem() == Items.BUCKET;
					case 1  : return stack.getItem() == ModItems.ROOT.get();
					default : return false;
				}
			}

			@Override
			public int getSlotLimit(int slot){
				return  1;
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate){
				if(!isItemValid(slot, stack))
				{
					return stack;
				}

				return super.insertItem(slot, stack, simulate);
			}
		};
	}

	/*-----------tank's creation------------*/
	private FluidTank createTank()
	{
		return new FluidTank(CAPACITY) {
			@Override
			public int getTanks(){
				return super.getTanks();
			}

			@Nonnull
			@Override
			public FluidStack getFluidInTank(int tank){
				return super.getFluidInTank(tank);
			}

			@Override
			public int getTankCapacity(int tank){
				return super.getTankCapacity(tank);
			}

			@Override
			public boolean isFluidValid(int tank, @Nonnull FluidStack stack){
				return super.isFluidValid(tank, stack);
			}

			@Override
			public int fill(FluidStack resource, FluidAction action){
				return super.fill(resource, action);
			}

			@Nonnull
			@Override
			public FluidStack drain(FluidStack resource, FluidAction action){
				return super.drain(resource, action);
			}

			@Nonnull
			@Override
			public FluidStack drain(int maxDrain, FluidAction action){
				return super.drain(maxDrain, action);
			}
		};
	}
	/*-----------------------------------------*/




	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap){
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return LazyOptional.of(() -> itemHandler).cast();
		}else if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return LazyOptional.of(() -> tank).cast();
		}

		return super.getCapability(cap);
	}





	/*---------------Accesseur---------------*/
	public FluidTank getTank(){
		return tank;
	}
	public int getTimer(){
		return this.timer;
	}
	/*---------------------------------------*/








	public boolean canCook()
	{
		return this.itemHandler.getStackInSlot(1).getCount() > 0 &&
				this.itemHandler.getStackInSlot(1).getItem()  == ModItems.ROOT.get();
	}
	public void liquid_root_creation()
	{

		if(this.canCook())
		{
			//retire l'item
			this.itemHandler.getStackInSlot(1).shrink(1);

			//retire l'énergie
			///////////////////////////////////////////////////////////////////////////////////////////////////////

			//ajoute le liquide
			this.tank.fill(new FluidStack(ModFluids.ROOT_FLUID.get(), 250), IFluidHandler.FluidAction.EXECUTE);

			System.out.println(this.tank.getFluidInTank(1).getAmount());
		}
	}

	@Override
	public String toString(){
		return  "tanks : " + this.tank.getTanks() + "\n" +
				"quant : " + this.tank.getFluidInTank(1).getAmount() + "\n" +
				"fluid : " + this.tank.getFluidInTank(1).getFluid().getFluid() + "\n";
	}

	@Override
	public void tick(){
		if (!world.isRemote() && this.isActive && this.canCook()){
			System.out.println(this.timer);
			this.timer++;
			if (this.timer > 20 * COOKING_TIME){
				this.timer = 0;

				this.liquid_root_creation();
			}
			BlockState state = world.getBlockState(this.pos);
			world.notifyBlockUpdate(this.pos, state, state, 2);
		}
	}



	//---------------------------------

}

