package net.IneiTsuki.testmod.screen;

import net.IneiTsuki.testmod.block.ModBlock;
import net.IneiTsuki.testmod.block.entity.CompressorBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CompressorMenu extends AbstractContainerMenu {

    public final CompressorBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;


    public CompressorMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    ///////////////////////ContainerData not SimpleContainerData here!
    public CompressorMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.COMPRESSOR_MENU.get(), id);
        checkContainerSize(inv,10); // same as the itemHandler in blockEntity
        blockEntity = (CompressorBlockEntity) entity;
        this.level = inv.player.level;
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler ->  {
            this.addSlot(new SlotItemHandler(handler, 0, 23, 8 + 14){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) { return true; }
            });
            this.addSlot(new SlotItemHandler(handler, 1, 42, 8 + 14){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) { return true; }
            });
            this.addSlot(new SlotItemHandler(handler, 2, 61, 8 + 14){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) { return true; }
            });
            this.addSlot(new SlotItemHandler(handler, 3, 23, 27 + 14){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) { return true; }
            });
            this.addSlot(new SlotItemHandler(handler, 4, 42, 27 + 14){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) { return true; }
            });
            this.addSlot(new SlotItemHandler(handler, 5, 61, 27 + 14){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) { return true; }
            });
            this.addSlot(new SlotItemHandler(handler, 6, 23, 46 + 14){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) { return true; }
            });
            this.addSlot(new SlotItemHandler(handler, 7, 42, 46 + 14){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) { return true; }
            });
            this.addSlot(new SlotItemHandler(handler, 8, 61, 46 + 14){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) { return true; }
            });


            this.addSlot(new SlotItemHandler(handler, 9, 124, 27 + 14){
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) { return false; }
            });

            //EXAMPLES FOR SLOT INPUT LIMITS!
            /*this.addSlot(new SlotItemHandler(handler, XXX, XXX, XXX){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return (stack.is(ItemTags.create(new ResourceLocation("forge:anytaguwant")))); //Allow u to limit input to a stack from tag as example
                }
            });
            /*this.addSlot(new SlotItemHandler(handler, XXX, XXX, XXX){
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false; ////blocks all items from player in Menu
                }
            });
             */

        });

        addDataSlots(data);

    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1); //max progress
        int progressArrowSize = 40; // this is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 10;  // must be the number of slots you have!

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }


    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, ModBlock.COMPRESSOR.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 103 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 161));
        }
    }
}