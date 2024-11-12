package net.A1exPrdgc.biotechmod.util;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;

public class Util
{
	public static float[] intToColor4f(int c)
	{
		float[] res = new float[4];
		Color col = new Color(c);

		res[0] = (float) col.getRed() / 100;
		res[1] = (float) col.getGreen() / 100;
		res[2] = (float) col.getBlue() / 100;
		res[3] = col.getAlpha();

		return res;

	}

	public static ResourceLocation getFluidTexture(FluidStack flu) {
		//FluidStack fluidStack = this.tileEntity.getTank().getFluid();

		if (!flu.isEmpty()) {
			return flu.getFluid().getAttributes().getStillTexture();
		}
		return null;
	}

	public static int getFluidColor(FluidStack flu)
	{
		//FluidStack fluidstack = this.tileEntity.getTank().getFluid();

		if(!flu.isEmpty())
		{
			return flu.getFluid().getAttributes().getColor();
		}
		return 0;
	}
}
