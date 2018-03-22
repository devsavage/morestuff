package io.savagedev.morestuff.core.helpers;

/*
 * NBTHelper.java
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

import io.savagedev.morestuff.core.Names;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.UUID;

public class NBTHelper
{
    public static boolean hasTag(ItemStack itemStack, String keyName) {
        return itemStack != null && itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey(keyName);
    }

    public static void removeTag(ItemStack itemStack, String keyName) {
        if (itemStack.getTagCompound() != null) {
            itemStack.getTagCompound().removeTag(keyName);
        }
    }

    public static boolean hasUUID(ItemStack itemStack) {
        return hasTag(itemStack, Names.NBT.UUID_MOST_SIG) && hasTag(itemStack, Names.NBT.UUID_LEAST_SIG);
    }

    public static void setUUID(ItemStack itemStack) {
        initNBTTagCompound(itemStack);

        // Set a UUID on the Alchemical Bag, if one doesn't exist already
        if (!hasTag(itemStack, Names.NBT.UUID_MOST_SIG) && !hasTag(itemStack, Names.NBT.UUID_LEAST_SIG)) {
            UUID itemUUID = UUID.randomUUID();
            setLong(itemStack, Names.NBT.UUID_MOST_SIG, itemUUID.getMostSignificantBits());
            setLong(itemStack, Names.NBT.UUID_LEAST_SIG, itemUUID.getLeastSignificantBits());
        }
    }

    /**
     * Initializes the NBT Tag Compound for the given ItemStack if it is null
     *
     * @param itemStack The ItemStack for which its NBT Tag Compound is being checked for initialization
     */
    private static void initNBTTagCompound(ItemStack itemStack) {
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
    }

    public static void setLong(ItemStack itemStack, String keyName, long keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setLong(keyName, keyValue);
    }

    // String
    public static String getString(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName)) {
            setString(itemStack, keyName, "");
        }

        return itemStack.getTagCompound().getString(keyName);
    }

    public static void setString(ItemStack itemStack, String keyName, String keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setString(keyName, keyValue);
    }

    // boolean
    public static boolean getBoolean(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName)) {
            setBoolean(itemStack, keyName, false);
        }

        return itemStack.getTagCompound().getBoolean(keyName);
    }

    public static void setBoolean(ItemStack itemStack, String keyName, boolean keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setBoolean(keyName, keyValue);
    }

    // byte
    public static byte getByte(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName)) {
            setByte(itemStack, keyName, (byte) 0);
        }

        return itemStack.getTagCompound().getByte(keyName);
    }

    public static void setByte(ItemStack itemStack, String keyName, byte keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setByte(keyName, keyValue);
    }

    // short
    public static short getShort(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName)) {
            setShort(itemStack, keyName, (short) 0);
        }

        return itemStack.getTagCompound().getShort(keyName);
    }

    public static void setShort(ItemStack itemStack, String keyName, short keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setShort(keyName, keyValue);
    }

    // int
    public static int getInt(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName)) {
            setInteger(itemStack, keyName, 0);
        }

        return itemStack.getTagCompound().getInteger(keyName);
    }

    public static void setInteger(ItemStack itemStack, String keyName, int keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setInteger(keyName, keyValue);
    }

    // long
    public static long getLong(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName)) {
            setLong(itemStack, keyName, 0);
        }

        return itemStack.getTagCompound().getLong(keyName);
    }

    // float
    public static float getFloat(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName)) {
            setFloat(itemStack, keyName, 0);
        }

        return itemStack.getTagCompound().getFloat(keyName);
    }

    public static void setFloat(ItemStack itemStack, String keyName, float keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setFloat(keyName, keyValue);
    }

    // double
    public static double getDouble(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName)) {
            setDouble(itemStack, keyName, 0);
        }

        return itemStack.getTagCompound().getDouble(keyName);
    }

    public static void setDouble(ItemStack itemStack, String keyName, double keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setDouble(keyName, keyValue);
    }

    // tag list
    public static NBTTagList getTagList(ItemStack itemStack, String keyName, int nbtBaseType) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName)) {
            setTagList(itemStack, keyName, new NBTTagList());
        }

        return itemStack.getTagCompound().getTagList(keyName, nbtBaseType);
    }

    public static void setTagList(ItemStack itemStack, String keyName, NBTTagList nbtTagList) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setTag(keyName, nbtTagList);
    }

    // tag compound
    public static NBTTagCompound getTagCompound(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName)) {
            setTagCompound(itemStack, keyName, new NBTTagCompound());
        }

        return itemStack.getTagCompound().getCompoundTag(keyName);
    }

    public static void setTagCompound(ItemStack itemStack, String keyName, NBTTagCompound nbtTagCompound) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setTag(keyName, nbtTagCompound);
    }
}
