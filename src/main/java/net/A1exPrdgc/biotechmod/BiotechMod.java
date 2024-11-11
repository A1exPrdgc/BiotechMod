package net.A1exPrdgc.biotechmod;

import net.A1exPrdgc.biotechmod.block.ModBlocks;
import net.A1exPrdgc.biotechmod.container.ModContainers;
import net.A1exPrdgc.biotechmod.container.SqueezerContainer;
import net.A1exPrdgc.biotechmod.energy.BioEnergizedFlux;
import net.A1exPrdgc.biotechmod.energy.BioEnergizedFluxStorage;
import net.A1exPrdgc.biotechmod.energy.IBioEnergizedFlux;
import net.A1exPrdgc.biotechmod.fluid.ModFluids;
import net.A1exPrdgc.biotechmod.item.ModItems;
import net.A1exPrdgc.biotechmod.screen.ExtractorScreen;
import net.A1exPrdgc.biotechmod.screen.SqueezerScreen;
import net.A1exPrdgc.biotechmod.tileentity.ModTileEntities;
import net.A1exPrdgc.biotechmod.util.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BiotechMod.MOD_ID)
public class BiotechMod
{
    // Directly reference a log4j logger.
    public static final String MOD_ID = "biotechmod";
    private static final Logger LOGGER = LogManager.getLogger();

    public BiotechMod() {
        // Register the setup method for modloading
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModTileEntities.register(eventBus);
        ModContainers.register(eventBus);
        ModFluids.register(eventBus);

        //PacketHandler.register();


        eventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        eventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        eventBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        eventBus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }



    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(ModBlocks.EXTRACTOR.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.SQUEEZER.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.REFINERY.get(), RenderType.getCutout());

            ScreenManager.registerFactory(ModContainers.SQUEEZER_CONTAINER.get(), SqueezerScreen::new);
            ScreenManager.registerFactory(ModContainers.EXTRACTOR_CONTAINER.get(), ExtractorScreen::new);

            CapabilityManager.INSTANCE.register(IBioEnergizedFlux.class, new BioEnergizedFluxStorage(), BioEnergizedFlux::new);
        });
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo(BiotechMod.MOD_ID, "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
