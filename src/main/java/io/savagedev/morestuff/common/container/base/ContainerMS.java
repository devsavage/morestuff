package io.savagedev.morestuff.common.container.base;

/*
 * ContainerMS.java
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

import io.savagedev.morestuff.core.helpers.ItemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMS extends Container
{
    protected final int PLAYER_INVENTORY_ROWS = 3;
    protected final int PLAYER_INVENTORY_COLUMNS = 9;

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    protected boolean mergeItemStack(ItemStack itemStack, int slotMin, int slotMax, boolean ascending) {
        boolean slotFound = false;
        int currentSlotIndex = ascending ? slotMax - 1 : slotMin;

        Slot slot;
        ItemStack stackInSlot;

        if (itemStack.isStackable()) {
            while (itemStack.stackSize > 0 && (!ascending && currentSlotIndex < slotMax || ascending && currentSlotIndex >= slotMin)) {
                slot = (Slot) this.inventorySlots.get(currentSlotIndex);
                stackInSlot = slot.getStack();

                if (slot.isItemValid(itemStack) && ItemHelper.equalsIgnoreStackSize(itemStack, stackInSlot)) {
                    int combinedStackSize = stackInSlot.stackSize + itemStack.stackSize;
                    int slotStackSizeLimit = Math.min(stackInSlot.getMaxStackSize(), slot.getSlotStackLimit());

                    if (combinedStackSize <= slotStackSizeLimit) {
                        itemStack.stackSize = 0;
                        stackInSlot.stackSize = combinedStackSize;
                        slot.onSlotChanged();
                        slotFound = true;
                    } else if (stackInSlot.stackSize < slotStackSizeLimit) {
                        itemStack.stackSize -= slotStackSizeLimit - stackInSlot.stackSize;
                        stackInSlot.stackSize = slotStackSizeLimit;
                        slot.onSlotChanged();
                        slotFound = true;
                    }
                }

                currentSlotIndex += ascending ? -1 : 1;
            }
        }

        if (itemStack.stackSize > 0) {
            currentSlotIndex = ascending ? slotMax - 1 : slotMin;

            while (!ascending && currentSlotIndex < slotMax || ascending && currentSlotIndex >= slotMin) {
                slot = (Slot) this.inventorySlots.get(currentSlotIndex);
                stackInSlot = slot.getStack();

                if (slot.isItemValid(itemStack) && stackInSlot == null) {
                    slot.putStack(ItemHelper.cloneItemStack(itemStack, Math.min(itemStack.stackSize, slot.getSlotStackLimit())));
                    slot.onSlotChanged();

                    if (slot.getStack() != null) {
                        itemStack.stackSize -= slot.getStack().stackSize;
                        slotFound = true;
                    }

                    break;
                }

                currentSlotIndex += ascending ? -1 : 1;
            }
        }

        return slotFound;
    }
}
