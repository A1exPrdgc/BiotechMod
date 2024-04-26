package net.A1exPrdgc.biotechmod.item.custom;


import net.A1exPrdgc.biotechmod.block.ModBlocks;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class FireBottle extends Item
{
	public FireBottle(Properties properties)
	{
		super(properties);

	}

	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context){
		World world = context.getWorld();

		if (!world.isRemote)
		{
			PlayerEntity playerEntity =Objects.requireNonNull(context.getPlayer());
			BlockState clickedBlock = world.getBlockState(context.getPos());

			RightClickOnCertainBlockState(clickedBlock, context, playerEntity);
			stack.damageItem(1, playerEntity, player -> player.sendBreakAnimation(context.getHand()));
		}


		return super.onItemUseFirst(stack, context);
	}

	private void RightClickOnCertainBlockState(BlockState clickedBlock, ItemUseContext context, PlayerEntity playerEntity)
	{
		boolean playerIsNotOnFire = !playerEntity.isBurning();

		if (playerIsNotOnFire && blockIsValidForResistance(clickedBlock))
		{
			//transforme le bois en cendre ou en charbon
			changeBlock(playerEntity, context.getWorld(), context.getPos());
		}
		else
		{
			//allume un feu
			lightEntityOnFire(playerEntity, 8);
			lightGroundOnFire(context);
		}

	}

	private boolean blockIsValidForResistance(BlockState clickedBlock)
	{
		return  clickedBlock.getBlock() == Blocks.OAK_LOG ||
				clickedBlock.getBlock() == Blocks.OAK_WOOD ||
				clickedBlock.getBlock() == Blocks.STRIPPED_OAK_LOG ||
				clickedBlock.getBlock() == Blocks.SPRUCE_LOG ||
				clickedBlock.getBlock() == Blocks.SPRUCE_WOOD ||
				clickedBlock.getBlock() == Blocks.STRIPPED_SPRUCE_LOG;
	}

	public static void lightEntityOnFire(Entity entity, int second)
	{
		entity.setFire(second);
	}

	private void changeBlock(PlayerEntity playerEntity, World world, BlockPos pos)
	{
		if(random.nextFloat() > 0.8f)
		{
			world.removeBlock(pos, true);
			world.setBlockState(pos, Blocks.COAL_BLOCK.getDefaultState());
			world.addParticle(ParticleTypes.ASH, pos.getX(), pos.getY(), pos.getZ(), 5, 5, 5);
		}
		else
		{
			world.removeBlock(pos, true);
			world.setBlockState(pos, ModBlocks.ASH_BLOCK.get().getDefaultState());
			world.addParticle(ParticleTypes.ASH, pos.getX(), pos.getY(), pos.getZ(), 5, 5, 5);
		}
	}

	public static void lightGroundOnFire(ItemUseContext context)
	{

		PlayerEntity playerentity = context.getPlayer();
		World world = context.getWorld();
		BlockPos blockpos = context.getPos().offset(context.getFace());

		if (AbstractFireBlock.canLightBlock(world, blockpos, context.getPlacementHorizontalFacing())) {
			world.playSound(playerentity, blockpos, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.BLOCKS, 1.0F,
					random.nextFloat() * 0.4F + 0.8F);

			BlockState blockstate = AbstractFireBlock.getFireForPlacement(world, blockpos);
			world.setBlockState(blockpos, blockstate, 11);
		}

	}
}