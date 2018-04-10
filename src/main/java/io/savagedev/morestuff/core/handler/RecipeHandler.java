package io.savagedev.morestuff.core.handler;

/*
 * RecipeHandler.java
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

import com.google.common.collect.Maps;
import io.savagedev.morestuff.common.recipes.InfusedStoneShapedRecipe;
import io.savagedev.morestuff.common.recipes.InfusedStoneShapelessRecipe;
import io.savagedev.morestuff.core.helpers.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Map;

public class RecipeHandler
{
    private String[][] toolRecipePatterns = new String[][] {{"XXX", " # ", " # "}, {"X", "#", "#"}, {"XX", "X#", " #"}, {"XX", " #", " #"}};
    private final String[][] armorRecipePatterns = new String[][] {{"XXX", "X X"}, {"X X", "XXX", "XXX"}, {"XXX", "X X", "X X"}, {"X X", "X X"}};
    private Object[][] toolRecipeItems = new Object[][] {{ObjHandler.soulMatter}, {ObjHandler.soulPickaxe}, {ObjHandler.soulShovel}, {ObjHandler.soulAxe}, {ObjHandler.soulHoe}};
    private Item[][] armorRecipeItems = new Item[][] {{ObjHandler.soulMatter}, {ObjHandler.soulHelmet}, {ObjHandler.soulChestplate}, {ObjHandler.soulLeggings}, {ObjHandler.soulBoots}};

    public void initTools() {
        for (int i = 0; i < this.toolRecipeItems[0].length; ++i) {
            Object object = this.toolRecipeItems[0][i];

            for (int j = 0; j < this.toolRecipeItems.length - 1; ++j) {
                Item item = (Item)this.toolRecipeItems[j + 1][i];
                GameRegistry.addRecipe(new ItemStack(item), this.toolRecipePatterns[j], '#', ObjHandler.emeraldRod, 'X', object);
            }
        }
    }

    public void initArmor() {
        for (int i = 0; i < this.armorRecipeItems[0].length; ++i) {
            Item item = this.armorRecipeItems[0][i];

            for (int j = 0; j < this.armorRecipeItems.length - 1; ++j) {
                Item item1 = this.armorRecipeItems[j + 1][i];
                GameRegistry.addRecipe(new ItemStack(item1), this.armorRecipePatterns[j], 'X', item);
            }
        }
    }

    public static void init() {
        GameRegistry.addRecipe(new ItemStack(ObjHandler.soulSword), " S ", " S ", " E ", 'S', ObjHandler.soulMatter, 'E', ObjHandler.emeraldRod);
        GameRegistry.addRecipe(new ItemStack(ObjHandler.infusionStone), " E ", "EDE", " E ", 'E', Items.EMERALD, 'D', Items.DIAMOND);
        GameRegistry.addRecipe(new InfusedStoneShapelessRecipe(new ItemStack(ObjHandler.emeraldRod), new ArrayList<ItemStack>(){{add(new ItemStack(Items.STICK));}}));
        GameRegistry.addRecipe(shapedInfusionStoneRecipe(new ItemStack(ObjHandler.blockInfuser), "III", "ISI", "III", 'I', Blocks.IRON_BLOCK, 'S', ObjHandler.infusionStone));
    }

    private static InfusedStoneShapedRecipe shapedInfusionStoneRecipe(ItemStack stack, Object... recipeComponents) {
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;

        if (recipeComponents[i] instanceof String[]) {
            String[] astring = (String[])((String[])recipeComponents[i++]);

            for (String s2 : astring) {
                ++k;
                j = s2.length();
                s = s + s2;
            }
        } else {
            while (recipeComponents[i] instanceof String) {
                String s1 = (String)recipeComponents[i++];
                ++k;
                j = s1.length();
                s = s + s1;
            }
        }

        Map<Character, ItemStack> map;

        for (map = Maps.<Character, ItemStack>newHashMap(); i < recipeComponents.length; i += 2) {
            Character character = (Character)recipeComponents[i];
            ItemStack itemstack = null;

            if (recipeComponents[i + 1] instanceof Item) {
                itemstack = new ItemStack((Item)recipeComponents[i + 1]);
            } else if (recipeComponents[i + 1] instanceof Block) {
                itemstack = new ItemStack((Block)recipeComponents[i + 1], 1, 32767);
            } else if (recipeComponents[i + 1] instanceof ItemStack) {
                itemstack = (ItemStack)recipeComponents[i + 1];
            }

            map.put(character, itemstack);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        for (int l = 0; l < j * k; ++l) {
            char c0 = s.charAt(l);

            if (map.containsKey(Character.valueOf(c0))) {
                aitemstack[l] = ((ItemStack)map.get(Character.valueOf(c0))).copy();
            } else {
                aitemstack[l] = null;
            }
        }

        InfusedStoneShapedRecipe shapedrecipes = new InfusedStoneShapedRecipe(j, k, aitemstack, stack);
        return shapedrecipes;
    }
}
