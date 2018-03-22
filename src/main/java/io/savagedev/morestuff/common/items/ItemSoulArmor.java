package io.savagedev.morestuff.common.items;

/*
 * ItemSoulArmor.java
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

import io.savagedev.morestuff.MoreStuff;
import io.savagedev.morestuff.common.items.materials.MaterialSoul;
import io.savagedev.morestuff.core.Reference;
import io.savagedev.morestuff.core.handler.ObjHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import java.util.List;

public class ItemSoulArmor extends ItemArmor
{
    private static String[] types = {"helmet", "chest", "legs", "boots"};

    public ItemSoulArmor(EntityEquipmentSlot equipmentSlotIn) {
        super(MaterialSoul.soulArmor, 3, equipmentSlotIn);
        this.setCreativeTab(MoreStuff.tabMoreStuff);
        this.canRepair = true;
        this.setUnlocalizedName(Reference.MOD_DOMAIN + "soulArmor.");
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        super.onArmorTick(world, player, itemStack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if (this == ObjHandler.soulChestplate || this == ObjHandler.soulHelmet || this == ObjHandler.soulBoots) {
            return Reference.MOD_DOMAIN + "textures/models/armor/soulArmor_layer_1.png";
        }

        if (this == ObjHandler.soulLeggings) {
            return Reference.MOD_DOMAIN + "textures/models/armor/soulArmor_layer_2.png";
        } else {
            return null;
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return this == ObjHandler.soulHelmet && repair.getItem() == Items.EMERALD ||
                this == ObjHandler.soulChestplate && repair.getItem() == Items.EMERALD ||
                this == ObjHandler.soulLeggings && repair.getItem() == Items.EMERALD ||
                this == ObjHandler.soulBoots && repair.getItem() == Items.EMERALD;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        //Temp
        switch (armorType) {
            case HEAD:
                return super.getUnlocalizedName(stack) + types[0];
            case CHEST:
                return super.getUnlocalizedName(stack) + types[1];
            case LEGS:
                return super.getUnlocalizedName(stack) + types[2];
            case FEET:
                return super.getUnlocalizedName(stack) + types[3];
        }

        return super.getUnlocalizedName(stack);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }
}
