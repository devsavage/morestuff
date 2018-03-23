package io.savagedev.morestuff.common.recipes;

/*
 * InfusedStoneShapelessRecipe.java
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

import com.google.common.collect.Lists;
import io.savagedev.morestuff.core.Names;
import io.savagedev.morestuff.core.handler.ObjHandler;
import io.savagedev.morestuff.core.helpers.LogHelper;
import io.savagedev.morestuff.core.helpers.NBTHelper;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;

public class InfusedStoneShapelessRecipe implements IRecipe
{
    private List<ItemStack> recipeItems;
    private ItemStack resultItem;

    public InfusedStoneShapelessRecipe(ItemStack output, List<ItemStack> recipeItems) {
        this.resultItem = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        List<ItemStack> list = Lists.newArrayList(recipeItems);
        list.add(new ItemStack(ObjHandler.infusionStone));

        for (int i = 0; i < inv.getHeight(); ++i) {
            for (int j = 0; j < inv.getWidth(); ++j) {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);

                if (itemstack != null) {
                    boolean flag = false;

                    for (ItemStack nextStack : list) {
                        if (itemstack.getItem() == nextStack.getItem() && (nextStack.getMetadata() == 32767 || itemstack.getMetadata() == nextStack.getMetadata())) {
                            if(itemstack.getItem() == ObjHandler.infusionStone && !NBTHelper.getBoolean(itemstack, Names.Common.INFUSED)) {
                                flag = false;
                                break;
                            }

                            flag = true;
                            list.remove(nextStack);
                            break;
                        }
                    }

                    if (!flag) {
                        return false;
                    }
                }
            }
        }

        return list.isEmpty();
    }

    @Nullable
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return this.resultItem.copy();
    }

    @Override
    public int getRecipeSize() {
        return 1;
    }

    @Nullable
    @Override
    public ItemStack getRecipeOutput() {
        return this.resultItem;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i) {
            ItemStack infusedStone = new ItemStack(ObjHandler.infusionStone);
            NBTHelper.setBoolean(infusedStone, Names.Common.INFUSED, true);
            ItemStack itemstack = inv.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
            if(itemstack != null && itemstack.getItem() == ObjHandler.infusionStone) {
                aitemstack[i] = infusedStone;
            }
        }

        return aitemstack;
    }
}
