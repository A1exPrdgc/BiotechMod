package net.A1exPrdgc.biotechmod.container;

import net.A1exPrdgc.biotechmod.BiotechMod;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers
{
	public static DeferredRegister<ContainerType<?>> CONTAINERS
			= DeferredRegister.create(ForgeRegistries.CONTAINERS, BiotechMod.MOD_ID);

	public static final RegistryObject<ContainerType<SqueezerContainer>> SQUEEZER_CONTAINER
			= CONTAINERS.register("squeezer_container",
			() -> IForgeContainerType.create(((windowId, inv, data) -> {
				BlockPos pos = data.readBlockPos();
				World world = inv.player.getEntityWorld();
				return new SqueezerContainer(windowId, world, pos, inv, inv.player);
			})));

	public static void register(IEventBus eventbus)
	{
		CONTAINERS.register(eventbus);
	}
}
