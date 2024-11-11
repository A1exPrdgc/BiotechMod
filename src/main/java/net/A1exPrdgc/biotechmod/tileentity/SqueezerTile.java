package net.A1exPrdgc.biotechmod.tileentity;

import net.A1exPrdgc.biotechmod.base.IFluidMachinery;
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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SqueezerTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IFluidMachinery
{
	//-----------Constants--------------
	public static final int TICK_VALUE = 20;
	public static final int COOKING_TIME = 5;
	public static final int COOKING_TIME_TICK = COOKING_TIME * TICK_VALUE;
	public static final int CAPACITY = 20_000;
	public static final int MB_PER_ROOT = 250;

	//-----------Obligatoire------------
	private final ItemStackHandler itemHandler = createHandler();
	private final FluidTank tank = createTank();

	private int timer = 0;
	private int count;

	/*-----------------NBT---------------*/
	private static final String NBT_FLUID = "FluidTank";
	private static final  String NBT_INV  = "inv";
	private static final String NBT_TIMER = "timer";
	private static final String NBT_ITEM_NUMBER = "itemquantity";


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
		itemHandler.deserializeNBT(nbt.getCompound(NBT_INV));
		tank.readFromNBT(nbt.getCompound(NBT_FLUID));
		timer = nbt.getInt(NBT_TIMER);
		count = nbt.getInt(NBT_ITEM_NUMBER);
	}
	@Override
	public CompoundNBT write(CompoundNBT compound){
		super.write(compound);
		compound.put(NBT_INV, itemHandler.serializeNBT());
		compound.put(NBT_FLUID, this.tank.writeToNBT(new CompoundNBT()));
		compound.putInt(NBT_TIMER, this.timer);
		compound.putInt(NBT_ITEM_NUMBER, this.getItemHandler().getStackInSlot(1).getCount());
		return compound;
	}
	/*-----------------------------------*/


	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket(){
		CompoundNBT nbt = new CompoundNBT();
		this.write(nbt);
		return new SUpdateTileEntityPacket(this.getPos(), 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
		this.read(null, pkt.getNbtCompound());
	}

	public void onTankChanged() {
		if (!this.world.isRemote) {  // Vérifiez que ce code est exécuté côté serveur
			this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), 3);
		}
	}

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
				int filled = super.fill(resource, action);
				if(filled > 0 && action == FluidAction.EXECUTE)
				{
					SqueezerTile.this.onTankChanged();
				}
				return filled;
			}

			@Nonnull
			@Override
			public FluidStack drain(FluidStack resource, FluidAction action){
				FluidStack drained = super.drain(resource, action);

				if(drained.getAmount() > 0 && action == FluidAction.EXECUTE)
				{
					SqueezerTile.this.onTankChanged();
				}
				return drained;
			}

			@Nonnull
			@Override
			public FluidStack drain(int maxDrain, FluidAction action){
				FluidStack drained = super.drain(maxDrain, action);

				if(drained.getAmount() > 0 && action == FluidAction.EXECUTE)
				{
					SqueezerTile.this.onTankChanged();
				}
				return drained;
			}
		};
	}





	/*-----------------------------------------*/







	/*---------------Accesseur---------------*/

	public int getTimer(){
		return this.timer;
	}
	public FluidTank getTank()
	{
		return this.tank;
	}

	public int getCount(){
		return count;
	}

	public ItemStackHandler getItemHandler(){
		return itemHandler;
	}
	@Override
	public ITextComponent getDisplayName(){
		return new TranslationTextComponent("Squeezer");
	}
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap){
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return LazyOptional.of(() -> itemHandler).cast();
		}
		if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return LazyOptional.of(() -> tank).cast();
		}

		return super.getCapability(cap);
	}
	/*---------------------------------------*/







	/*----------Recette----------*/
	public boolean canCook()
	{
		return this.count > 0 &&
				this.itemHandler.getStackInSlot(1).getItem()  == ModItems.ROOT.get() &&
				this.tank.getFluidAmount() + 250 <= SqueezerTile.CAPACITY;

	}
	public boolean canFill()
	{
		return  this.itemHandler.getStackInSlot(2).getCount() > 0 &&
				this.itemHandler.getStackInSlot(2).getItem() == Items.BUCKET &&
				this.tank.getFluidAmount() >= FluidAttributes.BUCKET_VOLUME;
	}

	public void fillBucket()
	{
		this.itemHandler.getStackInSlot(2).shrink(1);
		this.itemHandler.insertItem(2, new ItemStack(ModItems.ROOT_BUCKET.get()), false);
		this.tank.drain(FluidAttributes.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
	}

	public void liquid_root_creation()
	{

		if(this.canCook())
		{
			//retire l'item
			this.itemHandler.getStackInSlot(1).shrink(1);

			//ajoute le liquide
			this.tank.fill(new FluidStack(ModFluids.ROOT_FLUID.get(), SqueezerTile.MB_PER_ROOT), IFluidHandler.FluidAction.EXECUTE);

			System.out.println(this.tank.getFluidInTank(1).getAmount());
		}
	}
	/*---------------------------*/

	@Override
	public void tick()
	{
		if (!world.isRemote())
		{
			this.count = this.itemHandler.getStackInSlot(1).getCount();
			if(this.canCook())
			{
				this.timer++;
				if (this.timer >= COOKING_TIME_TICK)
				{
					this.timer = 0;

					this.liquid_root_creation();

				}
				this.onTankChanged();
			}

			if( this.canFill())
			{
				this.fillBucket();
				this.onTankChanged();

			}
		}
	}


	@Nullable
	@Override
	public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity){
		System.out.println("whot 1");
		return new SqueezerContainer(i, this.world, this.pos, playerInventory, playerEntity);
	}


	//---------------------------------

}

