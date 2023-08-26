package net.IneiTsuki.testmod.recipe;

import net.IneiTsuki.testmod.testmod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, testmod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CompressorRecipe>> COMPRESSOR_SERIALIZER =
            RECIPE_SERIALIZERS.register("compressor", () -> CompressorRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
