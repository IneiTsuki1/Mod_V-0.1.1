package net.IneiTsuki.testmod.item;

import net.IneiTsuki.testmod.testmod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, testmod.MOD_ID);

    public static final RegistryObject<Item> AAA = ITEMS.register("aaa",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INGOT_ALUMINUM = ITEMS.register("ingot_aluminum",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INGOT_LEAD = ITEMS.register("ingot_lead",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INGOT_TIN = ITEMS.register("ingot_tin",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INGOT_OSMIUM = ITEMS.register("ingot_osmium",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INGOT_TITANIUM = ITEMS.register("ingot_titanium",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INGOT_STEEL = ITEMS.register("ingot_steel",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STEEL_DUST = ITEMS.register("steel_dust",
            () -> new Item(new Item.Properties()));
    
    
    
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
