package net.IneiTsuki.testmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.IneiTsuki.testmod.testmod;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CompressorRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public CompressorRecipe(ResourceLocation id, ItemStack output,
                            NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }


    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()) {
            return false;
        }


        return recipeItems.get(0).test(pContainer.getItem(1));

    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer_) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CompressorRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "compressor";
    }

    public static class Serializer implements RecipeSerializer<CompressorRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(testmod.MOD_ID, "compressor");

        @Override
        public CompressorRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {

            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(9, Ingredient.EMPTY);
            //FluidStack fluid = FluidJSONUtil.readFluid(json.get("fluid").getAsJsonObject());

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new CompressorRecipe(pRecipeId, output, inputs);
        }

        @Override
        public @Nullable CompressorRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new CompressorRecipe(pRecipeId, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CompressorRecipe pRecipe) {
            buf.writeInt(pRecipe.getIngredients().size());

            for (Ingredient ing : pRecipe.getIngredients()) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(pRecipe.getResultItem(), false);
        }
    }
}
