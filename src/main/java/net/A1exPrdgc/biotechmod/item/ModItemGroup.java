package net.A1exPrdgc.biotechmod.item;

import net.A1exPrdgc.biotechmod.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ModItemGroup
{
	public static final ItemGroup BIOTECHMOD_GROUP = new ItemGroup("biotechModTab")
	{
		@Nonnull
		public ItemStack createIcon(){
			return new ItemStack(ModBlocks.EXTRACTOR.get());
		}
	};
}
