package net.A1exPrdgc.biotechmod.tileentity;

import net.A1exPrdgc.biotechmod.fluid.ModFluids;
import net.A1exPrdgc.biotechmod.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class SqueezerTile extends TileEntity implements IFluidHandler
{
	//-----------Obligatoire------------
	private final ItemStackHandler itemHandler = createHandler();
	private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
	public final FluidTank tank = new FluidTank(200_000);;

	private int cookingTime;
	private int totalCookingTime;

	private static final String NBTFLUID= "liq";
	private static final  String NBTINV = "inv";

	public SqueezerTile(TileEntityType<?> tileEntityTypeIn){
		super(tileEntityTypeIn);
	}

	public SqueezerTile()
	{
		this(ModTileEntities.SQUEEZER.get());
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt)
	{
		itemHandler.deserializeNBT(nbt.getCompound(NBTINV));
		tank.readFromNBT(nbt.getCompound(NBTFLUID));
		super.read(state, nbt);
	}

	public FluidTank getTank(){
		return tank;
	}

	@Override
	public CompoundNBT write(CompoundNBT compound){
		compound.put(NBTINV, itemHandler.serializeNBT());

		CompoundNBT fluid = new CompoundNBT();
		tank.writeToNBT(fluid);
		compound.put(NBTFLUID, fluid);
		return super.write(compound);
	}

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

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap){
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return handler.cast();
		}

		return super.getCapability(cap);
	}

	@Override
	public int getTanks(){
		return 1;
	}

	@Nonnull
	@Override
	public FluidStack getFluidInTank(int tank){
		return this.tank.getFluidInTank(tank);
	}

	@Override
	public int getTankCapacity(int tank){
		return this.tank.getTankCapacity(tank);
	}

	@Override
	public boolean isFluidValid(int tank, @Nonnull FluidStack stack){
		if(this.tank.getFluidInTank(tank) == stack)
			return true;
		return false;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action){
		return this.tank.fill(resource, action);
	}

	@Nonnull
	@Override
	public FluidStack drain(FluidStack resource, FluidAction action){
		return this.tank.drain(resource, action);
	}

	@Nonnull
	@Override
	public FluidStack drain(int maxDrain, FluidAction action){
		return this.tank.drain(maxDrain, action);
	}

	public void liquid_root_creation()
	{
		boolean hasRoot = this.itemHandler.getStackInSlot(1).getCount() > 0 &&
				          this.itemHandler.getStackInSlot(1).getItem()  == ModItems.ROOT.get();

		if(hasRoot)
		{
			//retire l'item
			this.itemHandler.getStackInSlot(1).shrink(1);

			//retire l'énergie
			///////////////////////////////////////////////////////////////////////////////////////////////////////

			//ajoute le liquide
			this.tank.fill(new FluidStack(ModFluids.ROOT_FLUID.get(), 250), FluidAction.EXECUTE);

			System.out.println(this.getFluidInTank(1).getAmount());
		}
	}

	@Override
	public String toString(){
		return  "tanks : " + this.getTanks() + "\n" +
				"quant : " + this.getFluidInTank(1).getAmount() + "\n" +
				"fluid : " + this.getFluidInTank(1).getFluid().getFluid() + "\n";
	}

	//---------------------------------

}

