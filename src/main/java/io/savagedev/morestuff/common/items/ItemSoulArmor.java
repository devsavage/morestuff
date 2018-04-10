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
import io.savagedev.morestuff.core.helpers.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import java.util.List;

public class ItemSoulArmor extends ItemArmor
{
    private static String[] types = {"helmet", "chest", "legs", "boots"};
    public static boolean enableAbilities = true;

    public ItemSoulArmor(EntityEquipmentSlot equipmentSlotIn) {
        super(MaterialSoul.soulArmor, 3, equipmentSlotIn);
        this.setCreativeTab(MoreStuff.tabMoreStuff);
        this.canRepair = true;
        this.setUnlocalizedName(Reference.MOD_DOMAIN + "soulArmor.");
    }

    private void resetArmorAbilities() {

    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        ItemStack leggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);

        if(helmet != null && helmet.getItem() == ObjHandler.soulHelmet) {
            player.setAir(300);
        }

        if(chest != null && chest.getItem() == ObjHandler.soulChestplate || player.capabilities.isCreativeMode || player.isSpectator()) {
            player.capabilities.allowFlying = true;
            player.sendPlayerAbilities();
        } else {
            player.capabilities.isFlying = false;
            player.capabilities.allowFlying = false;
        }

        if(leggings != null && leggings.getItem() == ObjHandler.soulLeggings) {
            if(!Minecraft.getMinecraft().gameSettings.autoJump) {
                player.stepHeight = 1.0F;
            } else {
                player.capabilities.setPlayerWalkSpeed(0.2F);
            }
        } else {
            player.capabilities.setPlayerWalkSpeed(0.1F);
            player.stepHeight = 0.5F;
        }

        if(boots != null && boots.getItem() == ObjHandler.soulBoots) {
            player.fallDistance = 0.0F;
        } else {
            player.fallDistance = 0.5F;
        }

        LogHelper.info(enableAbilities);
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        super.onUsingTick(stack, player, count);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if(this.armorType == EntityEquipmentSlot.HEAD)
            tooltip.add("- Underwater Breathing");

        if(this.armorType == EntityEquipmentSlot.CHEST) {
            tooltip.add("- Flying");
            TextFormatting color = playerIn.capabilities.getFlySpeed() > 0.05F ? TextFormatting.GREEN : TextFormatting.RED;
            tooltip.add("- Fly Speed: " + color + playerIn.capabilities.getFlySpeed());
        }

        if(this.armorType == EntityEquipmentSlot.LEGS)
            tooltip.add("- Step Assist");

        if(this.armorType == EntityEquipmentSlot.FEET)
            tooltip.add("- No Fall Damage");
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
