package net.A1exPrdgc.biotechmod.util;

import net.A1exPrdgc.biotechmod.container.ExtractorContainer;
import net.A1exPrdgc.biotechmod.container.SqueezerContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;

public class UpdateContainerDataPacket {
	private final int windowId;
	private final int index;
	private final int value;

	public UpdateContainerDataPacket(int windowId, int index, int value) {
		this.windowId = windowId;
		this.index = index;
		this.value = value;
	}

	// Encodage des données du paquet
	public static void encode(UpdateContainerDataPacket packet, PacketBuffer buffer) {
		buffer.writeInt(packet.windowId);
		buffer.writeInt(packet.index);
		buffer.writeInt(packet.value);
	}

	// Décodage des données du paquet
	public static UpdateContainerDataPacket decode(PacketBuffer buffer) {
		return new UpdateContainerDataPacket(buffer.readInt(), buffer.readInt(), buffer.readInt());
	}

	// Gestion du paquet côté client
	public static void handle(UpdateContainerDataPacket packet, Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(() -> {
			// Récupération du conteneur ouvert côté client
			Container container = Minecraft.getInstance().player.openContainer;

			// Vérification du type de conteneur et mise à jour des données
			if (container.windowId == packet.windowId && container instanceof ExtractorContainer) {
				((ExtractorContainer) container).setData(packet.index, packet.value);
			}
		});
		context.get().setPacketHandled(true);
	}
}

