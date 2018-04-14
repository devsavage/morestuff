package io.savagedev.morestuff.common.items;

/*
 * ItemMagnet.java
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

import io.savagedev.morestuff.common.items.base.ItemMS;
import io.savagedev.morestuff.core.Names;
import io.savagedev.morestuff.core.config.values.ConfigBooleanValues;
import io.savagedev.morestuff.core.helpers.NBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.naming.Name;
import java.util.Iterator;
import java.util.List;

public class ItemMagnet extends ItemMS
{
    public ItemMagnet() {
        super(Names.Items.magnet);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        this.canRepair = false;
    }

    private void searchForEntities(World world, EntityPlayer player, double distance) {
        List itemList = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(player.posX - distance, player.posY - distance, player.posZ - distance, player.posX + distance, player.posY + distance, player.posZ + distance));
        Iterator itemIterator = itemList.iterator();

        while(itemIterator.hasNext()) {
            EntityItem item = (EntityItem) itemIterator.next();
            if(!doesInventoryHaveRoom(item.getEntityItem(), player)) {
                continue;
            }

            if(player.getDistanceSqToEntity(item) < 1.5D) {
                continue;
            }

            attractEntityToPlayer(item, player);
            break;
        }

        List xpList = world.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(
                player.posX - distance,
                player.posY - distance,
                player.posZ - distance,
                player.posX - distance,
                player.posY - distance,
                player.posZ - distance
        ));
        Iterator xpIterator = xpList.iterator();

        while (xpIterator.hasNext()) {
            EntityXPOrb orb = (EntityXPOrb) xpIterator.next();
            if(player.xpCooldown > 0) {
                player.xpCooldown = 0;
            }

            if(player.getDistanceToEntity(orb) < 1.5D) {
                continue;
            }

            attractEntityToPlayer(orb, player);
            break;
        }
    }

    private boolean doesInventoryHaveRoom(ItemStack stack, EntityPlayer player) {
        int remainingStackSize = stack.stackSize;
        for(ItemStack itemStack : player.inventory.mainInventory) {
            if(itemStack == null) {
                continue;
            }

            if(itemStack.getItem() == stack.getItem() && itemStack.getItemDamage() == stack.getItemDamage()) {
                if(itemStack.stackSize + remainingStackSize <= itemStack.getMaxStackSize()) {
                    return true;
                } else {
                    int count = itemStack.stackSize;
                    while (count < itemStack.getMaxStackSize()) {
                        count++;
                        remainingStackSize--;
                        if(remainingStackSize == 0) {
                            return true;
                        }
                    }
                }
            }
        }

        for(int slot = 0; slot < player.inventory.mainInventory.length; slot++) {
            if(player.inventory.mainInventory[slot] == null) {
                return true;
            }
        }

        return false;
    }

    private void attractEntityToPlayer(Entity entity, EntityPlayer player) {
        player.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB,
                entity.posX + 0.5D + player.worldObj.rand.nextGaussian() / 8,
                entity.posY + 0.2D,
                entity.posZ + 0.5D + player.worldObj.rand.nextGaussian() / 8,
        0.9D, 0.9D, 0.0D);
        player.getLookVec();

        double x = player.posX + player.getLookVec().xCoord * 0.2D;
        double y = player.posY + player.height / 2F;
        double z = player.posZ + player.getLookVec().zCoord * 0.2D;

        entity.setPosition(x, y, z);

        if(!isAudioDisabled()) {
            player.worldObj.playSound((EntityPlayer)null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_TOUCH, SoundCategory.PLAYERS, 0.1F, 0.5F * ((player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat() * 0.7F * 1.8F)));
        }
    }

    private boolean isAudioDisabled() {
        return ConfigBooleanValues.DISABLE_MAGNET_SOUND.isEnabled();
    }

    private double getManualPullDistance() {
        return (double) 5;
    }

    private double getAutoPullDistance() {
        return (double) 5;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(worldIn.isRemote)
            return;

        if(!isAudioDisabled()) {
            if(NBTHelper.getShort(stack, Names.Common.SOUND_TIMER) > 0) {
                if(NBTHelper.getShort(stack, Names.Common.SOUND_TIMER) % 2 == 0) {
                    worldIn.playSound((EntityPlayer)null, entityIn.posX, entityIn.posY, entityIn.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_TOUCH, SoundCategory.PLAYERS, 0.1F, 0.5F * ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.8F));
                }
            }
        }

        if(!NBTHelper.getBoolean(stack, Names.Common.ACTIVE))
            return;

        EntityPlayer player = null;

        if(entityIn instanceof EntityPlayer) {
            player = (EntityPlayer) entityIn;
        }

        if(player == null) {
            return;
        }

        searchForEntities(worldIn, player, getAutoPullDistance());
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        if(player instanceof EntityPlayer)
            searchForEntities(player.worldObj, (EntityPlayer) player, getManualPullDistance());
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return NBTHelper.getBoolean(stack, Names.Common.ACTIVE);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if(playerIn.isSneaking()) {
            if(!isAudioDisabled()) {
                NBTHelper.setShort(itemStackIn, Names.Common.SOUND_TIMER, (short) 6);
            }

            if(!NBTHelper.getBoolean(itemStackIn, Names.Common.ACTIVE)) {
                playerIn.playSound(SoundEvents.BLOCK_ANVIL_HIT, 1.0F, 1.0F);
            }

            NBTHelper.setBoolean(itemStackIn, Names.Common.ACTIVE, true);

        } else {

            if(NBTHelper.getBoolean(itemStackIn, Names.Common.ACTIVE)) {
                playerIn.playSound(SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, 1.0F, 1.0F);
            }

            NBTHelper.setBoolean(itemStackIn, Names.Common.ACTIVE, false);
            NBTHelper.setShort(itemStackIn, Names.Common.SOUND_TIMER, (short) 0);
            playerIn.setActiveHand(hand);
        }

        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 1000;
    }
}
