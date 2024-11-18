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

public class CoolantRecipeSerializer<RECIPE extends NcRecipe> extends NcRecipeSerializer<RECIPE> {

    public CoolantRecipeSerializer(IFactory factory) {
        super(factory);
    }

    @Override
    public @NotNull RECIPE fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {

        inputFluids = new FluidStackIngredient[0];
        try {
            if(json.has("inputFluids")) {
                if (GsonHelper.isArrayNode(json, "inputFluids")) {
                    JsonElement input = GsonHelper.getAsJsonArray(json, "inputFluids");
                    inputFluids = new FluidStackIngredient[input.getAsJsonArray().size()];
                    int i = 0;
                    for (JsonElement in : input.getAsJsonArray()) {
                        inputFluids[i] = IngredientCreatorAccess.fluid().deserialize(in);
                        i++;
                    }
                } else {
                    JsonElement inputJson = GsonHelper.getAsJsonObject(json, "inputFluids");
                    inputFluids = new FluidStackIngredient[]{IngredientCreatorAccess.fluid().deserialize(inputJson)};
                }
            }
        } catch (Exception ex) {
            NuclearCraft.LOGGER.warn("Unable to parse input fluid for recipe: "+recipeId);
        }

        outputFluids = new FluidStackIngredient[0];
        try {
            if(json.has("outputFluids")) {
                if (GsonHelper.isArrayNode(json, "outputFluids")) {
                    JsonElement output = GsonHelper.getAsJsonArray(json, "outputFluids");
                    outputFluids = new FluidStackIngredient[output.getAsJsonArray().size()];
                    int i = 0;
                    for (JsonElement out : output.getAsJsonArray()) {
                        outputFluids[i] = IngredientCreatorAccess.fluid().deserialize(out.getAsJsonObject());
                        i++;
                    }
                } else {
                    JsonElement output = GsonHelper.getAsJsonObject(json, "outputFluids");
                    outputFluids = new FluidStackIngredient[]{IngredientCreatorAccess.fluid().deserialize(output.getAsJsonObject())};
                }
            }
        } catch (Exception ex) {
            NuclearCraft.LOGGER.warn("Unable to parse output fluid for recipe: "+recipeId);
        }
        double coolingRate = 1000D;
        try {
            coolingRate = GsonHelper.getAsDouble(json, "coolingRate", 1000.0);
        } catch (Exception ex) {
            NuclearCraft.LOGGER.warn("Unable to parse params for recipe: "+recipeId);
        }
        return this.factory.create(recipeId, new ItemStackIngredient[]{}, new ItemStackIngredient[]{}, inputFluids, outputFluids, 1, 1, 1, coolingRate);
    }


    @Override
    public RECIPE fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
        try {
            boolean isIncomplete = buffer.readBoolean();
            if(isIncomplete) {
                return emptyRecipe(recipeId);
            }
            readIngredients(buffer);
            double coolingRate = buffer.readDouble();
            double powerModifier = buffer.readDouble();
            double radiation = buffer.readDouble();

            return this.factory.create(recipeId, new ItemStackIngredient[]{}, new ItemStackIngredient[]{}, inputFluids,  outputFluids, coolingRate, 1, 1, 1);
        } catch (Exception e) {
            NuclearCraft.LOGGER.error("Error reading from packet.", e);
            throw e;
        }
    }
}
