package io.savagedev.morestuff.common.tileentity.base;

/*
 * TileEntityMS.java
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
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class TileEntityMS extends TileEntity
{
    protected String customName;
    protected String owner;

    public TileEntityMS() {
        customName = "";
        owner = "";
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return super.getUpdatePacket();
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        if (compound.hasKey(Names.NBT.CUSTOM_NAME)) {
            this.customName = compound.getString(Names.NBT.CUSTOM_NAME);
        }

        if (compound.hasKey(Names.NBT.OWNER)) {
            this.owner = compound.getString(Names.NBT.OWNER);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        if (this.hasCustomName()) {
            compound.setString(Names.NBT.CUSTOM_NAME, customName);
        }

        if (this.hasOwner()) {
            compound.setString(Names.NBT.OWNER, owner);
        }

        return compound;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        if (owner == null)
            owner = "";
        if (this.owner == null || this.owner.isEmpty())
            this.owner = owner;
    }

    public boolean hasCustomName() {
        return customName != null && customName.length() > 0;
    }

    public boolean hasOwner() {
        return owner != null && owner.length() > 0;
    }

    public int x() {
        return this.pos.getX();
    }

    public int y() {
        return this.pos.getY();
    }

    public int z() {
        return this.pos.getZ();
    }

    public BlockPos pos() {
        return this.pos;
    }

    public boolean isClient() {
        return this.worldObj.isRemote;
    }

    public boolean isServer() {
        return !isClient();
    }
}
