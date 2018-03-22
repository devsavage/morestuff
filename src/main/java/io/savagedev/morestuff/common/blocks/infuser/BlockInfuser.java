package io.savagedev.morestuff.common.blocks.infuser;

/*
 * BlockInfuser.java
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
import io.savagedev.morestuff.common.blocks.base.BlockContainerMS;
import io.savagedev.morestuff.core.Names;
import io.savagedev.morestuff.core.handler.ObjHandler;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockInfuser extends BlockContainerMS
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockInfuser() {
        super(Material.IRON);
        this.setSoundType(SoundType.STONE);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setLightLevel(0.875F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setUnlocalizedName(Names.Blocks.infuser);
    }

    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote) {
            IBlockState blockNorth = worldIn.getBlockState(pos.north());
            IBlockState blockSouth = worldIn.getBlockState(pos.south());
            IBlockState blockWest = worldIn.getBlockState(pos.west());
            IBlockState blockEast = worldIn.getBlockState(pos.east());
            EnumFacing enumFacing = (EnumFacing)state.getValue(FACING);

            if (enumFacing == EnumFacing.NORTH && blockNorth.isFullBlock() && !blockSouth.isFullBlock()) {
                enumFacing = EnumFacing.SOUTH;
            } else if (enumFacing == EnumFacing.SOUTH && blockSouth.isFullBlock() && !blockNorth.isFullBlock()) {
                enumFacing = EnumFacing.NORTH;
            } else if (enumFacing == EnumFacing.WEST && blockWest.isFullBlock() && !blockEast.isFullBlock()) {
                enumFacing = EnumFacing.EAST;
            } else if (enumFacing == EnumFacing.EAST && blockEast.isFullBlock() && !blockWest.isFullBlock()) {
                enumFacing = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, enumFacing), 2);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityInfuser) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityInfuser)tileentity);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumFacing = EnumFacing.getFront(meta);

        if (enumFacing.getAxis() == EnumFacing.Axis.Y) {
            enumFacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumFacing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    @Nullable
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(ObjHandler.blockInfuser);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        setDefaultFacing(worldIn, pos, state);
    }

    @Nullable
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        } else {
            playerIn.openGui(MoreStuff.instance, Names.Gui.infuserIndex, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityInfuser();
    }
}
