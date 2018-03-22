package io.savagedev.morestuff.core.helpers;

/*
 * ItemHelper.java
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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemHelper
{
    public static boolean equalsIgnoreStackSize(ItemStack itemStack1, ItemStack itemStack2) {
        if (itemStack1 != null && itemStack2 != null) {
            // Sort on itemID
            if (Item.getIdFromItem(itemStack1.getItem()) - Item.getIdFromItem(itemStack2.getItem()) == 0) {
                // Sort on item
                if (itemStack1.getItem() == itemStack2.getItem()) {
                    // Then sort on meta
                    if (itemStack1.getItemDamage() == itemStack2.getItemDamage()) {
                        // Then sort on NBT
                        if (itemStack1.hasTagCompound() && itemStack2.hasTagCompound()) {
                            // Then sort on stack size
                            if (ItemStack.areItemStackTagsEqual(itemStack1, itemStack2)) {
                                return true;
                            }
                        } else {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static ItemStack cloneItemStack(ItemStack itemStack, int stackSize) {
        ItemStack clonedItemStack = itemStack.copy();
        clonedItemStack.stackSize = stackSize;
        return clonedItemStack;
    }
}
