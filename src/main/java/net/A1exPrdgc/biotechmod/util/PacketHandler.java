package net.A1exPrdgc.biotechmod.util;

import net.A1exPrdgc.biotechmod.BiotechMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(BiotechMod.MOD_ID, "sync"),
			() -> "1.0", s -> true, s -> true);

	public static void register() {
		int id = 0;
		INSTANCE.registerMessage(id++, UpdateContainerDataPacket.class,
				UpdateContainerDataPacket::encode,
				UpdateContainerDataPacket::decode,
				UpdateContainerDataPacket::handle
		);
	}
}

