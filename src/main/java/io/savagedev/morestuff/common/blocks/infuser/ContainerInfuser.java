package io.savagedev.morestuff.common.blocks.infuser;

/*
 * ContainerInfuser.java
 * Copyright (C) 2018 Savage - github.com/devsavage
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import io.savagedev.morestuff.common.container.base.ContainerMS;
import io.savagedev.morestuff.core.Names;
import io.savagedev.morestuff.core.handler.ObjHandler;
import io.savagedev.morestuff.core.helpers.LogHelper;
import io.savagedev.morestuff.core.helpers.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ContainerInfuser extends ContainerMS
{
    private TileEntityInfuser tileEntityInfuser;
    public int FUEL_BURN_TIME;
    public int INFUSE_TIME;
    public int TOTAL_INFUSE_TIME;
    public int TOTAL_FUEL_STORED;

    public ContainerInfuser(InventoryPlayer inventoryPlayer, TileEntityInfuser tileEntityInfuser) {
        this.tileEntityInfuser = tileEntityInfuser;

        this.addSlotToContainer(new SlotFurnaceFuel(tileEntityInfuser, TileEntityInfuser.FUEL_SLOT_INDEX, 8, 61));
        this.addSlotToContainer(new SlotInfusionStone(tileEntityInfuser, TileEntityInfuser.INFUSER_ITEM_SLOT_INDEX, 49, 35));
        this.addSlotToContainer(new SlotRawSoulMatter(tileEntityInfuser, TileEntityInfuser.RAW_SOUL_MATTER_SLOT_INDEX, 71, 35));
        this.addSlotToContainer(new SlotOutput(tileEntityInfuser, TileEntityInfuser.OUTPUT_SLOT_INDEX, 127, 35));

        for (int inventoryRowIndex = 0; inventoryRowIndex < PLAYER_INVENTORY_ROWS; ++inventoryRowIndex) {
            for (int inventoryColumnIndex = 0; inventoryColumnIndex < PLAYER_INVENTORY_COLUMNS; ++inventoryColumnIndex) {
                this.addSlotToContainer(new Slot(inventoryPlayer, inventoryColumnIndex + inventoryRowIndex * 9 + 9, 8 + inventoryColumnIndex * 18, 84 + inventoryRowIndex * 18));
            }
        }

        for (int actionBarSlotIndex = 0; actionBarSlotIndex < PLAYER_INVENTORY_COLUMNS; ++actionBarSlotIndex) {
            this.addSlotToContainer(new Slot(inventoryPlayer, actionBarSlotIndex, 8 + actionBarSlotIndex * 18, 142));
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        tileEntityInfuser.closeInventory(playerIn);
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileEntityInfuser);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tileEntityInfuser.isUseableByPlayer(playerIn);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i) {
            IContainerListener containerListener = (IContainerListener)this.listeners.get(i);

            if(this.INFUSE_TIME != this.tileEntityInfuser.getField(0)) {
                containerListener.sendProgressBarUpdate(this, 0, this.tileEntityInfuser.getField(0));
            }

            if(this.TOTAL_INFUSE_TIME != this.tileEntityInfuser.getField(1)) {
                containerListener.sendProgressBarUpdate(this, 1, this.tileEntityInfuser.getField(1));
            }

            if(this.TOTAL_FUEL_STORED != this.tileEntityInfuser.getField(2)) {
                containerListener.sendProgressBarUpdate(this, 2, this.tileEntityInfuser.getField(2));
            }

            if(this.FUEL_BURN_TIME != this.tileEntityInfuser.getField(3)) {
                containerListener.sendProgressBarUpdate(this, 3, this.tileEntityInfuser.getField(3));
            }
        }

        this.INFUSE_TIME = this.tileEntityInfuser.getField(0);
        this.TOTAL_INFUSE_TIME = this.tileEntityInfuser.getField(1);
        this.TOTAL_FUEL_STORED = this.tileEntityInfuser.getField(2);
        this.FUEL_BURN_TIME = this.tileEntityInfuser.getField(3);
    }

    @Override
    public void updateProgressBar(int id, int data) {
        this.tileEntityInfuser.setField(id, data);
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int slotIndex) {
        ItemStack itemStack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotItemStack = slot.getStack();
            itemStack = slotItemStack.copy();

            if (slotIndex < TileEntityInfuser.INVENTORY_SIZE) {

                if (!this.mergeItemStack(slotItemStack, this.tileEntityInfuser.getSizeInventory(), inventorySlots.size(), true)) {
                    return null;
                }
            } else {
                if (!this.mergeItemStack(slotItemStack, 0, TileEntityInfuser.INVENTORY_SIZE, false)) {
                    return null;
                }
            }

            if (slotItemStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemStack;
    }

    private class SlotInfusionStone extends Slot
    {

        public SlotInfusionStone(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.getItem() == ObjHandler.infusionStone && NBTHelper.getBoolean(stack, Names.Common.INFUSED);
        }
    }

    private class SlotRawSoulMatter extends Slot
    {
        public SlotRawSoulMatter(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return stack.getItem() == ObjHandler.soulMatterRaw;
        }
    }

    private class SlotOutput extends Slot
    {
        public SlotOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }
    }
}
