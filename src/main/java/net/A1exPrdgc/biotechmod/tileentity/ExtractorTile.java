package net.A1exPrdgc.biotechmod.tileentity;

import net.A1exPrdgc.biotechmod.base.IFluidMachinery;
import net.A1exPrdgc.biotechmod.block.custom.Extractor;
import net.A1exPrdgc.biotechmod.container.ExtractorContainer;
import net.A1exPrdgc.biotechmod.fluid.ModFluids;
import net.A1exPrdgc.biotechmod.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BucketItem;
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

public class ExtractorTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider, IFluidMachinery
{

	/*----------NBT----------*/
	private static final String NBTINV = "extractinv";
	private static final String NBTFLUID = "extractflu";
	private static final int QUANTITY_EXTRACT_PER_TICK = 100;
	public static final int CAPACITY = 2_000;
	public static final int TICK_VALUE = 20;
	public static final int EXTRACT_TIME_IN_SEC = 1;
	public static final int EXTRACT_TIME_IN_TICK = TICK_VALUE * EXTRACT_TIME_IN_SEC;

	private int fluidAmount;

	/*----------InstanceVar----------*/
	private FluidTank tank = createTank();
	private int timer = 0;
	private final ItemStackHandler itemHandler = createHandler();

	public ExtractorTile(){
		super(ModTileEntities.EXTRACTOR.get());
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt)
	{
		super.read(state, nbt);
		itemHandler.deserializeNBT(nbt.getCompound(NBTINV));
		tank.readFromNBT(nbt.getCompound(NBTFLUID));
	}
	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		compound.put(NBTINV, itemHandler.serializeNBT());
		compound.put(NBTFLUID, this.tank.writeToNBT(new CompoundNBT()));
		return super.write(compound);
	}

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

	private ItemStackHandler createHandler()
	{
		return new ItemStackHandler(5)
		{

			@Override
			protected void onContentsChanged(int slot)
			{
				markDirty();
			}

			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack)
			{
				switch(slot)
				{
					case 0  : return stack.getItem() instanceof BucketItem;
					default : return false;
				}
			}

			@Override
			public int getSlotLimit(int slot)
			{
				return 1;
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
			{
				if(!isItemValid(slot, stack))
				{
					return stack;
				}

				return super.insertItem(slot, stack, simulate);
			}
		};
	}

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

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return LazyOptional.of(() -> itemHandler).cast();
		}
		else if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return LazyOptional.of(() -> tank).cast();
		}

		return super.getCapability(cap);
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent("Extractor");
	}

	public FluidTank getTank()
	{
		return this.tank;
	}

	public boolean canExtract()
	{
		Extractor temp = ((Extractor)world.getBlockState(pos).getBlock());
		return  temp.getNeighborFacing(world, pos) == Blocks.OAK_LOG &&
				this.fluidAmount < ExtractorTile.CAPACITY;
	}

	public boolean canFill()
	{
		return  this.tank.getFluidAmount() >= FluidAttributes.BUCKET_VOLUME &&
				this.itemHandler.getStackInSlot(0).getItem() == Items.BUCKET;
	}

	public void fillBucket()
	{
		this.itemHandler.getStackInSlot(0).shrink(1);
		this.itemHandler.insertItem(0, new ItemStack(ModItems.RESIN_BUCKET.get()), false);
		this.tank.drain(FluidAttributes.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
		this.fluidAmount = this.tank.getFluidAmount();
	}

	@Override
	public void tick()
	{
		if(!world.isRemote())
		{

			if(this.canExtract())
			{
				this.timer ++;

				if(this.timer >= EXTRACT_TIME_IN_TICK)
				{
					this.timer = 0;
					this.tank.fill(new FluidStack(ModFluids.RESIN_FLUID.get(), ExtractorTile.QUANTITY_EXTRACT_PER_TICK), IFluidHandler.FluidAction.EXECUTE);
					this.fluidAmount = this.tank.getFluidAmount();
					this.onTankChanged();
				}
			}

			if(this.canFill())
			{
				fillBucket();
				this.onTankChanged();
			}
		}

	}

	@Nullable
	@Override
	public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity)
	{
		return new ExtractorContainer(i, this.world, this.pos, playerInventory, playerEntity);
	}
}
