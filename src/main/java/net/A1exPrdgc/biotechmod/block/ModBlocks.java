package net.A1exPrdgc.biotechmod.block;

import net.A1exPrdgc.biotechmod.BiotechMod;
import net.A1exPrdgc.biotechmod.block.custom.Extractor;
import net.A1exPrdgc.biotechmod.block.custom.Squeezer;
import net.A1exPrdgc.biotechmod.item.ModItemGroup;
import net.A1exPrdgc.biotechmod.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks
{



	public static final DeferredRegister<Block> BLOCKS
			= DeferredRegister.create(ForgeRegistries.BLOCKS, BiotechMod.MOD_ID);

	public static final RegistryObject<Block> ROOT_BLOCK = registerBlock("root_block",
			() -> new Block(AbstractBlock.Properties.create(Material.WOOD)
					.harvestLevel(1).harvestTool(ToolType.AXE).setRequiresTool().hardnessAndResistance(3f)));

	public static final RegistryObject<Block> ASH_BLOCK = registerBlock("ash_block",
			() -> new Block(AbstractBlock.Properties.create(Material.SAND)
					.harvestLevel(0).harvestTool(ToolType.SHOVEL).hardnessAndResistance(0.5F).sound(SoundType.SAND)));

	public static final RegistryObject<Block> EXTRACTOR = registerBlock("extractor",
			() -> new Extractor(AbstractBlock.Properties.create(Material.IRON)
					.harvestLevel(0).notSolid().hardnessAndResistance(0.4F).harvestTool(ToolType.PICKAXE).sound(SoundType.STONE)));

	public static final RegistryObject<Block> SQUEEZER = registerBlock("squeezer",
			() -> new Squeezer(AbstractBlock.Properties.create(Material.IRON)
					.harvestLevel(0).notSolid().hardnessAndResistance(0.4F).harvestTool(ToolType.PICKAXE).sound(SoundType.STONE)));

	private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block)
	{
		RegistryObject<T> toReturn = BLOCKS.register(name, block);

		registerBlockItem(name, toReturn);

		return toReturn;
	}

	private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block)
	{
		ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
				new Item.Properties().group(ModItemGroup.BIOTECHMOD_GROUP)));
	}

	public static void register(IEventBus eventBus)
	{
		BLOCKS.register(eventBus);
	}
}
