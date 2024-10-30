package net.A1exPrdgc.biotechmod.item.custom;

import net.A1exPrdgc.biotechmod.BiotechMod;
import net.A1exPrdgc.biotechmod.block.ModBlocks;
import net.A1exPrdgc.biotechmod.fluid.ModFluids;
import net.A1exPrdgc.biotechmod.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;

import java.util.Objects;

public class Syringe extends Item
{
	public Syringe(Properties properties){
		super(properties);
	}

	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context){
		World world = context.getWorld();

		if (!world.isRemote())
		{
			PlayerEntity playerEntity =Objects.requireNonNull(context.getPlayer());
			BlockState clickedBlock = world.getBlockState(context.getPos());

			System.out.println("(" + stack.getMaxDamage() + " - " + stack.getDamage() + ") + 1 = " + ((stack.getMaxDamage() - stack.getDamage()) + 1));
			stack.setDamage((stack.getMaxDamage() - stack.getDamage()) + 1);

			RightClickOnCertainBlockState(clickedBlock, context, playerEntity);
		}


		return super.onItemUseFirst(stack, context);
	}

	private void RightClickOnCertainBlockState(BlockState clickedBlock, ItemUseContext context, PlayerEntity playerEntity)
	{
		if(blockIsValidForResistance(clickedBlock))
		{

		}
	}


	private boolean blockIsValidForResistance(BlockState clickedBlock)
	{
		return  clickedBlock.getBlock() == ModBlocks.ROOT_BLOCK.get() ||
				clickedBlock.getFluidState().getFluid() == ModFluids.ROOT_FLUID.get();
	}

}
