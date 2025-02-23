package igentuman.nc;

import igentuman.nc.handler.command.CommandNcPatrons;
import igentuman.nc.handler.command.NCRadiationCommand;
import igentuman.nc.handler.command.StructureCommand;
import igentuman.nc.handler.config.*;
import igentuman.nc.handler.event.server.WorldEvents;
import igentuman.nc.handler.command.CommandNcVeinCheck;
import igentuman.nc.radiation.data.PlayerRadiation;
import igentuman.nc.radiation.data.RadiationEvents;
import igentuman.nc.radiation.data.RadiationManager;
import igentuman.nc.radiation.data.WorldRadiation;
import igentuman.nc.network.PacketHandler;
import igentuman.nc.setup.ClientSetup;
import igentuman.nc.setup.ModSetup;
import igentuman.nc.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.GameShuttingDownEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static igentuman.nc.util.FileExtractor.preFetchProcessorsConfig;
import static igentuman.nc.util.FileExtractor.unpackFilesFromFolderToConfig;

@Mod(NuclearCraft.MODID)
public class NuclearCraft {

    public static final Logger LOGGER = LogManager.getLogger();
    public boolean isNcBeStopped = false;
    public static final WorldEvents worldTickHandler = new WorldEvents();
    public static final String MODID = "nuclearcraft";
    public static NuclearCraft instance;
    private final PacketHandler packetHandler;

    public static void registerConfigs()
    {
        preFetchProcessorsConfig();
        unpackFilesFromFolderToConfig("data/nuclearcraft/fission_fuel", "NuclearCraft/fission_fuel");
        unpackFilesFromFolderToConfig("data/nuclearcraft/heat_sinks", "NuclearCraft/heat_sinks");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MaterialsConfig.spec, "NuclearCraft/materials.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, OreGenConfig.spec, "NuclearCraft/ore_generation.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.spec, "NuclearCraft/common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ProcessorsConfig.spec, "NuclearCraft/processors.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FissionConfig.spec, "NuclearCraft/fission.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FusionConfig.spec, "NuclearCraft/fusion.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TurbineConfig.spec, "NuclearCraft/turbine.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RadiationConfig.spec, "NuclearCraft/radiation.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WorldConfig.spec, "NuclearCraft/world.toml");
    }



    public NuclearCraft() {
        instance = this;
        registerConfigs();
        packetHandler = new PacketHandler();
        //forceLoadConfig();
        MinecraftForge.EVENT_BUS.addListener(this::serverStopped);
        MinecraftForge.EVENT_BUS.addListener(this::serverStarted);
        MinecraftForge.EVENT_BUS.addListener(this::gameShuttingDownEvent);
        ModSetup.setup();
        Registration.init();
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        modbus.addListener(ModSetup::init);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modbus.addListener(ClientSetup::init));
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modbus.addListener(this::registerClientEventHandlers));
    }

    public static PacketHandler packetHandler() {
        return instance.packetHandler;
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.COMMON)
            CommonConfig.setLoaded();
    }

    private void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(CommandNcVeinCheck.register());
        event.getDispatcher().register(CommandNcPatrons.register());
        StructureCommand.register(event.getDispatcher());
        NCRadiationCommand.register(event.getDispatcher());
    }

    private void registerClientEventHandlers(FMLClientSetupEvent event) {
        ClientSetup.registerEventHandlers(event);
    }

    public static ResourceLocation rl(String path)
    {
        return new ResourceLocation(MODID, path);
    }

    private void serverStopped(ServerStoppedEvent event) {
        NuclearCraft.instance.isNcBeStopped = true;
        //stop capability tracking
        RadiationEvents.stopTracking();
        for(ServerLevel level: event.getServer().getAllLevels()) {
            RadiationManager.clear(level);
        }
    }
    private void gameShuttingDownEvent(GameShuttingDownEvent event) {
        NuclearCraft.instance.isNcBeStopped = true;
    }

    private void serverStarted(ServerStartedEvent event) {
        NuclearCraft.instance.isNcBeStopped = false;
        RadiationEvents.startTracking();
    }

    @SubscribeEvent
    public void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(WorldRadiation.class);
        event.register(PlayerRadiation.class);
    }
}
