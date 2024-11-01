package net.A1exPrdgc.biotechmod.tileentity;

import net.A1exPrdgc.biotechmod.BiotechMod;
import net.A1exPrdgc.biotechmod.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities
{
	public  static DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
			DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BiotechMod.MOD_ID);

	public static RegistryObject<TileEntityType<SqueezerTile>> SQUEEZER =
			TILE_ENTITIES.register("squeezer_tile", () -> TileEntityType.Builder.create(
					SqueezerTile::new, ModBlocks.SQUEEZER.get()).build(null)
			);

	public static RegistryObject<TileEntityType<ExtractorTile>> EXTRACTOR =
			TILE_ENTITIES.register("extractor_tile", () -> TileEntityType.Builder.create(
					ExtractorTile::new, ModBlocks.EXTRACTOR.get()).build(null)
			);

	public static void register(IEventBus eventbus)
	{
		TILE_ENTITIES.register(eventbus);
	}
}
