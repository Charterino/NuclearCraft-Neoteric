package igentuman.nc.setup.processors;

import igentuman.nc.block.entity.processor.NCProcessorBE;
import igentuman.nc.container.NCProcessorContainer;
import igentuman.nc.client.gui.processor.NCProcessorScreen;
import igentuman.nc.recipes.NcRecipe;
import igentuman.nc.setup.processors.config.ProcessorSlots;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.function.Supplier;

public class ProcessorBuilder <M extends NCProcessorContainer, U extends Screen & MenuAccess<M>>{
    public ProcessorPrefab processor;
    private ProcessorBuilder() { }

    public static ProcessorBuilder make(String name)
    {
        ProcessorBuilder builder = new ProcessorBuilder();
        builder.processor = new ProcessorPrefab(name);
        return builder;
    }

    public static ProcessorBuilder make(String name, int inFluids, int inItems, int outFluids, int outItems)
    {
        ProcessorBuilder builder = new ProcessorBuilder();
        builder.processor = new ProcessorPrefab(name, inFluids, inItems, outFluids, outItems);
        builder.container(NCProcessorContainer.class);
        if(FMLEnvironment.dist.isClient()){
            builder.screen(NCProcessorScreen::new);
        }
        return builder;
    }

    public ProcessorBuilder<M, U> container(Class container) {
        processor.setContainer(container);
        return this;
    }

    public ProcessorBuilder blockEntity(BlockEntityType.BlockEntitySupplier<? extends NCProcessorBE> be)
    {
        processor.setBlockEntity(be);
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    public ProcessorBuilder screen(MenuScreens.ScreenConstructor<M, U> screenConstructor)
    {
        processor.setScreenConstructor(screenConstructor);
        return this;
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public ProcessorBuilder<M, U> screen(Object screenConstructor)
    {
        return this;
    }

    public ProcessorPrefab<M, U> build()
    {
        return processor;
    }


    public ProcessorBuilder<M, U> slotsConfig(ProcessorSlots config)
    {
        processor.slotsConfig = config;
        return this;
    }

    public ProcessorBuilder<M, U> progressBar(int i) {
        processor.progressBar = i;
        return this;
    }

    public ProcessorBuilder<M, U> recipeSerializer(Supplier<RecipeSerializer<? extends NcRecipe>> sup) {
        processor.recipeSerializerSupplier = sup;
        return this;
    }
}
