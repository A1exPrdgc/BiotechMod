package net.A1exPrdgc.biotechmod.fluid;

import net.A1exPrdgc.biotechmod.BiotechMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;

public class LiquidRootHandler extends FluidHandlerItemStack
{

	protected static final FluidStack EMPTY = new FluidStack(ModFluids.ROOT_FLUID.get(), 0);

	/**
	 * @param container The container itemStack, data is stored on it directly as NBT.
	 * @param capacity  The maximum capacity of this fluid tank.
	 */
	public LiquidRootHandler(@Nonnull ItemStack container, int capacity){
		super(container, capacity);

		if(getFluidStack() == null)
		{
			setContainerToEmpty();
		}
	}

	@Override
	protected void setContainerToEmpty(){
		setFluidStack(EMPTY.copy());
		if(container.getTag() != null)
		{
			container.getTag().remove(FLUID_NBT_KEY);
		}
	}

	private FluidStack getFluidStack()
	{
		return getFluid();
	}

	@Override
	public boolean canFillFluidType(FluidStack fluid)
	{
		return (fluid.getFluid() == ModFluids.ROOT_FLUID.get());
	}

	public void setFluidStack(FluidStack parFluidStack)
	{
		setFluid(parFluidStack);
	}
}
