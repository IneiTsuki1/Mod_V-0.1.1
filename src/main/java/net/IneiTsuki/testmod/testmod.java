package net.IneiTsuki.testmod;

import com.mojang.logging.LogUtils;
import net.IneiTsuki.testmod.block.ModBlock;
import net.IneiTsuki.testmod.block.entity.ModBlockEntities;
import net.IneiTsuki.testmod.item.ModCreativeModeTabs;
import net.IneiTsuki.testmod.item.ModItems;
import net.IneiTsuki.testmod.recipe.ModRecipes;
import net.IneiTsuki.testmod.screen.CompressorScreen;
import net.IneiTsuki.testmod.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(testmod.MOD_ID)
public class testmod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "testmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    public testmod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlock.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModRecipes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(CreativeModeTabEvent.BuildContents event)
    {
        if(event.getTab() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.AAA);
        }

        if(event.getTab() == ModCreativeModeTabs.TUTORIAL_TAB) {
            event.accept(ModItems.INGOT_ALUMINUM);
            event.accept(ModItems.INGOT_LEAD);
            event.accept(ModItems.INGOT_TIN);
            event.accept(ModItems.INGOT_OSMIUM);
            event.accept(ModItems.INGOT_TITANIUM);
            event.accept(ModItems.INGOT_STEEL);
            event.accept(ModItems.STEEL_DUST);

            event.accept(ModBlock.ALUMINUM_BLOCK);
            event.accept(ModBlock.LEAD_BLOCK);
            event.accept(ModBlock.TIN_BLOCK);
            event.accept(ModBlock.COMPRESSOR);
            event.accept(ModBlock.DEEPSLATE_ALUMINUM);
            event.accept(ModBlock.DEEPSLATE_TIN);
            event.accept(ModBlock.DEEPSLATE_TITANIUM);
        }
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.COMPRESSOR_MENU.get(), CompressorScreen::new);
        }
    }
}
