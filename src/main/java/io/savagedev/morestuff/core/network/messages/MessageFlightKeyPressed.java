package io.savagedev.morestuff.core.network.messages;

/*
 * MessageFlightKeyPressed.java
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
import io.savagedev.morestuff.core.handler.ObjHandler;
import io.savagedev.morestuff.core.keybindings.IKeyBound;
import io.savagedev.morestuff.core.keybindings.Key;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageFlightKeyPressed implements IMessage
{
    private Key keyPressed;

    public MessageFlightKeyPressed() {}

    public MessageFlightKeyPressed(Key keyPressed) {
        if(keyPressed != null) {
            this.keyPressed = keyPressed;
        } else {
            this.keyPressed = Key.unknown;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.keyPressed = Key.getKey(buf.readByte());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte((byte) keyPressed.ordinal());
    }

    public static class MessageHandler implements IMessageHandler<MessageFlightKeyPressed, IMessage> {

        @Override
        public IMessage onMessage(MessageFlightKeyPressed message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;

            if (player != null) {
                // We have to hard code this so se can handle key inputs on the armor
                ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

                if(chest != null && chest.getItem() == ObjHandler.soulChestplate && chest.getItem() instanceof IKeyBound) {
                    ((IKeyBound) chest.getItem()).doKeyBindingAction(player, chest, message.keyPressed);
                }
            }

            return null;
        }
    }
}
