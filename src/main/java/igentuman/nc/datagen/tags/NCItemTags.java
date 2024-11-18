package igentuman.nc.datagen.tags;

import igentuman.nc.multiblock.fission.FissionBlocks;
import igentuman.nc.multiblock.fission.FissionReactor;
import igentuman.nc.multiblock.fusion.FusionReactor;
import igentuman.nc.setup.registration.FissionFuel;
import igentuman.nc.setup.registration.NCItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.List;

import static igentuman.nc.NuclearCraft.MODID;
import static igentuman.nc.setup.registration.NCItems.*;
import static igentuman.nc.setup.registration.Tags.*;

public class NCItemTags extends ItemTagsProvider {

    public NCItemTags(DataGenerator generator, BlockTagsProvider blockTags, GatherDataEvent event) {
        super(generator.getPackOutput(), event.getLookupProvider(), blockTags.contentsGetter(),  MODID, event.getExistingFileHelper());
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        ores();
        blocks();
        ingots();
        chunks();
        dusts();
        plates();
        nuggets();
        gems();
        parts();
        fuel();
        isotopes();
        tag(FusionReactor.CASING_ITEMS).add(
                FusionReactor.FUSION_ITEMS.get("fusion_reactor_casing").get(),
                FusionReactor.FUSION_ITEMS.get("fusion_reactor_casing_glass").get());
        tag(FissionBlocks.MODERATORS_ITEMS).add(NC_BLOCKS_ITEMS.get("graphite").get(), NC_BLOCKS_ITEMS.get("beryllium").get());
        tag(FissionBlocks.CASING_ITEMS).add(
                FissionReactor.FISSION_BLOCK_ITEMS.get("fission_reactor_casing").get(),
                FissionReactor.FISSION_BLOCK_ITEMS.get("fission_reactor_controller").get(),
                FissionReactor.FISSION_BLOCK_ITEMS.get("fission_reactor_glass").get(),
                FissionReactor.FISSION_BLOCK_ITEMS.get("fission_reactor_port").get()
        );
    }

    private void isotopes() {
        for(String name: FissionFuel.NC_ISOTOPES.keySet()) {
            tag(ISOTOPE_TAG).add(FissionFuel.NC_ISOTOPES.get(name).get());
            //tag(PLATES_TAG.get(name)).add(NCItems.NC_PLATES.get(name).get());
        }
    }

    private void fuel() {
        for(List<String> name: FissionFuel.NC_FUEL.keySet()) {
            tag(NC_FUEL_TAG).add(FissionFuel.NC_FUEL.get(name).get());
            //tag(PLATES_TAG.get(name)).add(NCItems.NC_PLATES.get(name).get());
        }

        for(List<String> name: FissionFuel.NC_DEPLETED_FUEL.keySet()) {
            tag(NC_DEPLETED_FUEL_TAG).add(FissionFuel.NC_DEPLETED_FUEL.get(name).get());
        }
    }

    private void parts() {
        for(String name: NCItems.NC_PARTS.keySet()) {
            tag(PARTS_TAG).add(NCItems.NC_PARTS.get(name).get());
        }
    }

    private void gems() {
        for(String name: NC_GEMS.keySet()) {
            tag(Tags.Items.GEMS).add(NC_GEMS.get(name).get());
            tag(GEMS_TAG.get(name)).add(NC_GEMS.get(name).get());
        }
    }

    private void ingots() {
        for(String name: NC_INGOTS.keySet()) {
            tag(Tags.Items.INGOTS).add(NC_INGOTS.get(name).get());
            tag(INGOTS_TAG.get(name)).add(NC_INGOTS.get(name).get());
        }
    }

    private void nuggets() {
        for(String name: NC_NUGGETS.keySet()) {
            tag(Tags.Items.NUGGETS).add(NC_NUGGETS.get(name).get());
            tag(NUGGETS_TAG.get(name)).add(NC_NUGGETS.get(name).get());
        }
    }

    private void plates() {
        for(String name: NCItems.NC_PLATES.keySet()) {
            tag(PLATE_TAG).add(NCItems.NC_PLATES.get(name).get());
            tag(PLATES_TAG.get(name)).add(NCItems.NC_PLATES.get(name).get());
        }
    }

    private void dusts() {
        for(String name: NC_DUSTS.keySet()) {
            tag(Tags.Items.DUSTS).add(NC_DUSTS.get(name).get());
            tag(DUSTS_TAG.get(name)).add(NC_DUSTS.get(name).get());
        }
    }

    private void chunks() {
        for(String name: NC_CHUNKS.keySet()) {
            tag(Tags.Items.RAW_MATERIALS).add(NC_CHUNKS.get(name).get());
            tag(CHUNKS_TAG.get(name)).add(NC_CHUNKS.get(name).get());
        }
    }

    private void ores() {
        for(String ore: ORE_BLOCK_ITEMS.keySet()) {
            tag(Tags.Items.ORES).add(ORE_BLOCK_ITEMS.get(ore).get());
            tag(ORE_ITEM_TAGS.get(ore.replaceAll("_deepslate|_end|_nether", ""))).add(ORE_BLOCK_ITEMS.get(ore).get());
        }
    }

    private void blocks() {
        for(String name: NC_BLOCKS_ITEMS.keySet()) {
            tag(Tags.Items.STORAGE_BLOCKS).add(NC_BLOCKS_ITEMS.get(name).get());
            if(BLOCK_ITEM_TAGS.get(name) != null) {
                tag(BLOCK_ITEM_TAGS.get(name)).add(NC_BLOCKS_ITEMS.get(name).get());
            }
        }
    }

    @Override
    public String getName() {
        return "NuclearCraft Item Tags";
    }
}