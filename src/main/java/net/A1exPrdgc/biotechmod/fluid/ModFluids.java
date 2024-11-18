package net.A1exPrdgc.biotechmod.fluid;

import net.A1exPrdgc.biotechmod.BiotechMod;
import net.A1exPrdgc.biotechmod.block.ModBlocks;
import net.A1exPrdgc.biotechmod.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFluids {
	public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
	public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
	public static final ResourceLocation WATER_OVERLAY_RL = new ResourceLocation("block/water_overlay");

	public static final DeferredRegister<Fluid> FLUIDS
			= DeferredRegister.create(ForgeRegistries.FLUIDS, BiotechMod.MOD_ID);

	public static final RegistryObject<FlowingFluid> ROOT_FLUID
			= FLUIDS.register("root_fluid", () -> new ForgeFlowingFluid.Source(ModFluids.ROOT_PROPERTIES));

	public static final RegistryObject<FlowingFluid> ROOT_FLOWING
			= FLUIDS.register("root_flowing", () -> new ForgeFlowingFluid.Flowing(ModFluids.ROOT_PROPERTIES));
	public static final ForgeFlowingFluid.Properties ROOT_PROPERTIES = new ForgeFlowingFluid.Properties(
			() -> ROOT_FLUID.get(), () -> ROOT_FLOWING.get(), FluidAttributes.builder(WATER_STILL_RL, WATER_FLOWING_RL)
			.density(50).luminosity(10).viscosity(5).sound(SoundEvents.ITEM_BUCKET_EMPTY).overlay(WATER_OVERLAY_RL)
			.color(0x00f621)).slopeFindDistance(5).levelDecreasePerBlock(2)
			.block(() -> ModFluids.LIQUID_ROOT_BLOCK.get()).bucket(() -> ModItems.ROOT_BUCKET.get());

	public static final RegistryObject<FlowingFluidBlock> LIQUID_ROOT_BLOCK = ModBlocks.BLOCKS.register("root",
			() -> new FlowingFluidBlock(() -> ModFluids.ROOT_FLUID.get(), AbstractBlock.Properties.create(Material.WATER)
					.doesNotBlockMovement().hardnessAndResistance(100f).noDrops()));

	public static final RegistryObject<FlowingFluid> RESIN_FLUID
			= FLUIDS.register("resin_fluid", () -> new ForgeFlowingFluid.Source(ModFluids.RESIN_PROPERTIES));

	public static final RegistryObject<FlowingFluid> RESIN_FLOWING
			= FLUIDS.register("resin_flowing", () -> new ForgeFlowingFluid.Flowing(ModFluids.RESIN_PROPERTIES));
	public static final ForgeFlowingFluid.Properties RESIN_PROPERTIES = new ForgeFlowingFluid.Properties(
			() -> RESIN_FLUID.get(), () -> RESIN_FLOWING.get(), FluidAttributes.builder(WATER_STILL_RL, WATER_FLOWING_RL)
			.density(100).luminosity(0).viscosity(50).sound(SoundEvents.BLOCK_HONEY_BLOCK_BREAK).overlay(WATER_OVERLAY_RL)
			.color(0x634a04)).slopeFindDistance(0).levelDecreasePerBlock(5)
			.block(() -> ModFluids.LIQUID_RESIN_BLOCK.get()).bucket(() -> ModItems.RESIN_BUCKET.get());

	public static final RegistryObject<FlowingFluidBlock> LIQUID_RESIN_BLOCK = ModBlocks.BLOCKS.register("resin",
			() -> new FlowingFluidBlock(() -> ModFluids.RESIN_FLUID.get(), AbstractBlock.Properties.create(Material.WATER)
					.doesNotBlockMovement().hardnessAndResistance(100f).noDrops()));

	public static final RegistryObject<FlowingFluid> BIO_ENERGIZED_FLUX_FLUID
			= FLUIDS.register("bioenergizedflux_fluid", () -> new ForgeFlowingFluid.Source(ModFluids.BIO_ENERGIZED_FLUX_PROPERTIES));

	public static final RegistryObject<FlowingFluid> BIO_ENERGIZED_FLUX_FLOWING
			= FLUIDS.register("bioenergizedflux_flowing", () -> new ForgeFlowingFluid.Flowing(ModFluids.BIO_ENERGIZED_FLUX_PROPERTIES));
	public static final ForgeFlowingFluid.Properties BIO_ENERGIZED_FLUX_PROPERTIES = new ForgeFlowingFluid.Properties(
			() -> BIO_ENERGIZED_FLUX_FLUID.get(), () -> BIO_ENERGIZED_FLUX_FLOWING.get(), FluidAttributes.builder(WATER_STILL_RL, WATER_FLOWING_RL)
			.density(100).luminosity(0).viscosity(5).sound(SoundEvents.BLOCK_HONEY_BLOCK_BREAK).overlay(WATER_OVERLAY_RL)
			.color(0xaeff0c)).slopeFindDistance(5).levelDecreasePerBlock(2)
			.block(() -> ModFluids.LIQUID_BIO_ENERGIZED_FLUX_BLOCK.get()).bucket(() -> ModItems.BIO_ENERGIZED_FLUX_BUCKET.get());

	public static final RegistryObject<FlowingFluidBlock> LIQUID_BIO_ENERGIZED_FLUX_BLOCK = ModBlocks.BLOCKS.register("bioenergizedflux",
			() -> new FlowingFluidBlock(() -> ModFluids.BIO_ENERGIZED_FLUX_FLUID.get(), AbstractBlock.Properties.create(Material.WATER)
					.doesNotBlockMovement().hardnessAndResistance(100f).noDrops()));



	public static void register(IEventBus eventBus) {
		FLUIDS.register(eventBus);
	}
}
