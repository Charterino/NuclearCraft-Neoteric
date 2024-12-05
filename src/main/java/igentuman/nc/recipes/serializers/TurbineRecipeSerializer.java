package igentuman.nc.recipes.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import igentuman.nc.NuclearCraft;
import igentuman.nc.recipes.ingredient.FluidStackIngredient;
import igentuman.nc.recipes.ingredient.ItemStackIngredient;
import igentuman.nc.recipes.ingredient.creator.IngredientCreatorAccess;
import igentuman.nc.recipes.type.NcRecipe;
import igentuman.nc.util.SerializerHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class TurbineRecipeSerializer<RECIPE extends NcRecipe> extends NcRecipeSerializer<RECIPE> {

    public TurbineRecipeSerializer(IFactory factory) {
        super(factory);
    }

    @Override
    public @NotNull RECIPE fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {

        FluidStackIngredient[] inputFluids = inputFluidsFromJson(json, recipeId);
        FluidStackIngredient[] outputFluids = outputFluidsFromJson(json, recipeId);

        double heatRequired = 1D;
        try {
            heatRequired = GsonHelper.getAsDouble(json, "heatRequired", 1D);
        } catch (Exception ex) {
            NuclearCraft.LOGGER.warn("Unable to parse params for recipe: "+recipeId);
        }
        return this.factory.create(recipeId, new ItemStackIngredient[]{}, new ItemStackIngredient[]{}, inputFluids, outputFluids, heatRequired, 1, 1, 1);
    }


    @Override
    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        try {
            ItemStackIngredient[] inputItems = readItems(buffer);
            ItemStackIngredient[] outputItems = readItems(buffer);
            FluidStackIngredient[] inputFluids = readFluids(buffer);
            FluidStackIngredient[] outputFluids = readFluids(buffer);

            double heatRequired = buffer.readDouble();
            double powerModifier = buffer.readDouble();
            double radiation = buffer.readDouble();

            return this.factory.create(recipeId, new ItemStackIngredient[]{}, new ItemStackIngredient[]{}, inputFluids,  outputFluids, heatRequired, 1, 1, 1);
        } catch (Exception e) {
            NuclearCraft.LOGGER.error("Error reading from packet.", e);
            throw e;
        }
    }
}
