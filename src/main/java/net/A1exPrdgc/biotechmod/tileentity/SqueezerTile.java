package net.A1exPrdgc.biotechmod.tileentity;

import net.A1exPrdgc.biotechmod.BiotechMod;
import net.A1exPrdgc.biotechmod.container.SqueezerContainer;
import net.A1exPrdgc.biotechmod.fluid.ModFluids;
import net.A1exPrdgc.biotechmod.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MinecartItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SqueezerTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider
{
	//-----------Constants--------------
	public static final int TICK_VALUE = 20;
	public static final int COOKING_TIME = 5;
	public static final int COOKING_TIME_TICK = COOKING_TIME * TICK_VALUE;
	public static final int CAPACITY = 20_000;

	//-----------Obligatoire------------
	private final ItemStackHandler itemHandler = createHandler();
	private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
	private final FluidTank tank = createTank();

	private boolean isActive = true;
	private int timer = 0;
	private IntArray data = new IntArray(3);

	/*-----------------NBT---------------*/
	private static final String NBTFLUID= "liq";
	private static final  String NBTINV = "inv";


	/*-------------Constructors-----------*/

	public SqueezerTile()
	{
		super(ModTileEntities.SQUEEZER.get());
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
					case 2  : return stack.getItem() == Items.BUCKET || stack.getItem() == ModItems.ROOT_BUCKET.get();
					case 1  : return stack.getItem() == ModItems.ROOT.get();
					default : return false;
				}
			}

			@Override
			public int getSlotLimit(int slot){
				switch(slot)
				{
					case 1 : return 64;
					default: return 1;
				}
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
		return timer;
	}

	public ItemStackHandler getItemHandler(){
		return itemHandler;
	}

	/*---------------------------------------*/








	public boolean canCook()
	{
		return this.itemHandler.getStackInSlot(1).getCount() > 0 &&
				this.itemHandler.getStackInSlot(1).getItem()  == ModItems.ROOT.get();
	}

	public boolean canFill()
	{
		return  this.itemHandler.getStackInSlot(2).getCount() > 0 &&
				this.itemHandler.getStackInSlot(2).getItem() == Items.BUCKET &&
				this.data.get(1) >= FluidAttributes.BUCKET_VOLUME ;
	}

	public void fillBucket()
	{
		if(this.canFill())
		{
			this.itemHandler.getStackInSlot(2).shrink(1);
			this.itemHandler.insertItem(2, new ItemStack(ModItems.ROOT_BUCKET.get()), false);
			this.tank.drain(FluidAttributes.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
		}
	}
	public void liquid_root_creation()
	{

		if(this.canCook())
		{
			//retire l'item
			this.itemHandler.getStackInSlot(1).shrink(1);

			//ajoute le liquide
			this.tank.fill(new FluidStack(ModFluids.ROOT_FLUID.get(), 250), IFluidHandler.FluidAction.EXECUTE);

			System.out.println(this.tank.getFluidInTank(1).getAmount());
		}
	}

	@Override
	public void tick(){
		if (!world.isRemote() && this.isActive)
		{
			this.data.set(0, this.itemHandler.getStackInSlot(1).getCount());

			if(this.canCook() && this.data.get(1) + 250 <= SqueezerTile.CAPACITY)
			{
				this.timer++;

				this.data.set(2, this.timer);

				if (this.timer >= COOKING_TIME_TICK)
				{
					this.timer = 0;

					this.liquid_root_creation();
					this.data.set(2, this.timer);
					this.data.set(1, this.tank.getFluidAmount());
				}
			}

			if( this.canFill() &&
				this.data.get(1) >= FluidAttributes.BUCKET_VOLUME &&
				this.itemHandler.getStackInSlot(2).getItem() == Items.BUCKET)
			{
				this.fillBucket();
				this.data.set(1, this.tank.getFluidAmount());
			}
		}
	}

	@Override
	public ITextComponent getDisplayName(){
		return new TranslationTextComponent("Squeezer");
	}

	@Nullable
	@Override
	public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity){
		System.out.println("whot 1");
		return new SqueezerContainer(i, this.world, this.pos, playerInventory, playerEntity, data);
	}


	//---------------------------------

}

