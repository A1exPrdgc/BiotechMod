package net.A1exPrdgc.biotechmod.tileentity;

import net.A1exPrdgc.biotechmod.BiotechMod;
import net.A1exPrdgc.biotechmod.fluid.ModFluids;
import net.A1exPrdgc.biotechmod.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class SqueezerTile extends TileEntity
{
	//-----------Obligatoire------------
	private final ItemStackHandler itemHandler = createHandler();
	private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

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
		itemHandler.deserializeNBT(nbt.getCompound("inv"));
		super.read(state, nbt);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound){
		compound.put("inv", itemHandler.serializeNBT());
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
	//---------------------------------

}

