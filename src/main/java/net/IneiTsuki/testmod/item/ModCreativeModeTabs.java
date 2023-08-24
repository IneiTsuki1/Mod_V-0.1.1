package net.IneiTsuki.testmod.item;

import net.IneiTsuki.testmod.item.ModItems;
import net.IneiTsuki.testmod.testmod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = testmod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {
    public static CreativeModeTab TUTORIAL_TAB;

    @SubscribeEvent
    public static void registerCreativeModeTabs(CreativeModeTabEvent.Register event) {
        TUTORIAL_TAB = event.registerCreativeModeTab(new ResourceLocation(testmod.MOD_ID, "tutorial_tab"),
                builder -> builder.icon(() -> new ItemStack(ModItems.INGOT_ALUMINUM.get()))
                        .title(Component.translatable("creativemodetab.tutorial_tab")));
    }
}