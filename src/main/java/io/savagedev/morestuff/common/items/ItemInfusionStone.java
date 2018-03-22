package io.savagedev.morestuff.common.items;

/*
 * ItemInfusionStone.java
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
import io.savagedev.morestuff.common.items.base.ItemMS;
import io.savagedev.morestuff.core.Names;
import io.savagedev.morestuff.core.helpers.LogHelper;
import io.savagedev.morestuff.core.helpers.NBTHelper;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.naming.Name;
import java.util.List;

public class ItemInfusionStone extends ItemMS
{
    public ItemInfusionStone() {
        super(Names.Items.infusionStone);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.canRepair = false;
    }

    private List<EntityMob> searchAreaForMobs(World world, EntityPlayer player) {
        return world.getEntitiesWithinAABB(EntityMob.class, player.getEntityBoundingBox().expand(10, 10, 10));
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if(NBTHelper.getBoolean(itemStackIn, Names.Common.INFUSED)) {
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        }

        List<EntityMob> mobList = searchAreaForMobs(worldIn, playerIn);

        if(searchAreaForMobs(worldIn, playerIn).size() > 3) {
            mobList = mobList.subList(0, 4);
            for (EntityMob mob : mobList) {
                mob.attackEntityFrom(DamageSource.lightningBolt, Float.MAX_VALUE);
                worldIn.spawnEntityInWorld(new EntityLightningBolt(worldIn, mob.posX, mob.posY, mob.posZ, true));
            }

            NBTHelper.setBoolean(itemStackIn, Names.Common.INFUSED, true);

            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
        }

        return new ActionResult(EnumActionResult.PASS, itemStackIn);
    }

    @Nonnull
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        if(NBTHelper.getBoolean(stack, Names.Common.INFUSED)) {
            return EnumRarity.EPIC;
        }

        return EnumRarity.COMMON;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return NBTHelper.getBoolean(stack, Names.Common.INFUSED);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if(NBTHelper.getBoolean(stack, Names.Common.INFUSED)) {
            tooltip.add(TextFormatting.ITALIC + Names.Common.INFUSED);
        }
    }
}
