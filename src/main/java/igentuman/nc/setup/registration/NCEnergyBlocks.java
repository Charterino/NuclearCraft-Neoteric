package igentuman.nc.setup.registration;

import igentuman.nc.block.BatteryBlock;
import igentuman.nc.block.DecayGeneratorBlock;
import igentuman.nc.block.RTGBlock;
import igentuman.nc.block.SolarPanelBlock;
import igentuman.nc.block.entity.energy.NCEnergy;
import igentuman.nc.item.BatteryBlockItem;
import igentuman.nc.content.energy.BatteryBlocks;
import igentuman.nc.content.energy.RTGs;
import igentuman.nc.content.energy.SolarPanels;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;
import igentuman.nc.block.entity.energy.DecayGeneratorBE;

import java.util.HashMap;

import static igentuman.nc.setup.registration.Registries.*;

public class NCEnergyBlocks {
    public static HashMap<String, RegistryObject<Block>> ENERGY_BLOCKS = new HashMap<>();
    public static HashMap<String, RegistryObject<Item>> BLOCK_ITEMS = new HashMap<>();
    public static final Item.Properties ENERGY_ITEM_PROPERTIES = new Item.Properties();
    public static final BlockBehaviour.Properties ENERGY_BLOCK_PROPERTIES = BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(2f).requiresCorrectToolForDrops();
    public static HashMap<String, RegistryObject<BlockEntityType<? extends NCEnergy>>> ENERGY_BE = new HashMap<>();

    public static void init() {
        registerBlocks();
        registerBlockEntities();
    }

    private static void registerBlockEntities() {
        for(String name: SolarPanels.all().keySet()) {
            String key = "solar_panel/"+name;
            ENERGY_BE.put(key, BLOCK_ENTITIES.register(key,
                    () -> BlockEntityType.Builder
                            .of(SolarPanels.all().get(name).getBlockEntity(), ENERGY_BLOCKS.get(key).get())
                            .build(null)));
        }
        for(String name: BatteryBlocks.all().keySet()) {
            ENERGY_BE.put(name, BLOCK_ENTITIES.register(name,
                    () -> BlockEntityType.Builder
                            .of(BatteryBlocks.all().get(name).getBlockEntity(), ENERGY_BLOCKS.get(name).get())
                            .build(null)));

        }
        for(String name: RTGs.all().keySet()) {
            ENERGY_BE.put(name, BLOCK_ENTITIES.register(name,
                    () -> BlockEntityType.Builder
                            .of(RTGs.all().get(name).getBlockEntity(), ENERGY_BLOCKS.get(name).get())
                            .build(null)));

        }
        ENERGY_BE.put("decay_generator", BLOCK_ENTITIES.register("decay_generator",
                () -> BlockEntityType.Builder
                        .of(DecayGeneratorBE::new, ENERGY_BLOCKS.get("decay_generator").get())
                        .build(null)));
    }

    private static void registerBlocks() {
        ENERGY_BLOCKS.put("decay_generator", BLOCKS.register("decay_generator", () -> new DecayGeneratorBlock(ENERGY_BLOCK_PROPERTIES)));
        BLOCK_ITEMS.put("decay_generator", fromBlock(ENERGY_BLOCKS.get("decay_generator")));

        for(String name: SolarPanels.all().keySet()) {
            String key = "solar_panel/"+name;
            ENERGY_BLOCKS.put(key, BLOCKS.register(key.replace("/","_"), () -> new SolarPanelBlock(ENERGY_BLOCK_PROPERTIES)));
            BLOCK_ITEMS.put(key, fromBlock(ENERGY_BLOCKS.get(key)));
        }

        for(String name: BatteryBlocks.all().keySet()) {
            ENERGY_BLOCKS.put(name, BLOCKS.register(name, () -> new BatteryBlock(ENERGY_BLOCK_PROPERTIES)));
            BLOCK_ITEMS.put(name, fromBatteryBlock(ENERGY_BLOCKS.get(name)));
        }

        for(String name: RTGs.all().keySet()) {
            ENERGY_BLOCKS.put(name, BLOCKS.register(name, () -> new RTGBlock(ENERGY_BLOCK_PROPERTIES)));
            BLOCK_ITEMS.put(name, fromBlock(ENERGY_BLOCKS.get(name)));
        }
    }

    public static <B extends Block> RegistryObject<Item> fromBatteryBlock(RegistryObject<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BatteryBlockItem(block.get(), ENERGY_ITEM_PROPERTIES));
    }

    public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), ENERGY_ITEM_PROPERTIES));
    }

}
