package net.IneiTsuki.testmod.block.entity;

import net.IneiTsuki.testmod.item.ModItems;
import net.IneiTsuki.testmod.recipe.CompressorRecipe;
import net.IneiTsuki.testmod.screen.CompressorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public class CompressorBlockEntity extends BlockEntity implements MenuProvider {

    public static int x;
    public static int y;
    public static int z;
    private final ItemStackHandler itemHandler = new ItemStackHandler(10) { //size means the amount of inventory slots
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;


    public CompressorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMPRESSOR.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CompressorBlockEntity.this.progress;
                    case 1 -> CompressorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CompressorBlockEntity.this.progress = value;
                    case 1 -> CompressorBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() { return 2; }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Compressor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new CompressorMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }


    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("compressor.progress", this.progress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("compressor.progress");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }


    public static void tick(Level level, BlockPos pos, BlockState state, CompressorBlockEntity pEntity) {

        //System.out.println("Is Ticking:" + "with - " + pEntity.progress);

        if(level.isClientSide()) {
            return;
        }

        if(hasRecipe(pEntity)) {
            pEntity.progress++;
            setChanged(level, pos, state);

            if(pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity);
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private void resetProgress() {
        //System.out.println("RESET PROGRESS");
        this.progress = 0;
    }

    private static void craftItem(CompressorBlockEntity pEntity) {
        //System.out.println("CRAFT ITEM");
        Level level = pEntity.level;
        SimpleContainer inventory = new SimpleContainer(pEntity.itemHandler.getSlots());
        for (int i = 0; i < pEntity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, pEntity.itemHandler.getStackInSlot(i));
        }

        Optional<CompressorRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(CompressorRecipe.Type.INSTANCE, inventory, level);

        if (hasRecipe(pEntity)) {
            pEntity.itemHandler.extractItem(0,1,false);
            pEntity.itemHandler.extractItem(1,1,false);
            pEntity.itemHandler.extractItem(2,1,false);
            pEntity.itemHandler.extractItem(3,1,false);
            pEntity.itemHandler.extractItem(4,1,false);
            pEntity.itemHandler.extractItem(5,1,false);
            pEntity.itemHandler.extractItem(6,1,false);
            pEntity.itemHandler.extractItem(7,1,false);
            pEntity.itemHandler.extractItem(8,1,false);

            pEntity.itemHandler.setStackInSlot(9, new ItemStack(recipe.get().getResultItem().getItem(),
                    pEntity.itemHandler.getStackInSlot(9).getCount() + 1));

            pEntity.resetProgress();
        }
    }

    private static boolean hasRecipe(CompressorBlockEntity entity) {
        //System.out.println("HAS RECIPE");
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<CompressorRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(CompressorRecipe.Type.INSTANCE, inventory, level);

        return recipe.isPresent() && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemAmountIntoOutputSlot(inventory, recipe.get().getResultItem());
    }

    private static boolean canInsertItemAmountIntoOutputSlot(SimpleContainer inventory, ItemStack itemStack) {
        return inventory.getItem(9).getItem() == itemStack.getItem() || inventory.getItem(9).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(9).getMaxStackSize() > inventory.getItem(9).getCount();
    }
}