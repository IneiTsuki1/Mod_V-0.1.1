package net.IneiTsuki.testmod.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.IntList;
import net.IneiTsuki.testmod.testmod;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompressorRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final boolean isSimple;

    public CompressorRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.isSimple = recipeItems.stream().allMatch(Ingredient::isSimple);
    }

    //    @Override
//    public boolean matches(@NotNull SimpleContainer pContainer, Level pLevel) {
//        if(pLevel.isClientSide()) {
//            return false;
//        }
//
//        return recipeItems.get(0).test(pContainer.getItem(0))
//				&& recipeItems.get(1).test(pContainer.getItem(1))
//				&& recipeItems.get(2).test(pContainer.getItem(2))
//				&& recipeItems.get(3).test(pContainer.getItem(3))
//				&& recipeItems.get(4).test(pContainer.getItem(4));
//    }
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        StackedContents stackedcontents = new StackedContents();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for(int j = 0; j < pContainer.getContainerSize(); ++j) {
            ItemStack itemstack = pContainer.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (isSimple)
                    stackedcontents.accountStack(itemstack, 1);
                else inputs.add(itemstack);
            }
        }

        return i == this.recipeItems.size() && (isSimple ? stackedcontents.canCraft(this,
                (IntList)null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.recipeItems) != null);
    }

    @Override
    public @NotNull String getGroup() { return "";     }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() { return recipeItems; }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer pContainer) { return output; }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) { return true; }

    @Override
    public @NotNull ItemStack getResultItem() { return output.copy(); }

    @Override
    public @NotNull ResourceLocation getId() { return id; }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() { return Serializer.INSTANCE; }

    @Override
    public @NotNull RecipeType<?> getType() { return Type.INSTANCE; }

    public static class Type implements RecipeType<CompressorRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "compressor";
    }

    public static class Serializer implements RecipeSerializer<CompressorRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(testmod.MOD_ID, "compressor");

        @Override
        public @NotNull CompressorRecipe fromJson(@NotNull ResourceLocation pRecipeId, @NotNull JsonObject pSerializedRecipe) {
//             ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

//            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
//            NonNullList<Ingredient> inputs = NonNullList.withSize(5, Ingredient.EMPTY);

            NonNullList<Ingredient> nonnulllist = itemsFromJson(GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients"));


//			for (int i = 0; i < inputs.size(); i++) {
//                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
//            }
//
//            return new CircuitFabricatorRecipe(pRecipeId, output, inputs);

            if (nonnulllist.size() > 5) {
                throw new JsonParseException("Too many ingredients for shapeless recipe. The maximum is " + 5);
            } else {
                ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
                return new CompressorRecipe(pRecipeId,  output, nonnulllist);
            }
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray pIngredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < pIngredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(pIngredientArray.get(i));
                if (true || !ingredient.isEmpty()) { // FORGE: Skip checking if an ingredient is empty during shapeless recipe deserialization to prevent complex ingredients from caching tags too early. Can not be done using a config value due to sync issues.
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        @Override
        public @Nullable CompressorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readVarInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); ++i) {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new CompressorRecipe(id, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CompressorRecipe recipe) {
            buf.writeVarInt(recipe.recipeItems.size());

            for (Ingredient ing : recipe.recipeItems) {
                ing.toNetwork(buf);
            }
            buf.writeItemStack(recipe.getResultItem(), false);
        }
    }
}
