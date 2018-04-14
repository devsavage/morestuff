package io.savagedev.morestuff.common.blocks.infuser;

/*
 * TileEntityInfuser.java
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

import io.savagedev.morestuff.common.tileentity.base.TileEntityMS;
import io.savagedev.morestuff.core.Names;
import io.savagedev.morestuff.core.handler.ObjHandler;
import io.savagedev.morestuff.core.helpers.ItemHelper;
import io.savagedev.morestuff.core.helpers.LogHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class TileEntityInfuser extends TileEntityMS implements IInventory, ITickable
{
    public static final int INVENTORY_SIZE = 4;
    public static final int FUEL_SLOT_INDEX = 0;
    public static final int INFUSER_ITEM_SLOT_INDEX = 1;
    public static final int RAW_SOUL_MATTER_SLOT_INDEX = 2;
    public static final int OUTPUT_SLOT_INDEX = 3;

    public int FUEL_BURN_TIME;
    public int INFUSE_TIME;
    public int TOTAL_INFUSE_TIME;

    public int TOTAL_FUEL_STORED;

    private int TOTAL_FUEL_CAPACITY = 14000;
    private int FUSION_COST = 2000;
    private int FUSION_TIME = 200;
    private int counter = 0;
    private int BASE_FUEL_BURN_TIME = 800;

    private EntityLivingBase fakePlayer;

    private ItemStack[] inventory;

    public TileEntityInfuser() {
        inventory = new ItemStack[INVENTORY_SIZE];
    }

    @SideOnly(Side.CLIENT)
    public int getFuelStored(int scale) {
        double stored = this.getField(2);
        double max = this.TOTAL_FUEL_CAPACITY;
        double value = ((stored /max) * scale);

        return (int)value;
    }

    private int getFuelStored() {
        return TOTAL_FUEL_STORED;
    }

    private boolean hasFuel() {
        return TOTAL_FUEL_STORED > 0;
    }

    private boolean isFuelFull() {
        return TOTAL_FUEL_STORED >= TOTAL_FUEL_CAPACITY;
    }

    public boolean isInfusing() {
        return this.FUEL_BURN_TIME > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isInfusing(IInventory inventory) {
        return inventory.getField(3) > 0;
    }

    private void infuse() {
        ItemStack infusionStone = inventory[INFUSER_ITEM_SLOT_INDEX];
        ItemStack rawSoulMatter = inventory[RAW_SOUL_MATTER_SLOT_INDEX];

        if(infusionStone != null && rawSoulMatter != null) {
            if(canInfuse()) {
                ItemStack soulMatter = new ItemStack(ObjHandler.soulMatter);

                if(inventory[OUTPUT_SLOT_INDEX] == null) {
                    inventory[OUTPUT_SLOT_INDEX] = soulMatter.copy();
                } else if(inventory[OUTPUT_SLOT_INDEX].getItem() == soulMatter.getItem()) {
                    inventory[OUTPUT_SLOT_INDEX].stackSize += soulMatter.stackSize;
                }

                --rawSoulMatter.stackSize;

                if(rawSoulMatter.stackSize <= 0) {
                    inventory[RAW_SOUL_MATTER_SLOT_INDEX] = null;
                }

                if(isInfusing()) {
                    --this.INFUSE_TIME;
                }
            }
        }
    }

    private boolean canInfuse() {
        if (inventory[INFUSER_ITEM_SLOT_INDEX] == null || inventory[RAW_SOUL_MATTER_SLOT_INDEX] == null) {
            return false;
        } else {
            ItemStack soulMatter = new ItemStack(ObjHandler.soulMatter);
            if(inventory[OUTPUT_SLOT_INDEX] == null) return true;
            if(!inventory[OUTPUT_SLOT_INDEX].isItemEqual(soulMatter)) return false;
            int result = inventory[OUTPUT_SLOT_INDEX].stackSize + soulMatter.stackSize;
            return result <= getInventoryStackLimit() && result <= inventory[OUTPUT_SLOT_INDEX].getMaxStackSize();
        }

    }

    private void updateTE() {
        worldObj.markBlockRangeForRenderUpdate(pos, pos);
        worldObj.notifyBlockUpdate(pos(), worldObj.getBlockState(pos), worldObj.getBlockState(pos), 3);
        worldObj.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
        markDirty();
    }

    @Override
    public void update() {
        boolean dirty = false;
        boolean checkForInfusing = this.isInfusing();

        if(inventory[FUEL_SLOT_INDEX] != null) {
            if(TileEntityFurnace.isItemFuel(inventory[FUEL_SLOT_INDEX])) {
                TOTAL_FUEL_STORED += 200;

                if(!isFuelFull()) {
                    --inventory[FUEL_SLOT_INDEX].stackSize;
                    if (inventory[FUEL_SLOT_INDEX].stackSize == 0) {
                        inventory[FUEL_SLOT_INDEX] = inventory[FUEL_SLOT_INDEX].getItem().getContainerItem(inventory[FUEL_SLOT_INDEX]);
                    }
                }

                if(TOTAL_FUEL_STORED >= TOTAL_FUEL_CAPACITY) {
                    TOTAL_FUEL_STORED = TOTAL_FUEL_CAPACITY;
                }

                dirty = true;
            }
        }

        if(isInfusing()) {
            this.FUEL_BURN_TIME--;
        }

        if(!worldObj.isRemote) {
            if(isInfusing() || inventory[INFUSER_ITEM_SLOT_INDEX] != null && inventory[RAW_SOUL_MATTER_SLOT_INDEX] != null && hasFuel()) {
                if(!isInfusing() && canInfuse()) {
                    this.FUEL_BURN_TIME = BASE_FUEL_BURN_TIME;
                    if(isInfusing()) {
                        dirty = true;
                        if(hasFuel()) {
                            this.TOTAL_FUEL_STORED -= this.FUSION_COST;
                            if(this.TOTAL_FUEL_STORED <= 0) {
                                this.TOTAL_FUEL_STORED = 0;
                            }
                        }
                    }
                }

                if(isInfusing() && canInfuse()) {
                    ++this.INFUSE_TIME;

                    if(this.INFUSE_TIME >= this.TOTAL_INFUSE_TIME) {
                        this.INFUSE_TIME = 0;
                        this.TOTAL_INFUSE_TIME = this.FUSION_TIME;
                        this.infuse();
                        dirty = true;
                    }
                } else {
                    this.INFUSE_TIME = 0;
                }
            } else if(!isInfusing() && this.INFUSE_TIME > 0) {
                this.INFUSE_TIME = MathHelper.clamp_int(this.INFUSE_TIME - 2, 0, this.TOTAL_INFUSE_TIME);
            }

            if(checkForInfusing != this.isInfusing()) {
                dirty = true;
            }
        }

        if(dirty) {
            updateTE();
        }
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory[index];
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int slotIndex, int decrementAmount) {
        ItemStack itemStack = getStackInSlot(slotIndex);
        if (itemStack != null) {
            if (itemStack.stackSize <= decrementAmount) {
                setInventorySlotContents(slotIndex, null);
            } else {
                itemStack = itemStack.splitStack(decrementAmount);
                if (itemStack.stackSize == 0) {
                    setInventorySlotContents(slotIndex, null);
                }
            }
        }

        return itemStack;
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int slotIndex) {
        ItemStack itemStack = getStackInSlot(slotIndex);

        if (itemStack != null) {
            setInventorySlotContents(slotIndex, null);
        }

        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, @Nullable ItemStack stack) {
        boolean flag = stack != null && stack.isItemEqual(this.inventory[slotIndex]) && ItemStack.areItemStackTagsEqual(stack, this.inventory[slotIndex]);
        inventory[slotIndex] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }

        if(slotIndex == 0 && !flag) {
            this.TOTAL_INFUSE_TIME = this.FUSION_TIME;
            this.INFUSE_TIME = 0;
            this.markDirty();
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.pos) == this && player.getDistanceSq((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D) <= 64D;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        switch (id) {
            case 0:
                return this.INFUSE_TIME;
            case 1:
                return this.TOTAL_INFUSE_TIME;
            case 2:
                return this.TOTAL_FUEL_STORED;
            case 3:
                return this.FUEL_BURN_TIME;
            default:
                return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.INFUSE_TIME = value;
                break;
            case 1:
                this.TOTAL_INFUSE_TIME = value;
            case 2:
                this.TOTAL_FUEL_STORED = value;
            case 3:
                this.FUEL_BURN_TIME = value;
                break;
        }
    }

    @Override
    public int getFieldCount() {
        return INVENTORY_SIZE;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.inventory.length; ++i) {
            this.inventory[i] = null;
        }
    }

    @Override
    public String getName() {
        return Names.Container.infuser;
    }

    @Nullable
    @Override
    public ITextComponent getDisplayName() {
        return (ITextComponent)(this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));

    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagList tagList = compound.getTagList(Names.NBT.ITEMS, 10);
        inventory = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            byte slotIndex = tagCompound.getByte("Slot");
            if (slotIndex >= 0 && slotIndex < inventory.length) {
                inventory[slotIndex] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }

        this.INFUSE_TIME = compound.getInteger(Names.NBT.INFUSE_TIME);
        this.TOTAL_INFUSE_TIME = compound.getInteger(Names.NBT.TOTAL_INFUSE_TIME);
        this.TOTAL_FUEL_STORED = compound.getInteger(Names.NBT.TOTAL_FUEL_STORED);
        this.FUEL_BURN_TIME = compound.getInteger(Names.NBT.FUEL_BURN_TIME);

        if (compound.hasKey(Names.NBT.CUSTOM_NAME, 8)) {
            this.customName = compound.getString(Names.NBT.CUSTOM_NAME);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger(Names.NBT.INFUSE_TIME, this.INFUSE_TIME);
        compound.setInteger(Names.NBT.TOTAL_INFUSE_TIME, this.TOTAL_INFUSE_TIME);
        compound.setInteger(Names.NBT.TOTAL_FUEL_STORED, this.TOTAL_FUEL_STORED);
        compound.setInteger(Names.NBT.FUEL_BURN_TIME, this.FUEL_BURN_TIME);

        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < inventory.length; ++currentIndex) {
            if (inventory[currentIndex] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                inventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }

        compound.setTag(Names.NBT.ITEMS, tagList);

        if (this.hasCustomName()) {
            compound.setString(Names.NBT.CUSTOM_NAME, this.customName);
        }

        return compound;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }
}
