package net.IneiTsuki.testmod.block.entity;

import net.IneiTsuki.testmod.block.ModBlock;
import net.IneiTsuki.testmod.testmod;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, testmod.MOD_ID);


public static final RegistryObject<BlockEntityType<CompressorBlockEntity>> COMPRESSOR =
        BLOCK_ENTITIES.register("compressor", () ->
                BlockEntityType.Builder.of(CompressorBlockEntity::new,
                        ModBlock.COMPRESSOR.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }


}
