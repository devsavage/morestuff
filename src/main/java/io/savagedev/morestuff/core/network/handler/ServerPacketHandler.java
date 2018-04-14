package io.savagedev.morestuff.core.network.handler;

/*
 * ServerPacketHandler.java
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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerPacketHandler implements IMessageHandler<ServerPacketHandler.MoreStuffMessage, IMessage>
{
    @Override
    public IMessage onMessage(MoreStuffMessage message, MessageContext ctx) {
        if(message.packet != null) {
            ctx.getServerHandler().sendPacket(message.packet);
        }

        return null;
    }

    private static Packet readData(ByteBuf data) {
        TileEntity te;
        int x, y, z, a;
        World world = DimensionManager.getWorld(data.readInt());
        EntityPlayer player;

        return null;
    }

    public static class MoreStuffMessage implements IMessage {

        public ByteBuf buf;
        public Packet packet;

        public MoreStuffMessage() {}

        public MoreStuffMessage(short packet, TileEntity te, Object... args) {
            ByteBuf buff = Unpooled.buffer();
            buff.writeInt(te.getWorld().provider.getDimension());
            buff.writeShort(packet);
            buff.writeInt(te.getPos().getX());
            buff.writeInt(te.getPos().getY());
            buff.writeInt(te.getPos().getZ());
            handleObjects(buff, args);
            buf = buff;
        }

        public MoreStuffMessage(short packet, Entity e, Object... args) {

            ByteBuf buff = Unpooled.buffer();
            buff.writeInt(e.worldObj.provider.getDimension());
            buff.writeShort(packet);
            buff.writeInt(e.getEntityId());
            handleObjects(buff, args);
            buf = buff;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            packet = readData(buf);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeBytes(this.buf);
        }

        private static void handleObjects(ByteBuf data, Object[] objects) {

            for (Object obj : objects) {
                Class<?> objClass = obj.getClass();
                if (objClass.equals(Integer.class)) {
                    data.writeInt((Integer) obj);
                } else if (objClass.equals(Boolean.class)) {
                    data.writeBoolean((Boolean) obj);
                } else if (objClass.equals(Byte.class)) {
                    data.writeByte((Byte) obj);
                } else if (objClass.equals(Short.class)) {
                    data.writeShort((Short) obj);
                } else if (objClass.equals(String.class)) {
                    ByteBufUtils.writeUTF8String(data, (String) obj);
                } else if (Entity.class.isAssignableFrom(objClass)) {
                    data.writeInt(((Entity) obj).getEntityId());
                } else if (objClass.equals(Double.class)) {
                    data.writeDouble((Double) obj);
                } else if (objClass.equals(Float.class)) {
                    data.writeFloat((Float) obj);
                } else if (objClass.equals(Long.class)) {
                    data.writeLong((Long) obj);
                }
            }
        }
    }
}

