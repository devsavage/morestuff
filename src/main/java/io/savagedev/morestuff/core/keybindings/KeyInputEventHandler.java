package io.savagedev.morestuff.core.keybindings;

/*
 * KeyInputEventHandler.java
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

import io.savagedev.morestuff.core.handler.ObjHandler;
import io.savagedev.morestuff.core.network.handler.PacketHandler;
import io.savagedev.morestuff.core.network.messages.MessageFlightKeyPressed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyInputEventHandler
{
    private static Key getPressedKeyBinding() {
        if(KeyBindings.flightSpeed.isKeyDown()) {
            return Key.flightSpeed;
        }

        return Key.unknown;
    }

    @SubscribeEvent
    public void handleKeyPressedEvent(InputEvent.KeyInputEvent event) {
        if(getPressedKeyBinding() == Key.unknown) {
            return;
        }

        if(FMLClientHandler.instance().getClient().inGameHasFocus) {
            if(FMLClientHandler.instance().getClientPlayerEntity() != null) {
                EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();

                if(player.getHeldItemMainhand() != null) {
                    ItemStack currentItem = player.getHeldItemMainhand();
                    if(currentItem.getItem() instanceof IKeyBound) {
                        if(player.worldObj.isRemote) {
                            PacketHandler.sendToServer(new MessageFlightKeyPressed(getPressedKeyBinding()));
                        } else {
                            ((IKeyBound) currentItem.getItem()).doKeyBindingAction(player, currentItem, getPressedKeyBinding());
                        }
                    }
                }

                // We have to hard code this so se can handle key inputs on the armor
                ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

                // We know ItemSoulArmor implements IKeyBound but we double check here.
                if(chest != null && chest.getItem() == ObjHandler.soulChestplate && chest.getItem() instanceof IKeyBound) {
                    if(player.worldObj.isRemote) {
                        PacketHandler.sendToServer(new MessageFlightKeyPressed(getPressedKeyBinding()));
                    } else {
                        ((IKeyBound) chest.getItem()).doKeyBindingAction(player, chest, getPressedKeyBinding());
                    }
                }
            }
        }
    }
}
