package io.savagedev.morestuff.core.handler;

/*
 * ObjHandler.java
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

import com.google.gson.JsonObject;
import io.savagedev.morestuff.MoreStuff;
import io.savagedev.morestuff.common.blocks.base.ItemBlockMS;
import io.savagedev.morestuff.common.blocks.infuser.BlockInfuser;
import io.savagedev.morestuff.common.blocks.infuser.TileEntityInfuser;
import io.savagedev.morestuff.common.items.*;
import io.savagedev.morestuff.core.Names;
import io.savagedev.morestuff.core.Reference;
import io.savagedev.morestuff.core.config.values.ConfigBlacklist;
import io.savagedev.morestuff.core.helpers.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

public class ObjHandler
{
    public static final Item soulAxe = new ItemSoulAxe();
    public static final Item soulHoe = new ItemSoulHoe();
    public static final Item soulPickaxe = new ItemSoulPickaxe();
    public static final Item soulShovel = new ItemSoulShovel();
    public static final Item soulSword = new ItemSoulSword();
    public static final Item soulHelmet = new ItemSoulArmor(EntityEquipmentSlot.HEAD);
    public static final Item soulChestplate = new ItemSoulArmor(EntityEquipmentSlot.CHEST);
    public static final Item soulLeggings = new ItemSoulArmor(EntityEquipmentSlot.LEGS);
    public static final Item soulBoots = new ItemSoulArmor(EntityEquipmentSlot.FEET);
    public static final Item soulMatterRaw = new ItemSoulMatterRaw();
    public static final Item soulMatter = new ItemSoulMatter();
    public static final Item infusionStone = new ItemInfusionStone();
    public static final Item emeraldRod = new ItemEmeraldRod();

    public static final Block blockInfuser = new BlockInfuser();

    public static void registerItems() {
        registerItem(soulAxe, Names.Items.soulAxe);
        registerItem(soulHoe, Names.Items.soulHoe);
        registerItem(soulPickaxe, Names.Items.soulPickaxe);
        registerItem(soulShovel, Names.Items.soulShovel);
        registerItem(soulSword, Names.Items.soulSword);
        registerItem(soulHelmet, Names.Items.soulHelmet);
        registerItem(soulChestplate, Names.Items.soulChestplate);
        registerItem(soulLeggings, Names.Items.soulLeggings);
        registerItem(soulBoots, Names.Items.soulBoots);
        registerItem(soulMatterRaw, Names.Items.soulMatterRaw);
        registerItem(soulMatter, Names.Items.soulMatter);
        registerItem(infusionStone, Names.Items.infusionStone);
        registerItem(emeraldRod, Names.Items.emeraldRod);
    }

    public static void registerBlocks() {
        registerBlock(blockInfuser, new ItemBlockMS(blockInfuser), Names.Blocks.infuser);
    }

    public static void registerTileEntities() {
        registerTileEntity(TileEntityInfuser.class, Names.Blocks.infuser);
    }

    public static void registerItemModels() {
        registerItemModel(soulAxe, Names.Items.soulAxe);
        registerItemModel(soulHoe, Names.Items.soulHoe);
        registerItemModel(soulPickaxe, Names.Items.soulPickaxe);
        registerItemModel(soulShovel, Names.Items.soulShovel);
        registerItemModel(soulSword, Names.Items.soulSword);
        registerItemModel(soulHelmet, Names.Items.soulHelmet);
        registerItemModel(soulChestplate, Names.Items.soulChestplate);
        registerItemModel(soulLeggings, Names.Items.soulLeggings);
        registerItemModel(soulBoots, Names.Items.soulBoots);
        registerItemModel(soulMatterRaw, Names.Items.soulMatterRaw);
        registerItemModel(soulMatter, Names.Items.soulMatter);
        registerItemModel(infusionStone, Names.Items.infusionStone);
        registerItemModel(emeraldRod, Names.Items.emeraldRod);
    }

    public static void registerBlockModels() {
        registerBlockModel(blockInfuser, Names.Blocks.infuser);
    }

    private static void registerItem(Item item, String name) {
        if(Arrays.asList(ConfigBlacklist.ITEM_BLACKLIST.getCurrentValues()).contains(name))
            return;

        registerObj(item, name);

        if(MoreStuff.developmentEnvironment)
            createModelFile(name);
    }

    private static void registerTileEntity(Class clazz, String name) {
        if(Arrays.asList(ConfigBlacklist.BLOCK_BLACKLIST.getCurrentValues()).contains(name))
            return;

        GameRegistry.registerTileEntity(clazz, Reference.MOD_ID + "." + name);
    }

    private static void registerBlock(Block block, ItemBlock itemBlock, String name) {
        if(Arrays.asList(ConfigBlacklist.BLOCK_BLACKLIST.getCurrentValues()).contains(name))
            return;

        block.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        GameRegistry.register(block);
        GameRegistry.register(itemBlock.setRegistryName(block.getRegistryName()));
    }

    private static void registerObj(IForgeRegistryEntry<?> o, String name) {
        GameRegistry.register(o, new ResourceLocation(Reference.MOD_ID, name));
    }

    private static void registerItemModelForAllVariants(Item item, String resourceName, ItemMeshDefinition itemMeshDefinition) {
        if(Arrays.asList(ConfigBlacklist.ITEM_BLACKLIST.getCurrentValues()).contains(resourceName))
            return;

        resourceName = Reference.MOD_DOMAIN + resourceName;

        ModelBakery.registerItemVariants(item, new ResourceLocation(resourceName));

        ModelLoader.setCustomMeshDefinition(item, itemMeshDefinition);
    }

    private static void registerItemModel(Item item, String resourceName) {
        if(Arrays.asList(ConfigBlacklist.ITEM_BLACKLIST.getCurrentValues()).contains(resourceName))
            return;

        registerItemModel(item, resourceName, 0, false);
    }

    private static void registerItemModel(Item item, String resourceName, int meta, boolean hasSubtypes) {
        if(hasSubtypes) {
            resourceName = resourceName + "_" + meta;
        }

        resourceName = Reference.MOD_DOMAIN + resourceName;

        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(resourceName, "inventory"));
    }

    private static void registerBlockModel(Block block, String resourceName) {
        if(Arrays.asList(ConfigBlacklist.BLOCK_BLACKLIST.getCurrentValues()).contains(resourceName))
            return;

        ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_DOMAIN + resourceName);

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(resourceLocation, "inventory"));
    }

    private static void createModelFile(String unlocalizedName) {
        JsonObject obj = new JsonObject();
        JsonObject layers = new JsonObject();

        obj.addProperty("parent", "item/handheld");
        layers.addProperty("layer0", "morestuff:items/" + unlocalizedName);
        obj.add("textures", layers);

        try {
            File file = new File("../src/main/resources/assets/morestuff/models/item/" + unlocalizedName + ".json");

            if(file.exists())
                file.delete();

            file.getParentFile().mkdir();
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write(obj.toString());

            writer.flush();
            writer.close();
        } catch(Exception e) {
            LogHelper.error("Error Creating Model File: " + e.getMessage());
        }
    }
}
