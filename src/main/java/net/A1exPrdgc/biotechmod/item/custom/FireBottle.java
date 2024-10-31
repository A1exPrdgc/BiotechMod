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

			RightClickOnCertainBlockState(clickedBlock, context, playerEntity);}


		return super.onItemUseFirst(stack, context);
	}

	private void RightClickOnCertainBlockState(BlockState clickedBlock, ItemUseContext context, PlayerEntity playerEntity)
	{
		boolean playerIsNotOnFire = !playerEntity.isBurning();

		if (playerIsNotOnFire && blockIsValidForResistance(clickedBlock))
		{
			//transforme le bois en cendre ou en charbon
			changeBlock(context);
			animateTick(context);
			playSound(context, playerEntity);


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

	private void changeBlock(ItemUseContext context)
	{
		World world = context.getWorld();
		BlockPos pos = context.getPos();


		world.removeBlock(pos, true);

		if(random.nextFloat() > 0.8f)
			world.setBlockState(pos, Blocks.COAL_BLOCK.getDefaultState());
		else
			world.setBlockState(pos, ModBlocks.ASH_BLOCK.get().getDefaultState());

	}


	private void animateTick(ItemUseContext context)
	{
		World world  = context.getWorld();
		BlockPos pos = context.getPos();

		for (int i=0; i < 360; i++)
		{
			if (i % 20 == 0)
			{
				world.addParticle(ParticleTypes.ASH,
						pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
						Math.cos(i) * 0.25, 0.15, Math.sin(i) * 0.25);
			}
		}
	}

	public void playSound(ItemUseContext context, PlayerEntity player)
	{
		context.getWorld().playSound(player, context.getPos(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS ,1.0F, random.nextFloat() * 0.4F + 0.8F);
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
