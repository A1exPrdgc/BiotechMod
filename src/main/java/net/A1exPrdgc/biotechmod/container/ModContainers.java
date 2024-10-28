package net.A1exPrdgc.biotechmod.container;

import net.A1exPrdgc.biotechmod.BiotechMod;
import net.A1exPrdgc.biotechmod.tileentity.SqueezerTile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntArray;
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

	/*public static final RegistryObject<ContainerType<SqueezerContainer>> SQUEEZER_CONTAINER
			= CONTAINERS.register("squeezer_container",
			() -> IForgeContainerType.create(((windowId, inv, data) -> {
				System.out.println("whot 2");
				return new SqueezerContainer(windowId, Minecraft.getInstance().world, data.readBlockPos(), inv,  new IntArray(2));
			})));*/

	public static final RegistryObject<ContainerType<SqueezerContainer>> SQUEEZER_CONTAINER = CONTAINERS.register("squeezer_container", () -> {
		return IForgeContainerType.create((int windowId, PlayerInventory inv, PacketBuffer data) -> new SqueezerContainer(windowId, Minecraft.getInstance().world, data.readBlockPos(), inv, inv.player));
	});



	public static void register(IEventBus eventbus)
	{
		CONTAINERS.register(eventbus);
	}
}
