package io.savagedev.morestuff.common.items.base;

/*
 * ItemSoulTool.java
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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import io.savagedev.morestuff.MoreStuff;
import io.savagedev.morestuff.common.items.materials.MaterialSoul;
import io.savagedev.morestuff.core.Names;
import io.savagedev.morestuff.core.Reference;
import io.savagedev.morestuff.core.handler.ObjHandler;
import io.savagedev.morestuff.core.handler.ToolLevelHandler;
import io.savagedev.morestuff.core.helpers.LogHelper;
import io.savagedev.morestuff.core.helpers.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import sun.rmi.runtime.Log;

import javax.tools.Tool;
import java.util.List;
import java.util.Set;

public class ItemSoulTool extends ItemTool
{
    private final String name;
    private Set<Block> effectiveBlocks;

    public ItemSoulTool(String name, Set<Block> effectiveBlocks, String unlocalizedName) {
        super(MaterialSoul.soulTool, effectiveBlocks);
        this.name = name;
        this.effectiveBlocks = effectiveBlocks;
        this.canRepair = true;
        this.setCreativeTab(MoreStuff.tabMoreStuff);
        this.setUnlocalizedName(Reference.MOD_ID + ":" + unlocalizedName);
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        switch (name) {
            case "axe":
                return effectiveBlocks.contains(blockIn.getBlock()) || blockIn.getMaterial() == Material.WOOD || blockIn.getMaterial() == Material.PLANTS || blockIn.getMaterial() == Material.VINE;
            case "pickaxe":
                return effectiveBlocks.contains(blockIn.getBlock()) || blockIn.getMaterial() == Material.ROCK || blockIn.getMaterial() == Material.ANVIL || blockIn.getMaterial() == Material.IRON;
            case "sword":
                return effectiveBlocks.contains(blockIn.getBlock()) || blockIn.getBlock() == Blocks.WEB;
            case "shovel":
                return effectiveBlocks.contains(blockIn.getBlock()) || blockIn.getMaterial() == Material.GRASS || blockIn.getMaterial() == Material.GROUND || blockIn.getMaterial() == Material.SAND || blockIn.getMaterial() == Material.SNOW;
            default:
                return super.canHarvestBlock(blockIn);
        }
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if(canHarvestBlock(state) && ToolLevelHandler.hasLevelTags(stack)) {
            return getToolMaterial().getEfficiencyOnProperMaterial() * ToolLevelHandler.getToolLevel(stack, NBTHelper.getTagCompound(stack, ToolLevelHandler.SOUL_TOOL_TAG));
        }

        return 1.0F;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if(ToolLevelHandler.hasLevelTags(stack) && !ToolLevelHandler.isMaxToolLevel(stack, NBTHelper.getTagCompound(stack, ToolLevelHandler.SOUL_TOOL_TAG))) {
            if(stack.getItem() == ObjHandler.soulSword) {
                if (attacker instanceof EntityPlayer) {
                    ToolLevelHandler.addXp(stack, (EntityPlayer) attacker, 6);
                }

                stack.damageItem(1, attacker);
            } else {
                stack.damageItem(0, attacker);
            }
        }

        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(NBTHelper.hasTag(stack, ToolLevelHandler.SOUL_TOOL_TAG) && ToolLevelHandler.getToolLevel(stack, NBTHelper.getTagCompound(stack, ToolLevelHandler.SOUL_TOOL_TAG)) <= 0) {
            ToolLevelHandler.addLevelTag(stack);
        }

        if(!NBTHelper.hasTag(stack, ToolLevelHandler.SOUL_TOOL_TAG) && !worldIn.isRemote) {
            NBTHelper.setString(stack, ToolLevelHandler.SOUL_TOOL_TAG, stack.getDisplayName());
            ToolLevelHandler.addLevelTag(stack);
        } else {
            if(stack.getItemDamage() == stack.getMaxDamage() - 1) {
                stack.setItemDamage(0);
            }
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (worldIn.isRemote || !(entityLiving instanceof EntityPlayer)) {
            return false;
        }

        if (ToolLevelHandler.isMaxToolLevel(stack, NBTHelper.getTagCompound(stack, ToolLevelHandler.SOUL_TOOL_TAG))) {
            stack.damageItem(0, entityLiving);
            return true;
        }

        if (effectiveBlocks.contains(worldIn.getBlockState(pos).getBlock()) || canHarvestBlock(state) && ToolLevelHandler.hasLevelTags(stack)) {
            ToolLevelHandler.addXp(stack, (EntityPlayer) entityLiving, entityLiving.getRNG().nextInt(6));
            if(!ToolLevelHandler.isMaxToolLevel(stack, NBTHelper.getTagCompound(stack, ToolLevelHandler.SOUL_TOOL_TAG)))
                stack.damageItem(1, entityLiving);
            else
                stack.damageItem(0, entityLiving);
        } else {
            if(ToolLevelHandler.hasLevelTags(stack) && !ToolLevelHandler.isMaxToolLevel(stack, NBTHelper.getTagCompound(stack, ToolLevelHandler.SOUL_TOOL_TAG)))
                stack.damageItem(1, entityLiving);
            else
                stack.damageItem(0, entityLiving);
        }

        return true;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of(name);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (ToolLevelHandler.hasLevelTags(stack) && !ToolLevelHandler.isMaxToolLevel(stack, NBTHelper.getTagCompound(stack, ToolLevelHandler.SOUL_TOOL_TAG))) {
            tooltip.add("Level: " + TextFormatting.AQUA + ToolLevelHandler.getToolLevel(stack, NBTHelper.getTagCompound(stack, ToolLevelHandler.SOUL_TOOL_TAG)));
            tooltip.add("XP: " + TextFormatting.GREEN + ToolLevelHandler.getToolExperience(stack, NBTHelper.getTagCompound(stack, ToolLevelHandler.SOUL_TOOL_TAG)));
        } else if (ToolLevelHandler.hasLevelTags(stack) && ToolLevelHandler.isMaxToolLevel(stack, NBTHelper.getTagCompound(stack, ToolLevelHandler.SOUL_TOOL_TAG))) {
            tooltip.add(TextFormatting.DARK_GREEN + "Max Level");
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return toRepair.getItem() instanceof ItemSoulTool && repair.getItem() == ObjHandler.soulMatter;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        if(ToolLevelHandler.hasLevelTags(stack)) {
            return getToolMaterial().getHarvestLevel() * ToolLevelHandler.getToolLevel(stack, NBTHelper.getTagCompound(stack, ToolLevelHandler.SOUL_TOOL_TAG));
        } else {
            return getToolMaterial().getHarvestLevel();
        }
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return ToolLevelHandler.isMaxToolLevel(stack, NBTHelper.getTagCompound(stack, ToolLevelHandler.SOUL_TOOL_TAG)) ? EnumRarity.EPIC : EnumRarity.COMMON;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();

        if(equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            if(stack.getItem() instanceof ItemSoulTool) {
                if(NBTHelper.hasTag(stack, ToolLevelHandler.SOUL_TOOL_TAG)) {
                    int level = ToolLevelHandler.getToolLevel(stack, NBTHelper.getTagCompound(stack, ToolLevelHandler.SOUL_TOOL_TAG));

                    multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", ((ItemSoulTool) stack.getItem()).getToolMaterial().getDamageVsEntity() * level, 0));
                    multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", ToolLevelHandler.isMaxToolLevel(stack, NBTHelper.getTagCompound(stack, ToolLevelHandler.SOUL_TOOL_TAG)) ? -1.4 : -2.4, 0));
                }
            }
        }

        return multimap;
    }
}
